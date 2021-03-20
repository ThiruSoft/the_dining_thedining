package com.kisaann.thedining.Service;

import android.annotation.TargetApi;
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
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kisaann.thedining.Config.Config;
import com.kisaann.thedining.Helpers.PushNotificationHelper;
import com.kisaann.thedining.OffersCouponsActivity;
import com.kisaann.thedining.OrderStatus;
import com.kisaann.thedining.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MyPushFirebaseMessaging extends FirebaseMessagingService {

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                showNotificationWithImageAPI26(bitmap);
            else
                showNotificationWithImage(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @TargetApi(Build.VERSION_CODES.O)
    private void showNotificationWithImageAPI26(Bitmap bitmap) {
        PushNotificationHelper helper = new PushNotificationHelper(this);
        Notification.Builder builder = helper.getAppChannelNotification(Config.title,Config.message,bitmap);
        helper.getManager().notify(0,builder.build());
    }

    private void showNotificationWithImage(Bitmap bitmap) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setSummaryText(Config.message);
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), OffersCouponsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .setStyle(style);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0,notificationBuilder.build());


    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendNotificationAPI26(remoteMessage);
            else
                sendNotificationImage(remoteMessage);
        }
    }

    private void sendNotificationImage(RemoteMessage remoteMessage) {


        Config.title = remoteMessage.getNotification().getTitle();
        Config.message = remoteMessage.getNotification().getBody();

        if (remoteMessage.getData() != null) {
            Config.imageLink = remoteMessage.getData().get("image");
            Log.e("imageLink", "" + Config.imageLink); // Prints the scan
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get().load(Config.imageLink)
                            .into(target);
                }
            });
        }
        Intent intent = new Intent(getApplicationContext(), OrderStatus.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Config.title)
                .setContentText(Config.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

    }


}
