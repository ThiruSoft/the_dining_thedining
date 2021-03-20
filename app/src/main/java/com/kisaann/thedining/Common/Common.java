package com.kisaann.thedining.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.format.DateFormat;
import android.util.Log;

import com.kisaann.thedining.Models.PaymentConfirmModel;
import com.kisaann.thedining.Models.Request;
import com.kisaann.thedining.Models.RestaurantMenuModel;
import com.kisaann.thedining.Models.User;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Remote.RetrofitClient;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class Common {
    static boolean logMessageOnOrOff = true;
    public static final int NUM_OF_COLUMN = 2;
    private static final String BASE_URL = "https://fcm.googleapis.com/";
    public static User currentUser;
    public static RestaurantMenuModel restaurantDetails;
    public static Request currentRequest;
    public static PaymentConfirmModel paymentConfirmModel;
    public static final String REST_OFFER_TYPE = "RestOfferType";
    public static final String REST_NAME = "RestName";
    public static final String REST_ADDRESS = "RestAddress";
    public static final String REST_GST_NO = "RestGSTNo";
    public static final String OWNER_NO = "OwnerNo";
    public static final String TOTAL = "Total";
    public static final String TABLE_NO = "TableNo";
    public static final String OTP = "Otp";
    public static final String CASH_PAYMENT = "CashPayment";
    public static final String TRANS_NO = "TransNo";
    public static final String COMPLETE_ORDER = "CompleteOrder";
    public static final String RESTAURANT_ID = "RestaurantId";
    public static final String KITCHEN_NO = "kitchenNo";
    public static final String WELCOME_OTP = "welcomeOtp";
    public static final String ORDER_ID = "OrderId";
    public static final String ISLOGIN = "LOGIN";
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final String USER_TYPE = "UserType";
    public static final String CASH_PAID = "cashPaid";
    public static final String TOTAL_PAy_AMOUNT = "TotalPayAmount";
    public static final String PROMO_DISCOUNT = "promoDiscount";
    public static final String WALLET_DISCOUNT = "walletDiscount";
    public static final String TOTAL_QTY = "totalQnty";
    public static final String TIP_AMOUNT = "tipAmount";
    public static final String FOOD_TAX_AMOUNT = "foodTaxAmount";
    public static final String SERVICE_TAX_AMOUNT = "serviceTaxAmount";
    public static final String OFFER_TYPE = "offersType";
    public static final String OFFER_DISCOUNT = "offersDiscount";
    public static final String ORDER_DATA = "orderData";
    public static final String USER_NAME = "UserName";
    public static final String USER_EMAIL = "UserEmail";
    public static final String USER_BAL = "UserBal";
    public static final String USER_IMAGE = "UserImage";
    public static final String UPDATE = "Update";
    public static final String DETETE = "Delete";
    public static String PHONE_TEXT = "userPhone";
    public static String currentKey;
    public static final String INTENT_FOOD_ID = "FoodId";
    public static double gps_lat = 0, gps_log =0;
    public static String postCode = "";
    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
    // this function will convert currency to number base on locale
    public static BigDecimal formatCurrency(String amount, Locale locale) throws java.text.ParseException {

        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        if (format instanceof DecimalFormat)
            ((DecimalFormat)format).setParseBigDecimal(true);

        return (BigDecimal)format.parse(amount.replace("[^\\d.,]",""));

    }
    public static String getDate(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(DateFormat.format("dd-MM-yyyy HH:mm",calendar).toString());

        return date.toString();
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();

        float pivotX = 0; float pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On the way";
        else if (code.equals("2"))
            return "Delivered";
        else
            return "Delivered";
    }
    public static String convertCodeToBookStatus(String code){
        if (code.equals("0"))
            return "Pending";
        else if (code.equals("1"))
            return "Confirm";
        else if (code.equals("2"))
            return "Cancel";
        else
            return "Pending";
    }
    public static void showLog(String logMsg, String logVal) {
        try {
            if (Common.logMessageOnOrOff) {
                if (!isValueNullOrEmpty(logMsg) && !isValueNullOrEmpty(logVal)) {
                    Log.e(logMsg, logVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isValueNullOrEmpty(String value) {
        boolean isValue = false;
        if (value == null || value.equals(null) || value.equals("")
                || value.equals("null") || value.trim().length() == 0) {
            isValue = true;
        }
        return isValue;
    }
}
