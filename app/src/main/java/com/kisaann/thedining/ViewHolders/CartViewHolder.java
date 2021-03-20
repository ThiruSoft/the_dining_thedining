package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{
    public TextView cart_item_name, cart_item_price;
    public TextView cart_item_original_price;
    public ElegantNumberButton btn_quantity;
    public ImageView cart_image;

    private ItemClickListener itemClickListener;
    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    public void setTxtCartName(TextView txtCartName) {
        this.cart_item_name = txtCartName;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        cart_item_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        cart_item_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        cart_item_original_price = (TextView)itemView.findViewById(R.id.cart_item_original_price);
        cart_image = (ImageView)itemView.findViewById(R.id.cart_image);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        view_background = (RelativeLayout)itemView.findViewById(R.id.view_background) ;
        view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground) ;

        itemView.setOnCreateContextMenuListener(this);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}