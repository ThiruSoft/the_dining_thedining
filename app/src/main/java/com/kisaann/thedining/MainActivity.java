package com.kisaann.thedining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kisaann.thedining.Adapters.MenuListDataAdapterA;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Models.Category;
import com.kisaann.thedining.Models.CustomerSupportModel;
import com.kisaann.thedining.Models.Food;
import com.kisaann.thedining.Models.MenuNamesModel;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Models.RestBannersModel;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Utils.Utility;
import com.kisaann.thedining.ViewHolders.FoodItemViewHolder;
import com.kisaann.thedining.ViewHolders.RestaurantMenuDataHolder;
import com.kisaann.thedining.ViewHolders.RestaurantMenuHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SearchView.OnQueryTextListener {

    public static TextView txt_viewCart, txt_cartPrice, txt_price;
    public static SearchView editsearch;
    public static SearchView editDialogSearch;
    TextView txtFullName;
    CircleImageView imageView;
    TextView txtDesignation;
    TextView txt_edit;
    TextView txt_wallet;
    TextView txt_all;
    TextView txt_starters;
    TextView txt_desserts;
    TextView txt_checkout;
    ImageView img_loading;
    String isLogin;
    RecyclerView recyclerView;
    RecyclerView recycler_menu;
    /*  MyAdapter adapter;
      List<MyItem> itemList;*/
    String tableNo;
    String restId;
    MaterialEditText edt_yourName;
    MaterialEditText edt_emailId;
    MaterialEditText edt_subject;
    MaterialEditText edt_message;
    MaterialEditText edt_last_name;
    Dialog dialog_customize_view;
    CustomerSupportModel customerSupportModel;
    DatabaseReference customerSupportForms;
    FirebaseDatabase database;
    String restaurantMedId;
    DatabaseReference categories;
    DatabaseReference foodListAll;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, RestaurantMenuHolder> restarurantadapter;
    FirebaseRecyclerAdapter<Food, RestaurantMenuDataHolder> restaurantMenuAdapter;
    FirebaseRecyclerAdapter<Food, FoodItemViewHolder> restMenuAdapter;
    Query listFoodByCategoryId;
    FirebaseRecyclerAdapter<Food, FoodItemViewHolder> foodAdapter;
    DatabaseReference foodList;
    List<Order> cart = new ArrayList<>();
    Dialog dialog_menu_view;
    List<MenuNamesModel> menuNames = new ArrayList<>();
    List<MenuNamesModel> barMenuNames = new ArrayList<>();
    List<MenuNamesModel> foodMenuNames = new ArrayList<>();
    int categoryPos = 0;
    ImageSlider image_slider;
    List<SlideModel> imageList = new ArrayList<>();
    String currentVersion;
    Dialog dialog_customize_update;
    String userPhone;
    String userFirstName;
    List<Order> cartItems = new ArrayList<>();
    Double totalAmount = 0.0;
    RecyclerView.SmoothScroller smoothScroller;
    Dialog dialog_customize_search;
    private LinearLayout layout1;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String time;
    private String date;
    private int cartQuantity = 1;
    private List<Food> mFood;
    private List<Food> mNames = new ArrayList<>();
    private MenuListDataAdapterA menuDataAdapter;
    private ListView listViewItems;
    private ListView listDialogViewItems;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // startActivity(new Intent(MainActivity.this,CartItemsActivity.class));
                    Calendar currTime = Calendar.getInstance();
                    int hour = currTime.get(Calendar.HOUR_OF_DAY);
                    int fromTime = 12;
                    int toTime = 19;
                    if (hour >= fromTime && hour < toTime) {
                        startActivity(new Intent(MainActivity.this, HappyHoursActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Happy 12pm to 7pm Only.", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case R.id.navigation_dashboard:
                    //    startActivity(new Intent(MainActivity.this, OrderStatus.class));

                    /*Intent intentA = new Intent(MainActivity.this, FoodMenuActivity.class);
                    intentA.putExtra("TableNo",tableNo);
                    startActivity(intentA);*/
                    searchProductsDialog();
                    return true;
                case R.id.navigation_notifications:
                    /*Intent i = new Intent(MainActivity.this, MessageChatActivity.class);
                    i.putExtra("from", "Owner");
                    startActivity(i);*/
                    Intent intent = new Intent(MainActivity.this, BarMenuActivity.class);
                    // intent.putExtra("TableNo",tableNo);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnTopNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all:
                    Intent foodList = new Intent(MainActivity.this, FoodList.class);
                    foodList.putExtra("CategoryId", "");
                    foodList.putExtra("CategoryName", "All");
                    foodList.putExtra("RestaurantId", restaurantMedId);
                    startActivity(foodList);
                    return true;
                case R.id.navigation_starters:
                    Intent foodList1 = new Intent(MainActivity.this, FoodList.class);
                    foodList1.putExtra("CategoryId", "");
                    foodList1.putExtra("CategoryName", "Starters");
                    foodList1.putExtra("RestaurantId", restaurantMedId);
                    startActivity(foodList1);
                    return true;
                case R.id.navigation_desserts:
                    Intent foodList2 = new Intent(MainActivity.this, FoodList.class);
                    foodList2.putExtra("CategoryId", "");
                    foodList2.putExtra("CategoryName", "Desserts");
                    foodList2.putExtra("RestaurantId", restaurantMedId);
                    startActivity(foodList2);
                    return true;

                case R.id.navigation_check_out:
                    startActivity(new Intent(MainActivity.this, CheckOutActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init paper
        Paper.init(this);
        // initData();
        initUi();
        //  setData();
        mFood = new ArrayList<>();
        restaurantMedId = Paper.book().read(Common.RESTAURANT_ID);
        userPhone = Paper.book().read(Common.USER_KEY);
        userFirstName = Paper.book().read(Common.USER_NAME);
        tableNo = Paper.book().read(Common.TABLE_NO);
        loadBannerImages(restaurantMedId);
        if (getIntent() != null) {
            isLogin = getIntent().getStringExtra("isLogin");
            /*restaurantMedId = getIntent().getStringExtra("RestaurantMenuId");*/
        }
        if (isLogin != null && isLogin.equalsIgnoreCase("true")) {
            if (userPhone != null && !userPhone.isEmpty()) {
                updateToken(FirebaseInstanceId.getInstance().getToken());
            }
        }
        // init firebase
        database = FirebaseDatabase.getInstance();
        customerSupportForms = database.getReference("CustomerSupport");
        categories = database.getReference(restaurantMedId + "_" + "Category");
        foodListAll = database.getReference(restaurantMedId + "_" + "All");

        readCategories();
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        txt_viewCart = findViewById(R.id.txt_viewCart);
        txt_cartPrice = findViewById(R.id.txt_cartPrice);
        txt_price = findViewById(R.id.txt_price);

        BottomNavigationView navView = findViewById(R.id.navigation);
        BottomNavigationView navViewTop = findViewById(R.id.top_navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navViewTop.setOnNavigationItemSelectedListener(mOnTopNavigationItemSelectedListener);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txt_fullName);
        txtDesignation = (TextView) headerView.findViewById(R.id.txt_emailId);
        txt_edit = (TextView) headerView.findViewById(R.id.txt_edit);
        txt_wallet = (TextView) headerView.findViewById(R.id.txt_wallet);
        imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        if (isLogin != null && isLogin.equalsIgnoreCase("true")) {
            txtFullName.setText(Paper.book().read(Common.USER_NAME));
            txtDesignation.setText(Paper.book().read(Common.USER_EMAIL));
            Double walletBal = Double.parseDouble(Paper.book().read(Common.USER_BAL));
            Locale locale = new Locale("en", "KD");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txt_wallet.setText(fmt.format(walletBal));
            String image = Paper.book().read(Common.USER_IMAGE);
            if (image.equals("default")) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(getApplicationContext()).load(image).into(imageView);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);

        smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        // init view

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //  loadRestaurantMenuData();
        /*if (restaurantMedId != null && !restaurantMedId.isEmpty()){
            loadFoodList(restaurantMedId);
        }*/
        txt_viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItems = new Database(getApplicationContext()).getCart(userPhone);
                if (cartItems.size() > 0) {
                    startActivity(new Intent(MainActivity.this, CartItemsActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Your cart is empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
       /* String total = Paper.book().read(Common.TOTAL);
        if (total != null && !total.isEmpty()) {
            txt_price.setText(total);
            int count1 = new Database(getBaseContext()).getCountCart(userPhone);
            txt_cartPrice.setText(" " + count1 + "  Item | " + total);
        }*/

        FloatingActionButton fab = findViewById(R.id.fab_menu);
        FloatingActionButton fab_barMenu = findViewById(R.id.fab_barMenu);
        /*if (restaurantMedId.equalsIgnoreCase("-LkYjyxYoG-m3Vl5zDAQ")) {
            fab_barMenu.setVisibility(View.VISIBLE);
        }*/
      /*  if (barMenuNames.size() > 0){k
            fab_barMenu.setVisibility(View.VISIBLE);
        }*/
        fab.setOnClickListener(view -> {
            displayMenuDialog("food");
        });
        fab_barMenu.setOnClickListener(view -> {
            displayMenuDialog("bar");
        });

        // version check

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //  new GetVersionCode().execute();
    }

    private void searchProductsDialog() {
        dialog_customize_search = new Dialog(this);
        if (dialog_customize_search.getWindow() != null) {
            dialog_customize_search.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_search.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_search.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_search.setContentView(R.layout.search_products_dialog);
        dialog_customize_search.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog_customize_search.setCancelable(false); // can dismiss alert screen when user click back buttonon
        dialog_customize_search.setCanceledOnTouchOutside(false);
        dialog_customize_search.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_search.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_customize_search.setCancelable(false);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        editDialogSearch = dialog_customize_search.findViewById(R.id.dialog_search);
        editDialogSearch.setOnQueryTextListener(this);
        listDialogViewItems = dialog_customize_search.findViewById(R.id.listDialogViewItems);
        ImageView img_close = dialog_customize_search.findViewById(R.id.img_close);


        mNames.clear();
        Query reference1 = categories;
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category.getName() != null && !category.getName().isEmpty()) {
                        if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                && category.getTimeTo() != null && !category.getTimeTo().isEmpty()) {
                            Calendar currTime = Calendar.getInstance();
                            int hour = currTime.get(Calendar.HOUR_OF_DAY);
                            Log.e("hour", "" + hour);
                            int fromTime = Integer.parseInt(category.getTimeFrom());
                            int toTime = Integer.parseInt(category.getTimeTo());
                            if (hour >= fromTime && hour < toTime) {
                                Log.e("condition", "true");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(restaurantMedId + "_" + category.getName());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            Food foodModel = snapshot.getValue(Food.class);

                                            if (foodModel.getProductId() != null && !foodModel.getProductId().isEmpty()) {
                                                mNames.add(foodModel);
                                                Log.e("productName", "" + foodModel.getProductId() + " " + foodModel.getName() + " " + foodModel.getCategoryName());
                                            }
                                        }
                                        menuDataAdapter = new MenuListDataAdapterA(MainActivity.this, mNames, userPhone, userFirstName);
                                        // recyclerView.setAdapter(menuDataAdapter);
                                        listDialogViewItems.setAdapter(menuDataAdapter);
                                        reference.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_customize_search.dismiss();
                loadRestaurantMenuData();
            }
        });
        dialog_customize_search.show();


    }

    private void loadBannerImages(String restaurantMedId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(restaurantMedId + "_Banners");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestBannersModel bannersModel = snapshot.getValue(RestBannersModel.class);
                    imageList.add(new SlideModel(bannersModel.getImageURL()));
                }
                image_slider.setImageList(imageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Are you sure?");
            alertDialog.setMessage("Want to exit the application.");
            alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            LayoutInflater inflater = LayoutInflater.from(this);

            alertDialog.setPositiveButton("Exit", (dialog, which) -> {
                dialog.dismiss();
                finish();

            });
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            /*super.onBackPressed();*/
        }
    }

    private void readCategories() {
        //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference(restaurantMedId+"_"+"Category");
        // Query reference = categories.orderByChild("offerType").equalTo("Non-Offer");
        Query reference = categories.orderByChild("categoryType").equalTo("Non-OfferFood");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodMenuNames.clear();
                categoryPos = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);

                    if (categoryPos == 0) {
                        if (category.getType() != null && !category.getType().isEmpty()) {
                            if (category.getType().equalsIgnoreCase("Food")) {
                                if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                        && category.getTimeTo() != null && !category.getTimeTo().isEmpty()) {
                                    Calendar currTime = Calendar.getInstance();
                                    int hour = currTime.get(Calendar.HOUR_OF_DAY);
                                    Log.e("hour", "" + hour);
                                    int fromTime = Integer.parseInt(category.getTimeFrom());
                                    int toTime = Integer.parseInt(category.getTimeTo());
                                    if (hour >= fromTime && hour < toTime) {
                                        Log.e("condition", "true");
                                        foodMenuNames.add(new MenuNamesModel(category.getName(), "" + categoryPos));
                                    }
                                }
                            }
                            Log.e("category : pos", "" + category.getName() + " : " + categoryPos);
                        }

                    } else {
                        if (category.getType() != null && !category.getType().isEmpty()) {
                            if (category.getType().equalsIgnoreCase("Food")) {
                                if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                        && category.getTimeTo() != null && !category.getTimeTo().isEmpty()) {
                                    Calendar currTime = Calendar.getInstance();
                                    int hour = currTime.get(Calendar.HOUR_OF_DAY);
                                    Log.e("hour", "" + hour);
                                    int fromTime = Integer.parseInt(category.getTimeFrom());
                                    int toTime = Integer.parseInt(category.getTimeTo());
                                    if (hour >= fromTime && hour < toTime) {
                                        Log.e("condition", "true");
                                        foodMenuNames.add(new MenuNamesModel(category.getName(), "" + categoryPos));
                                    }
                                }

                            }
                            Log.e("category : pos", "" + category.getName() + " : " + categoryPos);
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
        dialog_menu_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
        if (itemName.equalsIgnoreCase("food")) {
            for (int i = 0; i < foodMenuNames.size(); i++) {
                LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.menu_dialog_layout, mlly_menuItems, false);
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
        } else {
            for (int i = 0; i < barMenuNames.size(); i++) {
                LinearLayout layout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.menu_dialog_layout, mlly_menuItems, false);
                final TextView txt_menuName = (TextView) layout.findViewById(R.id.txt_menuName);
                final TextView txt_menuCount = (TextView) layout.findViewById(R.id.txt_menuCount);

                txt_menuName.setText(barMenuNames.get(i).getMenuName());
                //  txt_menuCount.setText("("+barMenuNames.get(i).getMenuCount()+" Items)");
                layout.setId(i);
                int finalI = i;
                layout.setOnClickListener(view -> {
                    int pos = view.getId();
                    int position = Integer.parseInt(barMenuNames.get(pos).getMenuCount());
                    //  recyclerView.scrollToPosition(position);
                    smoothScroller.setTargetPosition(position);
                    layoutManager.startSmoothScroll(smoothScroller);
                    dialog_menu_view.dismiss();
                });
                mlly_menuItems.addView(layout);
            }
        }

        dialog_menu_view.show();
    }

    private void loadFoodList(String restaurantId) {
        listFoodByCategoryId = foodListAll.orderByChild("restaurantId").equalTo(restaurantId);
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(listFoodByCategoryId, Food.class)
                .build();

        restMenuAdapter = new FirebaseRecyclerAdapter<Food, FoodItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodItemViewHolder viewHolder, int position, @NonNull Food model) {
                viewHolder.txt_itemName.setText(model.getName());
                viewHolder.txt_itemOriginalPrice.setText(getResources().getString(R.string.Rs) + "" + model.getPrice());
                viewHolder.txt_discount.setText(" " + model.getDiscount() + " " + "Off");

                Double original_price = Double.parseDouble(model.getPrice());
                int discount = Integer.parseInt(model.getDiscount());
                Double discount_price = (original_price / 100.0f) * discount;
                Double servicePrice = Double.parseDouble(model.getPrice()) - discount_price;
                String str_price = "" + new DecimalFormat("##.##").format(servicePrice);
                viewHolder.txt_itemPrice.setText(getResources().getString(R.string.Rs) + "" + str_price);
                Glide.with(getApplicationContext()).load(model.getImage()).into(viewHolder.img_itemImage);

                viewHolder.btn_addToCart.setId(position);
                viewHolder.btnDecreaseCart.setId(position);
                viewHolder.btnIncreaseCart.setId(position);
                viewHolder.img_itemImage.setId(position);
                viewHolder.txtQuantityCart.setId(position);

                viewHolder.btn_addToCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    new Database(getBaseContext()).addToCart(new Order(
                            userPhone,
                            restMenuAdapter.getRef(position).getKey(), model.getName(),
                            "1", model.getPrice(), model.getDiscount()
                            , model.getImage(), model.getCategoryType(), model.getCategoryName(),
                            model.getType(), model.getDepartment(), model.getParticular(), model.getGst(),
                            model.getHappyHour(), model.getKitchen(), userFirstName));

                    int count = new Database(getBaseContext()).getCountCart(userPhone);

                    Double price = Double.parseDouble(txt_price.getText().toString());
                    Double price1 = Double.parseDouble(str_price);
                    Double total = price + price1;
                    txt_price.setText(String.valueOf(total));
                    txt_cartPrice.setText(" " + count + "  Item | " + total);

                    viewHolder.mLlyCartCount.setVisibility(View.VISIBLE);
                    viewHolder.btn_addToCart.setVisibility(View.GONE);


                });
                viewHolder.btnDecreaseCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    String quantityValue = viewHolder.txtQuantityCart.getText().toString();
                    cartQuantity = Integer.parseInt(quantityValue);
                    if (cartQuantity <= 1) {
                        viewHolder.mLlyCartCount.setVisibility(View.GONE);
                        viewHolder.btn_addToCart.setVisibility(View.VISIBLE);

                        new Database(getBaseContext()).removeFromCart(restMenuAdapter.getRef(position).getKey(), userPhone);
                        Double priceA = Double.parseDouble(txt_price.getText().toString());
                        Double priceB = Double.parseDouble(str_price);
                        Double totalT = priceA - priceB;
                        txt_price.setText(String.valueOf(totalT));

                        int c = new Database(getBaseContext()).getCountCart(userPhone);
                        txt_cartPrice.setText(" " + c + "  Item | " + totalT);

                    } else {
                        cartQuantity = cartQuantity - 1;
                        Double price = Double.parseDouble(txt_price.getText().toString());
                        Double price1 = Double.parseDouble(str_price);
                        Double total = price - price1;
                        txt_price.setText(String.valueOf(total));

                        new Database(getBaseContext()).decreaseCart(userPhone, restMenuAdapter.getRef(position).getKey());
                        viewHolder.txtQuantityCart.setText(String.valueOf(cartQuantity));

                        int count1 = new Database(getBaseContext()).getCountCart(userPhone);
                        txt_cartPrice.setText(" " + count1 + "  Item | " + total);
                    }
                });
                viewHolder.btnIncreaseCart.setOnClickListener(view -> {
                    int pos = view.getId();
                    String quantityValue = viewHolder.txtQuantityCart.getText().toString();
                    cartQuantity = Integer.parseInt(quantityValue);
                    cartQuantity = cartQuantity + 1;

                    Double price = Double.parseDouble(txt_price.getText().toString());
                    Double price1 = Double.parseDouble(str_price);
                    Double total = price + price1;
                    txt_price.setText(String.valueOf(total));
                    /*txt_cartPrice.setText(String.valueOf(total));*/

                    new Database(getBaseContext()).increaseCart(userPhone, restMenuAdapter.getRef(position).getKey());
                    /*new Database(getBaseContext()).updateCart(cartQuantity,
                            userPhone,adapter.getRef(position).getKey());*/
                    viewHolder.txtQuantityCart.setText(String.valueOf(cartQuantity));

                    int count2 = new Database(getBaseContext()).getCountCart(userPhone);
                    txt_cartPrice.setText(" " + count2 + "  Item | " + total);
                });
                viewHolder.img_itemImage.setOnClickListener(view -> {
                    Intent foodDetails = new Intent(MainActivity.this, FoodDetails.class);
                    foodDetails.putExtra("FoodId", restMenuAdapter.getRef(position).getKey());
                    foodDetails.putExtra("RestaurantId", restaurantId);
                    foodDetails.putExtra("CategoryName", "All");
                    startActivity(foodDetails);
                });
               /* final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       *//* Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();*//*
                        // start new activity
                        Intent foodDetails = new Intent(FoodList.this, FoodDetails.class);
                        foodDetails.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetails);
                    }
                });*/
            }

            @NonNull
            @Override
            public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item_layout, parent, false);

                return new FoodItemViewHolder(itemView);
            }
        };
        restMenuAdapter.startListening();
        restMenuAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(restMenuAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRestaurantMenuData();
        /*String total = Paper.book().read(Common.TOTAL);
        if (total != null && !total.isEmpty()) {
            txt_price.setText(total);
            int count1 = new Database(getBaseContext()).getCountCart(userPhone);
            txt_cartPrice.setText(" " + count1 + "  Item | " + total);
        }*/
        //  readMenuListData();
        try {
            cartItems = new Database(getApplicationContext()).getCart(userPhone);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        totalAmount = 0.0;
        for (Order order : cartItems) {
            Double original_price = Double.parseDouble(order.getPrice());
            Double discount = Double.parseDouble(order.getDiscount() + ".0");
            Double discount_price = (original_price / 100.0f) * discount;
            Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

            totalAmount += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "KD");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        }
        String total = "" + totalAmount;
        if (total != null && !total.isEmpty()) {
            txt_price.setText(total);
            int count1 = new Database(getBaseContext()).getCountCart(userPhone);
            txt_cartPrice.setText(" " + count1 + "  Item | " + total);
        }
        if (restarurantadapter != null) {
            restarurantadapter.startListening();
        }
        if (restaurantMenuAdapter != null) {
            restaurantMenuAdapter.startListening();
        }
    }

    private void readMenuListData() {
        mNames.clear();
        Query reference1 = categories;
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category.getName() != null && !category.getName().isEmpty()) {
                        if (category.getTimeFrom() != null && !category.getTimeFrom().isEmpty()
                                && category.getTimeTo() != null && !category.getTimeTo().isEmpty()) {
                            Calendar currTime = Calendar.getInstance();
                            int hour = currTime.get(Calendar.HOUR_OF_DAY);
                            Log.e("hour", "" + hour);
                            int fromTime = Integer.parseInt(category.getTimeFrom());
                            int toTime = Integer.parseInt(category.getTimeTo());
                            if (hour >= fromTime && hour < toTime) {
                                Log.e("condition", "true");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(restaurantMedId + "_" + category.getName());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            Food foodModel = snapshot.getValue(Food.class);

                                            if (foodModel.getProductId() != null && !foodModel.getProductId().isEmpty()) {
                                                mNames.add(foodModel);
                                                Log.e("productName", "" + foodModel.getProductId() + " " + foodModel.getName() + " " + foodModel.getCategoryName());
                                            }
                                        }
                                        menuDataAdapter = new MenuListDataAdapterA(MainActivity.this, mNames, userPhone, userFirstName);
                                        // recyclerView.setAdapter(menuDataAdapter);
                                        listViewItems.setAdapter(menuDataAdapter);
                                        reference.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
                reference1.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, "true", userPhone); // false bcz token"
        // +send from
        // client app
        tokens.child(userPhone).setValue(data);
    }

    private void initUi() {
        listViewItems = findViewById(R.id.listViewItems);
        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        image_slider = findViewById(R.id.image_slider);
        img_loading = findViewById(R.id.img_loading);
        recyclerView = findViewById(R.id.recycler_view);
        recycler_menu = findViewById(R.id.recycler_menu);
        txt_all = findViewById(R.id.txt_all);
        txt_starters = findViewById(R.id.txt_starters);
        txt_desserts = findViewById(R.id.txt_desserts);
        txt_checkout = findViewById(R.id.txt_checkout);

        txt_all.setOnClickListener(this);
        txt_starters.setOnClickListener(this);
        txt_desserts.setOnClickListener(this);
        txt_checkout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*scanQrcode();*/
            startActivity(new Intent(MainActivity.this, CheckOutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, CartItemsActivity.class));
        }/* else if (id == R.id.nav_gallery) {
            scanQrcode();
        } */ else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, OrderStatus.class));
        } else if (id == R.id.nav_tools) {
            //  startActivity(new Intent(MainActivity.this,DownloadInvoiceActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, OffersCouponsActivity.class));
        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
        } else if (id == R.id.nav_chat) {
            Intent i = new Intent(MainActivity.this, MessageChatActivity.class);
            i.putExtra("from", "Owner");
            startActivity(i);
        } else if (id == R.id.nav_support) {
            Intent i = new Intent(MainActivity.this, MessageChatActivity.class);
            i.putExtra("from", "Customer");
            startActivity(i);
            /*displayOffersCustomizeDialog();*/
        } else if (id == R.id.nav_game) {
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            i.putExtra("from", "Customer");
            startActivity(i);
        } else if (id == R.id.nav_send) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Are you sure?");
            alertDialog.setMessage("Want to logout the application.");
            alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
            LayoutInflater inflater = LayoutInflater.from(this);

            alertDialog.setPositiveButton("Logout", (dialog, which) -> {
                dialog.dismiss();
                String orderId = Paper.book().read(Common.ORDER_ID);
                if (orderId == null || orderId.isEmpty()) {
                    // narmal logout
                    //   AccountKit.logOut();
                    Paper.book().destroy();
                    Common.currentUser = null;
                    Intent signIn = new Intent(MainActivity.this, SplashScreen.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signIn);
                    finish();
                } else {
                    AlertDialog.Builder alertDialogA = new AlertDialog.Builder(MainActivity.this);
                    alertDialogA.setTitle("Your payment pending");
                    alertDialogA.setMessage("Your payment not completed, please pay the bill amount. ");
                    alertDialogA.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                    LayoutInflater inflaterA = LayoutInflater.from(this);
                    alertDialogA.setPositiveButton("Okay", (dialogA, whichA) -> {
                        dialogA.dismiss();
                    });
                    alertDialogA.show();
                }


            });
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadRestaurantMenuData() {
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait....");
        mDialog.setCancelable(false);
        mDialog.show();
        if (Utility.isNetworkAvailable(getBaseContext())) {
            //   Query listFoodByCategoryId = categories.orderByChild("restaurantMenuId").equalTo(restaurantMedId);
            //  Query listFoodByCategoryId = categories.orderByChild("offerType").equalTo("Non-Offer");
            Query listFoodByCategoryId = categories.orderByChild("categoryType").equalTo("Non-OfferFood");
            FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                    .setQuery(listFoodByCategoryId, Category.class)
                    .build();
            /* menuNames.clear();*/

            restarurantadapter = new FirebaseRecyclerAdapter<Category, RestaurantMenuHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull RestaurantMenuHolder viewHolder, int position, @NonNull Category model) {
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

                                        boolean isExist = new Database(getBaseContext()).checkFoodExists(checkId, userPhone);
                                        if (isExist) {
                                            cart = new Database(getBaseContext()).getCartByProduct(checkId);
                                            for (Order order : cart) {
                                                txtQuantityCart.setText(order.getQuantity());
                                                mLlyCartCount.setVisibility(View.VISIBLE);
                                                btn_addToCart.setVisibility(View.GONE);
                                            }
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
                                                            userPhone,
                                                            checkId, foodModel.getName(),
                                                            "1", foodModel.getPrice(), foodModel.getDiscount()
                                                            , foodModel.getImage(), foodModel.getCategoryType(),
                                                            foodModel.getCategoryName(), foodModel.getType(),
                                                            foodModel.getDepartment(), foodModel.getParticular(), foodModel.getGst(),
                                                            foodModel.getHappyHour(), foodModel.getKitchen(), userFirstName));

                                                    int count = new Database(getBaseContext()).getCountCart(userPhone);

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
                                                    Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
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

                                                new Database(getBaseContext()).removeFromCart(checkId, userPhone);
                                                Double priceA = Double.parseDouble(txt_price.getText().toString());
                                                Double priceB = Double.parseDouble(str_price);
                                                Double totalT = priceA - priceB;
                                                txt_price.setText(String.valueOf(totalT));
                                                Paper.book().write(Common.TOTAL, "" + totalT);
                                                txtQuantityCart.setText(String.valueOf(cartQuantity));
                                                int c = new Database(getBaseContext()).getCountCart(userPhone);
                                                txt_cartPrice.setText(" " + c + "  Item | " + totalT);

                                            } else if (cartQuantity > 1) {
                                                cartQuantity = cartQuantity - 1;
                                                if (cartQuantity >= 1) {
                                                    Double price = Double.parseDouble(txt_price.getText().toString());
                                                    Double price1 = Double.parseDouble(str_price);
                                                    Double total = price - price1;
                                                    Paper.book().write(Common.TOTAL, "" + total);
                                                    txt_price.setText(String.valueOf(total));

                                                    new Database(getBaseContext()).decreaseCart(userPhone, checkId);
                                                    txtQuantityCart.setText(String.valueOf(cartQuantity));

                                                    int count1 = new Database(getBaseContext()).getCountCart(userPhone);
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

                                            new Database(getBaseContext()).increaseCart(userPhone, checkId);

                                            txtQuantityCart.setText(String.valueOf(cartQuantity));

                                            int count2 = new Database(getBaseContext()).getCountCart(userPhone);
                                            txt_cartPrice.setText(" " + count2 + "  Item | " + total);
                                        });
                                        img_itemImage.setOnClickListener(view -> {
                                            Intent foodDetails = new Intent(MainActivity.this, FoodDetails.class);
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
                public RestaurantMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.menu_cate_item_layout, parent, false);

                    return new RestaurantMenuHolder(itemView);
                }
            };
            restarurantadapter.startListening();
            //  restarurantadapter.notifyDataSetChanged();
            recyclerView.setAdapter(restarurantadapter);
            mDialog.dismiss();
        } else {
            mDialog.dismiss();
            Toast.makeText(MainActivity.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (restarurantadapter != null) {
            restarurantadapter.stopListening();
        }
        if (restaurantMenuAdapter != null) {
            restaurantMenuAdapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_all:
                callFoodActivity("All", restaurantMedId);
                break;
            case R.id.txt_starters:
                callFoodActivity("Starters", restaurantMedId);
                break;
            case R.id.txt_desserts:
                callFoodActivity("Desserts", restaurantMedId);
                break;
            case R.id.txt_checkout:
                startActivity(new Intent(MainActivity.this, CheckOutActivity.class));
                break;
        }
    }

    private void callFoodActivity(String name, String restaurantMedId) {
        Intent foodList = new Intent(MainActivity.this, FoodList.class);
        foodList.putExtra("CategoryId", "");
        foodList.putExtra("CategoryName", name);
        foodList.putExtra("RestaurantId", restaurantMedId);
        startActivity(foodList);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        if (menuDataAdapter != null) {
            menuDataAdapter.filter(text);
        }
        return false;
    }

    private void newUpdateDialog(String onlineVersion) {
        dialog_customize_update = new Dialog(this);
        if (dialog_customize_update.getWindow() != null) {
            dialog_customize_update.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_update.getWindow().setGravity(Gravity.CENTER);
        }
        dialog_customize_update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_update.setContentView(R.layout.app_update_custom_dialog);
        dialog_customize_update.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_update.setCancelable(false); // can dismiss alert screen when user click back buttonon
        dialog_customize_update.setCanceledOnTouchOutside(false);
        dialog_customize_update.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_update.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_customize_update.setCancelable(true);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);

        TextView txt_type = dialog_customize_update.findViewById(R.id.txt_type);
        TextView txt_updateMessage = dialog_customize_update.findViewById(R.id.txt_updateMessage);
        TextView txt_update = dialog_customize_update.findViewById(R.id.txt_update);
        ImageView img_close = dialog_customize_update.findViewById(R.id.img_close);

        txt_updateMessage.setText("Update " + onlineVersion + " is available to download. \n"
                + "Downloading the latest update you will get the latest features, improvements \n"
                + "and bug fixes of The Dining App.");
        txt_update.setOnClickListener(v -> {
            final String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        /*img_close.setOnClickListener(view -> dialog_customize_update.dismiss());*/

        dialog_customize_update.show();
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                Log.d("onlineVersion", "" + onlineVersion);
                /*if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show dialog
                }*/
                if (!currentVersion.equalsIgnoreCase(onlineVersion)) {
                    newUpdateDialog(onlineVersion);
                }
            }
            Log.d("update", "Current version " + currentVersion + " playstore version " + onlineVersion);
        }
    }
}
