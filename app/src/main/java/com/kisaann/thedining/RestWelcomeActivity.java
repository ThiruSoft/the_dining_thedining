package com.kisaann.thedining;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.RestaurantMenuModel;
import com.kisaann.thedining.Models.VerifyOtpModel;
import com.kisaann.thedining.Utils.Utility;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import io.paperdb.Paper;

public class RestWelcomeActivity extends AppCompatActivity implements View.OnClickListener  {

    String isLogin;
    String restaurantMedId;
    LinearLayout lly_restWelcome;
    ImageView img_restaurant;
    TextView txt_restDetails;
    RestaurantMenuModel menuModel;
    FirebaseDatabase database;
    DatabaseReference restaurantMenu;
    DatabaseReference reference;
    DatabaseReference verifyOtpForm;
    Dialog dialog_customize_view;
    String userPhone;
    String tableNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rest_welcome);

        Paper.init(this);
        if (getIntent() != null){
            isLogin = getIntent().getStringExtra("isLogin");
            restaurantMedId = getIntent().getStringExtra("RestaurantMenuId");
        }
         userPhone = Paper.book().read(Common.USER_KEY);
        tableNo = Paper.book().read(Common.TABLE_NO);
        // init firebase
        database = FirebaseDatabase.getInstance();
        restaurantMenu = database.getReference("RestaurantMenu");
        verifyOtpForm = database.getReference(restaurantMedId+"_VerifyOtp");

        img_restaurant = findViewById(R.id.img_restaurant);
        txt_restDetails = findViewById(R.id.txt_restDetails);
        lly_restWelcome = findViewById(R.id.lly_restWelcome);
        lly_restWelcome.setOnClickListener(this);
        restaurantMenu.orderByChild("restId").equalTo(restaurantMedId);
        restaurantMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    menuModel = snapshot.getValue(RestaurantMenuModel.class);
                    // Log.e("name",""+menuModel.getName()); // Prints the scan
                    // Log.e("img_restaurant",""+snapshot.getRef().getKey()); // Prints the scan
                    String restId = snapshot.getRef().getKey(); // Prints the scan
                    if (restaurantMedId.equalsIgnoreCase(restId)){
                        Common.restaurantDetails = menuModel ;
                        Paper.book().write(Common.REST_OFFER_TYPE, menuModel.getOfferType());
                        Paper.book().write(Common.REST_NAME, menuModel.getName());
                        Paper.book().write(Common.REST_ADDRESS, menuModel.getAddress());
                        Paper.book().write(Common.REST_GST_NO, menuModel.getGstNo());
                        Log.e("name",""+menuModel.getName()); // Prints the scan
                        Log.e("img_restaurant",""+restId); // Prints the scan
                        Log.e("REST_GST_NO",""+menuModel.getGstNo()); // Prints the scan
                        Glide.with(RestWelcomeActivity.this).load(menuModel.getImage()).into(img_restaurant);

                        txt_restDetails.setText("  Greetings from " +menuModel.getName()+" "+ menuModel.getCity()+"\n\n" +
                                "Thank you for choosing us. \n\n" +
                                "We look forward to delighting your taste buds \n\n" +
                                "Manager");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      //  verifyOTPDialog();
        new Thread(){
            public void run(){
                try {
                    sleep(5000);

                    Intent homeIntent = new Intent(RestWelcomeActivity.this, MainActivity.class);
                    homeIntent.putExtra("isLogin","true");
                    homeIntent.putExtra("RestaurantMenuId",restaurantMedId);
                    startActivity(homeIntent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        /*new Thread(){
            public void run(){
                try {
                    sleep(5000);

                    verifyOTPDialog();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    private void verifyOTPDialog() {
        Dialog dialog_customize_view = new Dialog(this);
        if (dialog_customize_view.getWindow() != null) {
            dialog_customize_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_view.setContentView(R.layout.otp_custom_dialog);
        dialog_customize_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_view.setCancelable(false); // can dismiss alert screen when user click back buttonon
        dialog_customize_view.setCanceledOnTouchOutside(false);
        dialog_customize_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_view.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_customize_view.setCancelable(true);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        TextView txt_type = dialog_customize_view.findViewById(R.id.txt_type);
        TextView txt_submit = dialog_customize_view.findViewById(R.id.txt_submit);
        ImageView img_close = dialog_customize_view.findViewById(R.id.img_close);
        MaterialEditText edt_otpNo = dialog_customize_view.findViewById(R.id.edt_otpNo);
        LinearLayout mlly_otp = dialog_customize_view.findViewById(R.id.mlly_otp);
        mlly_otp.setVisibility(View.GONE);
        txt_type.setText("Verify ");
        String welcomeOtp = Paper.book().read(Common.WELCOME_OTP);
        edt_otpNo.setText(welcomeOtp);
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpVerify = edt_otpNo.getText().toString().trim();
                if (Utility.isNetworkAvailable(getBaseContext())) {
                    /*String welcomeOtp = Paper.book().read(Common.WELCOME_OTP);
                    if (!otpVerify.isEmpty()) {
                        if (otpVerify.equalsIgnoreCase(welcomeOtp)) {

                            verifyOtpForm.orderByChild("otpNo").equalTo(otpVerify);
                            verifyOtpForm.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        VerifyOtpModel requestC = snapshot.getValue(VerifyOtpModel.class);
                                        String keyC = snapshot.getRef().getKey();
                                        if (otpVerify.equalsIgnoreCase(requestC.getOtpNo())){
                                            reference = FirebaseDatabase.getInstance().getReference(restaurantMedId+"_VerifyOtp").child(keyC);
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("status", "Yes");
                                            reference.updateChildren(hashMap);
                                        }
                                    }
                                    verifyOtpForm.removeEventListener(this);

                                    Intent homeIntent = new Intent(RestWelcomeActivity.this, MainActivity.class);
                                    homeIntent.putExtra("isLogin","true");
                                    homeIntent.putExtra("RestaurantMenuId",restaurantMedId);
                                    startActivity(homeIntent);
                                    finish();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }*/
                    // new
                    String welcomeOtp = Paper.book().read(Common.WELCOME_OTP);
                    verifyOtpForm.orderByChild("userPhoneNo").equalTo(userPhone);
                    verifyOtpForm.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                VerifyOtpModel requestC = snapshot.getValue(VerifyOtpModel.class);
                                String keyC = snapshot.getRef().getKey();
                                if (welcomeOtp.equalsIgnoreCase(requestC.getOtpNo())){
                                    if (requestC.getAccepted() != null && requestC.getAccepted().equalsIgnoreCase("Yes")){

                                        reference = FirebaseDatabase.getInstance().getReference(restaurantMedId+"_VerifyOtp").child(keyC);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("status", "Yes");
                                        reference.updateChildren(hashMap);

                                        Intent homeIntent = new Intent(RestWelcomeActivity.this, MainActivity.class);
                                        homeIntent.putExtra("isLogin","true");
                                        homeIntent.putExtra("RestaurantMenuId",restaurantMedId);
                                        startActivity(homeIntent);
                                        finish();
                                    }else {
                                        Toast.makeText(RestWelcomeActivity.this, "Not Verified Your Request ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            verifyOtpForm.removeEventListener(this);


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(RestWelcomeActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_customize_view.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lly_restWelcome:

                Intent homeIntent = new Intent(RestWelcomeActivity.this, MainActivity.class);
                homeIntent.putExtra("isLogin","true");
                homeIntent.putExtra("RestaurantMenuId",restaurantMedId);
                startActivity(homeIntent);
                finish();

                break;
        }
    }
}
