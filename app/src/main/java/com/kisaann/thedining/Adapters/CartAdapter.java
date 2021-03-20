package com.kisaann.thedining.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.kisaann.thedining.CartItemsActivity;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.R;
import com.kisaann.thedining.ViewHolders.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private CartItemsActivity cart;
    private String userNo;

    public CartAdapter(List<Order> listData, CartItemsActivity cart, String userNo) {
        this.listData = listData;
        this.cart = cart;
        this.userNo = userNo;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        // init paper
        Paper.init(cart);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
       /* TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position)
                .getQuantity(), Color.RED);
        holder.imgCartCount.setImageDrawable(drawable);*/
        String imageUrl = listData.get(position).getImage();
        Picasso.get()
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);
        holder.btn_quantity.setNumber(listData.get(position).getQuantity());
        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);
                Double total = 0.0;
                List<Order> orders = new Database(cart).getCart(userNo);
                for (Order item:orders){

                    Double original_price = Double.parseDouble(item.getPrice());
                    Double discount = Double.parseDouble(item.getDiscount());
                    Double discount_price = (original_price / 100.0f) * discount;
                    Double servicePrice = Double.parseDouble(item.getPrice()) - discount_price;
                    String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                    total+=(Double.parseDouble(str_price))*(Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en", "KD");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    Paper.book().write(Common.TOTAL, ""+total);
                    cart.totalAmount.setText(""+total);
                  //  cart.txtTotalPrice.setText(fmt.format(total));
                    cart.txtTotalPrice.setText( "KD " +total);

                }

            }
        });

        Locale locale = new Locale("en", "KD");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        Double original_price = Double.parseDouble(listData.get(position).getPrice());
        Double discount = Double.parseDouble(listData.get(position).getDiscount());
        Double discount_price = (original_price / 100.0f) * discount;
        Double servicePrice = Double.parseDouble(listData.get(position).getPrice()) - discount_price;
        String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

        int originalPrice = Integer.parseInt(listData.get(position).getPrice());
        /*int price = (Integer.parseInt(str_price))*(Integer.parseInt
                (listData.get(position).getQuantity()));*/
       // holder.cart_item_price.setText(fmt.format(servicePrice));
        holder.cart_item_price.setText( "KD "+ servicePrice);
        holder.cart_item_name.setText(listData.get(position).getProductName());
      //  holder.cart_item_original_price.setText(fmt.format(originalPrice));
        holder.cart_item_original_price.setText("KD "+ originalPrice);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Order item, int position){
        listData.add(position,item);

        notifyItemInserted(position);

    }
}
