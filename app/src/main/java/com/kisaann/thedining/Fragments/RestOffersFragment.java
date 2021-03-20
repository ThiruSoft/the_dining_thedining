package com.kisaann.thedining.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.Models.OffersCouponsModel;
import com.kisaann.thedining.R;
import com.kisaann.thedining.ViewHolders.OffersMenuHolder;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestOffersFragment extends Fragment {

    OffersCouponsModel offersCouponsModel;
    DatabaseReference offersCouponsForms;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter<OffersCouponsModel, OffersMenuHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String restId;
    String userNo;
    public RestOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rest_offers, container, false);
        // init paper
        Paper.init(getActivity());
        restId = Paper.book().read(Common.RESTAURANT_ID);
        userNo = Paper.book().read(Common.USER_KEY);
        // init firebase
        database = FirebaseDatabase.getInstance();
        offersCouponsForms = database.getReference(restId+"_Offers&Coupons");

        // init view
        recyclerView = view.findViewById(R.id.recycler_restOffers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

          Query listFoodByCategoryId = offersCouponsForms.orderByChild("visibility").equalTo("Yes");
        FirebaseRecyclerOptions<OffersCouponsModel> options = new FirebaseRecyclerOptions.Builder<OffersCouponsModel>()
                .setQuery(listFoodByCategoryId,OffersCouponsModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<OffersCouponsModel, OffersMenuHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OffersMenuHolder viewHolder, int position, @NonNull OffersCouponsModel model) {
                viewHolder.txt_title.setText(model.getOfferTitle());
                viewHolder.txt_description.setText("Status : "+model.getStatus());
                viewHolder.txt_code.setText("Coupon Code : "+model.getCouponCode());
                viewHolder.txt_dateTime.setText("Date : "+model.getDate());
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



        return view;
    }

}
