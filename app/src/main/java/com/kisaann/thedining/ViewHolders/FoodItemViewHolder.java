package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class FoodItemViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_itemName;
    public TextView txt_itemPrice;
    public TextView txt_itemOriginalPrice;
    public TextView txtQuantityCart;
    public TextView txt_customize;
    public TextView txt_itemDescription;
    public TextView txt_discount;
    public ImageView img_itemImage;
    /*ImageView img_nonVeg;
    ImageView img_veg;*/
    public LinearLayout mLlyCartCount;
    public Button btnDecreaseCart;
    public Button btnIncreaseCart;
    public Button btn_addToCart;
    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodItemViewHolder(View itemView) {
        super(itemView);

        txt_itemName = itemView.findViewById(R.id.txt_itemName);
        txt_itemPrice = itemView.findViewById(R.id.txt_itemPrice);
        txt_itemOriginalPrice = itemView.findViewById(R.id.txt_itemOriginalPrice);
        txtQuantityCart = itemView.findViewById(R.id.txtQuantityCart);
        txt_customize = itemView.findViewById(R.id.txt_customize);
        txt_itemDescription = itemView.findViewById(R.id.txt_itemDescription);
        txt_discount = itemView.findViewById(R.id.txt_discount);
        img_itemImage = itemView.findViewById(R.id.img_itemImage);
            /*img_nonVeg = itemView.findViewById(R.id.img_nonVeg);
            img_veg = itemView.findViewById(R.id.img_veg);*/
        mLlyCartCount = itemView.findViewById(R.id.mLlyCartCount);
        btnDecreaseCart = itemView.findViewById(R.id.btnDecreaseCart);
        btnIncreaseCart = itemView.findViewById(R.id.btnIncreaseCart);
        btn_addToCart = itemView.findViewById(R.id.btn_addToCart);

        /*itemView.setOnClickListener(this);*/
    }

  /*  @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }*/

  /*  @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action.");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DETETE);

    }*/
}
