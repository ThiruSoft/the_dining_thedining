package com.kisaann.thedining.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kisaann.thedining.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Random;

public class MyFirebaseServiceNew extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Token",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
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
                                                    showNotification(MyFirebaseServiceNew.this,
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
                    showNotification(MyFirebaseServiceNew.this,title,body,null,null);
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
                                                    showNotification(MyFirebaseServiceNew.this,
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
                    showNotification(MyFirebaseServiceNew.this,title,body,null,null);
                }

        }
    }

    private void showNotification(MyFirebaseServiceNew context,
                                  String title,
                                  String body,
                                  Intent pendingIntent,
                                  Bitmap bitmap) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setSummaryText(body);
        style.bigPicture(bitmap);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        String chanelId = "DiningApp";
        String channelName = "Dining";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(chanelId,channelName,notificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder;
        if (bitmap != null){
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText(title)
                    .setStyle(style);
        }else {
            builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText(title)
                    .setContentText(body);
        }
        if (pendingIntent != null){
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(pendingIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
        }
        notificationManager.notify(notificationId,builder.build());
    }
}
