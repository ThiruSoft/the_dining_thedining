package com.kisaann.thedining.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.DownloadInvoiceActivity;
import com.kisaann.thedining.FeedBackActivity;
import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.Models.PaymentConfirmModel;
import com.kisaann.thedining.Models.Request;
import com.kisaann.thedining.OrderDetails;
import com.kisaann.thedining.R;
import com.kisaann.thedining.ViewHolders.PaidOrderViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaidOrdersFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference confirmPayment;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<PaymentConfirmModel, PaidOrderViewHolder> adapter;
    Dialog dialog_customize_view;
    DatabaseReference reference;

    DatabaseReference orderStatus;
    DatabaseReference restOrderStatus;
    DatabaseReference unPaidRequestsRestaurant;
    String restId;
    String ownerNo;
    String userNo;
    public PaidOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paid_orders, container, false);

        restId = Paper.book().read(Common.RESTAURANT_ID);
        ownerNo = Paper.book().read(Common.OWNER_NO);
        userNo = Paper.book().read(Common.USER_KEY);
        // Firebase
        database = FirebaseDatabase.getInstance();
        confirmPayment = database.getReference("ConfirmPayment");
        orderStatus = database.getReference(userNo+"_Requests");
        restOrderStatus = database.getReference(restId+"_"+ownerNo+"_Requests");
        unPaidRequestsRestaurant = database.getReference(restId+"_"+ownerNo+"_UnPaidRequests");

        recyclerView = view.findViewById(R.id.recycler_paid);
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
        Query searchByUser = confirmPayment.orderByChild("phone").equalTo(phoneNo);
        FirebaseRecyclerOptions<PaymentConfirmModel> options = new FirebaseRecyclerOptions.Builder<PaymentConfirmModel>()
                .setQuery(searchByUser, PaymentConfirmModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<PaymentConfirmModel, PaidOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PaidOrderViewHolder viewHolder, final int position, @NonNull PaymentConfirmModel model) {
                viewHolder.txtOrderId.setText(model.getOrderId());
                viewHolder.txtOrderPnone.setText(model.getTableNo());
                viewHolder.txtOrderAddress.setText(model.getPaymentState());
                if (model.getPaymentState().equalsIgnoreCase("Progress")){
                    viewHolder.txtOrderAddress.setTextColor(getResources().getColor(R.color.light_orange));
                }else if (model.getPaymentState().equalsIgnoreCase("UnPaid")){
                    viewHolder.txtOrderAddress.setTextColor(getResources().getColor(R.color.red));
                }
                viewHolder.order_total.setText(model.getTotal());
                if (model.getRestName() != null && !model.getRestName().isEmpty()){
                    viewHolder.restName.setText(model.getRestName());
                }else {
                    viewHolder.txt_restName.setVisibility(View.GONE);
                    viewHolder.restName.setVisibility(View.GONE);
                }
                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("Placed")) {
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.light_orange));
                }
                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("On the way")) {
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.blue));
                }
                if (Common.convertCodeToStatus(model.getStatus()).equalsIgnoreCase("Delivered")) {
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.green));
                }
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                /*viewHolder.order_date.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));*/
                viewHolder.order_date.setText(model.getDate() + " & " + model.getTime());
                // viewHolder.btn_delete.setVisibility(View.GONE);
                if (model.getPaymentState().equalsIgnoreCase("Progress")){
                   // viewHolder.order_confirmOtp.setVisibility(View.VISIBLE);
                }
                viewHolder.order_confirmOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            verifyOtpDialog(adapter.getRef(position).getKey(),model.getPaymentOtp(),model.getRestaurantId(), model.getOrderId());
                    }
                });
                viewHolder.lly_invoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.paymentConfirmModel = model;
                        Common.currentKey = adapter.getRef(position).getKey();
                        Intent invoice = new Intent(getActivity(), DownloadInvoiceActivity.class);
                        invoice.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(invoice);
                    }
                });
                final PaymentConfirmModel clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Common.paymentConfirmModel = model;
                        Common.currentKey = adapter.getRef(position).getKey();
                        /* Toast.makeText(OrderStatus.this,"Password was update", Toast.LENGTH_SHORT).show();*/
                        Intent trackingOrder = new Intent(getActivity(), OrderDetails.class);
                        trackingOrder.putExtra("OrderId", adapter.getRef(position).getKey());
                        trackingOrder.putExtra("From","Paid");
                        startActivity(trackingOrder);
                    }
                });
            }

            @NonNull
            @Override
            public PaidOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paid_order_layout, parent, false);

                return new PaidOrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void verifyOtpDialog(String key, String otp, String restId, String orderId) {

            dialog_customize_view = new Dialog(getActivity());
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
            txt_type.setText("Verify Payment");
            txt_submit.setOnClickListener(view -> {
                String otpVerify = edt_otpNo.getText().toString().trim();
                if (!otpVerify.isEmpty()){
                    if (otpVerify.equalsIgnoreCase(otp)){
                        //    Toast.makeText(CheckOutActivity.this, "Payment successful !!!", Toast.LENGTH_SHORT).show();
                        Paper.book().write(Common.COMPLETE_ORDER, "Yes");
                        Paper.book().write(Common.ORDER_ID, "");

                        reference = FirebaseDatabase.getInstance().getReference("ConfirmPayment").child(key);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("paymentState", "Paid");
                        reference.updateChildren(hashMap);

                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_ConfirmPayment").child(key);
                        reference.updateChildren(hashMap);

                        unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                        unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())){
                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentState", "Paid");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                unPaidRequestsRestaurant.removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // update payment status in user request table
                        orderStatus.orderByChild("orderId").equalTo(orderId);
                        orderStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())){
                                        reference = FirebaseDatabase.getInstance().getReference(userNo+"_Requests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentState", "Paid");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                orderStatus.removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // update payment status in restaurant request table
                        restOrderStatus.orderByChild("orderId").equalTo(orderId);
                        restOrderStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())){
                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_Requests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentState", "Paid");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                restOrderStatus.removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        dialog_customize_view.show();
                        Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                        intent.putExtra("restId",restId);
                        intent.putExtra("orderId",orderId);
                        startActivity(intent);
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(), "Mismatch OTP", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Enter OTP", Toast.LENGTH_LONG).show();
                }
            });
            dialog_customize_view.show();
        }


    private void deleteOrder(final String key) {
        confirmPayment.child(key)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), new StringBuilder("Order ")
                                .append(key)
                                .append("has been deleted!").toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
