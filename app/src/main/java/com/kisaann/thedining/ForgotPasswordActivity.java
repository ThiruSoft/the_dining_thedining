package com.kisaann.thedining;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisaann.thedining.Utils.Utility;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_back;
    private TextView txt_note;
    private Button btn_forgot_password;
    private MaterialEditText edt_emailId;
    Dialog dialog_customize_view;
    Dialog dialog_customize_reset;
    FirebaseDatabase database;
    DatabaseReference table_user;
    DatabaseReference reference;
    String encodedUrl;
    String userPhone;
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initUI();
        onclickListener();

        // Init firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");
    }
    private void onclickListener() {
        img_back.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
    }

    private void initUI() {
        img_back = findViewById(R.id.img_back);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);
        txt_note = findViewById(R.id.txt_note);
        edt_emailId = findViewById(R.id.edt_emailId);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_forgot_password:
                SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
                 otp = simple1.format(new Date());
                 userPhone = edt_emailId.getText().toString().trim();

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
                new SendSMSServiceAPICall().execute(url);

               /* try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(userPhone, null, "From Dining App Login OTP is : "+otp, null, null);
                    Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_LONG).show();

                    verifyOTPDialog(otp, userPhone);
                    Log.e("OTP",otp);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }*/
                break;
        }
    }
    private void verifyOTPDialog(String otp, String userPhone) {
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
        txt_submit.setOnClickListener(view -> {
            String otpVerify = edt_otpNo.getText().toString().trim();
            if (!otpVerify.isEmpty()){
                if (otpVerify.equalsIgnoreCase(otp)){
                    if (Utility.isNetworkAvailable(getBaseContext())) {
                        dialog_customize_view.dismiss();
                        resetPasswordDialog(userPhone);

                    }else {
                        Toast.makeText(getApplicationContext(), "Please check your network connection. ", Toast.LENGTH_SHORT).show();
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

    private void resetPasswordDialog(String userPhone) {
        dialog_customize_reset = new Dialog(this);
        if (dialog_customize_reset.getWindow() != null) {
            dialog_customize_reset.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_reset.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_reset.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_reset.setContentView(R.layout.reset_password_custom_dialog);
        dialog_customize_reset.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_reset.setCancelable(false); // can dismiss alert screen when user click back buttonon
        dialog_customize_reset.setCanceledOnTouchOutside(false);
        dialog_customize_reset.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_reset.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_customize_reset.setCancelable(true);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        TextView txt_type = dialog_customize_reset.findViewById(R.id.txt_type);
        TextView txt_submit = dialog_customize_reset.findViewById(R.id.txt_submit);
        ImageView img_close = dialog_customize_reset.findViewById(R.id.img_close);
        MaterialEditText edt_newPassword = dialog_customize_reset.findViewById(R.id.edt_newPassword);
        MaterialEditText edt_rePassword = dialog_customize_reset.findViewById(R.id.edt_rePassword);

        txt_submit.setOnClickListener(v -> {
            String newPassword = edt_newPassword.getText().toString().trim();
            String rePassword = edt_rePassword.getText().toString().trim();
            if (!newPassword.isEmpty()){
                if (!rePassword.isEmpty()){
                    if (newPassword.equalsIgnoreCase(rePassword)){
                        if (Utility.isNetworkAvailable(getBaseContext())) {

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userPhone);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("password", newPassword);
                            reference.updateChildren(hashMap);
                            dialog_customize_reset.dismiss();
                            Toast.makeText(getApplicationContext(), "Password reset successful ", Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            Toast.makeText(getApplicationContext(), "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Enter re-password", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Enter new password", Toast.LENGTH_LONG).show();
            }
        });
        img_close.setOnClickListener(v -> dialog_customize_reset.dismiss());

        dialog_customize_reset.show();
    }

    private class SendSMSServiceAPICall extends AsyncTask<String, Integer, String> {
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
            verifyOTPDialog(otp, userPhone);
            Log.e("OTP",otp);
            // pdia.dismiss();
            Log.e("Result ", "" + result);
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
