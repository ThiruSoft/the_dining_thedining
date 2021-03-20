package com.kisaann.thedining;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.BookTableModel;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.QRCodeModel;
import com.kisaann.thedining.Models.Rating;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Models.VerifyOtpModel;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Utils.Utility;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsActivity extends AppCompatActivity implements RatingDialogListener {

    String restId;
    String restName;
    String restImage;
    String restInfo;
    String restNo;
    String restOwnerNo;
    TextView txt_rating;
    TextView txt_ratingCount;
    TextView txt_AboutDetails;
    TextView txt_About;
    LinearLayout lly_ratings;

    ImageView img_restaurant;
    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference foodsAll;
    DatabaseReference ratingTbl;
    FloatingActionButton btn_rating;
    RatingBar ratingBar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout layout1 ;
    RelativeLayout lly_reserveTable;
    RelativeLayout lly_myBookings;
    RelativeLayout lly_onlineOrder;
    Dialog dialog_customize_view;
    Dialog reserve_customize_view;
    private int mYear, mMonth, mDay, mHour, mMinute;

    DatabaseReference bookRequests;
    DatabaseReference bookRequestRestaurant;
    String ownerNo;
    APIService mService;
    String userNo;
    String kitchenNo;
    DatabaseReference restaurantQRCodeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        // init paper
        Paper.init(this);
        // init service
        mService = Common.getFCMService();
        if (getIntent() != null){
            restId = getIntent().getStringExtra("RestaurantMenuId");
            restName = getIntent().getStringExtra("RestaurantName");
            restImage = getIntent().getStringExtra("RestaurantImage");
            restInfo = getIntent().getStringExtra("RestaurantInfo");
            restNo = getIntent().getStringExtra("RestaurantNo");
            restOwnerNo = getIntent().getStringExtra("RestaurantOwnerNo");
        }
        userNo = Paper.book().read(Common.USER_KEY);
        // init firebase
        database = FirebaseDatabase.getInstance();
        restaurantQRCodeList = database.getReference("RestaurantQRCode");
        ratingTbl = database.getReference("Ratings");
        bookRequests = database.getReference("BookRequests");
        bookRequestRestaurant = database.getReference(restId+"_"+"BookRequests");

        btn_rating = (FloatingActionButton)findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        img_restaurant = (ImageView) findViewById(R.id.img_restaurant);
        txt_rating = findViewById(R.id.txt_rating);
        txt_ratingCount = findViewById(R.id.txt_ratingCount);
        txt_AboutDetails = findViewById(R.id.txt_AboutDetails);
        txt_About = findViewById(R.id.txt_About);
        lly_ratings = findViewById(R.id.lly_ratings);
        lly_reserveTable = findViewById(R.id.lly_reserveTable);
        lly_myBookings = findViewById(R.id.lly_myBookings);
        lly_onlineOrder = findViewById(R.id.lly_onlineOrder);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAdapter);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAdapter);
        collapsingToolbarLayout.setTitle(restName);
        Glide.with(getApplicationContext()).load(restImage).into(img_restaurant);
        txt_About.setText(restInfo);

        btn_rating.setOnClickListener(v -> showRatingDialog());
        txt_AboutDetails.setOnClickListener(v -> restaurantAboutDialog());
        lly_reserveTable.setOnClickListener(v -> restaurantTableBookDialog());
        lly_myBookings.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailsActivity.this,MyBookingsActivity.class);
            intent.putExtra("phoneNo",userNo);
            intent.putExtra("restId",restId);
            startActivity(intent);
        });

        lly_onlineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query listQRCodesId = restaurantQRCodeList.orderByChild("restaurantOwnerNo").equalTo(restOwnerNo);
                listQRCodesId.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            QRCodeModel qrCodeModel = snapshot.getValue(QRCodeModel.class);
                            assert qrCodeModel != null;
                            /*if (qrCodeModel.getQrName().equalsIgnoreCase("Online")) {
                                kitchenNo = qrCodeModel.getRestaurantKitchenNo();
                                Log.e("kitchenNo",""+kitchenNo);
                            }*/
                            Log.e("getQrName",""+qrCodeModel.getQrName());
                        }
                        listQRCodesId.removeEventListener(this);

                       // Paper.book().write(Common.KITCHEN_NO, ""+kitchenNo);
                        Paper.book().write(Common.KITCHEN_NO, "9985130462");
                        Paper.book().write(Common.OWNER_NO, restOwnerNo);
                        Paper.book().write(Common.TABLE_NO, "Online");
                        //Paper.book().write(Common.COMPLETE_ORDER, "No");
                        Paper.book().write(Common.RESTAURANT_ID, restId);

                        Intent homeIntent = new Intent(RestaurantDetailsActivity.this, RestWelcomeActivity.class);
                        homeIntent.putExtra("isLogin","true");
                        homeIntent.putExtra("RestaurantMenuId",restId);
                        startActivity(homeIntent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        if (!restId.isEmpty() ){
            if (Utility.isNetworkAvailable(getBaseContext())) {
                //  getFoodDetails(restId);
                getRatingFood(restId);
            }else {
                Toast.makeText(RestaurantDetailsActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void restaurantTableBookDialog() {
        reserve_customize_view = new Dialog(this);
        if (reserve_customize_view.getWindow() != null) {
            reserve_customize_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            reserve_customize_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        reserve_customize_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reserve_customize_view.setContentView(R.layout.book_restaurant_table_dialog);
        reserve_customize_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        reserve_customize_view.setCancelable(true); // can dismiss alert screen when user click back buttonon
        reserve_customize_view.setCanceledOnTouchOutside(true);
        reserve_customize_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = reserve_customize_view.getWindow();
        lp1.copyFrom(window1.getAttributes());
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        TextView txt_type = reserve_customize_view.findViewById(R.id.txt_type);
        MaterialEditText edt_date = reserve_customize_view.findViewById(R.id.edt_date);
        MaterialEditText edt_time = reserve_customize_view.findViewById(R.id.edt_time);
        MaterialEditText edt_noOfPeople = reserve_customize_view.findViewById(R.id.edt_noOfPeople);
        TextView txt_submit = reserve_customize_view.findViewById(R.id.txt_submit);
        edt_time.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(RestaurantDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    edt_time.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        edt_date.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(final DatePicker view, final int year, final int month,
                                              final int dayOfMonth) {

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            c.set(year, month, dayOfMonth);
                            String dateString = sdf.format(c.getTime());
                            edt_date.setText(dateString); // set the date
                        }
                    }, mYear, mMonth, mDay); // set date picker to current date
            datePickerDialog.show();
        });
        txt_submit.setOnClickListener(v -> {
            String bookDate = edt_date.getText().toString();
            String bookTime = edt_time.getText().toString();
            String noOfPeople = edt_noOfPeople.getText().toString();

            if (!noOfPeople.isEmpty()){
                if (!bookDate.isEmpty()){
                    if (!bookTime.isEmpty()){
                        String mobileNo = userNo;
                        String strLastFourDi = mobileNo.length() >= 4 ? mobileNo.substring(mobileNo.length() - 4): "";
                        SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
                        String key1 = simple1.format(new Date());
                        String bookId = "#DNGB"+strLastFourDi+""+key1;

                        String trans_number = String.valueOf(System.currentTimeMillis());
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                        String time = dateFormat.format(new Date());
                        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                           /* SimpleDateFormat simpleDate = new SimpleDateFormat
                                                    ("dd-MM-yyyy");*/
                        String date = simpleDate.format(new Date());
                        SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
                        String month = simpleMonth.format(new Date());
                        SimpleDateFormat simpleMYear = new SimpleDateFormat("yyyy");
                        String year = simpleMYear.format(new Date());

                        ownerNo = Paper.book().read(Common.OWNER_NO);
                        BookTableModel bookTableModel = new BookTableModel(bookId,Common.currentUser.getFirstName(),
                                userNo,bookDate,bookTime,noOfPeople,restName,"",time,date,month,year,"0","0","Guest",restId);

                        bookRequests.child(trans_number)
                                .setValue(bookTableModel);
                        bookRequestRestaurant.child(trans_number)
                                .setValue(bookTableModel);
                        sendNotificationBookOrder(bookId);
                        reserve_customize_view.dismiss();
                    }else {
                        Toast.makeText(RestaurantDetailsActivity.this, "Please select time ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RestaurantDetailsActivity.this, "Please select date ", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RestaurantDetailsActivity.this, "Please enter no of people ", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView img_close = reserve_customize_view.findViewById(R.id.img_close);

        img_close.setOnClickListener(v -> reserve_customize_view.dismiss());
        reserve_customize_view.show();
    }
    private void sendNotificationBookOrder(String bookId) {
        DatabaseReference tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo(restOwnerNo);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","Dining");
                    dataSend.put("message","You have a new Booking Id : " + bookId +"<?>"+ bookId+"<?>"+"BookOrder"+"<?>"+".");
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
                                            //  Toast.makeText(RestaurantDetailsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(RestaurantDetailsActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
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
        });
    }
    private void restaurantAboutDialog() {
        dialog_customize_view = new Dialog(this);
        if (dialog_customize_view.getWindow() != null) {
            dialog_customize_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_view.setContentView(R.layout.restaurant_about_dialog);
        dialog_customize_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_view.setCancelable(true); // can dismiss alert screen when user click back buttonon
        dialog_customize_view.setCanceledOnTouchOutside(true);
        dialog_customize_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_view.getWindow();
        lp1.copyFrom(window1.getAttributes());
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        TextView txt_type = dialog_customize_view.findViewById(R.id.txt_type);
        TextView txt_submit = dialog_customize_view.findViewById(R.id.txt_submit);
        TextView txt_about = dialog_customize_view.findViewById(R.id.txt_about);
        txt_about.setText(restInfo);
        ImageView img_close = dialog_customize_view.findViewById(R.id.img_close);

        img_close.setOnClickListener(v -> dialog_customize_view.dismiss());
        dialog_customize_view.show();
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comments here....")
                .setHintTextColor(R.color.colorPrimary)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(RestaurantDetailsActivity.this)
                .show();
    }
    private void getRatingFood(String restId) {
        lly_ratings.removeAllViews();
        Query foodRating = ratingTbl.orderByChild("restId").equalTo(restId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postShapshot:dataSnapshot.getChildren()){
                    Rating item = postShapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                    txt_ratingCount.setText(""+count+" Ratings");

                    layout1 = (LinearLayout)getLayoutInflater().inflate(R.layout.ratings_item_layout, lly_ratings, false);
                    final TextView txt_name = layout1.findViewById(R.id.txt_name);
                    final RatingBar ratingBar = layout1.findViewById(R.id.ratingBar);
                    final TextView txt_description = layout1.findViewById(R.id.txt_description);
                    txt_name.setText(item.getUserName());
                    txt_description.setText(item.getComment());
                    ratingBar.setRating(Float.parseFloat(item.getRateValue()));

                    lly_ratings.addView(layout1);
                }
                if(count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);
                    txt_rating.setText(""+average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

    }
}
