package com.kisaann.thedining;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DownloadInvoiceActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txt_restaurantName;
    TextView txt_restaurantAddress;
    TextView txt_date;
    TextView txt_time;
    TextView txt_orderNo;
    TextView txt_customerName;
    TextView txt_customerNo;
    TextView txt_total;
    TextView txt_subTotal;
    TextView txt_cancel;
    TextView txt_share;
    TextView txt_couponDiscount;
    TextView txt_walletDiscount;
    TextView txt_gstAmount;
    TextView txt_serviceTaxAmount;
    TextView txt_gstNo;
    LinearLayout mlly_itemDetails;
    private LinearLayout mlly_invoiceShare;
    TextView txt_offerDiscount;
    private LinearLayout mlly_invoice;
    private LinearLayout layout1 ;
    List<Order> myOrders;
    Double promoDiscount = 0.0;
    Double walletDiscount = 0.0;
    Double gstAmount = 0.0;
    Double serviceTaxAmount = 0.0;
    Double offerDiscount = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_invoice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        myOrders = Common.paymentConfirmModel.getFood();
        Paper.init(this);

        initUi();
        setData();
    }
    Double totalAmount = 0.0;
    Double totalSubAmount = 0.0;
    private void setData() {
       /* String name = Paper.book().read(Common.REST_NAME);
        String address = Paper.book().read(Common.REST_ADDRESS);
        txt_restaurantName.setText(name);
        txt_restaurantAddress.setText(address);*/
        txt_gstNo.setText(Common.paymentConfirmModel.getGstNo());
        if (Common.paymentConfirmModel.getRestName() != null && !Common.paymentConfirmModel.getRestName().isEmpty()) {
            txt_restaurantName.setText(Common.paymentConfirmModel.getRestName());
            txt_restaurantAddress.setText(Common.paymentConfirmModel.getRestAddress());
        }
        txt_date.setText(Common.paymentConfirmModel.getDate());
        txt_time.setText(Common.paymentConfirmModel.getTime());
        txt_orderNo.setText(Common.paymentConfirmModel.getOrderId());
        txt_customerName.setText(Common.paymentConfirmModel.getName());
        txt_customerNo.setText(Common.paymentConfirmModel.getPhone());

        if (Common.paymentConfirmModel.getPromoDiscount() != null && !Common.paymentConfirmModel.getPromoDiscount().isEmpty()) {
            promoDiscount = Double.parseDouble(Common.paymentConfirmModel.getPromoDiscount());
        }
        if (Common.paymentConfirmModel.getWalletDiscount() != null && !Common.paymentConfirmModel.getWalletDiscount().isEmpty()) {
            walletDiscount = Double.parseDouble(Common.paymentConfirmModel.getWalletDiscount());
        }
        if (Common.paymentConfirmModel.getFoodGst() != null && !Common.paymentConfirmModel.getFoodGst().isEmpty()) {
            gstAmount = Double.parseDouble(Common.paymentConfirmModel.getFoodGst());
        }
        if (Common.paymentConfirmModel.getServiceTax() != null && !Common.paymentConfirmModel.getServiceTax().isEmpty()) {
            serviceTaxAmount = Double.parseDouble(Common.paymentConfirmModel.getServiceTax());
        }
        if (Common.paymentConfirmModel.getOfferDiscount() != null && !Common.paymentConfirmModel.getOfferDiscount().isEmpty()) {
            offerDiscount = Double.parseDouble(Common.paymentConfirmModel.getOfferDiscount());
        }
        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txt_walletDiscount.setText(fmt.format(walletDiscount));
        txt_offerDiscount.setText(fmt.format(offerDiscount));
        txt_gstAmount.setText(fmt.format(gstAmount));
        txt_serviceTaxAmount.setText(fmt.format(serviceTaxAmount));
        txt_couponDiscount.setText(fmt.format(promoDiscount));
        if (myOrders != null){
        for (Order order : myOrders) {
            layout1 = (LinearLayout) getLayoutInflater().inflate(R.layout.invoice_items_details, mlly_itemDetails, false);
            final TextView txt_itemName = layout1.findViewById(R.id.txt_itemName);
            final TextView txt_quantity = layout1.findViewById(R.id.txt_quantity);
            final TextView txt_price = layout1.findViewById(R.id.txt_price);
            final TextView txt_amount = layout1.findViewById(R.id.txt_amount);
            final TextView txt_discount = layout1.findViewById(R.id.txt_discount);

            Double original_price = Double.parseDouble(order.getPrice());
            Double discount = Double.parseDouble(order.getDiscount());
            Double discount_price = (original_price / 100.0f) * discount;
            Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
            String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

            int quantity = Integer.parseInt(order.getQuantity());
            Double price = Double.parseDouble(order.getPrice());

            Double totalDis = quantity * discount_price;
            String discountPrice = "" + new DecimalFormat("##.##").format(totalDis);

            Double total = quantity * servicePrice;

            totalSubAmount += total;

            txt_itemName.setText(order.getProductName());
            txt_quantity.setText(order.getQuantity());
            txt_price.setText(order.getPrice());
            txt_discount.setText("Discount(" + order.getDiscount() + "%) " + discountPrice);
            txt_amount.setText("" + total);

            mlly_itemDetails.addView(layout1);

        }
    }
        txt_subTotal.setText(fmt.format(totalSubAmount));

        totalAmount = totalSubAmount - promoDiscount - offerDiscount - walletDiscount + gstAmount + serviceTaxAmount;
        int totalBillAmount = (int) Math.round(totalAmount);
        txt_total.setText(fmt.format(totalBillAmount));

    }

    private void initUi() {
        txt_restaurantName = findViewById(R.id.txt_restaurantName);
        txt_restaurantAddress = findViewById(R.id.txt_restaurantAddress);
        txt_date = findViewById(R.id.txt_date);
        txt_time = findViewById(R.id.txt_time);
        txt_orderNo = findViewById(R.id.txt_orderNo);
        txt_customerName = findViewById(R.id.txt_customerName);
        txt_customerNo = findViewById(R.id.txt_customerNo);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_total = findViewById(R.id.txt_total);
        txt_cancel = findViewById(R.id.txt_cancel);
        txt_share = findViewById(R.id.txt_share);
        mlly_itemDetails = findViewById(R.id.mlly_itemDetails);
        mlly_invoiceShare = findViewById(R.id.mlly_invoiceShare);
        mlly_invoice = findViewById(R.id.mlly_invoice);
        txt_offerDiscount = findViewById(R.id.txt_offerDiscount);
        txt_couponDiscount = findViewById(R.id.txt_couponDiscount);
        txt_walletDiscount = findViewById(R.id.txt_walletDiscount);
        txt_gstAmount = findViewById(R.id.txt_gstAmount);
        txt_serviceTaxAmount = findViewById(R.id.txt_serviceTaxAmount);
        txt_gstNo = findViewById(R.id.txt_gstNo);

        txt_share.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_share:

                Bitmap bitmapImg = loadBitmapFromView(mlly_invoiceShare, mlly_invoiceShare.getWidth(),mlly_invoiceShare.getHeight());
                Uri bmpUri = getLocalBitmapUri(bitmapImg);
                Log.e("bmpUri",""+bmpUri);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    startActivity(Intent.createChooser(shareIntent, "Share Invoice"));
                } else {
                    // ...sharing failed, handle error
                }
                break;
            case R.id.txt_cancel:
                finish();
                break;
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(Bitmap bitmap) {
        // Extract Bitmap from ImageView drawable

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), Common.paymentConfirmModel.getOrderId()+"_Invoice" + ".png");
            Log.e("file",""+file);
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
            Log.e("file_bmpUri",""+bmpUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException",e.getMessage());
        }
        return bmpUri;
    }
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "ERB Demo");
    }
}
