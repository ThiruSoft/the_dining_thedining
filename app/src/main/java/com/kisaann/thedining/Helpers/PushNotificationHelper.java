package com.kisaann.thedining.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.kisaann.thedining.Config.Config;
import com.kisaann.thedining.OffersCouponsActivity;
import com.kisaann.thedining.R;

public class PushNotificationHelper extends ContextWrapper {
    private static final String FODDY_MAIL_ID = "com.kisaann.thedining";
    private static final String FODDY_MAIL_NAME = "Dining ";

    private NotificationManager manager;
    public PushNotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // only working this fun id APi is 26 or hiher
            createChannel();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(FODDY_MAIL_ID, FODDY_MAIL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);

    }
    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getAppChannelNotification(String title, String body, Uri soundUri,Bitmap bitmap)
    {
        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

       return new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setStyle(style);

    }
    // iamge load
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getAppChannelNotification(String title, String body, Bitmap bitmap)
    {
        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);

        Intent intent = new Intent(getApplicationContext(), OffersCouponsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new Notification.Builder(getApplicationContext(),FODDY_MAIL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .setStyle(style);
    }
}
