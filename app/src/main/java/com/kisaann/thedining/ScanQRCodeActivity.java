package com.kisaann.thedining;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.QRCodeModel;
import com.kisaann.thedining.Models.RegistrationModel;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Models.VerifyOtpModel;
import com.kisaann.thedining.Remote.APIService;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRCodeActivity extends AppCompatActivity implements View.OnClickListener, AutoPermissionsListener {
    String isLogin;
    String tableNo;
    String ownerNo;
    String kitchenNo;
    String restId;
    ImageView img_qrCodeScan;
    CardView card_explore;
    DatabaseReference verifyOtpForm;
    FirebaseDatabase database;
    DatabaseReference restaurantQRCodeList;
    APIService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_qrcode);

        // init paper
        Paper.init(this);
        mService = Common.getFCMService();
        if (getIntent() != null){
            isLogin = getIntent().getStringExtra("isLogin");
        }
        AutoPermissions.Companion.loadActivityPermissions(ScanQRCodeActivity.this, 1);
        img_qrCodeScan = findViewById(R.id.img_qrCodeScan);
        card_explore = findViewById(R.id.card_explore);

        database = FirebaseDatabase.getInstance();
        restaurantQRCodeList = database.getReference("RestaurantQRCode");

        img_qrCodeScan.setOnClickListener(this);
        card_explore.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        /*new Thread(){
            public void run(){
                try {
                    sleep(3000);
                    scanQrcode();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_explore:

                Intent homeIntent = new Intent(ScanQRCodeActivity.this, RestaurantDiscoveryActivity.class);
                homeIntent.putExtra("isLogin","true");
                homeIntent.putExtra("RestaurantMenuId",restId);
                startActivity(homeIntent);
                break;
            case R.id.img_qrCodeScan:
                scanQrcode();
                break;
        }
    }
    private void scanQrcode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(ScanQRCodeActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning...");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null && result.getContents() != null){
            StringTokenizer stringTokenizer = new StringTokenizer(result.getContents(), "<?>");
            while (stringTokenizer.hasMoreElements()) {
                restId = stringTokenizer.nextElement().toString().trim();
                tableNo = stringTokenizer.nextElement().toString();
                ownerNo = stringTokenizer.nextElement().toString();
             //   kitchenNo = stringTokenizer.nextElement().toString();
                Log.e("tableNo",""+tableNo); // Prints the scan
                Log.e("restId",""+restId); // Prints the scan
                Log.e("ownerNo",""+ownerNo); // Prints the scan
             //   Log.e("kitchenNo",""+kitchenNo); // Prints the scan

            }
            new androidx.appcompat.app.AlertDialog.Builder(ScanQRCodeActivity.this)
                    .setTitle("Table No.")
                    .setMessage(tableNo)
                    .setCancelable(false)
                    .setPositiveButton("Okay", (dialog, which) -> {
                        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("result",tableNo);
                        manager.setPrimaryClip(clipData);
                        sendNotificationRequest();
                        Query listQRCodesId = restaurantQRCodeList.orderByChild("restaurantOwnerNo").equalTo(ownerNo);
                        listQRCodesId.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    QRCodeModel qrCodeModel = snapshot.getValue(QRCodeModel.class);
                                    assert qrCodeModel != null;
                                    if (qrCodeModel.getQrName().equalsIgnoreCase(tableNo)) {
                                        kitchenNo = qrCodeModel.getRestaurantKitchenNo();
                                        Log.e("kitchenNo",""+kitchenNo);
                                    }
                                }
                                listQRCodesId.removeEventListener(this);

                                Paper.book().write(Common.KITCHEN_NO, ""+kitchenNo);
                                Paper.book().write(Common.OWNER_NO, ownerNo);
                                Paper.book().write(Common.TABLE_NO, tableNo);
                                //Paper.book().write(Common.COMPLETE_ORDER, "No");
                                Paper.book().write(Common.RESTAURANT_ID, restId);

                                SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
                                String otp = simple1.format(new Date());
                                Log.e("WELCOME_OTP",otp);
                                Paper.book().write(Common.WELCOME_OTP, otp);
                                String userName = Paper.book().read(Common.USER_NAME);
                                String userPhone = Paper.book().read(Common.USER_KEY);

                                String trans_number = String.valueOf(System.currentTimeMillis());
                                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                                String time = dateFormat.format(new Date());
                                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                String date = simpleDate.format(new Date());


                                verifyOtpForm = database.getReference(restId+"_VerifyOtp");

                                VerifyOtpModel verifyOtpModel = new VerifyOtpModel(
                                        userPhone, userName, tableNo, otp, time, date,"No","No"
                                );
                                verifyOtpForm.child(trans_number).setValue(verifyOtpModel);

                                Intent homeIntent = new Intent(ScanQRCodeActivity.this, RestWelcomeActivity.class);
                                homeIntent.putExtra("isLogin","true");
                                homeIntent.putExtra("RestaurantMenuId",restId);
                                startActivity(homeIntent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }).setNegativeButton("Cancel", (dialog, which) ->
                    dialog.dismiss()).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendNotificationRequest() {
        DatabaseReference staffData = database.getReference("Users");
        Query listStaffByRestId = staffData.orderByChild("restaurantId").equalTo(restId);
        listStaffByRestId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RegistrationModel model = postSnapshot.getValue(RegistrationModel.class);
                    Log.e("Rest Users",model.getPhoneNo());
                    DatabaseReference tokens = database.getReference("Tokens");
                    Query data = tokens.orderByChild("phone").equalTo(model.getPhoneNo());//get all node with
                    // isSercertoken is true
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Token serverToken = postSnapshot.getValue(Token.class);
                                Map<String,String> dataSend = new HashMap<>();
                                dataSend.put("title","Dining");
                                dataSend.put("message","You have a new Request from : " + tableNo +"<?>"+ tableNo+"<?>"+"Request"+"<?>"+tableNo);
                                DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                                String text = new Gson().toJson(dataMessage);
                                Log.d("Content",text);

                                mService.sendNotification(dataMessage)
                                        .enqueue(new Callback<MyResponse>() {
                                            @Override
                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                // only run when get result
                                                // issue notification video 22
                                                if (response.code() == 200) {
                                                    if (response.body().success == 1) {
                                                        //    Toast.makeText(CartItemsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
                                                       // finish();
                                                        Log.e("Notification","sent");
                                                    } else {
                                                      //  Toast.makeText(ScanQRCodeActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                                Log.e("ERROR", t.getMessage());
                                            }
                                        });
                            }
                            data.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                listStaffByRestId.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*DatabaseReference tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo(ownerNo);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","Dining");
                    dataSend.put("message","You have a new Request from : " + tableNo +"<?>"+ tableNo+"<?>"+"Request"+"<?>"+tableNo);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.d("Content",text);

                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    // only run when get result
                                    // issue notification video 22
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            //    Toast.makeText(CartItemsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ScanQRCodeActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(ScanQRCodeActivity.this, requestCode, permissions, ScanQRCodeActivity.this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {

    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {

    }
}
