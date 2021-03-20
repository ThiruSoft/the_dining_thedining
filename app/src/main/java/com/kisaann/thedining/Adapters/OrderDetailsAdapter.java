package com.kisaann.thedining.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, quantity, price, discount;
    public TextView product_priceDiscount;
    public ImageView img_itemImage;
    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);
        product_priceDiscount = itemView.findViewById(R.id.product_priceDiscount);
        discount = itemView.findViewById(R.id.product_discount);
        img_itemImage = itemView.findViewById(R.id.img_itemImage);
    }
}
public class OrderDetailsAdapter extends RecyclerView.Adapter<MyViewHolder>{

    List<Order> myOrders;
    Context context;
    public OrderDetailsAdapter(Context context, List<Order> myOrders) {
        this.context = context;
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name : %s", order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s", order.getQuantity()));

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        Double original_price = Double.parseDouble(order.getPrice());
        Double discount = Double.parseDouble(order.getDiscount());
        Double discount_price = (original_price / 100.0f) * discount;
        Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
        String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

        int originalPrice = Integer.parseInt(order.getPrice());
        /*int price = (Integer.parseInt(str_price))*(Integer.parseInt
                (listData.get(position).getQuantity()));*/
        holder.product_priceDiscount.setText(fmt.format(originalPrice));

        holder.price.setText(fmt.format(servicePrice));
        holder.discount.setText(String.format("Discount : %s", order.getDiscount()+"%"));

        Glide.with(context).load(order.getImage()).into(holder.img_itemImage);

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}