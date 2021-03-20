package com.kisaann.thedining;

import android.content.Intent;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Models.Food;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Models.Rating;
import com.kisaann.thedining.Utils.Utility;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.DecimalFormat;
import java.util.Arrays;

import io.paperdb.Paper;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener {

    TextView food_name, txt_itemPrice, food_description;
    TextView txt_itemOriginalPrice,txt_discount, food_information;
    ImageView food_image;
    Button btnShowComment;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btn_rating;
    CounterFab btnCart;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;
    String foodId = "";
    String categoryName = "";
    String restaurantId = "";

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference foodsAll;
    DatabaseReference ratingTbl;
    Food currentFood;
    String userNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);


        // Get food id from intent
        if (getIntent() != null){
            foodId = getIntent().getStringExtra("FoodId");
            categoryName = getIntent().getStringExtra("CategoryName");
            restaurantId = getIntent().getStringExtra("RestaurantId");

        }
        // init paper
        Paper.init(this);
        // init firebase

        database = FirebaseDatabase.getInstance();
        foods = database.getReference(restaurantId+"_"+categoryName);
        ratingTbl = database.getReference("Rating");
        userNo = Paper.book().read(Common.USER_KEY);
        // init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (CounterFab)findViewById(R.id.btnCart);
        btn_rating = (FloatingActionButton)findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnShowComment = (Button) findViewById(R.id.btnShowComment);
        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetails.this, ShowComment.class);
                intent.putExtra(Common.INTENT_FOOD_ID, foodId);
                startActivity(intent);
            }
        });
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        userNo,
                        foodId,currentFood.getName(),
                        numberButton.getNumber(),currentFood.getPrice(),currentFood.getDiscount()
                        ,currentFood.getImage(),currentFood.getCategoryType(),
                        currentFood.getCategoryName(),currentFood.getType(),currentFood.getDepartment(),
                        currentFood.getParticular(),currentFood.getGst(),currentFood.getHappyHour(),
                        currentFood.getKitchen(),currentFood.getCaptainName()));

                Toast.makeText(FoodDetails.this,"Added to cart",Toast.LENGTH_SHORT).show();

            }
        });

        food_description = (TextView)findViewById(R.id.food_description);
        food_information = (TextView)findViewById(R.id.food_information);
        food_name = (TextView)findViewById(R.id.food_name);
        txt_itemPrice = (TextView)findViewById(R.id.txt_itemPrice);
        txt_itemOriginalPrice = (TextView)findViewById(R.id.txt_itemOriginalPrice);
        txt_discount = (TextView)findViewById(R.id.txt_discount);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAdapter);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAdapter);

        if (!foodId.isEmpty() ){
            if (Utility.isNetworkAvailable(getBaseContext())) {
                getFoodDetails(foodId);
                getRatingFood(foodId);
            }else {
                Toast.makeText(FoodDetails.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void getRatingFood(String foodId) {
        Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postShapshot:dataSnapshot.getChildren()){
                    Rating item = postShapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                .create(FoodDetails.this)
                .show();
    }

    private void getFoodDetails(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Double original_price = Double.parseDouble(currentFood.getPrice());
                int discount = Integer.parseInt(currentFood.getDiscount());
                Double discount_price = (original_price / 100.0f) * discount;
                Double servicePrice = Double.parseDouble(currentFood.getPrice()) - discount_price;
                String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                /* Picasso.get().load(currentFood.getImage()).into(food_image);*/
                Glide.with(getApplicationContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                txt_itemOriginalPrice.setText(getResources().getString(R.string.Rs)+""+currentFood.getPrice());
                txt_itemPrice.setText(getResources().getString(R.string.Rs)+""+str_price);
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
                food_information.setText(currentFood.getInformation());
                txt_discount.setText(" "+currentFood.getDiscount() +" Off");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        // get rating and upload to firebase
       /* final Rating rating = new Rating(Common.currentUser.getPhoneNo(),
                foodId,String.valueOf(value),comments);
        // user can give multile time
        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(FoodDetails.this,"Thank you for submit rating...",Toast.LENGTH_SHORT).show();
                    }
                });*/
       /* ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()){
                    //remove old value(you can delete or let it be useless function :D)
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                    // update new value
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }else {
                    // update new value
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                    Toast.makeText(FoodDetails.this,"Thank you for submit rating...",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
