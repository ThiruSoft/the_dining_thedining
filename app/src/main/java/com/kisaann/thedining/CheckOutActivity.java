package com.kisaann.thedining;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kisaann.thedining.Adapters.CheckOutAdapter;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.OderDatabase;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.OffersCouponsModel;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Models.PaymentConfirmModel;
import com.kisaann.thedining.Models.Request;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Utils.Utility;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
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

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    ProgressDialog mDialog;
    String TAG = "Payment Error";
    List<Order> cartConfirmOrder = new ArrayList<>();
    List<Order> cartPrintOrder = new ArrayList<>();
    List<Order> cartConfirmOnlineOrder = new ArrayList<>();
    List<Order> cartNcOrderItems = new ArrayList<>();
    List<Order> cartNcZomatoOrderItems = new ArrayList<>();

    List<Order> cartFoodNcOrderItems = new ArrayList<>();
    List<Order> cartFoodNcZomatoOrderItems = new ArrayList<>();

    List<Order> confirmOrder ;
    public TextView txt_totalPrice;
    TextView txt_packingPrice;
    TextView txt_taxPrice;
    TextView txt_discount;
    TextView txt_payAmount;
    TextView txt_applyCoupon;
    TextView txt_walletBal;
    TextView txt_walletDiscount;
    TextView txt_gstAmount;
    TextView txt_serviceTaxAmount;
    TextView txt_verifyOfferCoupon;
    ImageView img_couponCancel;
    MaterialEditText edtComment;
    MaterialEditText edt_couponCode;
    MaterialEditText edtTip;
    MaterialEditText edt_verifyOffersNo;
    RadioGroup mRdgPaymentType;
    RadioButton mRbtOnline;
    RadioButton mRbtCash;
    Button btn_confirmItems;
    APIService mService;
    PaymentConfirmModel paymentConfirmModel;
    LinearLayout lly_couponDiscount;
    LinearLayout lly_walletDiscount;
    LinearLayout mlly_wallet;
    LinearLayout mlly_kingsManVerify;
    CheckBox cbk_walletBal;
    int payAmount;
    String totalPayableAmount;
    FirebaseDatabase database;
    DatabaseReference confirmPayment;
    DatabaseReference restaurantConfirmPayment;
    CheckOutAdapter adapter;
    Double totalPayAmount;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Double total = 0.0;
    Double wineTotal = 0.0;
    Double foodTotal = 0.0;
    int totalQnty = 0;
    int totalAmount;
    String ownerNo;
    String kitchenNo;
    String userNo;
    String userFirstName;
    String couponCode;
    String restId;
    String orderId;
    String tableNo;
    String time;
    String date;
    String day;
    int hour;
    String walletDiscount = "0.0";
    String foodTaxAmount = "0.0";
    String serviceTaxAmount = "0.0";
    String promoDiscount = "";
    DatabaseReference offersCouponsForms;
    DatabaseReference membershipUsers;
    DatabaseReference restOffersCoupons;
    DatabaseReference reference;
    boolean couponAvailable = false;
    Double discount_price = 0.0;
    Double discount_wallet = 0.0;
    Double foodTax = 0.0;
    Double itemsGst = 0.0;
    Double serviceTax = 0.0;
    Double maxValue = 0.0;
    Double secondMaxValue = 0.0;
    Double thirdMaxValue = 0.0;
    int maxValueQty = 0;
    int secondMaxValueQty = 0;
    int thirdMaxValueQty = 0;

    Double foodMaxValue = 0.0;
    Double foodSecondMaxValue = 0.0;
    Double foodThirdMaxValue = 0.0;
    int foodMaxValueQty = 0;
    int foodSecondMaxValueQty = 0;
    int foodThirdMaxValueQty = 0;

    DatabaseReference orderStatus;
    DatabaseReference restOrderStatus;
    DatabaseReference unPaidRequestsRestaurant;
    DatabaseReference unPaidRequestsKitchen;
    Double walletBal = 0.0;
    Double discount;
    String restName;
    String restGSTNo;
    String restAddress;
    private RadioButton radioButton;
    DataMessage dataMessage;
    Dialog dialog_customize_view;
    String trans_number;
    String tipAmount = "";
    CheckBox cbk_ten, cbk_twenty, cbk_thirty, cbk_fifty, cbk_hundred;
    DatabaseReference orderCheckout;
    boolean cashPaymentSelected = false;
    Query searchByUser;
    Query searchByUnpaidOrders;
    Double kings_discount_price = 0.0;
    Double kingsDiscount;
    RadioGroup mRdgOffers;
    RadioButton rbtZomato;
    RadioButton rbtDineout;
    RadioButton rbtKingsMan;
    String offersType ="";
    String kotNo = "";
    String cash_Paid = "";
    String cash_otp = "";
    MaterialEditText edtOffersCommentCode;
    TextView txt_verify;
    TextView txt_offerType;
    DatabaseReference restaurantNCConfirmPayment;
    Query listFoodByProductId;
    DatabaseReference inventoryList;
    DatabaseReference foodListAll;
    Query listIngredientsById;
    String discountType = "";
    String restOfferType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setHomeButtonEnabled(true);

        Log.e("onCreate","onCreate");
        /**
         * Preload payment resources
         */
        initUI();
        Checkout.preload(getApplicationContext());
        // init paper
        Paper.init(this);
        // init service
        mService = Common.getFCMService();

     //   Paper.book().write(Common.CASH_PAYMENT,"False");

        restId = Paper.book().read(Common.RESTAURANT_ID);
        restOfferType = Paper.book().read(Common.REST_OFFER_TYPE);
        ownerNo = Paper.book().read(Common.OWNER_NO);
        kitchenNo = Paper.book().read(Common.KITCHEN_NO);
      //  userNo = Common.currentUser.getPhoneNo();
        userNo = Paper.book().read(Common.USER_KEY);
        userFirstName = Paper.book().read(Common.USER_NAME);
      //  orderId = Paper.book().read(Common.ORDER_ID);
        tableNo = Paper.book().read(Common.TABLE_NO);
        restName = Paper.book().read(Common.REST_NAME);
        restAddress = Paper.book().read(Common.REST_ADDRESS);
        restGSTNo = Paper.book().read(Common.REST_GST_NO);

        Log.e("kitchenNo",""+kitchenNo);
        Log.e("ownerNo",""+ownerNo);
        Log.e("restOfferType",""+restOfferType);

        walletBal = Double.parseDouble(Paper.book().read(Common.USER_BAL));
        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txt_walletBal.setText(fmt.format(walletBal));

        // init firebase
        database = FirebaseDatabase.getInstance();
        confirmPayment = database.getReference("ConfirmPayment");
        restaurantConfirmPayment = database.getReference(restId+"_"+ownerNo+"_ConfirmPayment");
        restaurantNCConfirmPayment = database.getReference(restId+"_"+ownerNo+"_NCConfirmPayment");
        offersCouponsForms = database.getReference("MyOffers&Coupons");
        membershipUsers = database.getReference(restId+"_Members");
        restOffersCoupons = database.getReference(restId+"_Offers&Coupons");
        orderStatus = database.getReference(userNo+"_Requests");
        restOrderStatus = database.getReference(restId+"_"+ownerNo+"_Requests");
        unPaidRequestsRestaurant = database.getReference(restId+"_"+ownerNo+"_UnPaidRequests");
       // unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666857"+"_UnPaidRequests");
        if (ownerNo.equalsIgnoreCase("8886666856")){
            unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666860"+"_UnPaidRequests");
        }else if (ownerNo.equalsIgnoreCase("8886666854") || ownerNo.equalsIgnoreCase("8886666855")) {
            unPaidRequestsKitchen = database.getReference(restId+"_"+"8886666857"+"_UnPaidRequests");
        }else if (ownerNo.equalsIgnoreCase("7995591106")){
            unPaidRequestsKitchen = database.getReference(restId+"_"+"7995591107"+"_UnPaidRequests");
        }else {
                unPaidRequestsKitchen = database.getReference(restId+"_"+kitchenNo+"_UnPaidRequests");
        }
        orderCheckout = database.getReference(userNo+"_Requests");

        foodListAll = database.getReference(restId+"_"+"All");
        inventoryList = database.getReference(restId+"_"+"Inventory");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        LoadListFood();

        cbk_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAmount = "10";
                edtTip.setText(tipAmount);
                if (cbk_twenty.isChecked()) {
                    cbk_twenty.setChecked(false);
                }
                if (cbk_thirty.isChecked()) {
                    cbk_thirty.setChecked(false);
                }
                if (cbk_fifty.isChecked()) {
                    cbk_fifty.setChecked(false);
                }
                if (cbk_hundred.isChecked()) {
                    cbk_hundred.setChecked(false);
                }
            }
        });
        cbk_twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAmount = "20";
                edtTip.setText(tipAmount);
                if (cbk_ten.isChecked()) {
                    cbk_ten.setChecked(false);
                }
                if (cbk_thirty.isChecked()) {
                    cbk_thirty.setChecked(false);
                }
                if (cbk_fifty.isChecked()) {
                    cbk_fifty.setChecked(false);
                }
                if (cbk_hundred.isChecked()) {
                    cbk_hundred.setChecked(false);
                }
            }
        });
        cbk_thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAmount = "30";
                edtTip.setText(tipAmount);
                if (cbk_ten.isChecked()) {
                    cbk_ten.setChecked(false);
                }
                if (cbk_twenty.isChecked()) {
                    cbk_twenty.setChecked(false);
                }
                if (cbk_fifty.isChecked()) {
                    cbk_fifty.setChecked(false);
                }
                if (cbk_hundred.isChecked()) {
                    cbk_hundred.setChecked(false);
                }
            }
        });
        cbk_fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAmount = "50";
                edtTip.setText(tipAmount);
                if (cbk_ten.isChecked()) {
                    cbk_ten.setChecked(false);
                }
                if (cbk_twenty.isChecked()) {
                    cbk_twenty.setChecked(false);
                }
                if (cbk_thirty.isChecked()) {
                    cbk_thirty.setChecked(false);
                }
                if (cbk_hundred.isChecked()) {
                    cbk_hundred.setChecked(false);
                }
            }
        });
        cbk_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipAmount = "100";
                edtTip.setText(tipAmount);
                if (cbk_ten.isChecked()) {
                    cbk_ten.setChecked(false);
                }
                if (cbk_twenty.isChecked()) {
                    cbk_twenty.setChecked(false);
                }
                if (cbk_thirty.isChecked()) {
                    cbk_thirty.setChecked(false);
                }
                if (cbk_fifty.isChecked()) {
                    cbk_fifty.setChecked(false);
                }
            }
        });
        mRdgOffers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton mRbtYes = (RadioButton)group.findViewById(checkedId);
                String offers_Type = ""+mRbtYes.getText().toString();
                Log.e("offersType",offers_Type);
                if (offers_Type.equalsIgnoreCase("Kings Man")){
                    offersType = "Kings Man";
                    mlly_kingsManVerify.setVisibility(View.VISIBLE);
                }else if (offers_Type.equalsIgnoreCase("Dineout")){
                    offersType = "Dineout"; //secondMaxValue
                    lly_walletDiscount.setVisibility(View.VISIBLE);
                    txt_verify.setVisibility(View.GONE);
                    txt_offerType.setText("Dineout");
                    //  edtOffersCommentCode.setText("Dineout");
                    edtOffersCommentCode.setEnabled(true);
                    edtOffersCommentCode.setCursorVisible(true);

                     // kings_discount_price = secondMaxValue;
                    if (restOfferType.equalsIgnoreCase("Wine")){
                        kings_discount_price = secondMaxValue;
                    }
                    if (restOfferType.equalsIgnoreCase("Food")){
                        kings_discount_price = foodSecondMaxValue;
                    }
                    Locale locale = new Locale("en", "IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    totalPayAmount = total - kings_discount_price - discount_wallet + foodTax + serviceTax;;
                    txt_walletDiscount.setText(fmt.format(kings_discount_price));

                    txt_payAmount.setText(fmt.format(totalPayAmount));
                    totalAmount = (int) Math.round(totalPayAmount);

                   // btn_confirmItems.setText("Pay "+fmt.format(totalAmount));
                    btn_confirmItems.setText(fmt.format(total));
                }else if (offers_Type.equalsIgnoreCase("Zomato")){
                    offersType = "Zomato";
                    lly_walletDiscount.setVisibility(View.VISIBLE);
                    txt_verify.setVisibility(View.GONE);
                    txt_offerType.setText("Zomato");
                    // edtOffersCommentCode.setText("Zomato");
                    edtOffersCommentCode.setEnabled(true);
                    edtOffersCommentCode.setCursorVisible(true);

                    // kings_discount_price = secondMaxValue * 2;
                    if (restOfferType.equalsIgnoreCase("Wine")){
                        kings_discount_price = secondMaxValue * 2;
                    }
                    if (restOfferType.equalsIgnoreCase("Food")){
                        kings_discount_price = foodSecondMaxValue * 1;
                    }

                    Locale locale = new Locale("en", "IN");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    totalPayAmount = total - kings_discount_price - discount_wallet + foodTax + serviceTax;;
                    txt_walletDiscount.setText(fmt.format(kings_discount_price));

                   // txt_payAmount.setText(fmt.format(totalPayAmount));
                    txt_payAmount.setText("KD " + totalPayAmount);
                    totalAmount = (int) Math.round(totalPayAmount);

                  //  btn_confirmItems.setText("Pay "+fmt.format(totalAmount));
                  //  btn_confirmItems.setText(fmt.format(total));
                    btn_confirmItems.setText("KD "+ total);
                }
              /*  if (offersType != null && offersType.equalsIgnoreCase("Yes")){
                    mRbtYes.setChecked(true);
                }else if (offersType != null && offersType.equalsIgnoreCase("No")){
                    mRbtNo.setChecked(true);
                }*/
            }
        });
       /* edtTip.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                edtTip.setText("");
                Log.e("s",""+s);
            }
        });*/
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
     //   LoadListFood();
    }*/

    private void initUI() {
        txt_offerType = findViewById(R.id.txt_offerType);
        txt_verify = findViewById(R.id.txt_verify);
        edtOffersCommentCode = findViewById(R.id.edtOffersCommentCode);
        recyclerView = findViewById(R.id.listCart);
        txt_totalPrice = findViewById(R.id.txt_totalPrice);
        txt_packingPrice = findViewById(R.id.txt_packingPrice);
        txt_taxPrice = findViewById(R.id.txt_taxPrice);
        txt_discount = findViewById(R.id.txt_discount);
        txt_payAmount = findViewById(R.id.txt_payAmount);
        txt_verifyOfferCoupon = findViewById(R.id.txt_verifyOfferCoupon);
        btn_confirmItems = findViewById(R.id.btn_confirmItems);
        edtComment = findViewById(R.id.edtComment);
        edt_couponCode = findViewById(R.id.edt_couponCode);
        edtTip = findViewById(R.id.edtTip);
        edt_verifyOffersNo = findViewById(R.id.edt_verifyOffersNo);
        txt_applyCoupon = findViewById(R.id.txt_applyCoupon);
        txt_walletBal = findViewById(R.id.txt_walletBal);
        txt_walletDiscount = findViewById(R.id.txt_walletDiscount);
        txt_gstAmount = findViewById(R.id.txt_gstAmount);
        txt_serviceTaxAmount = findViewById(R.id.txt_serviceTaxAmount);
        img_couponCancel = findViewById(R.id.img_couponCancel);
        lly_couponDiscount = findViewById(R.id.lly_couponDiscount);
        lly_walletDiscount = findViewById(R.id.lly_walletDiscount);
        mlly_kingsManVerify = findViewById(R.id.mlly_kingsManVerify);
        mlly_wallet = findViewById(R.id.mlly_wallet);
        cbk_walletBal = findViewById(R.id.cbk_walletBal);
        mRdgPaymentType = findViewById(R.id.mRdgPaymentType);
        mRdgOffers = findViewById(R.id.mRdgOffers);
        mRbtCash = findViewById(R.id.mRbtCash);
        mRbtOnline = findViewById(R.id.mRbtOnline);

        cbk_ten = findViewById(R.id.cbk_ten);
        cbk_twenty = findViewById(R.id.cbk_twenty);
        cbk_thirty = findViewById(R.id.cbk_thirty);
        cbk_fifty = findViewById(R.id.cbk_fifty);
        cbk_hundred = findViewById(R.id.cbk_hundred);


     //   btn_confirmItems.setOnClickListener(this);
        txt_applyCoupon.setOnClickListener(this);
        img_couponCancel.setOnClickListener(this);
        mlly_wallet.setOnClickListener(this);
        mRdgPaymentType.setOnClickListener(this);
        txt_verifyOfferCoupon.setOnClickListener(this);

        btn_confirmItems.setEnabled(true);
    }

    private void LoadListFood() {

        cash_Paid = "";
        cash_otp = "";
        cartConfirmOrder.clear();
        cartPrintOrder.clear();
        new OderDatabase(getBaseContext()).cleanOrderCart(tableNo);
       // new
      //  String cash_Paid = Paper.book().read(Common.CASH_PAYMENT);
        if (cash_Paid != null && !cash_Paid.isEmpty()) {
            Log.e("cashPaid",""+cash_Paid);
            if (cash_Paid.equalsIgnoreCase("True")) {
                 searchByUser = orderCheckout.orderByChild("paymentState").equalTo("Progress");
            }else {
                 searchByUser = orderCheckout.orderByChild("paymentState").equalTo("UnPaid");
            }
        }else {
            searchByUser = orderCheckout.orderByChild("paymentState").equalTo("UnPaid");
        }

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.currentUser.getPhoneNo()+"_Requests");
        searchByUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodTotal = 0.0;
                wineTotal = 0.0;
                itemsGst = 0.0;
                maxValue = 0.0;
                foodTax = 0.0;
                kotNo = "";
                secondMaxValue = 0.0;
                thirdMaxValue = 0.0;
                maxValueQty = 0;
                secondMaxValueQty = 0;
                thirdMaxValueQty = 0;

                foodSecondMaxValue = 0.0;
                foodThirdMaxValue = 0.0;
                foodMaxValueQty = 0;
                foodSecondMaxValueQty = 0;
                foodThirdMaxValueQty = 0;

                cartConfirmOrder.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    assert request != null;
                    if (request.getTableNo().equalsIgnoreCase(tableNo)) {
                    //  cartConfirmOrder.add(request.getFood());
                    if (request.getKot() != null && !request.getKot().isEmpty()) {
                        kotNo = kotNo + request.getKot() + "/";
                    }

                    orderId = request.getOrderId();
                    cash_Paid = request.getCashPaid();
                    cash_otp = request.getCashOtp();

                        Paper.book().write(Common.CASH_PAID, ""+cash_Paid);
                    if (request.getFood() != null) {
                        for (Order order : request.getFood()) {

                            // store data
                            boolean isExist = new OderDatabase(getBaseContext()).checkFoodExists(order.getProductId(), tableNo);
                            if (!isExist) {
                                new OderDatabase(getBaseContext()).addToOrderCart(new Order(
                                        tableNo, order.getProductId(), order.getProductName(),
                                        order.getQuantity(), order.getPrice(), order.getDiscount()
                                        , order.getImage(), order.getType(),order.getCategoryName(),
                                        order.getItemType(),order.getDepartment(),order.getParticular(),""+order.getGst(),
                                        order.getHappyHour(),order.getKitchen(),order.getCaptainName()));
                            } else {
                                int quantityA = Integer.parseInt(order.getQuantity());
                                if (quantityA == 1) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 2) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 3) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 4) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 5) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 6) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 7) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 8) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 9) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 10) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 11) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 12) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 13) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 14) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 15) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 16) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 17) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 18) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 19) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 20) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 21) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 22) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 23) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 24) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 25) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 26) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 27) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 28) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 29) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                } else if (quantityA == 30) {
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                    new OderDatabase(getBaseContext()).increaseOrderCart(tableNo, order.getProductId());
                                }
                            }
                            cartConfirmOrder.add(order);

                            // calculate total price
                            Double original_price = Double.parseDouble(order.getPrice());
                            Double discount = Double.parseDouble(order.getDiscount() + ".0");
                            Double discount_price = (original_price / 100.0f) * discount;
                            Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
                            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);
                            // max value
                            // maxValue = original_price;
                            if (order.getType().equalsIgnoreCase("Wine")) {
                                if (original_price > maxValue) {
                                    maxValue = original_price;
                                    maxValueQty = Integer.parseInt(order.getQuantity());
                                    //  secondMaxValue = original_price;
                                    Log.e("max_Value", "" + maxValue + " " + maxValueQty);
                                } else if (original_price > secondMaxValue && original_price < maxValue) {
                                    cartNcOrderItems.clear();
                                    cartNcZomatoOrderItems.clear();
                                    secondMaxValue = original_price;
                                    secondMaxValueQty = Integer.parseInt(order.getQuantity());
                                    Log.e("second_Max_Value", "" + secondMaxValue + " " + secondMaxValueQty);
                                    cartNcOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                            order.getProductName(), "1", order.getPrice(),
                                            order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                            order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                            order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                    if (order.getQuantity() != null && order.getQuantity().equalsIgnoreCase("1")) {
                                        cartNcZomatoOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                                order.getProductName(), "2", order.getPrice(),
                                                order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                                order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                                order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                    }

                                } else if (original_price < secondMaxValue && original_price > thirdMaxValue && original_price < maxValue) {
                                    thirdMaxValue = original_price;
                                    thirdMaxValueQty = Integer.parseInt(order.getQuantity());
                                    Log.e("third_Max_Value", "" + thirdMaxValue + " " + thirdMaxValueQty);
                                    cartNcZomatoOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                            order.getProductName(), "2", order.getPrice(),
                                            order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                            order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                            order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                }
                            }else if (order.getType().equalsIgnoreCase("Food")) {
                                if (original_price > foodMaxValue) {
                                    foodMaxValue = original_price;
                                    foodMaxValueQty = Integer.parseInt(order.getQuantity());
                                    //  secondMaxValue = original_price;
                                    Log.e("Food_max_Value", "" + foodMaxValue + " " + foodMaxValueQty);
                                } else if (original_price > foodSecondMaxValue && original_price < foodMaxValue) {
                                    cartFoodNcOrderItems.clear();
                                    cartFoodNcZomatoOrderItems.clear();
                                    foodSecondMaxValue = original_price;
                                    foodSecondMaxValueQty = Integer.parseInt(order.getQuantity());
                                    Log.e("Food second_Max_Value", "" + foodSecondMaxValue + " " + foodSecondMaxValueQty);
                                    cartFoodNcOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                            order.getProductName(), "1", order.getPrice(),
                                            order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                            order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                            order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                    if (order.getQuantity() != null && order.getQuantity().equalsIgnoreCase("1")) {
                                        cartFoodNcZomatoOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                                order.getProductName(), "2", order.getPrice(),
                                                order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                                order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                                order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                    }

                                } else if (original_price < foodSecondMaxValue && original_price > foodThirdMaxValue && original_price < foodMaxValue) {
                                    foodThirdMaxValue = original_price;
                                    foodThirdMaxValueQty = Integer.parseInt(order.getQuantity());
                                    Log.e("food third_Max_Value", "" + foodThirdMaxValue + " " + foodThirdMaxValueQty);
                                    cartFoodNcZomatoOrderItems.add(new Order(order.getUserPhone(), order.getProductId(),
                                            order.getProductName(), "2", order.getPrice(),
                                            order.getDiscount(), order.getImage(), order.getType(), order.getCategoryName(),
                                            order.getItemType(), order.getDepartment(), order.getParticular(), order.getGst(),
                                            order.getHappyHour(), order.getKitchen(), order.getCaptainName()));
                                }
                            }
                            if (order.getType() != null && !order.getType().isEmpty()) {
                                if (order.getType().equalsIgnoreCase("Food")) {
                                    foodTotal += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
                                } else {
                                    wineTotal += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
                                    if (order.getGst() != null && !order.getGst().isEmpty() && order.getGst().equalsIgnoreCase("Yes")) {
                                        Double itemTotal = (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
                                        itemsGst += (itemTotal / 100.0f * 5);
                                        Log.e("itemsGst", "" + itemsGst);
                                    }
                                }
                            }
                            // totalQnty += Integer.parseInt(order.getQuantity());
                            Log.e("Type", order.getType());
                            totalQnty++;
                            Locale locale = new Locale("en", "IN");
                            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                            total = wineTotal + foodTotal;
                           // txt_totalPrice.setText(fmt.format(total));
                            txt_totalPrice.setText("KD "+total);
                            if (cartConfirmOrder != null && !cartConfirmOrder.isEmpty()) {
                                Locale localeA = new Locale("en", "IN");
                                NumberFormat fmtA = NumberFormat.getCurrencyInstance(localeA);
                                txt_packingPrice.setText(fmtA.format(00.00));
                                txt_taxPrice.setText(fmtA.format(00.00));
                                txt_discount.setText(fmtA.format(00.00));

                                Double packing_price = Double.parseDouble("0");
                                Double tax_price = Double.parseDouble("0");
                                Double discount_priceA = Double.parseDouble("0");

                               /* if (walletBal >1){
                                    discount_wallet = (walletBal / 100.0f) * 5;
                                    walletDiscount = "" + new DecimalFormat("##.##").format(discount_wallet);
                                    txt_walletDiscount.setText(getResources().getString(R.string.Rs)+""+walletDiscount);
                                }*/

                                serviceTax = (total / 100.0f * 5);
                                serviceTaxAmount = "" + new DecimalFormat("##.##").format(serviceTax);
                                txt_serviceTaxAmount.setText(getResources().getString(R.string.Rs) + "" + serviceTaxAmount);

                                Double foodAndService = foodTotal + serviceTax;
                                //   Double foodAndService = foodTotal ;
                                foodTax = (foodAndService / 100.0f * 5);
                                foodTaxAmount = "" + new DecimalFormat("##.##").format(foodTax + itemsGst);
                                txt_gstAmount.setText(getResources().getString(R.string.Rs) + "" + foodTaxAmount);

                                totalPayAmount = total + packing_price + tax_price - discount_price - discount_wallet + foodTax + itemsGst + serviceTax;
                              //  txt_payAmount.setText(fmt.format(totalPayAmount));

                               // txt_payAmount.setText(fmt.format(total));
                                txt_payAmount.setText("KD "+total);

                                totalAmount = (int) Math.round(totalPayAmount);

                              //  btn_confirmItems.setText("Pay " + fmt.format(totalAmount));
                               // btn_confirmItems.setText(fmt.format(total));
                                btn_confirmItems.setText("KD "+total);
                            } else {
                                Locale localeC = new Locale("en", "IN");
                                NumberFormat fmtC = NumberFormat.getCurrencyInstance(localeC);
                                txt_totalPrice.setText(fmt.format(00.00));
                                txt_packingPrice.setText(fmt.format(00.00));
                                txt_taxPrice.setText(fmt.format(00.00));
                                txt_discount.setText(fmt.format(00.00));

                                Double packing_price = Double.parseDouble("00");
                                Double tax_price = Double.parseDouble("00");
                                Double discount_priceC = Double.parseDouble("00");
                                totalPayAmount = total + packing_price + tax_price - discount_priceC;
                               // txt_payAmount.setText(fmt.format(total));
                                txt_payAmount.setText("KD "+total);

                                totalAmount = (int) Math.round(totalPayAmount);

                              //  btn_confirmItems.setText("Pay " + fmtC.format(totalAmount));
                                btn_confirmItems.setText(fmtC.format(total));
                            }
                        }
                    }
                }
            }
                Log.e("maxValue",""+maxValue);
                Log.e("secondMaxValue",""+secondMaxValue);
                Log.e("thirdMaxValue",""+thirdMaxValue);
                Log.e("FoodmaxValue",""+foodMaxValue);
                Log.e("FoodsecondMaxValue",""+foodSecondMaxValue);
                Log.e("FoodthirdMaxValue",""+foodThirdMaxValue);
                searchByUser.removeEventListener(this);

                cartPrintOrder = new OderDatabase(CheckOutActivity.this).getOrderCart(tableNo);
               // adapter = new CheckOutAdapter(cartConfirmOrder, CheckOutActivity.this);
                adapter = new CheckOutAdapter(cartPrintOrder, CheckOutActivity.this);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.mRdgPaymentType:
                int selectedId = mRdgPaymentType.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                String paymentMethod = ""+radioButton.getText();
                Log.e("paymentMethod",paymentMethod);
                break;*/
            case R.id.txt_verifyOfferCoupon:
                if (offersType.equalsIgnoreCase("Kings Man")){
                kings_discount_price = 0.0;
                String verifyNo = edt_verifyOffersNo.getText().toString().trim();
                if (!verifyNo.isEmpty()) {
                    if (Utility.isNetworkAvailable(getBaseContext())) {
                        membershipUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(verifyNo).exists()) {
                                    lly_walletDiscount.setVisibility(View.VISIBLE);
                                    kingsDiscount = 20.0;
                                    kings_discount_price = (total / 100.0f) * kingsDiscount;
                   /* promoDiscount = "" + new DecimalFormat("##.##").format(kings_discount_price);
                    txt_discount.setText(getResources().getString(R.string.Rs)+""+kings_discount_price);*/

                                    Locale locale = new Locale("en", "IN");
                                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                                    totalPayAmount = total - kings_discount_price - discount_wallet + foodTax + itemsGst + serviceTax;
                                    ;
                                    txt_walletDiscount.setText(fmt.format(kings_discount_price));

                                  //  txt_payAmount.setText(fmt.format(totalPayAmount));
                                    txt_payAmount.setText(fmt.format(total));
                                    totalAmount = (int) Math.round(totalPayAmount);

                                 //  btn_confirmItems.setText("Pay " + fmt.format(totalAmount));
                                    btn_confirmItems.setText(fmt.format(total));

                                    edt_verifyOffersNo.setEnabled(false);
                                    edt_verifyOffersNo.setCursorVisible(false);
                                    txt_verifyOfferCoupon.setText("Verified");
                                } else {
                                    Toast.makeText(CheckOutActivity.this, "Mobile Number Does't Exist.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {
                        Toast.makeText(CheckOutActivity.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(CheckOutActivity.this, "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                }
                }else {
                  //  Toast.makeText(CheckOutActivity.this, "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_confirmItems:
               /* tipAmount = edtTip.getText().toString().trim();
                Log.e("tipAmount",tipAmount);*/
                if (cartConfirmOrder.size() > 0) {

                    // int totalPay = (int) Math.round(totalPayAmount);
                    tipAmount = edtTip.getText().toString().trim();
                    if (tipAmount != null && !tipAmount.isEmpty()){
                        Double tipA = Double.parseDouble(tipAmount);
                        totalPayAmount = totalPayAmount + tipA;
                    }

                    Log.e("cartConfirmOrder",""+cartConfirmOrder);
                    payAmount = (int) Math.round(totalPayAmount) ;
                    int totalPay = payAmount * 100;
                   // totalAmount = (int) Math.round(totalPayAmount);
                    totalPayableAmount = "" + new DecimalFormat("##.##").format(totalPayAmount);

                    Paper.book().write(Common.TOTAL_PAy_AMOUNT, ""+payAmount);
                    Paper.book().write(Common.PROMO_DISCOUNT, ""+promoDiscount);
                    Paper.book().write(Common.WALLET_DISCOUNT, ""+walletDiscount);
                    Paper.book().write(Common.TOTAL_QTY, ""+totalQnty);
                    Paper.book().write(Common.TIP_AMOUNT, ""+tipAmount);
                    Paper.book().write(Common.FOOD_TAX_AMOUNT, ""+foodTaxAmount);
                    Paper.book().write(Common.SERVICE_TAX_AMOUNT, ""+serviceTaxAmount);
                    Paper.book().write(Common.OFFER_TYPE, ""+offersType);
                    Paper.book().write(Common.OFFER_DISCOUNT, ""+kings_discount_price);
                   // Paper.book().write(Common.ORDER_DATA, ""+cartConfirmOrder);

                    if (Utility.isNetworkAvailable(getBaseContext())) {
                       // startPayment(payAmount);
                        int selectedI = mRdgPaymentType.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        radioButton = (RadioButton) findViewById(selectedI);
                        String paymentType = ""+radioButton.getText();
                       // Log.e("paymentMethod",paymentType);
                        btn_confirmItems.setEnabled(false);
                        if (paymentType.equalsIgnoreCase("Online")){
                            startPayment(totalPay);
                        }else {
                          // String cashPaid = Paper.book().read(Common.CASH_PAYMENT);
                            if (cash_Paid != null && !cash_Paid.isEmpty() && cash_Paid.equalsIgnoreCase("True")){
                                /*if (cashPaid.equalsIgnoreCase("True")){*/
                                    String otp = Paper.book().read(Common.OTP);
                                    String tranSNo = Paper.book().read(Common.TRANS_NO);
                                    verifyOTPDialog(cash_otp,restId,orderId,tranSNo);
                               /* }*/
                                Log.e("cashPaid",cash_Paid);
                            }else {
                                //   Log.e("cashPaid",cashPaid);
                                SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
                                String otp = simple1.format(new Date());
                                Log.e("paymentOtp", otp);

                                Paper.book().write(Common.OTP, otp);
                                Paper.book().write(Common.CASH_PAYMENT, "True");

                                trans_number = String.valueOf(System.currentTimeMillis());
                                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                                time = dateFormat.format(new Date());
                                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = simpleDate.format(new Date());

                                SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
                                String month = simpleMonth.format(new Date());
                                SimpleDateFormat simpleMYear = new SimpleDateFormat("yyyy");
                                String year = simpleMYear.format(new Date());

                                Format dayFormat = new SimpleDateFormat("EEE");
                                day = dayFormat.format(new Date());

                                Calendar currTime = Calendar.getInstance();
                                hour = currTime.get(Calendar.HOUR_OF_DAY);

                                //Log.e("day",day);

                                paymentConfirmModel = new PaymentConfirmModel(
                                        userNo, userFirstName, "", userNo,
                                        "2", edtComment.getText().toString(),
                                        "Cash", "" + payAmount, promoDiscount, walletDiscount,
                                        "Progress", restId, ownerNo, orderId,
                                        "", tableNo, time, date, "", month, year,
                                        "" + totalQnty, restName, restAddress, restGSTNo, otp, tipAmount,
                                        foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price, "", day, "" + hour, "User",
                                        kotNo, "", "", "", "", "",
                                        "", "", "", "", "", "Yes", discountType, "", "", cartConfirmOrder
                                );
                                Paper.book().write(Common.TRANS_NO, trans_number);
                                restaurantConfirmPayment.child(trans_number).setValue(paymentConfirmModel);
                                confirmPayment.child(trans_number).setValue(paymentConfirmModel);
                                if (restOfferType.equalsIgnoreCase("Food")) {
                                    if (offersType.equalsIgnoreCase("Dineout")) {

                                        PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                                userNo, userFirstName, "", userNo,
                                                "2", edtOffersCommentCode.getText().toString(),
                                                paymentType, "" + payAmount, promoDiscount, walletDiscount,
                                                "Paid", restId, ownerNo, orderId, "",
                                                tableNo, time, date, "", month, year,
                                                "" + totalQnty, restName, restAddress, restGSTNo, otp, tipAmount,
                                                foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                                "" + kings_discount_price, day, "" + hour, "User", kotNo, "" + "", "",
                                                "", "", "", "",
                                                "", "", "", "", "Yes", discountType, "", "", cartFoodNcOrderItems);

                                        restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                                    } else if (offersType.equalsIgnoreCase("Zomato")) {
                                        PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                                userNo, userFirstName, "", userNo,
                                                "2", edtOffersCommentCode.getText().toString(),
                                                paymentType, "" + payAmount, promoDiscount, walletDiscount,
                                                "Paid", restId, ownerNo, orderId,
                                                "", tableNo, time, date, "", month, year,
                                                "" + totalQnty, restName, restAddress, restGSTNo, otp, tipAmount,
                                                foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                                "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "",
                                                "", "", "", "", "", "", "",
                                                "", "Yes", discountType, "", "", cartFoodNcZomatoOrderItems);

                                        restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                                    }
                                }else {
                                if (offersType.equalsIgnoreCase("Dineout")) {

                                    PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                            userNo, userFirstName, "", userNo,
                                            "2", edtOffersCommentCode.getText().toString(),
                                            paymentType, "" + payAmount, promoDiscount, walletDiscount,
                                            "Paid", restId, ownerNo, orderId, "",
                                            tableNo, time, date, "", month, year,
                                            "" + totalQnty, restName, restAddress, restGSTNo, otp, tipAmount,
                                            foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                            "" + kings_discount_price, day, "" + hour, "User", kotNo, "" + "", "",
                                            "", "", "", "", "", "", "", "", "Yes", discountType, "", "", cartNcOrderItems);

                                    restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                                } else if (offersType.equalsIgnoreCase("Zomato")) {
                                    PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                            userNo, userFirstName, "", userNo,
                                            "2", edtOffersCommentCode.getText().toString(),
                                            paymentType, "" + payAmount, promoDiscount, walletDiscount,
                                            "Paid", restId, ownerNo, orderId,
                                            "", tableNo, time, date, "", month, year,
                                            "" + totalQnty, restName, restAddress, restGSTNo, otp, tipAmount,
                                            foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                            "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "",
                                            "", "", "", "", "", "", "",
                                            "", "Yes", discountType, "", "", cartNcZomatoOrderItems);

                                    restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                                }
                            }
                                    unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                                    unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                                          @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                          for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                Request requestA = snapshot.getValue(Request.class);
                                                String keyA = snapshot.getRef().getKey();
                                                if (orderId.equalsIgnoreCase(requestA.getOrderId())){
                                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_UnPaidRequests").child(keyA);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("cashPaid", "True");
                                                    hashMap.put("cashOtp", otp);
                                                    reference.updateChildren(hashMap);
                                                }
                                            }
                                            unPaidRequestsRestaurant.removeEventListener(this);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    unPaidRequestsKitchen.orderByChild("orderId").equalTo(orderId);
                                    unPaidRequestsKitchen.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                Request requestA = snapshot.getValue(Request.class);
                                                String keyA = snapshot.getRef().getKey();
                                                if (orderId.equalsIgnoreCase(requestA.getOrderId())){
                                                    /*reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"8886666857"+"_UnPaidRequests").child(keyA);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("paymentMethod", "Cash");
                                                    hashMap.put("paymentState", "Progress");
                                                    reference.updateChildren(hashMap);*/
                                                    if (ownerNo.equalsIgnoreCase("8886666856")) {
                                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666860" + "_UnPaidRequests").child(keyA);
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("cashPaid", "True");
                                                        hashMap.put("cashOtp", otp);
                                                        reference.updateChildren(hashMap);
                                                    }else if (ownerNo.equalsIgnoreCase("8886666854") || (ownerNo.equalsIgnoreCase("8886666855"))){
                                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"8886666857"+"_UnPaidRequests").child(keyA);
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("cashPaid", "True");
                                                        hashMap.put("cashOtp", otp);
                                                        reference.updateChildren(hashMap);
                                                    }else if (ownerNo.equalsIgnoreCase("7995591106")){
                                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"7995591107"+"_UnPaidRequests").child(keyA);
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("cashPaid", "True");
                                                        hashMap.put("cashOtp", otp);
                                                        Log.e("orderId1",orderId);
                                                        reference.updateChildren(hashMap);
                                                    }else {
                                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+kitchenNo+"_UnPaidRequests").child(keyA);
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("cashPaid", "True");
                                                        hashMap.put("cashOtp", otp);
                                                        reference.updateChildren(hashMap);
                                                    }
                                                }
                                            }
                                            unPaidRequestsKitchen.removeEventListener(this);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    // update payment status in user request table
                                    orderStatus.orderByChild("orderId").equalTo(orderId);
                                    orderStatus.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                Request requestB = snapshot.getValue(Request.class);
                                                String keyB = snapshot.getRef().getKey();
                                                if (orderId.equalsIgnoreCase(requestB.getOrderId())){
                                                    reference = FirebaseDatabase.getInstance().getReference(userNo+"_Requests").child(keyB);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("cashPaid", "True");
                                                    hashMap.put("cashOtp", otp);
                                                    reference.updateChildren(hashMap);
                                                    Log.e("request",requestB.getOrderId());
                                                }
                                            }
                                            orderStatus.removeEventListener(this);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    // update payment status in restaurant request table
                                    restOrderStatus.orderByChild("orderId").equalTo(orderId);
                                    restOrderStatus.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                Request requestC = snapshot.getValue(Request.class);
                                                String keyC = snapshot.getRef().getKey();
                                                if (orderId.equalsIgnoreCase(requestC.getOrderId())){
                                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_Requests").child(keyC);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("cashPaid", "True");
                                                    hashMap.put("cashOtp", otp);
                                                    reference.updateChildren(hashMap);
                                                }
                                            }
                                            restOrderStatus.removeEventListener(this);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            /*unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                            unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        String key = snapshot.getRef().getKey();
                                        reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"Requests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentState", "Paid");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/
                                  //  updateUserWalletBalance();
                                    // Delete Cart
                                    new OderDatabase(getBaseContext()).cleanOrderCart(userNo);
                                    sendNotificationOrder(trans_number,"Cash");

                                    verifyOTPDialog(otp,restId,orderId, trans_number);

                                }
                        }
                    }else {
                        Toast.makeText(CheckOutActivity.this, "Please check your network connection. ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(CheckOutActivity.this, "Your cart is empty!!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.txt_applyCoupon:
                couponAvailable = false;
                String coupon = edt_couponCode.getText().toString().trim();
                if (!TextUtils.isEmpty(coupon)){
                    if (Utility.isNetworkAvailable(getBaseContext())) {
                        applyCoupon(coupon);
                    } else {
                            Toast.makeText(CheckOutActivity.this, "Please check your network connection. ", Toast.LENGTH_LONG).show();
                        }
                }else {
                    Toast.makeText(CheckOutActivity.this, "Enter coupon code", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_couponCancel:
                couponAvailable = false;
                edt_couponCode.setEnabled(true);
                edt_couponCode.setFocusable(true);
                edt_couponCode.setCursorVisible(true);


                txt_discount.setText(getResources().getString(R.string.Rs)+""+discount_price);

                Locale locale = new Locale("en", "IN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                totalPayAmount = total - discount_wallet + foodTax + itemsGst + serviceTax;
              //  txt_payAmount.setText(fmt.format(totalPayAmount));
                txt_payAmount.setText(fmt.format(total));

                totalAmount = (int) Math.round(totalPayAmount);

              //  btn_confirmItems.setText("Pay "+fmt.format(totalAmount));
                btn_confirmItems.setText(fmt.format(total));
                txt_applyCoupon.setVisibility(View.VISIBLE);
                img_couponCancel.setVisibility(View.INVISIBLE);
                lly_couponDiscount.setVisibility(View.GONE);

                discount_price = 0.0;
                promoDiscount = "0.0";
                break;
            case R.id.cbk_walletBal:
                // discount_wallet = 0.0;
                Locale localeA = new Locale("en", "IN");
                NumberFormat fmtA = NumberFormat.getCurrencyInstance(localeA);
                if (cbk_walletBal.isChecked()){
                    lly_walletDiscount.setVisibility(View.VISIBLE);
                    discount_wallet = (total / 100.0f) * 5;
                    txt_walletDiscount.setText(getResources().getString(R.string.Rs)+""+discount_wallet);

                    totalPayAmount = total - discount_wallet ;
                 //   txt_payAmount.setText(fmtA.format(totalPayAmount));
                    txt_payAmount.setText(fmtA.format(total));

                    totalAmount = (int) Math.round(totalPayAmount);

                   // btn_confirmItems.setText("Pay "+fmtA.format(totalAmount));
                    btn_confirmItems.setText(fmtA.format(total));

                }else {
                    discount_wallet = 0.0;
                    lly_walletDiscount.setVisibility(View.GONE);
                    totalPayAmount = total ;
                    txt_payAmount.setText(fmtA.format(totalPayAmount));

                    totalAmount = (int) Math.round(totalPayAmount);

                   // btn_confirmItems.setText("Pay "+fmtA.format(totalAmount));
                    btn_confirmItems.setText(fmtA.format(total));
                }


                break;
        }
    }

    private void verifyOTPDialog(String otp,String restID, String orderID, String transNumber) {
        dialog_customize_view = new Dialog(this);
        if (dialog_customize_view.getWindow() != null) {
            dialog_customize_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog_customize_view.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog_customize_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_customize_view.setContentView(R.layout.otp_custom_dialog);
        dialog_customize_view.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_customize_view.setCancelable(false); // can dismiss alert screen when user click back buttonon
        dialog_customize_view.setCanceledOnTouchOutside(false);
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
        MaterialEditText edt_otpNo = dialog_customize_view.findViewById(R.id.edt_otpNo);
        LinearLayout mlly_otp = dialog_customize_view.findViewById(R.id.mlly_otp);
        txt_type.setText("Verify Payment");
        txt_submit.setText("SUBMIT");
        mlly_otp.setVisibility(View.VISIBLE);
        txt_submit.setOnClickListener(view -> {
            String otpVerify = edt_otpNo.getText().toString().trim();
            if (!otpVerify.isEmpty()){
                if (otpVerify.equalsIgnoreCase(otp)){
                    //    Toast.makeText(CheckOutActivity.this, "Payment successful !!!", Toast.LENGTH_SHORT).show();
                    mDialog = new ProgressDialog(CheckOutActivity.this);
                    mDialog.setMessage("Please wait....");
                    mDialog.setCancelable(false);
                    mDialog.show();

                    confirmPayment.orderByChild("orderId").equalTo(orderId);
                    confirmPayment.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                PaymentConfirmModel requestAB = snapshot.getValue(PaymentConfirmModel.class);
                                String keyAB = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestAB.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference("ConfirmPayment").child(keyAB);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentState", "Paid");
                                    reference.updateChildren(hashMap);
                                }
                            }
                            confirmPayment.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    restaurantConfirmPayment.orderByChild("orderId").equalTo(orderId);
                    restaurantConfirmPayment.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                PaymentConfirmModel requestAB = snapshot.getValue(PaymentConfirmModel.class);
                                String keyAB = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestAB.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_ConfirmPayment").child(keyAB);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentState", "Paid");
                                    reference.updateChildren(hashMap);
                                }
                            }
                            restaurantConfirmPayment.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                    unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Request requestA = snapshot.getValue(Request.class);
                                String keyA = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestA.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Cash");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", otp);
                                    reference.updateChildren(hashMap);
                                }
                            }
                            unPaidRequestsRestaurant.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    unPaidRequestsKitchen.orderByChild("orderId").equalTo(orderId);
                    unPaidRequestsKitchen.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Request requestA = snapshot.getValue(Request.class);
                                String keyA = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestA.getOrderId())) {
                               /* if (orderId.equalsIgnoreCase(requestA.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"8886666857"+"_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Cash");
                                    hashMap.put("paymentState", "Paid");
                                    reference.updateChildren(hashMap);
                                }*/
                                    if (ownerNo.equalsIgnoreCase("8886666856")) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666860" + "_UnPaidRequests").child(keyA);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Cash");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", otp);
                                        reference.updateChildren(hashMap);
                                    } else if (ownerNo.equalsIgnoreCase("8886666854") || (ownerNo.equalsIgnoreCase("8886666855"))) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666857" + "_UnPaidRequests").child(keyA);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Cash");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", otp);
                                        reference.updateChildren(hashMap);
                                    } else if (ownerNo.equalsIgnoreCase("7995591106")) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "7995591107" + "_UnPaidRequests").child(keyA);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Cash");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", otp);
                                        reference.updateChildren(hashMap);
                                        Log.e("orderId0", orderId);
                                    } else {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + kitchenNo + "_UnPaidRequests").child(keyA);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Cash");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", otp);
                                        reference.updateChildren(hashMap);
                                    }
                                }
                            }

                            unPaidRequestsKitchen.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // update payment status in user request table
                    orderStatus.orderByChild("orderId").equalTo(orderId);
                    orderStatus.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Request requestB = snapshot.getValue(Request.class);
                                String keyB = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestB.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference(userNo+"_Requests").child(keyB);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Cash");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", otp);
                                    reference.updateChildren(hashMap);
                                    Log.e("paid",userNo);
                                }
                            }
                            orderStatus.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // update payment status in restaurant request table
                    restOrderStatus.orderByChild("orderId").equalTo(orderId);
                    restOrderStatus.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Request requestC = snapshot.getValue(Request.class);
                                String keyC = snapshot.getRef().getKey();
                                if (orderId.equalsIgnoreCase(requestC.getOrderId())){
                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+ownerNo+"_Requests").child(keyC);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Cash");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", otp);
                                    reference.updateChildren(hashMap);
                                }
                            }
                            restOrderStatus.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // update user coupon status
        /*if (couponAvailable){
            reference = FirebaseDatabase.getInstance().getReference("MyOffers&Coupons").child(userNo+"_"+couponCode);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", "Used");
            reference.updateChildren(hashMap);
        }*/
                    dialog_customize_view.dismiss();

                    /*DatabaseReference user_Order = FirebaseDatabase.getInstance().getReference("UserOrderDetail").child(Common.currentUser.getPhoneNo());;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("phoneNo", Common.currentUser.getPhoneNo());
                    hashMap.put("cashPayment", "False");
                    hashMap.put("otpNo", "");
                    hashMap.put("transactionNo", "");
                    hashMap.put("orderId", "");
                    hashMap.put("orderStatus", "Yes");
                    user_Order.updateChildren(hashMap);*/

                    Paper.book().write(Common.CASH_PAYMENT,"False");
                    Paper.book().write(Common.COMPLETE_ORDER, "Yes");
                    Paper.book().write(Common.ORDER_ID, "");

                    // Inventory Deduction
                   /* if (cartConfirmOrder != null && !cartConfirmOrder.isEmpty()) {
                        for (Order order : cartConfirmOrder) {
                            String productId = order.getProductId();
                            listFoodByProductId = foodListAll.orderByChild("productId").equalTo(productId);
                            listFoodByProductId.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Food food = snapshot.getValue(Food.class);
                                        if (productId.equalsIgnoreCase(food.getProductId())) {
                                            if (food.getQuantity() != null && !food.getQuantity().isEmpty()) {
                                                Long Qty = Long.parseLong(food.getQuantity());
                                                Long itemQty = Long.parseLong(order.getQuantity());
                                                Long updateQty = Qty - itemQty;

                                                String key = food.getProductId();
                                                Log.e("ProductId", key);
                                                reference = FirebaseDatabase.getInstance().getReference(restId + "_" + food.getCategoryName()).child(key);
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("foodQuantity", updateQty);
                                                hashMap.put("quantity", "" + updateQty);
                                                reference.updateChildren(hashMap);

                                                reference = FirebaseDatabase.getInstance().getReference(restId + "_All").child(key);
                                                HashMap<String, Object> hashMapA = new HashMap<>();
                                                hashMapA.put("foodQuantity", updateQty);
                                                hashMapA.put("quantity", "" + updateQty);
                                                reference.updateChildren(hashMapA);
                                            }
                                            if (food.getIngredients() != null && !food.getIngredients().isEmpty()) {
                                                for (Ingredients ingredients : food.getIngredients()) {
                                                    String keyID = ingredients.getKeyId();

                                                    Log.e("ingredients",ingredients.getName()+"ID"+keyID);

                                                    listIngredientsById = inventoryList.orderByChild("key_ID").equalTo(keyID);
                                                    listIngredientsById.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                InventoryModel inventoryModel = snapshot.getValue(InventoryModel.class);
                                                                if (keyID.equalsIgnoreCase(inventoryModel.getKey_ID())){

                                                                    Log.e("ingredients!",ingredients.getName()+"ID!"+keyID);

                                                                    int dQty = Integer.parseInt(order.getQuantity());
                                                                    Long quantity = ingredients.getQuantity();
                                                                    Long intQty = inventoryModel.getQuantity();
                                                                    Long finalQty = intQty - dQty * quantity ;
                                                                    String ivt_key = inventoryModel.getKey_ID();
                                                                    Log.e("finalQty",""+finalQty);
                                                                    Log.e("inventory Items",inventoryModel.getName()+"ID"+inventoryModel.getQuantity());
                                                                    reference = FirebaseDatabase.getInstance().getReference(restId+"_"+"Inventory").child(ivt_key);
                                                                    HashMap<String, Object> hashMapA = new HashMap<>();
                                                                    hashMapA.put("quantity", finalQty);
                                                                    reference.updateChildren(hashMapA);

                                                                }
                                                            }
                                                            listIngredientsById.removeEventListener(this);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                    listFoodByProductId.removeEventListener(this);

                                    new Thread(){
                                        public void run(){
                                            try {
                                                sleep(3000);
                                                 mDialog.dismiss();
                                                Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                                intent.putExtra("restId",restID);
                                                intent.putExtra("orderId",orderID);
                                                intent.putExtra("ownerNo",ownerNo);
                                                startActivity(intent);
                                                finish();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }*/

                    new Thread(){
                        public void run(){
                            try {
                                sleep(10000);
                                mDialog.dismiss();
                                Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                intent.putExtra("restId",restID);
                                intent.putExtra("orderId",orderID);
                                intent.putExtra("ownerNo",ownerNo);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }else {
                Toast.makeText(getApplicationContext(), "Mismatch OTP", Toast.LENGTH_LONG).show();
            }
            }else {
                Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_LONG).show();
            }
        });
        dialog_customize_view.show();
    }

    private void applyCoupon(String coupon) {

        offersCouponsForms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userNo+"_"+coupon).exists()) {
                    OffersCouponsModel offersCouponsModel = dataSnapshot.child(userNo+"_"+coupon).getValue(OffersCouponsModel.class);

                    if (offersCouponsModel.getStatus().equalsIgnoreCase("Active")){
                        couponAvailable = true;
                        couponCode = coupon;
                        lly_couponDiscount.setVisibility(View.VISIBLE);
                        if (offersCouponsModel.getDiscount() != null){
                            discount = Double.parseDouble(offersCouponsModel.getDiscount());
                        }else {
                            discount = 10.0;
                        }
                        discount_price = (total / 100.0f) * discount;
                        promoDiscount = "" + new DecimalFormat("##.##").format(discount_price);
                        txt_discount.setText(getResources().getString(R.string.Rs)+" "+discount_price);

                        Locale locale = new Locale("en", "IN");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        totalPayAmount = total - discount_price - discount_wallet + foodTax + itemsGst + serviceTax;;
                      //  txt_payAmount.setText(fmt.format(totalPayAmount));
                        txt_payAmount.setText(fmt.format(total));

                        totalAmount = (int) Math.round(totalPayAmount);

                       // btn_confirmItems.setText("Pay "+fmt.format(totalAmount));
                        btn_confirmItems.setText(fmt.format(total));

                        edt_couponCode.setEnabled(false);
                        //edt_couponCode.setBackgroundColor(Color.TRANSPARENT);
                        //  edt_couponCode.setFocusable(false);
                        edt_couponCode.setCursorVisible(false);
                        // edt_couponCode.setKeyListener(null);

                        txt_applyCoupon.setVisibility(View.INVISIBLE);
                        img_couponCancel.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(CheckOutActivity.this, "Oops, Coupon already used ! ", Toast.LENGTH_SHORT).show();
                    }

                    //restOffersCoupons
                }else {
                    restOffersCoupons.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(restId+"_"+coupon).exists()) {
                                OffersCouponsModel offersCouponsModel = dataSnapshot.child(restId+"_"+coupon).getValue(OffersCouponsModel.class);

                                if (offersCouponsModel.getStatus().equalsIgnoreCase("Active")){
                                     discountType = offersCouponsModel.getOfferType();
                                     Log.e("discountType",discountType);
                                    couponAvailable = true;
                                    couponCode = coupon;
                                    lly_couponDiscount.setVisibility(View.VISIBLE);
                                    if (offersCouponsModel.getDiscount() != null){
                                        discount = Double.parseDouble(offersCouponsModel.getDiscount());
                                    }else {
                                        discount = 10.0;
                                    }
                                    if (discountType.equalsIgnoreCase("Food")){
                                        discount_price = (foodTotal / 100.0f) * discount;
                                    }else if (discountType.equalsIgnoreCase("Bar")){
                                        discount_price = (wineTotal / 100.0f) * discount;
                                    }else if (discountType.equalsIgnoreCase("Total")){
                                        discount_price = (total / 100.0f) * discount;
                                    }

                                   // discount_price = (total / 100.0f) * discount;
                                    promoDiscount = "" + new DecimalFormat("##.##").format(discount_price);
                                    txt_discount.setText(getResources().getString(R.string.Rs)+""+promoDiscount);

                                    Locale locale = new Locale("en", "IN");
                                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                                    totalPayAmount = total - discount_price - discount_wallet + foodTax + itemsGst + itemsGst + serviceTax;;
                                  //  txt_payAmount.setText(fmt.format(totalPayAmount));
                                    txt_payAmount.setText(fmt.format(total));

                                    totalAmount = (int) Math.round(totalPayAmount);

                                   // btn_confirmItems.setText("Pay "+fmt.format(totalAmount));
                                    btn_confirmItems.setText(fmt.format(total));

                                    edt_couponCode.setEnabled(false);
                                    //edt_couponCode.setBackgroundColor(Color.TRANSPARENT);
                                    //  edt_couponCode.setFocusable(false);
                                    edt_couponCode.setCursorVisible(false);
                                    // edt_couponCode.setKeyListener(null);

                                    txt_applyCoupon.setVisibility(View.INVISIBLE);
                                    img_couponCancel.setVisibility(View.VISIBLE);
                                }else {
                                    Toast.makeText(CheckOutActivity.this, "Oops, Coupon already used ! ", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(CheckOutActivity.this, "Oops, Coupon code not exist !!! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // Toast.makeText(CheckOutActivity.this, "Oops, Coupon code not exist !!! ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendNotificationOrder(final String order_number, final String paymentType) {

        DatabaseReference tokens = database.getReference("Tokens");
        Query data = tokens.orderByChild("phone").equalTo(ownerNo);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    if (paymentType.equalsIgnoreCase("Online")){
                        Map<String,String> dataSend = new HashMap<>();
                        dataSend.put("title","Dining");
                        dataSend.put("message","Payment done by order Id : " + orderId +"<?>"+ order_number+"<?>"+"Payment"+"<?>"+tableNo);
                         dataMessage = new DataMessage(serverToken.getToken(),dataSend);
                    }else {
                        Map<String,String> dataSend = new HashMap<>();
                        dataSend.put("title","Dining");
                        dataSend.put("message","Collect Cash by order Id : " + orderId +"<?>"+ order_number+"<?>"+"Payment"+"<?>"+tableNo);
                         dataMessage = new DataMessage(serverToken.getToken(),dataSend);
                    }

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
                                            /*Toast.makeText(CheckOutActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();*/

                                        } else {
                                            Toast.makeText(CheckOutActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            cash_Paid = Paper.book().read(Common.CASH_PAID);
            if (cash_Paid != null && !cash_Paid.isEmpty() && cash_Paid.equalsIgnoreCase("True")) {
                Log.e("cashPaid", cash_Paid + " " + orderId);
                mDialog = new ProgressDialog(CheckOutActivity.this);
                mDialog.setMessage("Please wait....");
                mDialog.setCancelable(false);
                mDialog.show();
                cartConfirmOnlineOrder.clear();
                searchByUnpaidOrders = orderCheckout.orderByChild("paymentState").equalTo("UnPaid");
                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.currentUser.getPhoneNo()+"_Requests");
                searchByUnpaidOrders.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request request = snapshot.getValue(Request.class);

                            assert request != null;
                            if (tableNo.equalsIgnoreCase(request.getTableNo())) {
                                for (Order order : request.getFood()) {
                                    orderId = request.getOrderId();
                                    cartConfirmOnlineOrder.add(order);
                                    Log.e("itemName", order.getProductName());
                                }
                            }
                        }
                        searchByUnpaidOrders.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                new Thread() {
                    public void run() {
                        try {
                            sleep(4000);
                // for performance improve
                confirmPayment.orderByChild("orderId").equalTo(orderId);
                confirmPayment.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PaymentConfirmModel requestAB = snapshot.getValue(PaymentConfirmModel.class);
                            String keyAB = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestAB.getOrderId())) {
                                reference = FirebaseDatabase.getInstance().getReference("ConfirmPayment").child(keyAB);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("paymentState", "Paid");
                                hashMap.put("paymentMethod", "Online");
                                reference.updateChildren(hashMap);
                            }
                        }
                        confirmPayment.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                restaurantConfirmPayment.orderByChild("orderId").equalTo(orderId);
                restaurantConfirmPayment.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PaymentConfirmModel requestAB = snapshot.getValue(PaymentConfirmModel.class);
                            String keyAB = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestAB.getOrderId())) {
                                reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_ConfirmPayment").child(keyAB);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("paymentState", "Paid");
                                hashMap.put("paymentMethod", "Online");
                                reference.updateChildren(hashMap);
                            }
                        }
                        restaurantConfirmPayment.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request requestA = snapshot.getValue(Request.class);
                            String keyA = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestA.getOrderId())) {
                                reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_UnPaidRequests").child(keyA);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("paymentMethod", "Online");
                                hashMap.put("paymentState", "Paid");
                                hashMap.put("cashPaid", "False");
                                hashMap.put("cashOtp", cash_otp);
                                reference.updateChildren(hashMap);
                            }
                        }
                        unPaidRequestsRestaurant.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                unPaidRequestsKitchen.orderByChild("orderId").equalTo(orderId);
                unPaidRequestsKitchen.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request requestA = snapshot.getValue(Request.class);
                            String keyA = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestA.getOrderId())) {

                                if (ownerNo.equalsIgnoreCase("8886666856")) {
                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666860" + "_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Online");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", cash_otp);
                                    reference.updateChildren(hashMap);
                                } else if (ownerNo.equalsIgnoreCase("8886666854") || (ownerNo.equalsIgnoreCase("8886666855"))) {
                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666857" + "_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Online");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", cash_otp);
                                    reference.updateChildren(hashMap);
                                } else if (ownerNo.equalsIgnoreCase("7995591106")) {
                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "7995591107" + "_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Online");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", cash_otp);
                                    reference.updateChildren(hashMap);
                                    Log.e("orderId0", orderId);
                                } else {
                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_" + kitchenNo + "_UnPaidRequests").child(keyA);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("paymentMethod", "Online");
                                    hashMap.put("paymentState", "Paid");
                                    hashMap.put("cashPaid", "False");
                                    hashMap.put("cashOtp", cash_otp);
                                    reference.updateChildren(hashMap);
                                }
                            }
                        }

                        unPaidRequestsKitchen.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // update payment status in user request table
                orderStatus.orderByChild("orderId").equalTo(orderId);
                orderStatus.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request requestB = snapshot.getValue(Request.class);
                            String keyB = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestB.getOrderId())) {
                                reference = FirebaseDatabase.getInstance().getReference(userNo + "_Requests").child(keyB);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("paymentMethod", "Online");
                                hashMap.put("paymentState", "Paid");
                                hashMap.put("cashPaid", "False");
                                hashMap.put("cashOtp", cash_otp);
                                reference.updateChildren(hashMap);
                                Log.e("paid", userNo);
                            }
                        }
                        orderStatus.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // update payment status in restaurant request table
                restOrderStatus.orderByChild("orderId").equalTo(orderId);
                restOrderStatus.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Request requestC = snapshot.getValue(Request.class);
                            String keyC = snapshot.getRef().getKey();
                            if (orderId.equalsIgnoreCase(requestC.getOrderId())) {
                                reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_Requests").child(keyC);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("paymentMethod", "Online");
                                hashMap.put("paymentState", "Paid");
                                hashMap.put("cashPaid", "False");
                                hashMap.put("cashOtp", cash_otp);
                                reference.updateChildren(hashMap);
                            }
                        }
                        restOrderStatus.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // update user coupon status
        /*if (couponAvailable){
            reference = FirebaseDatabase.getInstance().getReference("MyOffers&Coupons").child(userNo+"_"+couponCode);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", "Used");
            reference.updateChildren(hashMap);
        }*/

                    /*DatabaseReference user_Order = FirebaseDatabase.getInstance().getReference("UserOrderDetail").child(Common.currentUser.getPhoneNo());;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("phoneNo", Common.currentUser.getPhoneNo());
                    hashMap.put("cashPayment", "False");
                    hashMap.put("otpNo", "");
                    hashMap.put("transactionNo", "");
                    hashMap.put("orderId", "");
                    hashMap.put("orderStatus", "Yes");
                    user_Order.updateChildren(hashMap);*/

                Paper.book().write(Common.CASH_PAYMENT, "False");
                Paper.book().write(Common.COMPLETE_ORDER, "Yes");
                Paper.book().write(Common.ORDER_ID, "");
                Paper.book().write(Common.CASH_PAID, "");

                // Inventory Deductions
                /*if (cartConfirmOnlineOrder != null && !cartConfirmOnlineOrder.isEmpty()) {
                    for (Order order : cartConfirmOnlineOrder) {
                        String productId = order.getProductId();
                        listFoodByProductId = foodListAll.orderByChild("productId").equalTo(productId);
                        listFoodByProductId.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Food food = snapshot.getValue(Food.class);
                                    if (productId.equalsIgnoreCase(food.getProductId())) {
                                        if (food.getQuantity() != null && !food.getQuantity().isEmpty()) {
                                            Long Qty = Long.parseLong(food.getQuantity());
                                            Long itemQty = Long.parseLong(order.getQuantity());
                                            Long updateQty = Qty - itemQty;

                                            String key = food.getProductId();
                                            Log.e("ProductId", key);
                                            reference = FirebaseDatabase.getInstance().getReference(restId + "_" + food.getCategoryName()).child(key);
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("foodQuantity", updateQty);
                                            hashMap.put("quantity", "" + updateQty);
                                            reference.updateChildren(hashMap);

                                            reference = FirebaseDatabase.getInstance().getReference(restId + "_All").child(key);
                                            HashMap<String, Object> hashMapA = new HashMap<>();
                                            hashMapA.put("foodQuantity", updateQty);
                                            hashMapA.put("quantity", "" + updateQty);
                                            reference.updateChildren(hashMapA);
                                        }
                                        if (food.getIngredients() != null && !food.getIngredients().isEmpty()) {
                                            for (Ingredients ingredients : food.getIngredients()) {
                                                String keyID = ingredients.getKeyId();

                                                Log.e("ingredients", ingredients.getName() + "ID" + keyID);

                                                listIngredientsById = inventoryList.orderByChild("key_ID").equalTo(keyID);
                                                listIngredientsById.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            InventoryModel inventoryModel = snapshot.getValue(InventoryModel.class);
                                                            if (keyID.equalsIgnoreCase(inventoryModel.getKey_ID())) {

                                                                Log.e("ingredients!", ingredients.getName() + "ID!" + keyID);

                                                                int dQty = Integer.parseInt(order.getQuantity());
                                                                Long quantity = ingredients.getQuantity();
                                                                Long intQty = inventoryModel.getQuantity();
                                                                Long finalQty = intQty - dQty * quantity;
                                                                String ivt_key = inventoryModel.getKey_ID();
                                                                Log.e("finalQty", "" + finalQty);
                                                                Log.e("inventory Items", inventoryModel.getName() + "ID" + inventoryModel.getQuantity());
                                                                reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "Inventory").child(ivt_key);
                                                                HashMap<String, Object> hashMapA = new HashMap<>();
                                                                hashMapA.put("quantity", finalQty);
                                                                reference.updateChildren(hashMapA);

                                                            }
                                                        }
                                                        listIngredientsById.removeEventListener(this);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                listFoodByProductId.removeEventListener(this);

                                new Thread() {
                                    public void run() {
                                        try {
                                            sleep(3000);
                                            mDialog.dismiss();
                                            Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                            intent.putExtra("restId", restId);
                                            intent.putExtra("orderId", orderId);
                                            intent.putExtra("ownerNo", ownerNo);
                                            startActivity(intent);
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }*/


                            new Thread() {
                                public void run() {
                                    try {
                                        sleep(10000);
                                        mDialog.dismiss();
                                        Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                        intent.putExtra("restId", restId);
                                        intent.putExtra("orderId", orderId);
                                        intent.putExtra("ownerNo", ownerNo);
                                        startActivity(intent);
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
            }  catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }else {
            mDialog = new ProgressDialog(CheckOutActivity.this);
            mDialog.setMessage("Please wait....");
            mDialog.setCancelable(false);
            mDialog.show();

            Log.e("razorpayPaymentID", "" + razorpayPaymentID);
            String pay_Amount = Paper.book().read(Common.TOTAL_PAy_AMOUNT);
            Log.e("pay_Amount", "" + pay_Amount);
            promoDiscount = Paper.book().read(Common.PROMO_DISCOUNT);
            walletDiscount = Paper.book().read(Common.WALLET_DISCOUNT);
            String total_Qty = Paper.book().read(Common.TOTAL_QTY);
            tipAmount = Paper.book().read(Common.TIP_AMOUNT);
            foodTaxAmount = Paper.book().read(Common.FOOD_TAX_AMOUNT);
            serviceTaxAmount = Paper.book().read(Common.SERVICE_TAX_AMOUNT);
            offersType = Paper.book().read(Common.OFFER_TYPE);
            String offer_discount_price = Paper.book().read(Common.OFFER_DISCOUNT);

            cartConfirmOnlineOrder.clear();
            searchByUnpaidOrders = orderCheckout.orderByChild("paymentState").equalTo("UnPaid");
            //DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.currentUser.getPhoneNo()+"_Requests");
            searchByUnpaidOrders.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request request = snapshot.getValue(Request.class);
                        assert request != null;
                        if (tableNo.equalsIgnoreCase(request.getTableNo())) {
                            for (Order order : request.getFood()) {
                                cartConfirmOnlineOrder.add(order);
                                Log.e("itemName", order.getProductName());
                            }
                        }
                    }
                    searchByUnpaidOrders.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                        String trans_number = String.valueOf(System.currentTimeMillis());
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                        time = dateFormat.format(new Date());
                        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = simpleDate.format(new Date());

                        SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
                        String month = simpleMonth.format(new Date());
                        SimpleDateFormat simpleMYear = new SimpleDateFormat("yyyy");
                        String year = simpleMYear.format(new Date());

                        Format dayFormat = new SimpleDateFormat("EEE");
                        day = dayFormat.format(new Date());

                        Calendar currTime = Calendar.getInstance();
                        hour = currTime.get(Calendar.HOUR_OF_DAY);

                        paymentConfirmModel = new PaymentConfirmModel(
                                userNo, userFirstName, "", userNo,
                                "2", edtComment.getText().toString(),
                                "Online", "" + pay_Amount, promoDiscount, walletDiscount,
                                "Paid", restId, ownerNo, orderId, "",
                                tableNo, time, date, razorpayPaymentID, month, year,
                                "" + total_Qty, restName, restAddress, restGSTNo, "", tipAmount,
                                foodTaxAmount, serviceTaxAmount, offersType, "" + offer_discount_price, "", day, "" + hour, "User",
                                kotNo, "", "", "", "", "", "",
                                "", "", "", "", "Yes", discountType, "", "", cartConfirmOnlineOrder
                        );

                        Log.e("order Details", "A" + payAmount + "F" + cartConfirmOnlineOrder);
                        restaurantConfirmPayment.child(trans_number).setValue(paymentConfirmModel);
                        confirmPayment.child(trans_number).setValue(paymentConfirmModel);
                        if (restOfferType.equalsIgnoreCase("Food")) {
                            if (offersType.equalsIgnoreCase("Dineout")) {

                                PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                        userNo, userFirstName, "", userNo,
                                        "2", edtOffersCommentCode.getText().toString(), "Online",
                                        "" + payAmount, promoDiscount, walletDiscount, "Paid",
                                        restId, ownerNo, orderId, "", tableNo, time, date, "", month, year,
                                        "" + totalQnty, restName, restAddress, restGSTNo, "", tipAmount,
                                        foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                        "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "", "",
                                        "", "", "", "", "", "",
                                        "", "Yes", discountType, "", "", cartFoodNcOrderItems);

                                restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                            } else if (offersType.equalsIgnoreCase("Zomato")) {
                                PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                        userNo, userFirstName, "", userNo, "2",
                                        edtOffersCommentCode.getText().toString(), "Online", "" + payAmount, promoDiscount,
                                        walletDiscount, "Paid", restId, ownerNo, orderId, "", tableNo, time, date, "",
                                        month, year, "" + totalQnty, restName, restAddress, restGSTNo, "", tipAmount,
                                        foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                        "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "", "", "",
                                        "", "", "", "", "", "",
                                        "Yes", discountType, "", "", cartFoodNcZomatoOrderItems);

                                restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                            }
                        }else {
                        if (offersType.equalsIgnoreCase("Dineout")) {

                            PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                    userNo, userFirstName, "", userNo,
                                    "2", edtOffersCommentCode.getText().toString(), "Online",
                                    "" + payAmount, promoDiscount, walletDiscount, "Paid",
                                    restId, ownerNo, orderId, "", tableNo, time, date, "", month, year,
                                    "" + totalQnty, restName, restAddress, restGSTNo, "", tipAmount,
                                    foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                    "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "", "",
                                    "", "", "", "", "", "",
                                    "", "Yes", discountType, "", "", cartNcOrderItems);

                            restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                        } else if (offersType.equalsIgnoreCase("Zomato")) {
                            PaymentConfirmModel paymentNcConfirmModel = new PaymentConfirmModel(
                                    userNo, userFirstName, "", userNo, "2",
                                    edtOffersCommentCode.getText().toString(), "Online", "" + payAmount, promoDiscount,
                                    walletDiscount, "Paid", restId, ownerNo, orderId, "", tableNo, time, date, "",
                                    month, year, "" + totalQnty, restName, restAddress, restGSTNo, "", tipAmount,
                                    foodTaxAmount, serviceTaxAmount, offersType, "" + kings_discount_price,
                                    "" + kings_discount_price, day, "" + hour, "User", kotNo, "", "", "", "",
                                    "", "", "", "", "", "",
                                    "Yes", discountType, "", "", cartNcZomatoOrderItems);

                            restaurantNCConfirmPayment.child(trans_number).setValue(paymentNcConfirmModel);
                        }
                    }

                        unPaidRequestsRestaurant.orderByChild("orderId").equalTo(orderId);
                        unPaidRequestsRestaurant.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                unPaidRequestsRestaurant.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        unPaidRequestsKitchen.orderByChild("orderId").equalTo(orderId);
                        unPaidRequestsKitchen.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();

                                    if (ownerNo.equalsIgnoreCase("8886666856")) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666860" + "_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    } else if (ownerNo.equalsIgnoreCase("8886666854") || (ownerNo.equalsIgnoreCase("8886666855"))) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "8886666857" + "_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    } else if (ownerNo.equalsIgnoreCase("7995591106")) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "7995591107" + "_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    } else {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + kitchenNo + "_UnPaidRequests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                unPaidRequestsKitchen.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // update payment status in user request table
                        orderStatus.orderByChild("orderId").equalTo(orderId);
                        orderStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())) {
                                        reference = FirebaseDatabase.getInstance().getReference(userNo + "_Requests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                        Log.e("paid", userNo);
                                    }
                                }
                                orderStatus.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
