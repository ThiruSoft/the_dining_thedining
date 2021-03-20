package com.kisaann.thedining.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisaann.thedining.Interfaces.ItemClickListener;
import com.kisaann.thedining.R;

public class FoodMenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        /* View.OnCreateContextMenuListener*/{

    /* public TextView txtMenuName;*/
    public TextView mTxt_menu_name;
    public TextView mTxt_menu_nameC;
    public TextView txt_menuDescription;
    public TextView txt_menuDescriptionC;
    public LinearLayout mLlyInnerLayout;
    public LinearLayout mLlyParentLayout;
    public LinearLayout mLlyItem;
    public LinearLayout mLlyItemClose;
    public RecyclerView recycler_food_menu;
    /*public ImageView imageView;*/
    private ItemClickListener itemClickListener;

    public FoodMenuHolder(View itemView) {
        super(itemView);
        mTxt_menu_name = (TextView)itemView.findViewById(R.id.mTxt_menu_name);
        mTxt_menu_nameC = (TextView)itemView.findViewById(R.id.mTxt_menu_nameC);
        txt_menuDescription = (TextView) itemView.findViewById(R.id.txt_menuDescription);
        txt_menuDescriptionC = (TextView) itemView.findViewById(R.id.txt_menuDescriptionC);
        mLlyInnerLayout = itemView.findViewById(R.id.mLlyInnerLayout);
        mLlyParentLayout = itemView.findViewById(R.id.mLlyParentLayout);
        mLlyItem = itemView.findViewById(R.id.mLlyItem);
        mLlyItemClose = itemView.findViewById(R.id.mLlyItemClose);
        recycler_food_menu = itemView.findViewById(R.id.recycler_food_menu);

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

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action.");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DETETE);

    }*/
}

