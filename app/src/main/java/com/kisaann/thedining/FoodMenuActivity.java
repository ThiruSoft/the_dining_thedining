package com.kisaann.thedining;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Models.Category;
import com.kisaann.thedining.Models.Food;
import com.kisaann.thedining.Models.MenuNamesModel;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Utils.Utility;
import com.kisaann.thedining.ViewHolders.FoodMenuHolder;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class FoodMenuActivity extends AppCompatActivity {
    String restaurantMedId;
    RecyclerView recycler_menu;
    RecyclerView recyclerView;
    DatabaseReference categories;
    FirebaseRecyclerAdapter<Category, FoodMenuHolder> foodMenuAdapter;
    TextView txt_viewCart, txt_cartPrice, txt_price;
    RecyclerView.LayoutManager layoutManager;
    private LinearLayout layout1 ;
    List<Order> cart = new ArrayList<>();
    private int cartQuantity = 1;
    FirebaseDatabase database;
    List<MenuNamesModel> menuNames = new ArrayList<>();
    List<MenuNamesModel> foodMenuNames = new ArrayList<>();
    int categoryPos = 0;
    Dialog dialog_menu_view;
    String userNo;
    String tableNo;
    Double totalAmount = 0.0;
    List<Order> cartItems = new ArrayList<>();

    final int ITEM_LOAD_COUNT = 11;
    int total_item = 0, last_visible_item;

    boolean isLoading = false, isMaxData = false;
    String last_node = "",last_key = "";
    RecyclerView.SmoothScroller smoothScroller;
    String userFirstName;
    String restaurant_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Menu");
        getSupportActionBar().setHomeButtonEnabled(true);

        // init paper
        Paper.init(this);
        if (getIntent() != null) {
            tableNo = getIntent().getStringExtra("TableNo");
            /*restaurantMedId = getIntent().getStringExtra("RestaurantMenuId");*/
        }
        restaurant_Id = Paper.book().read(Common.RESTAURANT_ID);
        if (restaurant_Id != null && !restaurant_Id.isEmpty()){
            restaurantMedId = restaurant_Id;
        }
        userNo = Paper.book().read(Common.USER_KEY);
        userFirstName = Paper.book().read(Common.USER_NAME);
        // init firebase
        database = FirebaseDatabase.getInstance();
        categories = database.getReference(restaurantMedId+"_"+"Category");
        initUi();

        smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        readCategories();

        loadRestaurantMenuData();

        FloatingActionButton fab =  findViewById(R.id.fab_menu);
        FloatingActionButton fab_barMenu =  findViewById(R.id.fab_barMenu);

        fab.setOnClickListener(view -> {
            displayMenuDialog("food");
        });
        fab_barMenu.setOnClickListener(view -> {
            displayMenuDialog("food");
        });

        txt_viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    txt_viewCart.setEnabled(false);
                cartItems = new Database(getApplicationContext()).getCart(userNo);
                if (cartItems.size() > 0) {
                    Intent intent = new Intent(FoodMenuActivity.this, CartItemsActivity.class);
                    intent.putExtra("TableNo",tableNo);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodMenuActivity.this, "Your cart is empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* String total = Paper.book().read(Common.TOTAL);
        if (total != null && !total.isEmpty()){
            txt_price.setText(total);
            int count1 = new Database(getBaseContext()).getCountCart(tableNo);
            txt_cartPrice.setText(" "+count1 +"  Item | "+total);
        }*/
        cartItems = new Database(getApplicationContext()).getCart(userNo);
        totalAmount = 0.0;
        for (Order order : cartItems) {
            Double original_price = Double.parseDouble(order.getPrice());
            Double discount = Double.parseDouble(order.getDiscount()+".0");
            Double discount_price = (original_price / 100.0f) * discount;
            Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

            totalAmount += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        }
        String total = ""+totalAmount;
        if (total != null && !total.isEmpty()) {
            txt_price.setText(total);
            int count1 = new Database(getBaseContext()).getCountCart(userNo);
            txt_cartPrice.setText(" " + count1 + "  Item | " + total);
        }
        if (foodMenuAdapter != null){
            foodMenuAdapter.startListening();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (foodMenuAdapter != null){
            foodMenuAdapter.stopListening();
        }
    }
    private void loadRestaurantMenuData() {
        if (Utility.isNetworkAvailable(getBaseContext())) {
            //   Query listFoodByCategoryId = categories.orderByChild("restaurantMenuId").equalTo(restaurantMedId);
            Query listFoodByCategoryId = categories.orderByChild("categoryType").equalTo("Non-OfferFood");
            //   Query listFoodByCategoryId = categories.orderByChild("type").equalTo("Food");
            FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                    .setQuery(listFoodByCategoryId, Category.class)
                    .build();
            /* menuNames.clear();*/

            foodMenuAdapter = new FirebaseRecyclerAdapter<Category, FoodMenuHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FoodMenuHolder viewHolder, int position, @NonNull Category model) {
                    viewHolder.mTxt_menu_name.setText(model.getName());
                    viewHolder.mTxt_menu_nameC.setText(model.getName());
                    /*menuNames.add(new MenuNamesModel(model.getName(),""+position));*/

                    viewHolder.mLlyInnerLayout.removeAllViews();
                    viewHolder.mLlyParentLayout.setId(position);
                    viewHolder.mLlyItem.setId(position);
                    viewHolder.mLlyItemClose.setId(position);
                    viewHolder.mLlyInnerLayout.setId(position);
                    viewHolder.mLlyItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* viewHolder.mLlyInnerLayout.setVisibility(View.GONE);
                            viewHolder.mLlyItemClose.setVisibility(View.VISIBLE);
                            viewHolder.mLlyItem.setVisibility(View.GONE);*/
                        }
                    });

                    viewHolder.mLlyItemClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.mLlyInnerLayout.setVisibility(View.VISIBLE);
                            viewHolder.mLlyItemClose.setVisibility(View.GONE);
                            viewHolder.mLlyItem.setVisibility(View.VISIBLE);
                        }
                    });
                    /*if (model.getOfferType() != null && !model.getOfferType().isEmpty()
                            && model.getOfferType().equalsIgnoreCase("Non-Offer")) {*/
                    if (model.getTimeFrom() != null && !model.getTimeFrom().isEmpty()
                            && model.getTimeTo() != null && !model.getTimeTo().isEmpty()) {
                        Calendar currTime = Calendar.getInstance();
                        int hour = currTime.get(Calendar.HOUR_OF_DAY);
                        Log.e("hour", "" + hour);
                        int fromTime = Integer.parseInt(model.getTimeFrom());
                        int toTime = Integer.parseInt(model.getTimeTo());
                        if (hour >= fromTime && hour < toTime) {
                            Log.e("condition", "true");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(restaurantMedId + "_" + model.getName());
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        Food foodModel = snapshot.getValue(Food.class);
                                        layout1 = (LinearLayout) getLayoutInflater().inflate(R.layout.menu_item_layout, viewHolder.mLlyInnerLayout, false);

                                        final LinearLayout mlly_items = layout1.findViewById(R.id.mlly_items);
                                        final TextView txt_itemName = layout1.findViewById(R.id.txt_itemName);
                                        final TextView txt_itemPrice = layout1.findViewById(R.id.txt_itemPrice);
                                        final TextView txt_itemOriginalPrice = layout1.findViewById(R.id.txt_itemOriginalPrice);
                                        final TextView txtQuantityCart = layout1.findViewById(R.id.txtQuantityCart);
                                        final TextView txt_customize = layout1.findViewById(R.id.txt_customize);
                                        final TextView txt_itemDescription = layout1.findViewById(R.id.txt_itemDescription);
                                        final TextView txt_outOfStack = layout1.findViewById(R.id.txt_outOfStack);
                                        final TextView txt_discount = layout1.findViewById(R.id.txt_discount);
                                        final ImageView img_itemImage = layout1.findViewById(R.id.img_itemImage);
                                        ImageView img_nonVeg;
                                        ImageView img_veg;
                                        final LinearLayout mLlyCartCount = layout1.findViewById(R.id.mLlyCartCount);
                                        final Button btnDecreaseCart = layout1.findViewById(R.id.btnDecreaseCart);
                                        final Button btnIncreaseCart = layout1.findViewById(R.id.btnIncreaseCart);
                                        final Button btn_addToCart = layout1.findViewById(R.id.btn_addToCart);

                                        if (foodModel.getTimeFrom() != null && !foodModel.getTimeFrom().isEmpty()
                                                && foodModel.getTimeTo() != null && !foodModel.getTimeTo().isEmpty()) {
                                            Calendar currTime = Calendar.getInstance();
                                            int hour = currTime.get(Calendar.HOUR_OF_DAY);
                                            //
                                            int fromTime = Integer.parseInt(foodModel.getTimeFrom());
                                            int toTime = Integer.parseInt(foodModel.getTimeTo());
                                            if (hour >= fromTime && hour < toTime) {
                                                Log.e("hour", "" + hour);
                                            } else {
                                                mlly_items.setVisibility(View.GONE);
                                            }
                                        }
                                        String checkId = snapshot.getKey();

                                        boolean isExist = new Database(getBaseContext()).checkFoodExists(checkId, userNo);
                                        if (isExist) {
                                            cart = new Database(getBaseContext()).getCartByProduct(checkId);
                                            for (Order order : cart) {
                                                txtQuantityCart.setText(order.getQuantity());
                                                mLlyCartCount.setVisibility(View.VISIBLE);
                                                btn_addToCart.setVisibility(View.GONE);
                                            }
                                        }else {
                                            mLlyCartCount.setVisibility(View.GONE);
                                            btn_addToCart.setVisibility(View.VISIBLE);
                                        }
                                        Double original_price = Double.parseDouble(foodModel.getPrice());
                                        int discount = Integer.parseInt(foodModel.getDiscount());
                                        Double discount_price = (original_price / 100.0f) * discount;
                                        Double servicePrice = Double.parseDouble(foodModel.getPrice()) - discount_price;
                                        String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                                        txt_itemPrice.setText(getResources().getString(R.string.Rs) + " " + str_price);
                                           Glide.with(getApplicationContext()).load(foodModel.getImage()).into(img_itemImage);

                                        // txt_itemPrice.setText(foodModel.getPrice());
                                        txt_itemName.setText(foodModel.getName());
                                        txt_itemOriginalPrice.setText(getResources().getString(R.string.Rs) + " " + foodModel.getPrice());
                                        if (foodModel.getDiscount() != null && !foodModel.getDiscount().isEmpty()) {
                                            double dis = Double.parseDouble(foodModel.getDiscount());
                                            if (dis > 0) {
                                                txt_itemOriginalPrice.setVisibility(View.VISIBLE);
                                                txt_discount.setVisibility(View.VISIBLE);
                                                txt_discount.setText(" " + foodModel.getDiscount() + " " + "Off");
                                            }
                                        }
                                           txt_itemDescription.setText(foodModel.getDescription());

                                        if (foodModel.getAvailability() != null && !foodModel.getAvailability().isEmpty()) {
                                            if (foodModel.getAvailability().equalsIgnoreCase("No")) {
                                                txt_outOfStack.setVisibility(View.VISIBLE);
                                                btn_addToCart.setVisibility(View.GONE);
                                            }
                                        }
                                        btn_addToCart.setOnClickListener(view -> {
                                            if (foodModel.getAvailability() != null && !foodModel.getAvailability().isEmpty()) {
                                                if (foodModel.getAvailability().equalsIgnoreCase("Yes")) {
                                                    int pos = view.getId();
                                                    new Database(getBaseContext()).addToCart(new Order(
                                                            userNo,
                                                            checkId, foodModel.getName(),
                                                            "1", foodModel.getPrice(), foodModel.getDiscount()
                                                            , foodModel.getImage(), foodModel.getCategoryType(),
                                                            foodModel.getCategoryName(),foodModel.getType(),
                                                            foodModel.getDepartment(),foodModel.getParticular(),""+foodModel.getGst(),
                                                            ""+foodModel.getHappyHour(),""+foodModel.getKitchen(),userFirstName));
                                                    int count = new Database(getBaseContext()).getCountCart(userNo);

                                                    Double price = Double.parseDouble(txt_price.getText().toString());
                                                    Double price1 = Double.parseDouble(str_price);
                                                    Double total = price + price1;
                                                    txt_price.setText(String.valueOf(total));
                                                    txt_cartPrice.setText(" " + count + "  Item | " + total);
                                                    Paper.book().write(Common.TOTAL, "" + total);
                                                    txtQuantityCart.setText(String.valueOf(1));
                                                    mLlyCartCount.setVisibility(View.VISIBLE);
                                                    btn_addToCart.setVisibility(View.GONE);
                                                } else {
                                                    Toast.makeText(FoodMenuActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                        btnDecreaseCart.setOnClickListener(view -> {
                                            int pos = view.getId();
                                            String quantityValue = txtQuantityCart.getText().toString();
                                            cartQuantity = Integer.parseInt(quantityValue);
                                            if (cartQuantity != 0 && cartQuantity <= 1) {
                                                cartQuantity = cartQuantity - 1;
                                                mLlyCartCount.setVisibility(View.GONE);
                                                btn_addToCart.setVisibility(View.VISIBLE);

                                                new Database(getBaseContext()).removeFromCart(checkId, userNo);
                                                Double priceA = Double.parseDouble(txt_price.getText().toString());
                                                Double priceB = Double.parseDouble(str_price);
                                                Double totalT = priceA - priceB;
                                                txt_price.setText(String.valueOf(totalT));
                                                Paper.book().write(Common.TOTAL, "" + totalT);
                                                txtQuantityCart.setText(String.valueOf(cartQuantity));
                                                int c = new Database(getBaseContext()).getCountCart(userNo);
                                                txt_cartPrice.setText(" " + c + "  Item | " + totalT);

                                            } else {
                                                cartQuantity = cartQuantity - 1;
                                                if (cartQuantity >= 1) {
                                                    Double price = Double.parseDouble(txt_price.getText().toString());
                                                    Double price1 = Double.parseDouble(str_price);
                                                    Double total = price - price1;
                                                    Paper.book().write(Common.TOTAL, "" + total);
                                                    txt_price.setText(String.valueOf(total));


                                                    new Database(getBaseContext()).decreaseCart(userNo, checkId);
                                                    txtQuantityCart.setText(String.valueOf(cartQuantity));

                                                    int count1 = new Database(getBaseContext()).getCountCart(userNo);
                                                    txt_cartPrice.setText(" " + count1 + "  Item | " + total);
                                                }

                                            }
                                        });
                                        btnIncreaseCart.setOnClickListener(view -> {
                                            int pos = view.getId();
                                            String quantityValue = txtQuantityCart.getText().toString();
                                            cartQuantity = Integer.parseInt(quantityValue);
                                            cartQuantity = cartQuantity + 1;

                                            Double price = Double.parseDouble(txt_price.getText().toString());
                                            Double price1 = Double.parseDouble(str_price);
                                            Double total = price + price1;
                                            Paper.book().write(Common.TOTAL, "" + total);
                                            txt_price.setText(String.valueOf(total));
                                            txt_cartPrice.setText(String.valueOf(total));

                                            new Database(getBaseContext()).increaseCart(userNo, checkId);

                                            txtQuantityCart.setText(String.valueOf(cartQuantity));

                                            int count2 = new Database(getBaseContext()).getCountCart(userNo);
                                            txt_cartPrice.setText(" " + count2 + "  Item | " + total);
                                        });
                                        img_itemImage.setOnClickListener(view -> {
                                            Intent foodDetails = new Intent(FoodMenuActivity.this, FoodDetails.class);
                                            foodDetails.putExtra("FoodId", checkId);
                                            foodDetails.putExtra("RestaurantId", restaurantMedId);
                                            foodDetails.putExtra("CategoryName", "All");
                                            startActivity(foodDetails);
                                        });

                                        viewHolder.mLlyInnerLayout.addView(layout1);
                                    }
                                    //   reference.removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Log.e("condition", "false");
                            viewHolder.mLlyItem.setVisibility(View.GONE);
                            viewHolder.mLlyItemClose.setVisibility(View.GONE);
                            viewHolder.mLlyParentLayout.setVisibility(View.GONE);
                        }
                    }
                }
                @NonNull
                @Override
                public FoodMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.menu_cate_item_layout, parent, false);

                    return new FoodMenuHolder(itemView);
                }
            };
            foodMenuAdapter.startListening();
            // barMenuAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(foodMenuAdapter);

        } else {
            Toast.makeText(FoodMenuActivity.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
        }
    }
    private void readCategories() {
        Query reference = categories.orderByChild("categoryType").equalTo("Non-OfferFood");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodMenuNames.clear();
                categoryPos = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Category category = snapshot.getValue(Category.class);

                    if (categoryPos == 0){
                        if (category.getType() != null && !category.getType().isEmpty()){
                            if (category.getType().equalsIgnoreCase("Food")){
                                if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                        && category.getTimeTo() != null && !category.getTimeTo().isEmpty())
                                {
                                    Calendar currTime = Calendar.getInstance();
                                    int hour = currTime.get(Calendar.HOUR_OF_DAY);
                                    Log.e("hour",""+hour);
                                    int fromTime = Integer.parseInt(category.getTimeFrom());
                                    int toTime = Integer.parseInt(category.getTimeTo());
                                    if (hour >= fromTime && hour < toTime) {
                                        Log.e("condition", "true");
                                        foodMenuNames.add(new MenuNamesModel(category.getName(),""+categoryPos));
                                    }
                                }
                            }
                            Log.e("category : pos",""+category.getName()+" : "+categoryPos);
                        }

                    }else {
                        if (category.getType() != null && !category.getType().isEmpty()){
                            if (category.getType().equalsIgnoreCase("Food")){
                                if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                        && category.getTimeTo() != null && !category.getTimeTo().isEmpty())
                                {
                                    Calendar currTime = Calendar.getInstance();
                                    int hour = currTime.get(Calendar.HOUR_OF_DAY);
                                    Log.e("hour",""+hour);
                                    int fromTime = Integer.parseInt(category.getTimeFrom());
                                    int toTime = Integer.parseInt(category.getTimeTo());
                                    if (hour >= fromTime && hour < toTime) {
                                        Log.e("condition", "true");
                                        foodMenuNames.add(new MenuNamesModel(category.getName(),""+categoryPos));
                                    }
                                }

                            }
                            Log.e("category : pos",""+category.getName()+" : "+categoryPos);
                        }
                    }
                    categoryPos++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void initUi() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_menu);
        txt_viewCart = findViewById(R.id.txt_viewCart);
        txt_viewCart.setEnabled(true);
        txt_cartPrice = findViewById(R.id.txt_cartPrice);
        txt_price = findViewById(R.id.txt_price);
    }
    private void displayMenuDialog(String itemName) {
        dialog_menu_view = new Dialog(this);
        if (dialog_menu_view.getWindow() != null) {
            dialog_menu_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_menu_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_menu_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_menu_view.setContentView(R.layout.menu_dialog);
        dialog_menu_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_menu_view.setCancelable(true); // can dismiss alert screen when user click back buttonon
        dialog_menu_view.setCanceledOnTouchOutside(true);
        dialog_menu_view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_menu_view.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_menu_view.setCancelable(true);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        LinearLayout mlly_menuItems = dialog_menu_view.findViewById(R.id.mlly_menuItems);
        mlly_menuItems.removeAllViews();
        if (itemName.equalsIgnoreCase("food")){
            for (int i = 0; i < foodMenuNames.size(); i++) {
                LinearLayout layout = (LinearLayout)this.getLayoutInflater().inflate(R.layout.menu_dialog_layout, mlly_menuItems, false);
                final TextView txt_menuName = (TextView) layout.findViewById(R.id.txt_menuName);
                final TextView txt_menuCount = (TextView) layout.findViewById(R.id.txt_menuCount);

                txt_menuName.setText(foodMenuNames.get(i).getMenuName());
                //  txt_menuCount.setText("("+menuNames.get(i).getMenuCount()+" Items)");
                layout.setId(i);
                int finalI = i;
                layout.setOnClickListener(view -> {
                    int pos = view.getId();
                    int position = Integer.parseInt(foodMenuNames.get(pos).getMenuCount());
                    //  recyclerView.scrollToPosition(position);
                    smoothScroller.setTargetPosition(position);
                    layoutManager.startSmoothScroll(smoothScroller);
                    dialog_menu_view.dismiss();
                });
                mlly_menuItems.addView(layout);
            }
        }/*else {
            for (int i = 0; i < foodMenuNames.size(); i++) {
                LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.menu_dialog_layout, mlly_menuItems, false);
                final TextView txt_menuName = (TextView) layout.findViewById(R.id.txt_menuName);
                final TextView txt_menuCount = (TextView) layout.findViewById(R.id.txt_menuCount);

                txt_menuName.setText(foodMenuNames.get(i).getMenuName());
                //  txt_menuCount.setText("("+barMenuNames.get(i).getMenuCount()+" Items)");
                layout.setId(i);
                int finalI = i;
                layout.setOnClickListener(view -> {
                    int pos = view.getId();
                    int position = Integer.parseInt(foodMenuNames.get(pos).getMenuCount());
                    //  recyclerView.scrollToPosition(position);
                    smoothScroller.setTargetPosition(position);
                    layoutManager.startSmoothScroll(smoothScroller);
                    dialog_menu_view.dismiss();
                });
                mlly_menuItems.addView(layout);
            }
        }*/

        dialog_menu_view.show();
    }
}