// update payment status in restaurant request table
                        restOrderStatus.orderByChild("orderId").equalTo(orderId);
                        restOrderStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Request request = snapshot.getValue(Request.class);
                                    String key = snapshot.getRef().getKey();
                                    if (orderId.equalsIgnoreCase(request.getOrderId())) {
                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + ownerNo + "_Requests").child(key);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("paymentMethod", "Online");
                                        hashMap.put("paymentState", "Paid");
                                        hashMap.put("cashPaid", "False");
                                        hashMap.put("cashOtp", "");
                                        reference.updateChildren(hashMap);
                                    }
                                }
                                restOrderStatus.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // update user coupon status
       /* if (couponAvailable){
            reference = FirebaseDatabase.getInstance().getReference("MyOffers&Coupons").child(userNo+"_"+couponCode);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", "Used");
            reference.updateChildren(hashMap);
        }*/

                        //   updateUserWalletBalance();
//        Toast.makeText(CheckOutActivity.this, "Payment successful !!!", Toast.LENGTH_SHORT).show();
                        Paper.book().write(Common.COMPLETE_ORDER, "Yes");
                        Paper.book().write(Common.ORDER_ID, "");

                        // Delete Cart
                        new OderDatabase(getBaseContext()).cleanOrderCart(userNo);
                        sendNotificationOrder(trans_number, "Online");
                        //    Toast.makeText(CheckOutActivity.this, "Payment successful !!!", Toast.LENGTH_SHORT).show();

                        // Inventory Deductions
                        /*if (cartConfirmOnlineOrder != null && !cartConfirmOnlineOrder.isEmpty()) {
                            for (Order order : cartConfirmOnlineOrder) {
                                String productId = order.getProductId();
                                listFoodByProductId = foodListAll.orderByChild("productId").equalTo(productId);
                                listFoodByProductId.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Food food = snapshot.getValue(Food.class);
                                            if (productId.equalsIgnoreCase(food.getProductId())) {
                                                if (food.getQuantity() != null && !food.getQuantity().isEmpty()) {
                                                    Long Qty = Long.parseLong(food.getQuantity());
                                                    Long itemQty = Long.parseLong(order.getQuantity());
                                                    Long updateQty = Qty - itemQty;

                                                    String key = food.getProductId();
                                                    Log.e("ProductId", key);
                                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_" + food.getCategoryName()).child(key);
                                                    HashMap<String, Object> hashMap = new HashMap<>();
                                                    hashMap.put("foodQuantity", updateQty);
                                                    hashMap.put("quantity", "" + updateQty);
                                                    reference.updateChildren(hashMap);

                                                    reference = FirebaseDatabase.getInstance().getReference(restId + "_All").child(key);
                                                    HashMap<String, Object> hashMapA = new HashMap<>();
                                                    hashMapA.put("foodQuantity", updateQty);
                                                    hashMapA.put("quantity", "" + updateQty);
                                                    reference.updateChildren(hashMapA);
                                                }
                                                if (food.getIngredients() != null && !food.getIngredients().isEmpty()) {
                                                    for (Ingredients ingredients : food.getIngredients()) {
                                                        String keyID = ingredients.getKeyId();

                                                        Log.e("ingredients", ingredients.getName() + "ID" + keyID);

                                                        listIngredientsById = inventoryList.orderByChild("key_ID").equalTo(keyID);
                                                        listIngredientsById.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    InventoryModel inventoryModel = snapshot.getValue(InventoryModel.class);
                                                                    if (keyID.equalsIgnoreCase(inventoryModel.getKey_ID())) {

                                                                        Log.e("ingredients!", ingredients.getName() + "ID!" + keyID);

                                                                        int dQty = Integer.parseInt(order.getQuantity());
                                                                        Long quantity = ingredients.getQuantity();
                                                                        Long intQty = inventoryModel.getQuantity();
                                                                        Long finalQty = intQty - dQty * quantity;
                                                                        String ivt_key = inventoryModel.getKey_ID();
                                                                        Log.e("finalQty", "" + finalQty);
                                                                        Log.e("inventory Items", inventoryModel.getName() + "ID" + inventoryModel.getQuantity());
                                                                        reference = FirebaseDatabase.getInstance().getReference(restId + "_" + "Inventory").child(ivt_key);
                                                                        HashMap<String, Object> hashMapA = new HashMap<>();
                                                                        hashMapA.put("quantity", finalQty);
                                                                        reference.updateChildren(hashMapA);

                                                                    }
                                                                }
                                                                listIngredientsById.removeEventListener(this);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                        listFoodByProductId.removeEventListener(this);

                                        // Delete Cart
                                        new OderDatabase(getBaseContext()).cleanOrderCart(userNo);

                                        new Thread() {
                                            public void run() {
                                                try {
                                                    sleep(3000);
                                                    mDialog.dismiss();
                                                    Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                                    intent.putExtra("restId", restId);
                                                    intent.putExtra("orderId", orderId);
                                                    intent.putExtra("ownerNo", ownerNo);
                                                    startActivity(intent);
                                                    finish();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }*/

                        new Thread() {
                            public void run() {
                                try {
                                    sleep(8000);
                                    mDialog.dismiss();
                                    Intent intent = new Intent(CheckOutActivity.this, FeedBackActivity.class);
                                    intent.putExtra("restId", restId);
                                    intent.putExtra("orderId", orderId);
                                    intent.putExtra("ownerNo", ownerNo);
                                    startActivity(intent);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("Exception",""+e.getMessage());
    }

    }
   /* @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckOutActivity.this);
        alertDialog.setTitle("Please Wait.....");
        alertDialog.setMessage("Updating information.....");
        alertDialog.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        LayoutInflater inflater = LayoutInflater.from(this);

        alertDialog.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();

        });
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
        *//*super.onBackPressed();*//*

    }*/
    private void updateUserWalletBalance() {
        Double updateBal = walletBal - discount_wallet;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userNo);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("balance", ""+updateBal);
        reference.updateChildren(hashMap);
    }

    @Override
    public void onPaymentError(int code, String response) {
        Log.e("response", ""+response);
        Log.e("response_code", ""+code);
        Toast.makeText(CheckOutActivity.this, "Payment Failed !!!", Toast.LENGTH_SHORT).show();
        // Checkout.NETWORK_ERROR : A network error, for example loss of internet connectivity
        // Checkout.INVALID_OPTIONS : An issue with options passed in checkout.open
        // Checkout.PAYMENT_CANCELED : User cancelled the payment
        // Checkout.TLS_ERROR : Device does not support TLS v1.1 or TLS v1.2
    }
    public void startPayment(int totalAmount) {
        Log.e("totalAmount",""+totalAmount);
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        // checkout.setFullScreenDisable(true);   // Disable Checkout in Full screen
        // Checkout.clearUserData(this); // Erase User Data from SDK
        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher_round);
        /**
         * Reference to current activity
         */
        final Activity activity = this;
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Dining");
            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", orderId);
            options.put("currency", "INR");
            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            /*options.put("amount", "500");*/
            options.put("amount", totalAmount);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

}
