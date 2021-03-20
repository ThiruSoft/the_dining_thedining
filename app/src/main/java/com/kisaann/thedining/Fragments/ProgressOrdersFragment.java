package com.kisaann.thedining.Fragments;


import android.content.Intent;
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
import com.kisaann.thedining.Models.Request;
import com.kisaann.thedining.OrderDetails;
import com.kisaann.thedining.R;
import com.kisaann.thedining.ViewHolders.OrderViewHolder;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressOrdersFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference orderStatus;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    String userNo;
    public ProgressOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress_orders, container, false);

        Paper.init(getActivity());
        userNo = Paper.book().read(Common.USER_KEY);
        // Firebase
        database = FirebaseDatabase.getInstance();
        orderStatus = database.getReference(userNo+"_Requests");

        recyclerView = view.findViewById(R.id.recycler_progress);
        LinearLayoutManager layoutManage = new LinearLayoutManager(getActivity());
        layoutManage.setReverseLayout(true);
        layoutManage.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManage);

        loadOrders(userNo);
        return view;

    }
    private void loadOrders(String phoneNo) {
        Query searchByUser = orderStatus.orderByChild("paymentState").equalTo("Progress");
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(searchByUser,Request.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull Request model) {
                String status = model.getPaymentState();
                viewHolder.txtOrderId.setText(model.getOrderId());
                viewHolder.txtOrderPnone.setText(model.getTableNo());
                if (model.getRestName() != null && !model.getRestName().isEmpty()){
                    viewHolder.restName.setText(model.getRestName());
                }else {
                    viewHolder.txt_restName.setVisibility(View.GONE);
                    viewHolder.restName.setVisibility(View.GONE);
                }

                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("Placed")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.light_orange));
                }
                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("On the way")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.blue));
                }
                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("Delivered")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.green));
                }

                viewHolder.txtOrderAddress.setText(model.getPaymentState());
                if (model.getPaymentState().equalsIgnoreCase("Progress")){
                   // viewHolder.order_confirmOtp.setVisibility(View.VISIBLE);
                    viewHolder.txtOrderAddress.setTextColor(getResources().getColor(R.color.light_orange));
                }else if (model.getPaymentState().equalsIgnoreCase("UnPaid")){
                    viewHolder.txtOrderAddress.setTextColor(getResources().getColor(R.color.red));
                }
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                /*viewHolder.order_date.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));*/
                viewHolder.order_date.setText(model.getDate()+" & "+model.getTime());
                viewHolder.btn_delete.setVisibility(View.GONE);

                viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                final Request clickItem  = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Common.currentRequest = model;
                        Common.currentKey = adapter.getRef(position).getKey();
                        /* Toast.makeText(OrderStatus.this,"Password was update", Toast.LENGTH_SHORT).show();*/
                        Intent trackingOrder = new Intent(getActivity(), OrderDetails.class);
                        trackingOrder.putExtra("OrderId",adapter.getRef(position).getKey());
                        trackingOrder.putExtra("From","UnPaid");
                        startActivity(trackingOrder);
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout,parent,false);

                return new OrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
