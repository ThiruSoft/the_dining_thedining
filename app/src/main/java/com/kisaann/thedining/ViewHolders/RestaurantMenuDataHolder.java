package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class RestaurantMenuDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener itemClickListener;
    public LinearLayout mlly_items;
    public LinearLayout mLlyCartCount;
    public Button btnDecreaseCart;
    public Button btnIncreaseCart;
    public Button btn_addToCart;
    public TextView txt_itemName;
    public TextView txt_itemPrice;
    public TextView txt_itemOriginalPrice;
    public TextView txtQuantityCart;
    public TextView txt_customize;
    public TextView txt_itemDescription;
    public TextView txt_outOfStack;
    public TextView txt_discount;
    public ImageView img_itemImage;
    public RestaurantMenuDataHolder(View itemView) {
        super(itemView);

         mlly_items = itemView.findViewById(R.id.mlly_items);
         txt_itemName = itemView.findViewById(R.id.txt_itemName);
         txt_itemPrice = itemView.findViewById(R.id.txt_itemPrice);
         txt_itemOriginalPrice = itemView.findViewById(R.id.txt_itemOriginalPrice);
         txtQuantityCart = itemView.findViewById(R.id.txtQuantityCart);
         txt_customize = itemView.findViewById(R.id.txt_customize);
         txt_itemDescription = itemView.findViewById(R.id.txt_itemDescription);
         txt_outOfStack = itemView.findViewById(R.id.txt_outOfStack);
         txt_discount = itemView.findViewById(R.id.txt_discount);
         img_itemImage = itemView.findViewById(R.id.img_itemImage);

         mLlyCartCount = itemView.findViewById(R.id.mLlyCartCount);
         btnDecreaseCart = itemView.findViewById(R.id.btnDecreaseCart);
         btnIncreaseCart = itemView.findViewById(R.id.btnIncreaseCart);
         btn_addToCart = itemView.findViewById(R.id.btn_addToCart);

        /*itemView.setOnCreateContextMenuListener(this);*/
        /* itemView.setOnClickListener(this);*/
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
