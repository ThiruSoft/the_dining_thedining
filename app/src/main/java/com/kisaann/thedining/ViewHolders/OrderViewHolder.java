package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId;
    public TextView order_date;
    public TextView txtOrderPnone;
    public TextView txtOrderAddress;
    public TextView txtOrderStatus;
    public TextView restName;
    public TextView txt_restName;
    public TextView order_confirmOtp;
    public ImageView btn_delete;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        order_date = (TextView)itemView.findViewById(R.id.order_date);
        txtOrderPnone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        restName = (TextView)itemView.findViewById(R.id.restName);
        order_confirmOtp = (TextView)itemView.findViewById(R.id.order_confirmOtp);
        txt_restName = (TextView)itemView.findViewById(R.id.txt_restName);

        btn_delete = itemView.findViewById(R.id.btn_delete);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}

