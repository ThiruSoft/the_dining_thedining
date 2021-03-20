package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class BookTableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId;
    public TextView order_date;
    public TextView txtOrderPnone;
    public TextView txtOrderStatus;
    public TextView txt_restName;
    public ImageView btn_delete;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BookTableViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        order_date = (TextView)itemView.findViewById(R.id.order_date);
        txtOrderPnone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txt_restName = (TextView)itemView.findViewById(R.id.txt_restName);

        btn_delete = itemView.findViewById(R.id.btn_delete);

        //   itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
