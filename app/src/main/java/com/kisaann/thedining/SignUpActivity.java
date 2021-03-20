package com.kisaann.thedining;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.AdsNotificationModel;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.RegistrationModel;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Utils.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btn_sign_up,img_logIn;
    private EditText edt_first_name;
    private EditText edt_last_name;
    private EditText edt_emailId;
    private EditText edt_mobileNo;
    private EditText edt_password;
    private EditText edt_re_password;
    private LinearLayout mlly_login;
    private ImageView img_back;
    private RadioGroup mRdgGender;

    private String firstName;
    private String lastName;
    private String location;
    private String gender;
    private String emaiId;
    private String mobileNo;
    private String password;
    private String time;
    private String date;
    RegistrationModel registrationModel;
    DatabaseReference registrationForms;
    FirebaseDatabase database;
    AdsNotificationModel adsNotificationModel;
    APIService mService;
    DatabaseReference adsNotificationDatabase;
    DatabaseReference tokens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        onClickListener();

       /* client = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);
        getLatLong();*/
        // init service
        mService = Common.getFCMService();
        // init firebase
        database = FirebaseDatabase.getInstance();
        registrationForms = database.getReference("Users");
        adsNotificationDatabase = database.getReference("Notifications");
    }
    private void initUI() {
        btn_sign_up = findViewById(R.id.btn_sign_up);
        edt_first_name = findViewById(R.id.edt_first_name);
     //   edt_last_name = findViewById(R.id.edt_last_name);
        edt_emailId = findViewById(R.id.edt_emailId);
        edt_mobileNo = findViewById(R.id.edt_mobileNo);
        edt_password = findViewById(R.id.edt_password);
        edt_re_password = findViewById(R.id.edt_re_password);

     //   mlly_login = findViewById(R.id.mlly_login);
        img_back = findViewById(R.id.img_back);
        img_logIn = findViewById(R.id.img_logIn);
      //  mRdgGender = findViewById(R.id.mRdgGender);

    }
    private void onClickListener() {
        btn_sign_up.setOnClickListener(this);
        img_back.setOnClickListener(this);
        img_logIn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                /*startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                SignUpActivity.this.finish();*/

               /* Intent homeIntent1 = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(homeIntent1);*/
                String phoneNumber = edt_mobileNo.getText().toString().trim();
                if(phoneNumber.startsWith("+") || phoneNumber.startsWith("0"))
                {
                    if(phoneNumber.length()==11) {
                        String str_getMOBILE=phoneNumber.substring(1);
                        edt_mobileNo.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==12) {
                        String str_getMOBILE=phoneNumber.substring(2);
                        edt_mobileNo.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==13) {
                        String str_getMOBILE=phoneNumber.substring(3);
                        edt_mobileNo.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==14) {
                        String str_getMOBILE=phoneNumber.substring(4);
                        edt_mobileNo.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==15) {
                        String str_getMOBILE=phoneNumber.substring(5);
                        edt_mobileNo.setText(str_getMOBILE);
                    }
                } else {
                    edt_mobileNo.setText(phoneNumber);
                }

               attemptRegistration();
                break;
            case R.id.img_logIn:
                Intent homeIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void attemptRegistration() {
        // Reset errors.
        edt_first_name.setError(null);
      //  edt_last_name.setError(null);
        edt_emailId.setError(null);
        edt_mobileNo.setError(null);
     //   edt_password.setError(null);
       /* int selectedAvailId = mRdgGender.getCheckedRadioButtonId();
        RadioButton rBtGender = (RadioButton) findViewById(selectedAvailId);*/
        // Store values at the time of the login attempt.
        firstName = edt_first_name.getText().toString().trim();
      //  lastName = edt_last_name.getText().toString().trim();
        lastName = "";
        emaiId = edt_emailId.getText().toString().trim();
        mobileNo = edt_mobileNo.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        password = "123456";
     //   gender = ""+rBtGender.getText();
        gender = "gender";


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            edt_first_name.setError(getString(R.string.error_field_required));
            focusView = edt_first_name;
            cancel = true;
        }
        /*// Check for a valid email address.
        else if (TextUtils.isEmpty(lastName)) {
            edt_last_name.setError(getString(R.string.error_field_required));
            focusView = edt_last_name;
            cancel = true;
        }*/ else if (TextUtils.isEmpty(emaiId)) {
            edt_emailId.setError(getString(R.string.error_field_required));
            focusView = edt_emailId;
            cancel = true;
        } else if (!emaiId.matches("[0-9]+") && !Utility.isEmailValid(emaiId)) {
            edt_emailId.setError(getString(R.string.error_invalid_email));
            focusView = edt_emailId;
            cancel = true;
        }else if (TextUtils.isEmpty(mobileNo)) {
            edt_mobileNo.setError(getString(R.string.error_field_required));
            focusView = edt_mobileNo;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(password)) {
            edt_password.setError(getString(R.string.error_field_required));
            focusView = edt_password;
            cancel = true;
        } else if (!Utility.isPasswordValid(password)) {
            edt_password.setError(getString(R.string.error_invalid_password));
            focusView = edt_password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (Utility.isNetworkAvailable(getBaseContext())) {
                if (!TextUtils.isEmpty(gender)) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                    mDialog.setMessage("Please wait....");
                    mDialog.show();
                    registrationForms.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // check if already user phone
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,hh:mm:ss");
                            String dateTime = simpleDateFormat.format(new Date());

                            SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                            String key = simple.format(new Date());

                            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                            time = dateFormat.format(new Date());
                            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                           /* SimpleDateFormat simpleDate = new SimpleDateFormat
                                                    ("dd-MM-yyyy");*/
                            date = simpleDate.format(new Date());

                            registrationModel = new RegistrationModel(
                                    firstName, lastName, emaiId, mobileNo, password, date, time,
                                    dateTime, "U",gender,"default","offline",firstName.toLowerCase(),"0",
                                    "","","","","","","","","Active");
                            registrationForms.child(mobileNo).setValue(registrationModel);
                            /*sendNotificationOrder();*/
                            Toast.makeText(SignUpActivity.this, "Successfully " + "registered" + " !", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mDialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(SignUpActivity.this, "Please select gender. ", Toast
                            .LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(SignUpActivity.this, "Please check your network connection. ", Toast
                        .LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void sendNotificationOrder() {
        tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo("9985130460");//get all node with
        // isSercertoken is true
        Log.e("Content",""+data);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);

                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title",firstName+" "+lastName+"<>"+mobileNo+"<>"+"Registration");
                    dataSend.put("message", "New User Registered : " + firstName+" "+lastName+", "+mobileNo + ", "+emaiId);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.e("Content Text",text);
                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            adsNotificationModel = new AdsNotificationModel
                                                    ("Registration",firstName+" "+lastName,time,date,firstName+" "+lastName, mobileNo
                                                            ,"F",mobileNo);
                                            adsNotificationDatabase.child(mobileNo).setValue(adsNotificationModel);

                                            Log.e("notification","s !!!"); // Prints the scan
                                            // format
                                        } else {
                                            /* Toast.makeText(AddListingActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();*/
                                            Log.e("notification","Failed !!!!");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("onFailure", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled", databaseError.getDetails());
            }
        });

    }
}
