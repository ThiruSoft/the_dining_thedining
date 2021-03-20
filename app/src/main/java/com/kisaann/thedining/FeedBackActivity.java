package com.kisaann.thedining;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.PaymentConfirmModel;
import com.kisaann.thedining.Models.Rating;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import io.paperdb.Paper;

public class FeedBackActivity extends AppCompatActivity implements RatingDialogListener {
    FirebaseDatabase database;
    DatabaseReference ratingTbl;
    DatabaseReference ratingRestaurantTbl;
    String orderId;
    String restId;
    String userNo;
    String ownerNo;
    String userName;
    DatabaseReference restaurantConfirmPayment;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setHomeButtonEnabled(true);
// init paper
        Paper.init(this);
        if (getIntent() != null){
            orderId = getIntent().getStringExtra("orderId");
            restId = getIntent().getStringExtra("restId");
            ownerNo = getIntent().getStringExtra("ownerNo");
            Log.e("orderId",orderId);
            Log.e("restId",restId);
            Log.e("ownerNo",ownerNo);
        }
        // init firebase
        userNo = Paper.book().read(Common.USER_KEY);
        userName = Paper.book().read(Common.USER_NAME);
        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Ratings");
        ratingRestaurantTbl = database.getReference(restId+"_"+"Ratings");
        restaurantConfirmPayment = database.getReference(restId+"_"+ownerNo+"_ConfirmPayment");
        showRatingDialog();

    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good", "Excellent"))
                .setDefaultRating(3)
                .setTitle("Rate your experience")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.black)
                .setDescriptionTextColor(R.color.gray)
                .setHint("Please write your comments here....")
                .setHintTextColor(R.color.colorPrimary)
                .setCommentTextColor(android.R.color.black)
                .setCommentBackgroundColor(R.color.gray)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FeedBackActivity.this)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onNegativeButtonClicked() {
        new Thread(){
            public void run(){
                try {
                    sleep(3000);
                    Intent signIn = new Intent(FeedBackActivity.this, SplashScreen.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signIn);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = dateFormat.format(new Date());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                           /* SimpleDateFormat simpleDate = new SimpleDateFormat
                                                    ("dd-MM-yyyy");*/
        String date = simpleDate.format(new Date());

        restaurantConfirmPayment.orderByChild("orderId").equalTo(orderId);
        restaurantConfirmPayment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PaymentConfirmModel requestAB = snapshot.getValue(PaymentConfirmModel.class);
                    String keyAB = snapshot.getRef().getKey();
                    if (orderId.equalsIgnoreCase(requestAB.getOrderId())) {
                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_ConfirmPayment").child(keyAB);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("rateValue", ""+value);
                        hashMap.put("rateComments", ""+comments);
                        reference.updateChildren(hashMap);
                    }
                }
                restaurantConfirmPayment.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String trans_number = String.valueOf(System.currentTimeMillis());
        final Rating rating = new Rating(userNo,
                userName,restId,String.valueOf(value),comments,date,time,orderId);

        ratingRestaurantTbl.child(trans_number).setValue(rating);

        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(FeedBackActivity.this,"Thank you for your feedback.",Toast.LENGTH_SHORT).show();

                        new Thread(){
                            public void run(){
                                try {
                                    sleep(3000);
                                    Intent signIn = new Intent(FeedBackActivity.this, SplashScreen.class);
                                    signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(signIn);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FailureRating",e.getMessage());
                new Thread(){
                    public void run(){
                        try {
                            sleep(3000);
                            Intent signIn = new Intent(FeedBackActivity.this, SplashScreen.class);
                            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(signIn);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


    }
   /* @Override
    public void onBackPressed() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FeedBackActivity.this);
            alertDialog.setTitle("Please Wait.....");
            alertDialog.setMessage("Updating information.....");
            alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            LayoutInflater inflater = LayoutInflater.from(this);

            alertDialog.setPositiveButton("Okay", (dialog, which) -> {
                dialog.dismiss();

            });
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            *//*super.onBackPressed();*//*

    }*/
}
