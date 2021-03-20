package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class OffersMenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_title;
    public TextView txt_description;
    public TextView txt_code;
    public TextView txt_dateTime;
    public ImageView product_image;
    private ItemClickListener itemClickListener;

    public OffersMenuHolder(View itemView) {
        super(itemView);
        txt_title = (TextView)itemView.findViewById(R.id.txt_title);
        txt_description = (TextView)itemView.findViewById(R.id.txt_description);
        txt_code = (TextView)itemView.findViewById(R.id.txt_code);
        txt_dateTime = (TextView)itemView.findViewById(R.id.txt_dateTime);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);


        // itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action.");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DETETE);

    }*/
}

