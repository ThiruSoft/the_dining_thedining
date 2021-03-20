package com.kisaann.thedining.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kisaann.thedining.Helpers.NotificationHelper;
import com.kisaann.thedining.MessageChatActivity;
import com.kisaann.thedining.MyBookingsActivity;
import com.kisaann.thedining.OffersCouponsActivity;
import com.kisaann.thedining.OrderStatus;
import com.kisaann.thedining.R;
import com.kisaann.thedining.RestWelcomeActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by HOME on 6/8/2019.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {

    String message;
    String tableNo;
    String key;
    String from;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
               // sendNotificationAPI26(remoteMessage);
                if (remoteMessage.getNotification() != null)
                {
                    String title = remoteMessage.getNotification().getTitle();
                    String body = remoteMessage.getNotification().getBody();
                    String url = "";
                    if (remoteMessage.getData() != null){
                        url = remoteMessage.getData().get("image");
                        if (!TextUtils.isEmpty(url)){
                            final String finalUrl = url;
                            new Handler(Looper.getMainLooper())
                                    .post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.get()
                                                    .load(finalUrl)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /*showNotification(MyFirebaseServiceNew.this,
                                                                    title,body,null,bitmap);*/
                                                            sendNotificationAPI26(remoteMessage,
                                                                    MyFirebaseMessaging.this,
                                                                    title,body,null,bitmap);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        }
                                    });
                        }else {
                            sendNotificationAPI26(remoteMessage,
                                    MyFirebaseMessaging.this,
                                    title,body,null,null);
                        }
                    }
                }
                else {
                    String title = remoteMessage.getData().get("title");
                    String body = remoteMessage.getData().get("message");
                    String url = remoteMessage.getData().get("image");
                    if (!TextUtils.isEmpty(url)){
                        final String finalUrl = url;
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.get()
                                                .load(finalUrl)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        sendNotificationAPI26(remoteMessage,
                                                                MyFirebaseMessaging.this,
                                                                title,body,null,bitmap);
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                    }
                                                });
                                    }
                                });
                    }
                    else {
                        sendNotificationAPI26(remoteMessage,
                                MyFirebaseMessaging.this,
                                title,body,null,null);
                    }

                }
            } else{
              //  sendNotification(remoteMessage);
                if (remoteMessage.getNotification() != null)
                {
                    String title = remoteMessage.getNotification().getTitle();
                    String body = remoteMessage.getNotification().getBody();
                    String url = "";
                    if (remoteMessage.getData() != null){
                        url = remoteMessage.getData().get("image");
                        if (!TextUtils.isEmpty(url)){
                            final String finalUrl = url;
                            new Handler(Looper.getMainLooper())
                                    .post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Picasso.get()
                                                    .load(finalUrl)
                                                    .into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            /*showNotification(MyFirebaseServiceNew.this,
                                                                    title,body,null,bitmap);*/
                                                            sendNotification(remoteMessage,
                                                                    MyFirebaseMessaging.this,
                                                                    title,body,null,bitmap);
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                        }
                                    });
                        }else {
                            sendNotification(remoteMessage,
                                    MyFirebaseMessaging.this,
                                    title,body,null,null);
                        }
                    }
                }
                else {
                    String title = remoteMessage.getData().get("title");
                    String body = remoteMessage.getData().get("message");
                    String url = remoteMessage.getData().get("image");
                    if (!TextUtils.isEmpty(url)){
                        final String finalUrl = url;
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.get()
                                                .load(finalUrl)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        sendNotification(remoteMessage,
                                                                MyFirebaseMessaging.this,
                                                                title,body,null,bitmap);
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                    }
                                                });
                                    }
                                });
                    }
                    else {
                        sendNotification(remoteMessage,
                                MyFirebaseMessaging.this,
                                title,body,null,null);
                    }

                }
            }

        }
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage,
                                       MyFirebaseMessaging context,
                                       String title,
                                       String body,
                                       Intent pendingIntentA,
                                       Bitmap bitmap) {
        /*RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = notification.getTitle();
        String body = notification.getBody();*/
       /* Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("message");
        String bitmap = data.get("image");*/
        Log.e("body", "" + body); // Prints the scan

        if (body.contains("<?>")) {
            StringTokenizer stringTokenizer = new StringTokenizer(body, "<?>");
            while (stringTokenizer.hasMoreElements()) {
                 message = stringTokenizer.nextElement().toString();
                 key = stringTokenizer.nextElement().toString().trim();
                 from = stringTokenizer.nextElement().toString().trim();
                tableNo = stringTokenizer.nextElement().toString().trim();
                Log.e("message", "" + message); // Prints the scan
                Log.e("key", "" + key); // Prints the scan
                Log.e("from", "" + from); // Prints the scan
                Log.e("tableNo", "" + tableNo); // Prints the scan
            }
        }
        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);
        // here we will click to notification > go to order list
        PendingIntent pendingIntent;
        NotificationHelper helper;
        Notification.Builder builder;

        /*if (Common.currentUser !=null) {*/
            if (from.equalsIgnoreCase("Chat")){
                Intent intent = new Intent(this, MessageChatActivity.class);
                intent.putExtra("from","Owner");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }else if (from.equalsIgnoreCase("ChatAdmin")){
                Intent intent = new Intent(this, MessageChatActivity.class);
                intent.putExtra("from" , "Customer");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }else if (from.equalsIgnoreCase("OrderBook")){
                Intent intent = new Intent(this, MyBookingsActivity.class);
                intent.putExtra("phoneNo",key);
                intent.putExtra("restId",tableNo);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }else if (from.equalsIgnoreCase("Push")){
                Intent intent = new Intent(this, OffersCouponsActivity.class);
                intent.putExtra("phoneNo" , key);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }else if (from.equalsIgnoreCase("Request")){
                Intent intent = new Intent(this, RestWelcomeActivity.class);
                intent.putExtra("isLogin" , "True");
                intent.putExtra("RestaurantMenuId" , key);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }else {
                Intent intent = new Intent(this, OrderStatus.class);
                intent.putExtra("OrderId" , key);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
             helper = new NotificationHelper(this);
            builder = helper.getAppChannelNotification(title, message,
                    pendingIntent, defaultSoundUri, bitmap);
        helper.getManager().notify(new Random().nextInt(), builder.build());
            // get random id for notification to show all notifications
       /*     helper.getManager().notify(new Random().nextInt(), builder.build());
        }else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            helper = new NotificationHelper(this);
            Bitmap bitmap1;
            builder = helper.getAppChannelNotification(title, message, defaultSoundUri,bitmap);

            helper.getManager().notify(new Random().nextInt(), builder.build());
        }*/

    }

    private void sendNotification(RemoteMessage remoteMessage,
                                  MyFirebaseMessaging context,
                                  String title,
                                  String body,
                                  Intent pendingIntentA,
                                  Bitmap bitmap) {

       /* RemoteMessage.Notification notification = remoteMessage.getNotification();*/
        /*Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("message");*/
        Log.e("body", "" + body); // Prints the scan

        if (body.contains("<?>")) {
            StringTokenizer stringTokenizer = new StringTokenizer(body, "<?>");
            while (stringTokenizer.hasMoreElements()) {
                message = stringTokenizer.nextElement().toString();
                key = stringTokenizer.nextElement().toString().trim();
                from = stringTokenizer.nextElement().toString().trim();
                tableNo = stringTokenizer.nextElement().toString().trim();
                Log.e("message", "" + message); // Prints the scan
                Log.e("key", "" + key); // Prints the scan
                Log.e("from", "" + from); // Prints the scan
                Log.e("tableNo", "" + tableNo); // Prints the scan
            }
        }
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setSummaryText(message);
        style.bigLargeIcon(null);
        style.bigPicture(bitmap);

        PendingIntent pendingIntent;
        if (from.equalsIgnoreCase("Chat")){
            Intent intent = new Intent(this, MessageChatActivity.class);
            intent.putExtra("from","Owner");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else if (from.equalsIgnoreCase("ChatAdmin")){
            Intent intent = new Intent(this, MessageChatActivity.class);
            intent.putExtra("from" , "Customer");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else if (from.equalsIgnoreCase("OrderBook")){
            Intent intent = new Intent(this, MyBookingsActivity.class);
            intent.putExtra("phoneNo" , key);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else if (from.equalsIgnoreCase("Push")){
            Intent intent = new Intent(this, OffersCouponsActivity.class);
            intent.putExtra("phoneNo" , key);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else {
            Intent intent = new Intent(this, OrderStatus.class);
            intent.putExtra("OrderId" , key);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder;
        if (bitmap != null){
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setSound(defaultSoundUri)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent)
                    .setStyle(style);
        }else {
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentText(message)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());


    }
}
