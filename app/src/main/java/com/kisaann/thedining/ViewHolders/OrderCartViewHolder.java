package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class OrderCartViewHolder extends RecyclerView.ViewHolder{
    public TextView cart_item_name, cart_item_price;
    public TextView cart_item_original_price;
    public TextView txt_quantity;
    public ImageView cart_image;

    private ItemClickListener itemClickListener;
    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    public void setTxtCartName(TextView txtCartName) {
        this.cart_item_name = txtCartName;
    }

    public OrderCartViewHolder(View itemView) {
        super(itemView);
        cart_item_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        cart_item_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        cart_item_original_price = (TextView)itemView.findViewById(R.id.cart_item_original_price);
        cart_image = (ImageView)itemView.findViewById(R.id.cart_image);
        txt_quantity =  itemView.findViewById(R.id.txt_quantity);
        view_background = (RelativeLayout)itemView.findViewById(R.id.view_background) ;
        view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground) ;



    }

}