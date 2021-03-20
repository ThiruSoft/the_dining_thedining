package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class PaidOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId;
    public TextView order_date;
    public TextView txtOrderPnone;
    public TextView txtOrderAddress;
    public TextView txtOrderStatus;
    public TextView order_total;
    public TextView txt_restName;
    public TextView restName;
    public TextView order_confirmOtp;
    public ImageView btn_delete;
    public LinearLayout lly_invoice;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PaidOrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        order_date = itemView.findViewById(R.id.order_date);
        txtOrderPnone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        order_total = itemView.findViewById(R.id.order_total);
        order_confirmOtp = itemView.findViewById(R.id.order_confirmOtp);
        restName = itemView.findViewById(R.id.restName);
        txt_restName = itemView.findViewById(R.id.txt_restName);
        lly_invoice = itemView.findViewById(R.id.lly_invoice);

        btn_delete = itemView.findViewById(R.id.btn_delete);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
