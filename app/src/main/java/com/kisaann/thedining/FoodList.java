package com.kisaann.thedining;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Models.Food;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.ViewHolders.FoodItemViewHolder;
import com.kisaann.thedining.ViewHolders.FoodViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference foodListAll;
    FirebaseStorage storage;
    StorageReference storageReference;
    private int cartQuantity = 1;
    String categoryId = "";
    String categoryName = "";
    String restaurantId = "";
    FirebaseRecyclerAdapter<Food, FoodItemViewHolder> adapter;
    // Search functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    /* MaterialSearchBar materialSearchBar;*/
    FloatingActionButton fab;

    MaterialEditText edtFoodName,edtDescription,edtPrice, edtDiscount;
    Button btnSelect, btnUpload;
    TextView txt_viewCart, txt_cartPrice, txt_price;

    Food newFood;
    Uri saveUri;
    Dialog dialog_customize_view;
    DrawerLayout drawer;
    Query listFoodByCategoryId;
    String userNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Management");
        getSupportActionBar().setHomeButtonEnabled(true);
        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
            categoryName = getIntent().getStringExtra("CategoryName");
            restaurantId = getIntent().getStringExtra("RestaurantId");
        }
// init paper
        Paper.init(this);
        // init firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference(restaurantId+"_"+categoryName);
        foodListAll = database.getReference(restaurantId+"_"+"All");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userNo = Paper.book().read(Common.USER_KEY);
        // init UI

        txt_viewCart = findViewById(R.id.txt_viewCart);
        txt_cartPrice = findViewById(R.id.txt_cartPrice);
        txt_price = findViewById(R.id.txt_price);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*loadAddFoodDialog();*/
                startActivity(new Intent(FoodList.this,CartItemsActivity.class));
            }
        });
        txt_viewCart.setOnClickListener(v ->
                startActivity(new Intent(FoodList.this,CartItemsActivity.class)));

        if (!restaurantId.isEmpty()){
            loadFoodList(restaurantId);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.startListening();
        }
    }
    private void loadFoodList(String restaurantId) {
        if (categoryName.equalsIgnoreCase("All")){
            listFoodByCategoryId = foodListAll.orderByChild("restaurantId").equalTo(restaurantId);
        }else {
            listFoodByCategoryId = foodList.orderByChild("restaurantId").equalTo(restaurantId);
        }
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(listFoodByCategoryId,Food.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodItemViewHolder viewHolder, int position, @NonNull Food model) {
                viewHolder.txt_itemName.setText(model.getName());
                viewHolder.txt_itemOriginalPrice.setText(getResources().getString(R.string.Rs)+""+model.getPrice());
                viewHolder.txt_discount.setText(" "+model.getDiscount() +" "+"Off");

                Double original_price = Double.parseDouble(model.getPrice());
                int discount = Integer.parseInt(model.getDiscount());
                Double discount_price = (original_price / 100.0f) * discount;
                Double servicePrice = Double.parseDouble(model.getPrice()) - discount_price;
                String str_price = "" + new DecimalFormat("##.##").format(servicePrice);
                viewHolder.txt_itemPrice.setText(getResources().getString(R.string.Rs)+" "+str_price);
                Glide.with(getApplicationContext()).load(model.getImage()).into(viewHolder.img_itemImage);

                viewHolder.btn_addToCart.setId(position);
                viewHolder.btnDecreaseCart.setId(position);
                viewHolder.btnIncreaseCart.setId(position);
                viewHolder.img_itemImage.setId(position);
                viewHolder.txtQuantityCart.setId(position);
                viewHolder.btn_addToCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    new Database(getBaseContext()).addToCart(new Order(
                            userNo,
                            adapter.getRef(position).getKey(),model.getName(),
                            "1",model.getPrice(),model.getDiscount()
                            ,model.getImage(),model.getCategoryType(),
                            model.getCategoryName(),model.getType(),model.getDepartment(),model.getParticular(),
                            model.getGst(),model.getHappyHour(),model.getKitchen(),model.getCaptainName()));
                    int count = new Database(getBaseContext()).getCountCart(userNo);

                    Double price = Double.parseDouble(txt_price.getText().toString());
                    Double price1 = Double.parseDouble(str_price);
                    Double total = price + price1;
                    txt_price.setText(String.valueOf(total));
                    txt_cartPrice.setText(" "+count +"  Item | "+total);

                    viewHolder.mLlyCartCount.setVisibility(View.VISIBLE);
                    viewHolder.btn_addToCart.setVisibility(View.GONE);


                });
                viewHolder.btnDecreaseCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    String quantityValue = viewHolder.txtQuantityCart.getText().toString();
                    cartQuantity = Integer.parseInt(quantityValue);
                    if (cartQuantity <= 1){
                        viewHolder.mLlyCartCount.setVisibility(View.GONE);
                        viewHolder.btn_addToCart.setVisibility(View.VISIBLE);
                    }else {
                        cartQuantity = cartQuantity - 1;
                        Double price = Double.parseDouble(txt_price.getText().toString());
                        Double price1 = Double.parseDouble(str_price);
                        Double total = price + price1;
                        txt_price.setText(String.valueOf(total));


                        new Database(getBaseContext()).decreaseCart(userNo,adapter.getRef(position).getKey());
                        viewHolder.txtQuantityCart.setText(String.valueOf(cartQuantity));

                        int count1 = new Database(getBaseContext()).getCountCart(userNo);
                        txt_cartPrice.setText(" "+count1 +"  Item | "+total);
                    }
                });
                viewHolder.btnIncreaseCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    String quantityValue = viewHolder.txtQuantityCart.getText().toString();
                    cartQuantity = Integer.parseInt(quantityValue);
                    cartQuantity = cartQuantity+1;

                    Double price = Double.parseDouble(txt_price.getText().toString());
                    Double price1 = Double.parseDouble(str_price);
                    Double total = price + price1;
                    txt_price.setText(String.valueOf(total));
                    txt_cartPrice.setText(String.valueOf(total));

                    new Database(getBaseContext()).increaseCart(userNo,adapter.getRef(position).getKey());
                    /*new Database(getBaseContext()).updateCart(cartQuantity,
                            userNo,adapter.getRef(position).getKey());*/
                    viewHolder.txtQuantityCart.setText(String.valueOf(cartQuantity));

                    int count2 = new Database(getBaseContext()).getCountCart(userNo);
                    txt_cartPrice.setText(" "+count2 +"  Item | "+total);
                });
                viewHolder.img_itemImage.setOnClickListener(view -> {
                    Intent foodDetails = new Intent(FoodList.this, FoodDetails.class);
                    foodDetails.putExtra("FoodId",adapter.getRef(position).getKey());
                    foodDetails.putExtra("RestaurantId",restaurantId);
                    foodDetails.putExtra("CategoryName",categoryName);
                    startActivity(foodDetails);
                });
               /* final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       *//* Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();*//*
                        // start new activity
                        Intent foodDetails = new Intent(FoodList.this, FoodDetails.class);
                        foodDetails.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetails);
                    }
                });*/
            }

            @NonNull
            @Override
            public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item_layout,parent,false);

                return new FoodItemViewHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            /* showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));*/
        }else if (item.getTitle().equals(Common.DETETE)){
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);

    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
        Toast.makeText(FoodList.this,"Item Deleted !!", Toast.LENGTH_SHORT).show();
    }

}
