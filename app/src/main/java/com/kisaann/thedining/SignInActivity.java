package com.kisaann.thedining;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Models.User;
import com.kisaann.thedining.Utils.Utility;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueButton;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueException;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;
import com.truecaller.android.sdk.clients.VerificationCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, AutoPermissionsListener , SMSReceiver.OTPReceiveListener,
        GoogleApiClient.ConnectionCallbacks{
    private ImageButton btn_login;
    private ImageView img_back;
    private ImageButton txt_sign_up;
    private TextView txt_forgot_pwd;
    private TextView txt_account;
    private TextView txt_skip;
    private EditText edt_emailId;
    private EditText edt_password;
    private String mEmailId, mPassword;
    CheckBox cbk_RememberMe;
    FirebaseDatabase database;
    DatabaseReference table_user;
    Dialog dialog_customize_view;
    String userPhone;
    String otp;
    String strLastFourDi;
    String encodedUrl;
    String userPassword;
    private final static int REQUEST_CODE = 999;
  //  ITrueCallback sdkCallback;
 //   VerificationCallback apiCallback;
  GoogleApiClient mGoogleApiClient;
    private int RESOLVE_HINT = 2;
    public static final String TAG = SignInActivity.class.getSimpleName();
    private SMSReceiver smsReceiver;
    Context context;
    private TrueSdkScope trueScope;
    private TrueSDK trueSDK;
    private TrueButton button_trueCaller;
    String str_getMOBILE ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        context = this.context;
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

         trueScope = new TrueSdkScope.Builder(this, sdkCallback)
                .consentMode(TrueSdkScope.CONSENT_MODE_POPUP )
                .consentTitleOption( TrueSdkScope.SDK_CONSENT_TITLE_VERIFY )
                .footerType(TrueSdkScope.FOOTER_TYPE_CONTINUE)
                .consentTitleOption(TrueSdkScope.SDK_CONSENT_TITLE_LOG_IN)
                .build();

        TrueSDK.init(trueScope);
        trueSDK = TrueSDK.getInstance();
      /*  Locale locale = new Locale("en");
        TrueSDK.getInstance().setLocale(locale);*/
        boolean user = TrueSDK.getInstance().isUsable();
        Log.e("type0",""+user);
        if (user) {
            Locale locale = new Locale("en");
            trueSDK.setLocale(locale);
            trueSDK.getUserProfile(this);
        } else {
            getHintPhoneNumber();
        }


        initUI();
        onClickListener();
        AutoPermissions.Companion.loadActivityPermissions(SignInActivity.this, 1);
        // init paper
        Paper.init(this);
        Paper.book().write("sub_new","true");
        // Init firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

        // get mobile number from phone
     //   getHintPhoneNumber();
      //  printKeyHash();
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        // This code requires one time to get Hash keys do comment and share key
        Log.i(TAG, "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

    }

    private void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.kisaann.thedining", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {


        btn_login = findViewById(R.id.btn_login);
        img_back = findViewById(R.id.img_back);
        txt_sign_up = findViewById(R.id.txt_sign_up);
        txt_forgot_pwd = findViewById(R.id.txt_forgot_pwd);
      //  txt_account = findViewById(R.id.txt_account);
        txt_skip = findViewById(R.id.txt_skip);
        edt_password = findViewById(R.id.edt_password);
        edt_emailId = findViewById(R.id.edt_emailId);
        cbk_RememberMe = findViewById(R.id.cbk_RememberMe);
    }
    private void onClickListener() {
        btn_login.setOnClickListener(this);
        txt_sign_up.setOnClickListener(this);
        txt_forgot_pwd.setOnClickListener(this);
        img_back.setOnClickListener(this);
        txt_skip.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String phoneNumber = edt_emailId.getText().toString().trim();
                if(phoneNumber.startsWith("+") || phoneNumber.startsWith("0"))
                {
                    if(phoneNumber.length()==11) {
                        String str_getMOBILE=phoneNumber.substring(1);
                        edt_emailId.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==12) {
                        String str_getMOBILE=phoneNumber.substring(2);
                        edt_emailId.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==13) {
                        String str_getMOBILE=phoneNumber.substring(3);
                        edt_emailId.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==14) {
                        String str_getMOBILE=phoneNumber.substring(4);
                        edt_emailId.setText(str_getMOBILE);
                    } else if(phoneNumber.length()==15) {
                        String str_getMOBILE=phoneNumber.substring(5);
                        edt_emailId.setText(str_getMOBILE);
                    }
                } else {
                    edt_emailId.setText(phoneNumber);
                }
             //   startLoginPage(LoginType.PHONE);
                 userPhone = edt_emailId.getText().toString();
                 userPassword = edt_password.getText().toString();
                 userPassword = "123456";
                SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
                 otp = simple1.format(new Date());
                if (!userPhone.isEmpty()){
                    if (!userPassword.isEmpty()){
                      //  sendSMSAPICall();
                       /* try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(userPhone, null, "From Dining App Login OTP is : "+otp, null, null);
                            Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_LONG).show();

                            verifyOTPDialog(otp, userPhone, userPassword);
                            Log.e("OTP",otp);

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                                    Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }*/
                        if (Utility.isNetworkAvailable(getBaseContext())) {
                            // save user and Pwd
                                    final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                                    mDialog.setMessage("Please wait....");
                                    mDialog.setCancelable(false);
                                    mDialog.show();
                                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // check if user not exist in database
                                            if (dataSnapshot.child(edt_emailId.getText().toString()).exists()) {
                                                // Get user information

                                                User user = dataSnapshot.child(edt_emailId.getText().toString()).getValue(User.class);
                                                user.setPhoneNo(edt_emailId.getText().toString());
                                                if (user.getUserType().equalsIgnoreCase("U")){
                                                    if (user.getStatusType() != null && user.getStatusType().equalsIgnoreCase("Active")) {
                                                        Common.currentUser = user;
                                                        mDialog.dismiss();


                                                        //Your authentication key
                                                        //    String authkey = lastFourDigits+"AobWaYGkqR5d47fcae";
                                                        String authkey = "288318AobWaYGkqR5d47fcae";
                                                        //Multiple mobiles numbers separated by comma
                                                        String mobiles = userPhone;
                                                        //Sender ID,While using route4 sender id should be 6 characters long.
                                                        String senderId = "DINING";
                                                        //Your message to send, Add URL encoding here.
                                                        String message = "Dear Dining app user,\n" + otp +" is your otp for loging in to the dining app. \n" +
                                                                "Power to control your dining experience now in your hands.\n" +
                                                                "Cheers ! ";
                                                        //define route
                                                        try {
                                                            String urlMsg = message;
                                                            encodedUrl = URLEncoder.encode(urlMsg, "UTF-8");
                                                            // System.out.println("Encoded URL " + encodedUrl);
                                                            Log.e("encodedUrl",encodedUrl);
                                                        } catch (UnsupportedEncodingException e) {
                                                            System.err.println(e);
                                                        }
                                                        String route="4";
                                                        // http://smsp.myoperator.co/api/sendhttp.php?authkey=288318AobWaYGkqR5d47fcae&mobiles=8886666847&message=From%20Dining%20App%20Login%20OTP%20is%20%3A%201010%20&sender=DINING&route=4&country=91
                                                        String url = "http://smsp.myoperator.co/api/sendhttp.php?authkey="+authkey+"&mobiles="+mobiles+"&message="
                                                                +encodedUrl+"&sender=DINING&route="+"4"+"&country="+"91";
                                                        Log.e("url ", "" + url);
                                                     //   new SendSMSServiceAPICall().execute(url);

                                                        loginWithOutVerification();

                                                        table_user.removeEventListener(this);
                                                        //          Toast.makeText(SignInActivity.this, "Sign in Successfully !", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        mDialog.dismiss();
                                                        Toast.makeText(SignInActivity.this, "You don't have access to login !!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    mDialog.dismiss();
                                                    Toast.makeText(SignInActivity.this, "You are not a User!", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                mDialog.dismiss();
                                                Toast.makeText(SignInActivity.this, "User not exist ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        }else {
                            Toast.makeText(SignInActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(SignInActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignInActivity.this, "please enter phone number", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.txt_sign_up:
                Intent homeIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                homeIntent.putExtra("from", "signIn");
                startActivity(homeIntent);
                break;
            case R.id.txt_skip:
                Intent homeIntenta = new Intent(SignInActivity.this, MainActivity.class);
                homeIntenta.putExtra("isLogin","false");
                startActivity(homeIntenta);
                finish();
                break;
            case R.id.txt_forgot_pwd:
                startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void loginWithOutVerification() {
        if (Utility.isNetworkAvailable(getBaseContext())) {

            Paper.book().write(Common.USER_TYPE, Common.currentUser.getUserType());
            Paper.book().write(Common.USER_KEY, Common.currentUser.getPhoneNo());
            Paper.book().write(Common.PWD_KEY, Common.currentUser.getPassword());
            Paper.book().write(Common.USER_NAME, Common.currentUser.getFirstName());
            Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
            Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
            Paper.book().write(Common.USER_BAL, Common.currentUser.getBalance());
            Paper.book().write(Common.USER_IMAGE, Common.currentUser.getImageURL());

            updateToken(FirebaseInstanceId.getInstance().getToken());

            Intent homeIntent = new Intent(SignInActivity.this, ScanQRCodeActivity.class);
            homeIntent.putExtra("isLogin","true");
            Paper.book().write(Common.ISLOGIN , "true");
            startActivity(homeIntent);
            finish();
        }else {
            Toast.makeText(SignInActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
            return;
        }
    }

   /* private void startLoginPage(LoginType loginType) {
        if (loginType == LoginType.PHONE){
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                            AccountKitActivity.ResponseType.CODE); // use token when enable
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());
            startActivityForResult(intent,REQUEST_CODE);
        }else if (loginType == LoginType.EMAIL){
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL,
                            AccountKitActivity.ResponseType.TOKEN); // use token when enable
            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());
            startActivityForResult(intent,REQUEST_CODE);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            trueSDK.onActivityResultObtained( this,resultCode, data);
        }catch (RuntimeException e){
            Log.e("error",e.getMessage());
        }
        Log.e("Data",""+data.getData()+" Code"+requestCode);
        if (requestCode == REQUEST_CODE) { // confirm that this response matches your request
            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    "H",
                    Toast.LENGTH_LONG)
                    .show();
        }
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                 //   TrueSDK.getInstance().onActivityResultObtained( this,resultCode, data);
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    String phoneNumber = credential.getId();
                    String name = credential.getName();
                    Log.e("name , number",name +","+phoneNumber);
                    if(phoneNumber.startsWith("+") || phoneNumber.startsWith("0"))
                    {
                        if(phoneNumber.length()==11) {
                            String str_getMOBILE=phoneNumber.substring(1);
                            edt_emailId.setText(str_getMOBILE);
                        } else if(phoneNumber.length()==12) {
                            String str_getMOBILE=phoneNumber.substring(2);
                            edt_emailId.setText(str_getMOBILE);
                        } else if(phoneNumber.length()==13) {
                            String str_getMOBILE=phoneNumber.substring(3);
                            edt_emailId.setText(str_getMOBILE);
                        } else if(phoneNumber.length()==14) {
                            String str_getMOBILE=phoneNumber.substring(4);
                            edt_emailId.setText(str_getMOBILE);
                        } else if(phoneNumber.length()==15) {
                            String str_getMOBILE=phoneNumber.substring(5);
                            edt_emailId.setText(str_getMOBILE);
                        }
                    } else {
                        edt_emailId.setText(phoneNumber);
                    }
                //    edt_emailId.setText(credential.getId());
                }

            }
        }
    }

    private void sendSMSAPICall() {
       // strLastFourDi = userPhone.length() >= 2 ? userPhone.substring(userPhone.length() - 2):
        strLastFourDi = "987725";
        //Your authentication key
       // String authkey = strLastFourDi+"AobWaYGkqR5d47fcae";
        String authkey = "288318AobWaYGkqR5d47fcae";
//Multiple mobiles numbers separated by comma
        String mobiles = userPhone;
//Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "DINING";
//Your message to send, Add URL encoding here.
        String message = "Dear Dining app user,\n" + otp+" is your otp for loging in to the dining app. \n" +
                "Power to control your dining experience now in your hands.\n" +
                "Cheers ! ";
//define route
        String route="4";

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        //encoding message
        String encoded_message= URLEncoder.encode(message);

        //Send SMS API
        String mainUrl="http://smsp.myoperator.co/api/sendhttp.php?";


        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

//final string
        mainUrl = sbPostData.toString();

        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.e("RESPONSE", ""+response);

            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void verifyOTPDialog(String otp, String userPhone, String userPassword) {
        dialog_customize_view = new Dialog(this);
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
        mlly_otp.setVisibility(View.VISIBLE);
        txt_submit.setText("SUBMIT");
        txt_type.setText("Verify Otp");
        txt_submit.setOnClickListener(view -> {
            String otpVerify = edt_otpNo.getText().toString().trim();
            if (!otpVerify.isEmpty()){
                if (otpVerify.equalsIgnoreCase(otp)){
                    if (Utility.isNetworkAvailable(getBaseContext())) {
                        dialog_customize_view.dismiss();

                        Paper.book().write(Common.USER_TYPE, Common.currentUser.getUserType());
                        Paper.book().write(Common.USER_KEY, Common.currentUser.getPhoneNo());
                        Paper.book().write(Common.PWD_KEY, Common.currentUser.getPassword());
                        Paper.book().write(Common.USER_NAME, Common.currentUser.getFirstName());
                        Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
                        Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
                        Paper.book().write(Common.USER_BAL, Common.currentUser.getBalance());
                        Paper.book().write(Common.USER_IMAGE, Common.currentUser.getImageURL());

                        updateToken(FirebaseInstanceId.getInstance().getToken());

                        Intent homeIntent = new Intent(SignInActivity.this, ScanQRCodeActivity.class);
                        homeIntent.putExtra("isLogin","true");
                        Paper.book().write(Common.ISLOGIN , "true");
                        startActivity(homeIntent);
                        finish();
                    }else {
                        Toast.makeText(SignInActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Mismatch OTP", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_LONG).show();
            }
        });
        img_close.setOnClickListener(view -> dialog_customize_view.dismiss());

        dialog_customize_view.show();
    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, "true", userPhone); // false bcz token"
        // +send from
        // client app
        tokens.child(userPhone).setValue(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(SignInActivity.this, requestCode, permissions, SignInActivity.this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {

    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {

    }

    private final ITrueCallback sdkCallback = new ITrueCallback() {

        @Override
        public void onSuccessProfileShared(@NonNull final TrueProfile trueProfile) {

            // This method is invoked when either the truecaller app is installed on the device and the user gives his
            // consent to share his truecaller profile OR when the user has already been verified before on the same
            // device using the same number and hence does not need OTP to verify himself again.
            Log.d( "TAG", "Verified without OTP! (Truecaller User): " + trueProfile.firstName );
            Log.d(TAG, "onSuccessProfileShared: " + trueProfile.phoneNumber);
            Log.d(TAG, "onSuccessProfileShared: " + trueProfile.email);
            Log.d(TAG, "onSuccessProfileShared: " + trueProfile.firstName);
            Log.d(TAG, "onSuccessProfileShared: " + trueProfile.lastName);

            Log.e("name , number",trueProfile.firstName +","+trueProfile.phoneNumber);
           String phoneNumber = trueProfile.phoneNumber;

            if(phoneNumber.startsWith("+"))
            {
                if(phoneNumber.length()==13) {
                     str_getMOBILE = phoneNumber.substring(3);
                    edt_emailId.setText(str_getMOBILE);
                } else if(phoneNumber.length()==14) {
                     str_getMOBILE = phoneNumber.substring(4);
                    edt_emailId.setText(str_getMOBILE);
                } else if(phoneNumber.length()==15) {
                     str_getMOBILE = phoneNumber.substring(5);
                    edt_emailId.setText(str_getMOBILE);
                }
            } else {
                edt_emailId.setText(phoneNumber);
            }

            Log.d(TAG, "onSuccessProfileShared Mobile: " + str_getMOBILE);
            userPhone = str_getMOBILE;

            if (Utility.isNetworkAvailable(getBaseContext())) {
                // save user and Pwd
                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please wait....");
                mDialog.setCancelable(false);
                mDialog.show();
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // check if user not exist in database
                        if (dataSnapshot.child(edt_emailId.getText().toString()).exists()) {
                            // Get user information

                            User user = dataSnapshot.child(edt_emailId.getText().toString()).getValue(User.class);
                            user.setPhoneNo(edt_emailId.getText().toString());
                            if (user.getUserType().equalsIgnoreCase("U")){
                                if (user.getStatusType() != null && user.getStatusType().equalsIgnoreCase("Active")) {
                                    Common.currentUser = user;
                                    mDialog.dismiss();

                                    Paper.book().write(Common.USER_TYPE, Common.currentUser.getUserType());
                                    Paper.book().write(Common.USER_KEY, Common.currentUser.getPhoneNo());
                                    Paper.book().write(Common.PWD_KEY, Common.currentUser.getPassword());
                                    Paper.book().write(Common.USER_NAME, Common.currentUser.getFirstName());
                                    Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
                                    Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
                                    Paper.book().write(Common.USER_BAL, Common.currentUser.getBalance());
                                    Paper.book().write(Common.USER_IMAGE, Common.currentUser.getImageURL());

                                    updateToken(FirebaseInstanceId.getInstance().getToken());

                                    Intent homeIntent = new Intent(SignInActivity.this, ScanQRCodeActivity.class);
                                    homeIntent.putExtra("isLogin","true");
                                    Paper.book().write(Common.ISLOGIN , "true");
                                    startActivity(homeIntent);
                                    finish();

                                    table_user.removeEventListener(this);
                                    //          Toast.makeText(SignInActivity.this, "Sign in Successfully !", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "You don't have access to login !!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                mDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "You are not a User!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exist ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }else {
                Toast.makeText(SignInActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        public void onFailureProfileShared(@NonNull final TrueError trueError) {
            // This method is invoked when some error occurs or if an invalid request for verification is made

            Log.d( "TAG", "onFailureProfileShared: " + trueError.getErrorType() );
        }

        @Override
        public void onVerificationRequired() {

            // This method is invoked when truecaller app is not present on the device or if the user wants to
            // continue with a different number and hence, missed call verification is required to complete the flow
            // You can initiate the missed call verification flow from within this callback method by using :
            Log.e("verifyOTP","VerifyOTP");
            trueSDK.requestVerification("IN", str_getMOBILE, apiCallback);

            //  Here, the first parameter is the country code of the mobile number for which the verification needs to be
            // triggered and PHONE_NUMBER_STRING should be the 10-digit mobile number of the user

        }
    };

    static final VerificationCallback apiCallback = new VerificationCallback() {

        @Override
        public void onRequestSuccess(int requestCode, @Nullable String accessToken) {
            if (requestCode == VerificationCallback.TYPE_OTP) {

                // This method is invoked when the missed call has been triggered successfully to the input mobile
                // number. You can now ask the user to input his first name and last name

                Log.d( "TAG", "Missed Call Triggered" );

            }
        }

        @Override
        public void onRequestFailure(final int requestCode, @NonNull final TrueException e) {

            // Invoked when some error has occured while verifying the provided mobile number via the missed call flow
            Log.d( "TAG", "OnFailureApiCallback: " + e.getExceptionMessage() );
        }
    };

    @Override
    public void onOTPReceived(String otp) {
        showToast("OTP Received: " + otp);

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {
        showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }
    private class SendSMSAPICall extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            //Your authentication key
            String authkey = strLastFourDi+"AobWaYGkqR5d47fcae";
//Multiple mobiles numbers separated by comma
            String mobiles = userPhone;
//Sender ID,While using route4 sender id should be 6 characters long.
            String senderId = "DINING";
//Your message to send, Add URL encoding here.
            String message = "From Dining App Login OTP is : "+otp;
//define route
            String route="4";

            URLConnection myURLConnection= null;
            URL myURL= null;
            BufferedReader reader= null;

            //encoding message
            String encoded_message= URLEncoder.encode(message);

            //Send SMS API
            String mainUrl="http://smsp.myoperator.co/api/sendhttp.php?";


            //Prepare parameter string
            StringBuilder sbPostData= new StringBuilder(mainUrl);
            sbPostData.append("authkey="+authkey);
            sbPostData.append("&mobiles="+mobiles);
            sbPostData.append("&message="+encoded_message);
            sbPostData.append("&route="+route);
            sbPostData.append("&sender="+senderId);

//final string
            mainUrl = sbPostData.toString();
// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                //prepare connection
                myURL = new URL(mainUrl);
                myURLConnection =  myURL.openConnection();
                myURLConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = myURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.e("forecastJsonStr",forecastJsonStr);
                return forecastJsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  tvWeatherJson.setText(s);
            Log.i("json", s);
        }
    }

    private class SendSMSServiceAPICall extends AsyncTask<String, Integer, String>{
      //  private ProgressDialog pdia;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pdia = new ProgressDialog(getApplicationContext());
            pdia.setMessage("Loading...");
            pdia.setCanceledOnTouchOutside(false);
            pdia.show();*/
        }
        @Override
        protected String doInBackground(String... params) {
            String res=PostData(params);
            return res;
        }
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplicationContext(), "Verification code sent to registered mobile number.", Toast.LENGTH_LONG).show();
            verifyOTPDialog(otp, userPhone, userPassword);
            Log.e("OTP",otp);
           // pdia.dismiss();
            Log.e("Result ", "" + result);
            startSMSListener();
        }

    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);
        //    Task<Void> task = SmsRetriever.getClient(context).startSmsUserConsent(senderPhoneNumber /* or null */);
            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String PostData(String[] values) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpGet httpPost=new HttpGet(values[0]);
            HttpResponse httpResponse=  httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);
        }
        catch(Exception e)
        {
            Log.e("Error ", "" + e);
        }
        return s;
    }

    private String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return return_text;
    }
}
