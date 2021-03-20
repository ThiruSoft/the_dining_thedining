package com.kisaann.thedining;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kisaann.thedining.Adapters.OrderDetailsAdapter;
import com.kisaann.thedining.Common.Common;

public class OrderDetails extends AppCompatActivity {
    TextView order_id,order_phone,total_price,table_no,comments,dateTime;
    TextView txt_restName,restName;
    String order_id_value = "";
    String from = "";
    RecyclerView listFoods;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Orders Details");
        getSupportActionBar().setHomeButtonEnabled(true);

        order_id = (TextView)findViewById(R.id.order_id);
        order_phone = (TextView)findViewById(R.id.order_phone);
        total_price = (TextView)findViewById(R.id.total_price);
        table_no = (TextView)findViewById(R.id.table_no);
        dateTime = (TextView)findViewById(R.id.dateTime);
        txt_restName = (TextView)findViewById(R.id.txt_restName);
        restName = (TextView)findViewById(R.id.restName);
        comments = (TextView)findViewById(R.id.comments);

        listFoods = (RecyclerView)findViewById(R.id.listFoods);
        listFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listFoods.setLayoutManager(layoutManager);

        if (getIntent() != null){
            order_id_value = getIntent().getStringExtra("OrderId");
            from = getIntent().getStringExtra("From");
        }

        if (from.equalsIgnoreCase("Paid")){
            // set values
            order_id.setText(Common.paymentConfirmModel.getOrderId());
            dateTime.setText(Common.paymentConfirmModel.getDate()+" & "+Common.paymentConfirmModel.getTime());
            order_phone.setText(Common.paymentConfirmModel.getPhone());
            total_price.setText(Common.paymentConfirmModel.getTotal());
            table_no.setText(Common.paymentConfirmModel.getTableNo());
            comments.setText(Common.paymentConfirmModel.getComments());
            if (Common.paymentConfirmModel.getRestName() != null && !Common.paymentConfirmModel.getRestName().isEmpty()){
                restName.setText(Common.paymentConfirmModel.getRestName());
            }else {
               txt_restName.setVisibility(View.GONE);
                restName.setVisibility(View.GONE);
            }
            if (Common.paymentConfirmModel.getFood() != null) {
                OrderDetailsAdapter adapter = new OrderDetailsAdapter(getApplicationContext(), Common.paymentConfirmModel.getFood());
                adapter.notifyDataSetChanged();
                listFoods.setAdapter(adapter);
            }
        }else {
            // set values
            order_id.setText(Common.currentRequest.getOrderId());
            dateTime.setText(Common.currentRequest.getDate()+" & "+Common.currentRequest.getTime());
            order_phone.setText(Common.currentRequest.getPhone());
            total_price.setText(Common.currentRequest.getTotal());
            table_no.setText(Common.currentRequest.getTableNo());
            comments.setText(Common.currentRequest.getComments());
            if (Common.currentRequest.getRestName() != null && !Common.currentRequest.getRestName().isEmpty()){
                restName.setText(Common.currentRequest.getRestName());
            }else {
                txt_restName.setVisibility(View.GONE);
                restName.setVisibility(View.GONE);
            }
            if (Common.currentRequest.getFood() != null) {
                OrderDetailsAdapter adapter = new OrderDetailsAdapter(getApplicationContext(), Common.currentRequest.getFood());
                adapter.notifyDataSetChanged();
                listFoods.setAdapter(adapter);
            }
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
