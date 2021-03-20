package com.kisaann.thedining;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.Models.RestaurantMenuModel;
import com.kisaann.thedining.ViewHolders.RestaurantDiscMenusHolder;

public class RestaurantDiscoveryActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference restaurantMenu;
    FirebaseRecyclerAdapter<RestaurantMenuModel, RestaurantDiscMenusHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_discovery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Restaurants");
        getSupportActionBar().setHomeButtonEnabled(true);

        // init firebase
        database = FirebaseDatabase.getInstance();
        restaurantMenu = database.getReference("RestaurantMenu");

        recyclerView = findViewById(R.id.recycler_menu);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setReverseLayout(true);
        layoutManage.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
        /*// init view
        recyclerView = (RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
        loadMenuData();

    }
    private void loadMenuData() {
        Query searchByUser = restaurantMenu.orderByChild("status").equalTo("Active");
        FirebaseRecyclerOptions<RestaurantMenuModel> options = new FirebaseRecyclerOptions.Builder<RestaurantMenuModel>()
                .setQuery(searchByUser,RestaurantMenuModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<RestaurantMenuModel, RestaurantDiscMenusHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantDiscMenusHolder viewHolder, int position, @NonNull RestaurantMenuModel model) {
                viewHolder.txtMenuName.setText(model.getName());
                Glide.with(getApplicationContext()).load(model.getImage()).into(viewHolder.imageView);
                final RestaurantMenuModel clickItem  = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent foodList = new Intent(RestaurantDiscoveryActivity.this, RestaurantDetailsActivity.class);
                        // Bcz categoryId is key, so we just get key of this item
                        foodList.putExtra("RestaurantMenuId", adapter.getRef(position).getKey());
                        foodList.putExtra("RestaurantName", clickItem.getName());
                        foodList.putExtra("RestaurantImage", clickItem.getImage());
                        foodList.putExtra("RestaurantInfo", clickItem.getAbout());
                        foodList.putExtra("RestaurantNo", clickItem.getRestaurantNo());
                        foodList.putExtra("RestaurantOwnerNo", clickItem.getRestaurantOwnerNo());
                        startActivity(foodList);

                    }
                });
            }

            @NonNull
            @Override
            public RestaurantDiscMenusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item,parent,false);

                return new RestaurantDiscMenusHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
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
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
