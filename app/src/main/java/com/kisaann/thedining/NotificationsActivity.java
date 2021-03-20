package com.kisaann.thedining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.Models.OffersCouponsModel;
import com.kisaann.thedining.ViewHolders.OffersMenuHolder;

public class NotificationsActivity extends AppCompatActivity {

    OffersCouponsModel offersCouponsModel;
    DatabaseReference offersCouponsForms;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter<OffersCouponsModel, OffersMenuHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // init firebase
        database = FirebaseDatabase.getInstance();
        offersCouponsForms = database.getReference("News&Block");
        // init view
        recyclerView = (RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadNotificationsMenuData();
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
    private void loadNotificationsMenuData() {
        Query listFoodByCategoryId = offersCouponsForms.orderByChild("type").equalTo("O");
        FirebaseRecyclerOptions<OffersCouponsModel> options = new FirebaseRecyclerOptions.Builder<OffersCouponsModel>()
                .setQuery(listFoodByCategoryId,OffersCouponsModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<OffersCouponsModel, OffersMenuHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OffersMenuHolder viewHolder, int position, @NonNull OffersCouponsModel model) {
                viewHolder.txt_title.setText(model.getOfferTitle());
                viewHolder.txt_description.setText(model.getOfferDescription());
                viewHolder.txt_code.setVisibility(View.GONE);
                viewHolder.txt_dateTime.setText(model.getDateTime());
                /*Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);*/
                final OffersCouponsModel clickItem  = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public OffersMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_offers_coupons,parent,false);

                return new OffersMenuHolder(itemView);
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
}
