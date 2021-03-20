package com.kisaann.thedining.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kisaann.thedining.CheckOutActivity;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.R;
import com.kisaann.thedining.ViewHolders.OrderCartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckOutAdapter extends RecyclerView.Adapter<OrderCartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private CheckOutActivity cart;

    public CheckOutAdapter(List<Order> listData, CheckOutActivity cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public OrderCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.order_cart_layout,parent,false);

        return new OrderCartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCartViewHolder holder, final int position) {
       /* TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position)
                .getQuantity(), Color.RED);
        holder.imgCartCount.setImageDrawable(drawable);*/
        String imageUrl = listData.get(position).getImage();
        Picasso.get()
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);
        holder.txt_quantity.setText(" Qty : "+listData.get(position).getQuantity());

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        Double original_price = Double.parseDouble(listData.get(position).getPrice());
        Double discount = Double.parseDouble(listData.get(position).getDiscount());
        Double discount_price = (original_price / 100.0f) * discount;
        Double servicePrice = Double.parseDouble(listData.get(position).getPrice()) - discount_price;
        String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

        int originalPrice = Integer.parseInt(listData.get(position).getPrice());
        /*int price = (Integer.parseInt(str_price))*(Integer.parseInt
                (listData.get(position).getQuantity()));*/
      //  holder.cart_item_price.setText(fmt.format(servicePrice));
        holder.cart_item_price.setText("KD "+servicePrice);
        holder.cart_item_name.setText(listData.get(position).getProductName());
      //  holder.cart_item_original_price.setText(fmt.format(originalPrice));
        holder.cart_item_original_price.setText("KD " +originalPrice);

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
