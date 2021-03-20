package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class RestaurantDiscMenusHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        /* View.OnCreateContextMenuListener*/{

    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public RestaurantDiscMenusHolder(View itemView) {
        super(itemView);
        txtMenuName = itemView.findViewById(R.id.manu_name);
        imageView = itemView.findViewById(R.id.menu_image);

        /*itemView.setOnCreateContextMenuListener(this);*/
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action.");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DETETE);

    }*/
}
