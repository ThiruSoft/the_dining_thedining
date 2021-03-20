package com.kisaann.thedining;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kisaann.thedining.Adapters.CartAdapter;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.Database;
import com.kisaann.thedining.Helpers.RecyclerItemTouchListener;
import com.kisaann.thedining.Interfaces.RecyclerItemTouchHelperListener;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Models.Request;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Utils.Utility;
import com.kisaann.thedining.ViewHolders.CartViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemsActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RelativeLayout rootLayout;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    DatabaseReference orderPlaceCheck;
    DatabaseReference requestsRestuarant;
    DatabaseReference unPaidRequestsRestaurant;
    DatabaseReference unPaidRequestsKitchen;

    public TextView txtTotalPrice;
    public TextView totalAmount;
    Button btnPlaceOrder;
    List<Order> cart = new ArrayList<>();
    List<Order> cartConfirmOrder = new ArrayList<>();

    String shipingAddress;
    CartAdapter adapter;
    String month;

    String address, comments;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int UPDATE_INTERVAL = 5000;
    private static final int FATEST_INTERVAL = 3000;
    private static final int DISPLACEMENT = 10;
    private static final int LOCATION_REQUEST_CODE = 99;
    private static final int PLAY_SERVICE_REQUEST = 9997;
    // declare google map retrofit
    APIService mService;
    Request requestCoD;
    Dialog dialog_customize_view;
    MaterialEditText edt_comments;
    String ownerNo;
    String kitchenNo;
    String restId;
    String restName;
    String restGSTNo;
    String restAddress;
    String orderId;
    String tableNo;
    String time;
    String date;
    String totalOrderAmount;
    String orderAsFood = "";
    String userNo;
    String userFirstName;
    ProgressDialog mDialog;
    Query orderPlaceCheckTableNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Place Order");
        getSupportActionBar().setHomeButtonEnabled(true);
        // init paper
        Paper.init(this);
        // init service
        mService = Common.getFCMService();

        restId = Paper.book().read(Common.RESTAURANT_ID);
        ownerNo = Paper.book().read(Common.OWNER_NO);
        kitchenNo = Paper.book().read(Common.KITCHEN_NO);
        restName = Paper.book().read(Common.REST_NAME);
        restAddress = Paper.book().read(Common.REST_ADDRESS);
        restGSTNo = Paper.book().read(Common.REST_GST_NO);
      //  userNo = Common.currentUser.getPhoneNo();
        userNo = Paper.book().read(Common.USER_KEY);
        userFirstName = Paper.book().read(Common.USER_NAME);
        tableNo = Paper.book().read(Common.TABLE_NO);
        Log.e("kitchenNo",""+kitchenNo);
        Log.e("ownerNo",""+ownerNo);
        // init firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference(userNo+"_Requests");
        orderPlaceCheck = database.getReference(userNo+"_Requests");
        requestsRestuarant = database.getReference(restId+"_"+ownerNo+"_"+"Requests");
        unPaidRequestsRestaurant = database.getReference(restId+"_"+ownerNo+"_UnPaidRequests");
       // unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666857"+"_UnPaidRequests");
        if (ownerNo.equalsIgnoreCase("8886666856")){
            unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666860"+"_UnPaidRequests");
        }else if (ownerNo.equalsIgnoreCase("8886666854") || ownerNo.equalsIgnoreCase("8886666855")){
            unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666857"+"_UnPaidRequests");
        }else if (ownerNo.equalsIgnoreCase("7995591106")){
            unPaidRequestsKitchen = database.getReference(restId+"_"+"7995591107"+"_UnPaidRequests");
        }else {
            unPaidRequestsKitchen = database.getReference(restId+"_"+kitchenNo+"_UnPaidRequests");
        }
        recyclerView = findViewById(R.id.listCart);
        rootLayout = findViewById(R.id.rootLayout);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchListener(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        txtTotalPrice = findViewById(R.id.total);
        totalAmount = findViewById(R.id.totalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        LoadListFood();

        btnPlaceOrder.setOnClickListener(v -> {
            // create new request
            if (cart.size() > 0)
                /* startActivity(new Intent(CartItemsActivity.this,CheckOutActivity.class));
                 *//*showAlertDialog();*/
                displayConfirmOrderCustomizeDialog();
            else
                Toast.makeText(CartItemsActivity.this, "Your cart is empty!!", Toast.LENGTH_SHORT).show();
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void displayConfirmOrderCustomizeDialog() {
        dialog_customize_view = new Dialog(this);
        if (dialog_customize_view.getWindow() != null) {
            dialog_customize_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_view.setContentView(R.layout.confirm_order_customize_layout);
        dialog_customize_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_view.setCancelable(true); // can dismiss alert screen when user click back buttonon
        dialog_customize_view.setCanceledOnTouchOutside(true);
        dialog_customize_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_customize_view.getWindow();
        lp1.copyFrom(window1.getAttributes());
        dialog_customize_view.setCancelable(true);
        // This makes the dialog take up the full width
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        TextView txt_type = dialog_customize_view.findViewById(R.id.txt_type);
        TextView txt_submit = dialog_customize_view.findViewById(R.id.txt_submit);
        ImageView img_close = dialog_customize_view.findViewById(R.id.img_close);
        edt_comments = dialog_customize_view.findViewById(R.id.edt_comments);

        txt_submit.setOnClickListener(view -> {
            if (Utility.isNetworkAvailable(getBaseContext())) {
                if (cart.size() > 0) {
                    /*mDialog = new ProgressDialog(CartItemsActivity.this);
                    mDialog.setMessage("Please wait....");
                    mDialog.setCancelable(false);
                    mDialog.show();*/

                  //  orderId = Paper.book().read(Common.ORDER_ID);
                    orderPlaceCheckTableNo = orderPlaceCheck.orderByChild("paymentState").equalTo("UnPaid");
                    orderPlaceCheckTableNo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Request request = snapshot.getValue(Request.class);
                                assert request != null;
                                if (request.getTableNo().equalsIgnoreCase(tableNo)) {
                                    orderId = request.getOrderId();
                                    Log.e("TableNo , OrderId",""+request.getOrderId()+""+tableNo);
                                }
                            }
                            orderPlaceCheckTableNo.removeEventListener(this);

                            callPlaceOrderMethod();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(CartItemsActivity.this, "Your cart is empty!!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(CartItemsActivity.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
            }
        });

        img_close.setOnClickListener(view -> dialog_customize_view.dismiss());

        dialog_customize_view.show();
    }

    private void callPlaceOrderMethod() {
      //  dialog_customize_view.dismiss();
        if (orderId == null || orderId.isEmpty()) {
            String mobileNo = userNo;
            String strLastFourDi = mobileNo.length() >= 4 ? mobileNo.substring(mobileNo.length() - 4) : "";
            SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
            String key1 = simple1.format(new Date());

            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);

            SimpleDateFormat simpleDate = new SimpleDateFormat("dd");
            String date = simpleDate.format(new Date());
            String id = strLastFourDi + date+""+hour+""+key1;

            if (ownerNo.equalsIgnoreCase("8886666854")){
                orderId = "#G" + id;
            }else if (ownerNo.equalsIgnoreCase("8886666855")){
                orderId = "#F" + id;
            }else if (ownerNo.equalsIgnoreCase("8886666856")){
                orderId = "#T" + id;
            }else if (ownerNo.equalsIgnoreCase("7995591106")){
                orderId = "#G" + id;
            }else {
                orderId = "#G" + id;
            }
            //  orderId = "#DNG" + strLastFourDi + "" + key1;
            Paper.book().write(Common.ORDER_ID, orderId);
        }
        String trans_number = String.valueOf(System.currentTimeMillis());
        Log.e("trans_number", trans_number);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = dateFormat.format(new Date());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                           /* SimpleDateFormat simpleDate = new SimpleDateFormat
                                                    ("dd-MM-yyyy");*/
        date = simpleDate.format(new Date());
        SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
        month = simpleMonth.format(new Date());
        SimpleDateFormat simpleMYear = new SimpleDateFormat("yyyy");
        String year = simpleMYear.format(new Date());

        SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
        String key1 = simple1.format(new Date());

        Calendar currTime = Calendar.getInstance();
        int hour = currTime.get(Calendar.HOUR_OF_DAY);

        SimpleDateFormat simpleDateA = new SimpleDateFormat("dd");
        String dateA = simpleDateA.format(new Date());
        String kot = dateA+""+hour+""+key1;

        String kot_id = "K"+kot;

        requestCoD = new Request(
                userNo, userFirstName,
                "", "0", edt_comments.getText().toString(),
                "COD", totalAmount.getText().toString(),
                "UnPaid", restId, ownerNo, orderId,
                "", tableNo, time, date, month, year, restName, restAddress,
                restGSTNo,"No","No","User",kot_id,"No","False","","No",cart
        );
        unPaidRequestsRestaurant.child(trans_number).setValue(requestCoD);
        unPaidRequestsKitchen.child(trans_number).setValue(requestCoD);
        requests.child(trans_number).setValue(requestCoD);
        requestsRestuarant.child(trans_number).setValue(requestCoD);

        // add user to restaurant list...
        DatabaseReference chatRefA = FirebaseDatabase.getInstance().getReference(restId + "_Users")
                .child(userNo);
        chatRefA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRefA.child("id").setValue(userNo);
                    chatRefA.child("name").setValue(userFirstName);
                    chatRefA.child("time").setValue(time);
                    chatRefA.child("date").setValue(date);
                    chatRefA.child("month").setValue(month);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Delete Cart
        new Database(getBaseContext()).cleanCart(userNo);
        Paper.book().write(Common.TOTAL, "0");

        sendNotificationOrder(orderId);
        Paper.book().write(Common.COMPLETE_ORDER, "No");
        Toast.makeText(CartItemsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
        dialog_customize_view.dismiss();

                    new Thread(){
                        public void run(){
                            try {
                                sleep(2000);
                                startActivity(new Intent(CartItemsActivity.this, MainActivity.class));
                                finish();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
    }

    private void sendNotificationOrder(final String order_number) {

        DatabaseReference tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo(ownerNo);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","Dining");
                    dataSend.put("message","You have a new order Id : " + order_number +"<?>"+ order_number+"<?>"+"Order"+"<?>"+tableNo);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.d("Content",text);

                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    // only run when get result
                                    // issue notification video 22
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                        //    Toast.makeText(CartItemsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(CartItemsActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void sendNotificationFoodOrder(final String order_number) {
        DatabaseReference tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo("8886666857");//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","QRC");
                    dataSend.put("message","You have a new order Id : " + order_number +"<?>"+ order_number+"<?>"+"Order"+"<?>"+tableNo);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.d("Content",text);

                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    // only run when get result
                                    // issue notification video 22
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                         //   Toast.makeText(CartItemsActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(CartItemsActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void LoadListFood()     {
        cart = new Database(this).getCart(userNo);
        adapter = new CartAdapter(cart, CartItemsActivity.this, userNo);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        // calculate total price

        Double total = 0.0;
        for (Order order : cart) {
            if (order.getType() != null && !order.getType().isEmpty()){
                if (order.getType().equalsIgnoreCase("Food")){
                    orderAsFood = "Food";
                }
            }
            Double original_price = Double.parseDouble(order.getPrice());
            Double discount = Double.parseDouble(order.getDiscount()+".0");
            Double discount_price = (original_price / 100.0f) * discount;
            Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

            total += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "IN");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            totalAmount.setText(getResources().getString(R.string.Rs) + " " +total);
           // txtTotalPrice.setText(fmt.format(total));
            txtTotalPrice.setText(getResources().getString(R.string.Rs) + " " +total);

        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return true;
    }
    private void deleteCart(int position) {
        cart.remove(position);

        new Database(this).cleanCart(userNo);

        for (Order item : cart) {
            new Database(this).addToCart(item);
            // refresh
            LoadListFood();
        }

    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder
                    .getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder
                    .getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);

            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), userNo);

            Double total = 0.0;
            Double original_price = 0.0 ;
            Double discount = 0.0;
            Double discount_price = 0.0;
            Double servicePrice = 0.0;
            String str_price = "";
            List<Order> orders = new Database(getBaseContext()).getCart(userNo);
            for (Order item : orders) {

                original_price = Double.parseDouble(item.getPrice());
                discount = Double.parseDouble(item.getDiscount()+".0");
                discount_price = (original_price / 100.0f) * discount;
                servicePrice = Double.parseDouble(item.getPrice()) - discount_price;
                str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                total += (Double.parseDouble(str_price)) * (Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en", "KD");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                Paper.book().write(Common.TOTAL, ""+total);
                totalAmount.setText(getResources().getString(R.string.Rs) + " " +total);
               // txtTotalPrice.setText(fmt.format(total));
                txtTotalPrice.setText(getResources().getString(R.string.Rs) + " " +total);
            }
            // make snake bar
            Snackbar snackbar = Snackbar.make(rootLayout,name+"removed from cart!",Snackbar
                    .LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    Double total = 0.0;
                    List<Order> orders = new Database(getBaseContext()).getCart(userNo);
                    for (Order item : orders) {

                        Double original_price = Double.parseDouble(item.getPrice());
                        Double discount = Double.parseDouble(item.getDiscount()+".0");
                        Double discount_price = (original_price / 100.0f) * discount;
                        Double servicePrice = Double.parseDouble(item.getPrice()) - discount_price;
                        String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                        total += (Double.parseDouble(str_price)) * (Integer.parseInt(item.getQuantity()));
                        Locale locale = new Locale("en", "IN");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        Paper.book().write(Common.TOTAL, ""+total);
                        totalAmount.setText(getResources().getString(R.string.Rs) + " " +total);
                      //  txtTotalPrice.setText(fmt.format(total));
                        txtTotalPrice.setText(getResources().getString(R.string.Rs) + " " +total);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
