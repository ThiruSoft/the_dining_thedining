package com.kisaann.thedining.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kisaann.thedining.BarMenuActivity;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.FoodDetails;
import com.kisaann.thedining.MainActivity;
import com.kisaann.thedining.Models.Food;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MenuListDataAdapterA extends BaseAdapter {
        private Context context;
        private ArrayList<Food> mFoodData;
        private boolean isChat;
        String theLastMessage;
        String tableNo;
        String userFirstName;
        List<Order> cart = new ArrayList<>();
        private int cartQuantity = 1;
        private LayoutInflater mLayoutInflater;
        private List<Food> itemNamesList = null;
        private ArrayList<Food> arrayList;
        Food foodModel;
    public MenuListDataAdapterA(Context context, List<Food> itemNamesList, String tableNo, String userFirstName) {
        this.context = context;
        // mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater = LayoutInflater.from(context);
        //   this.mFoodData = mFoodData;
        this.itemNamesList = itemNamesList;
        this.tableNo = tableNo;
        this.userFirstName = userFirstName;
        arrayList = new ArrayList<Food>();
        arrayList.addAll(itemNamesList);
    }

        /* Helper method to use, when updating the list */
        public void updateItems(List<Food> itemNamesList){
        if(itemNamesList != null){
            // Either clear the list, or do some logic for skipping duplicates
            this.itemNamesList.clear();
            this.itemNamesList.addAll(itemNamesList);
        }
        notifyDataSetChanged();
    }
        @Override
        public int getCount() {
        return itemNamesList.size();
    }

    /*@Override
    public Object getItem(int i) {
        return itemNamesList.get(i);
    }*/
        @Override
        public long getItemId(int i) {
        return i;
    }
        @Override
        public Food getItem(int position) {
        return itemNamesList.get(position);
    }
        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
        // final Holder viewHolder;
        if (convertView == null) {
            // foodModel = itemNamesList.get(i);
            convertView = mLayoutInflater.inflate(R.layout.menu_item_layout, null);
           /* viewHolder = new Holder();
            setUI(viewHolder, convertView);
            convertView.setTag(viewHolder);*/

        } else {
            //   viewHolder = (Holder) convertView.getTag();


        }
        Food foodModel = itemNamesList.get(i);

        /*if (foodModel.getTimeFrom() != null && !foodModel.getTimeFrom().isEmpty()
                && foodModel.getTimeTo() != null && !foodModel.getTimeTo().isEmpty()) {
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);
            //
            int fromTime = Integer.parseInt(foodModel.getTimeFrom());
            int toTime = Integer.parseInt(foodModel.getTimeTo());
            if (hour >= fromTime && hour < toTime) {
                Log.e("hour", "" + hour);
            } else {
                viewHolder.mlly_items.setVisibility(View.GONE);
            }
        }*/
        if (foodModel != null){
            LinearLayout mlly_items = convertView.findViewById(R.id.mlly_items);
            TextView txt_itemName = convertView.findViewById(R.id.txt_itemName);
            TextView txt_itemPrice = convertView.findViewById(R.id.txt_itemPrice);
            TextView txt_itemOriginalPrice = convertView.findViewById(R.id.txt_itemOriginalPrice);
            TextView txtQuantityCart = convertView.findViewById(R.id.txtQuantityCart);
            TextView txt_customize = convertView.findViewById(R.id.txt_customize);
            TextView txt_itemDescription = convertView.findViewById(R.id.txt_itemDescription);
            TextView txt_outOfStack = convertView.findViewById(R.id.txt_outOfStack);
            TextView txt_discount = convertView.findViewById(R.id.txt_discount);
            ImageView img_itemImage = convertView.findViewById(R.id.img_itemImage);
            LinearLayout mLlyCartCount = convertView.findViewById(R.id.mLlyCartCount);
            Button btnDecreaseCart = convertView.findViewById(R.id.btnDecreaseCart);
            Button btnIncreaseCart = convertView.findViewById(R.id.btnIncreaseCart);
            Button btn_addToCart = convertView.findViewById(R.id.btn_addToCart);

            mLlyCartCount.setTag(i);
            btn_addToCart.setTag(i);
            String checkId = foodModel.getProductId();
            String restaurantMedId = foodModel.getRestaurantId();
            if (foodModel.getProductId() != null && !foodModel.getProductId().isEmpty()) {
                boolean isExist = new Database(context).checkFoodExists(checkId, tableNo);
                if (isExist) {
                    cart = new Database(context).getCartByProduct(checkId);
                    for (Order order : cart) {
                        txtQuantityCart.setText(order.getQuantity());
                        mLlyCartCount.setVisibility(android.view.View.VISIBLE);
                        btn_addToCart.setVisibility(android.view.View.GONE);
                    }
                }else {
                    btn_addToCart.setVisibility(android.view.View.VISIBLE);
                }
            }

            txt_itemName.setText(foodModel.getName());
            txt_itemDescription.setText(foodModel.getDescription());
            Glide.with(context).load(foodModel.getImage()).into(img_itemImage);
            Double original_price = Double.parseDouble(foodModel.getPrice());
            int discount = Integer.parseInt(foodModel.getDiscount());
            Double discount_price = (original_price / 100.0f) * discount;
            Double servicePrice = Double.parseDouble(foodModel.getPrice()) - discount_price;
            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

            txt_itemPrice.setText(context.getResources().getString(R.string.Rs) + "" + str_price);
            txt_itemOriginalPrice.setText(context.getResources().getString(R.string.Rs) + "" + foodModel.getPrice());
            if (foodModel.getDiscount() != null && !foodModel.getDiscount().isEmpty()) {
                double dis = Double.parseDouble(foodModel.getDiscount());
                if (dis > 0) {
                    txt_itemOriginalPrice.setVisibility(android.view.View.VISIBLE);
                    txt_discount.setVisibility(android.view.View.VISIBLE);
                    txt_discount.setText(" " + foodModel.getDiscount() + " " + "Off");
                }
            }
        /*if (foodModel.getAvailability() != null && !foodModel.getAvailability().isEmpty()) {
            if (foodModel.getAvailability().equalsIgnoreCase("No")) {
                txt_outOfStack.setVisibility(View.VISIBLE);
                btn_addToCart.setVisibility(View.GONE);
            }
        }*/
            img_itemImage.setOnClickListener(view -> {
                Intent foodDetails = new Intent(context, FoodDetails.class);
                foodDetails.putExtra("FoodId", checkId);
                foodDetails.putExtra("RestaurantId", restaurantMedId);
                foodDetails.putExtra("CategoryName", "All");
                context.startActivity(foodDetails);
            });
            btn_addToCart.setTag(i);
            btn_addToCart.setOnClickListener(view -> {
                MainActivity.editsearch.clearFocus();
                int pos = view.getId();
                if (foodModel.getAvailability() != null && !foodModel.getAvailability().isEmpty()) {
                    if (foodModel.getAvailability().equalsIgnoreCase("Yes")) {

                        new Database(context).addToCart(new Order(
                                tableNo,
                                checkId, foodModel.getName(),
                                "1", foodModel.getPrice(), foodModel.getDiscount()
                                , foodModel.getImage(), foodModel.getCategoryType(),
                                foodModel.getCategoryName(), foodModel.getType(), foodModel.getDepartment(),
                                foodModel.getParticular(), ""+foodModel.getGst(),""+foodModel.getHappyHour(),
                                ""+foodModel.getKitchen(),userFirstName));

                        int count = new Database(context).getCountCart(tableNo);

                        Double price = Double.parseDouble(MainActivity.txt_price.getText().toString());
                        Double price1 = Double.parseDouble(str_price);
                        Double total = price + price1;
                        MainActivity.txt_price.setText(String.valueOf(total));
                        MainActivity.txt_cartPrice.setText(" " + count + "  Item | " + total);
                        Paper.book().write(Common.TOTAL, "" + total);
                        txtQuantityCart.setText(String.valueOf(1));
                        mLlyCartCount.setVisibility(android.view.View.VISIBLE);
                        btn_addToCart.setVisibility(android.view.View.GONE);
                    } else {
                        Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            btnDecreaseCart.setTag(i);
            btnDecreaseCart.setOnClickListener(view -> {
                int pos = view.getId();
                String quantityValue = txtQuantityCart.getText().toString();
                cartQuantity = Integer.parseInt(quantityValue);
                if (cartQuantity != 0 && cartQuantity <= 1) {
                    cartQuantity = cartQuantity - 1;
                    btn_addToCart.setVisibility(android.view.View.VISIBLE);
                    mLlyCartCount.setVisibility(android.view.View.GONE);

                    new Database(context).removeFromCart(checkId, tableNo);
                    Double priceA = Double.parseDouble(MainActivity.txt_price.getText().toString());
                    Double priceB = Double.parseDouble(str_price);
                    Double totalT = priceA - priceB;
                    MainActivity.txt_price.setText(String.valueOf(totalT));
                    Paper.book().write(Common.TOTAL, "" + totalT);
                    txtQuantityCart.setText(String.valueOf(cartQuantity));
                    int c = new Database(context).getCountCart(tableNo);
                    MainActivity.txt_cartPrice.setText(" " + c + "  Item | " + totalT);

                }else if (cartQuantity > 1){
                    cartQuantity = cartQuantity - 1;
                    if (cartQuantity >= 1) {
                        Log.e("cartQuantityD",""+cartQuantity);
                        Double price = Double.parseDouble(MainActivity.txt_price.getText().toString());
                        Double price1 = Double.parseDouble(str_price);
                        Double total = price - price1;
                        Paper.book().write(Common.TOTAL, "" + total);
                        MainActivity.txt_price.setText(String.valueOf(total));


                        new Database(context).decreaseCart(tableNo, checkId);
                        txtQuantityCart.setText(String.valueOf(cartQuantity));

                        int count1 = new Database(context).getCountCart(tableNo);
                        MainActivity.txt_cartPrice.setText(" " + count1 + "  Item | " + total);
                    }

                }
            });
            btnIncreaseCart.setTag(i);
            btnIncreaseCart.setOnClickListener(view -> {
                int pos = view.getId();
                String quantityValue = txtQuantityCart.getText().toString();
                cartQuantity = Integer.parseInt(quantityValue);
                cartQuantity = cartQuantity + 1;

                Double price = Double.parseDouble(MainActivity.txt_price.getText().toString());
                Double price1 = Double.parseDouble(str_price);
                Double total = price + price1;
                Paper.book().write(Common.TOTAL, "" + total);
                MainActivity.txt_price.setText(String.valueOf(total));
                MainActivity.txt_cartPrice.setText(String.valueOf(total));

                new Database(context).increaseCart(tableNo, checkId);

                txtQuantityCart.setText(String.valueOf(cartQuantity));

                int count2 = new Database(context).getCountCart(tableNo);
                MainActivity.txt_cartPrice.setText(" " + count2 + "  Item | " + total);
            });
        }
        return convertView;
    }

        // Filter Class
        public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemNamesList.clear();
        if (charText.length() == 0) {
            itemNamesList.addAll(arrayList);
        } else {
            for (Food wp : arrayList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
        private void setUI(Holder holder, android.view.View convertView) {
        holder.mlly_items = convertView.findViewById(R.id.mlly_items);
        holder.txt_itemName = convertView.findViewById(R.id.txt_itemName);
        holder.txt_itemPrice = convertView.findViewById(R.id.txt_itemPrice);
        holder.txt_itemOriginalPrice = convertView.findViewById(R.id.txt_itemOriginalPrice);
        holder.txtQuantityCart = convertView.findViewById(R.id.txtQuantityCart);
        holder.txt_customize = convertView.findViewById(R.id.txt_customize);
        holder.txt_itemDescription = convertView.findViewById(R.id.txt_itemDescription);
        holder.txt_outOfStack = convertView.findViewById(R.id.txt_outOfStack);
        holder.txt_discount = convertView.findViewById(R.id.txt_discount);
        holder.img_itemImage = convertView.findViewById(R.id.img_itemImage);
        holder.mLlyCartCount = convertView.findViewById(R.id.mLlyCartCount);
        holder.btnDecreaseCart = convertView.findViewById(R.id.btnDecreaseCart);
        holder.btnIncreaseCart = convertView.findViewById(R.id.btnIncreaseCart);
        holder.btn_addToCart = convertView.findViewById(R.id.btn_addToCart);
    }

        private class Holder {
            LinearLayout mlly_items ;
            TextView txt_itemName ;
            TextView txt_itemPrice ;
            TextView txt_itemOriginalPrice ;
            TextView txtQuantityCart ;
            TextView txt_customize ;
            TextView txt_itemDescription ;
            TextView txt_outOfStack ;
            TextView txt_discount ;
            ImageView img_itemImage ;
            LinearLayout mLlyCartCount ;
            Button btnDecreaseCart ;
            Button btnIncreaseCart ;
            Button btn_addToCart ;
        }
    }
