package com.kisaann.thedining;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.User;
import com.kisaann.thedining.Utils.Utility;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");
        // init paper
        Paper.init(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        if (Utility.isNetworkAvailable(getBaseContext())) {
            new Thread(){
                public void run(){
                    try {
                        sleep(3000);
                        // check rememberMe
                        String user = Paper.book().read(Common.USER_KEY);
                        String userPwd = Paper.book().read(Common.PWD_KEY);
                        String userType = Paper.book().read(Common.USER_TYPE);
                        if (user != null && userPwd != null) {
                            if (!user.isEmpty() && !userPwd.isEmpty()) {
                                loginUser(user, userPwd, userType);
                            }else {
                                Intent homeIntent = new Intent(SplashScreen.this, SignInActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }
                        }else {
                            Intent homeIntent = new Intent(SplashScreen.this, SignInActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }else {
            Toast.makeText(SplashScreen.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
        }
    }
    private void loginUser(final String userID, final String userPwd, String userType) {

        if (Utility.isNetworkAvailable(getBaseContext())) {
           /* final ProgressDialog mDialog = new ProgressDialog(SplashScreen.this);
            mDialog.setMessage("Please wait....");
            mDialog.show();*/

            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // check if user not exist in database
                    if (dataSnapshot.child(userID).exists()) {
                        // Get user information

                        User user = dataSnapshot.child(userID).getValue(User.class);
                        user.setPhoneNo(userID);
                        if (user.getPassword().equals(userPwd)) {
                            Common.currentUser = user;

                            /*mDialog.dismiss();*/
                            if (Common.currentUser.getUserType().equalsIgnoreCase("U")) {
                                if (user.getStatusType() != null && user.getStatusType().equalsIgnoreCase("Active")) {
                                Paper.book().write(Common.USER_NAME, Common.currentUser.getFirstName());
                                Paper.book().write(Common.USER_EMAIL, Common.currentUser.getEmailId());
                                Paper.book().write(Common.USER_BAL, Common.currentUser.getBalance());
                                Paper.book().write(Common.USER_IMAGE, Common.currentUser.getImageURL());

                                String completeOrder = Paper.book().read(Common.COMPLETE_ORDER);

                                if (completeOrder != null && !completeOrder.isEmpty() && completeOrder.equalsIgnoreCase("No")){
                                    String restaurantMenuId = Paper.book().read(Common.RESTAURANT_ID);
                                    Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                                    homeIntent.putExtra("isLogin","true");
                                    homeIntent.putExtra("RestaurantMenuId",restaurantMenuId);
                                    startActivity(homeIntent);
                                    finish();

                                }else {
                                    Intent homeIntent = new Intent(SplashScreen.this, ScanQRCodeActivity.class);
                                    homeIntent.putExtra("isLogin","true");
                                    startActivity(homeIntent);
                                    finish();
                                }
                                } else {
                                    Toast.makeText(SplashScreen.this, "You don't have access to login !!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(SplashScreen.this, "Your not User!!!", Toast.LENGTH_SHORT).show();
                            }
                            table_user.removeEventListener(this);
                            /* Toast.makeText(Signin.this, "Sign in Successfully !", Toast.LENGTH_SHORT).show();*/
                        } else {
                            /*mDialog.dismiss();*/
                            Toast.makeText(SplashScreen.this, "Wrong password !!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        /* mDialog.dismiss();*/
                        Toast.makeText(SplashScreen.this, "User not exist ", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }/*else {
            Toast.makeText(SplashScreen.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
        }*/
    }
}
