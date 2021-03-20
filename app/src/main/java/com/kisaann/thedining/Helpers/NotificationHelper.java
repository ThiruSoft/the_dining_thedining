package com.kisaann.thedining.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kisaann.thedining.R;


/**
 * Created by HOME on 8/10/2018.
 */

public class NotificationHelper extends ContextWrapper {
    private static final String FODDY_MAIL_ID = "com.kisaann.thedining";
    private static final String FODDY_MAIL_NAME = "Dining ";

    private NotificationManager manager;
    public NotificationHelper(Context base) {
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
    public Notification.Builder getAppChannelNotification(String title, String body,
                                                          PendingIntent pendingIntent,
                                                          Uri soundUri,
                                                          Bitmap bitmap)
    {
        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);

        if (bitmap != null){
            return new Notification.Builder(getApplicationContext(), FODDY_MAIL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setStyle(style);
        }else {
            return new Notification.Builder(getApplicationContext(), FODDY_MAIL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(soundUri)
                    .setAutoCancel(true);
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getAppChannelNotification(String title, String body,
                                                          Uri soundUri,
                                                          Bitmap bitmap)
    {
        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);
        /*NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null);*/
        if (bitmap != null){
            return new Notification.Builder(getApplicationContext(), FODDY_MAIL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setStyle(style);
        }else {
            return new Notification.Builder(getApplicationContext(), FODDY_MAIL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(soundUri)
                    .setAutoCancel(true);
        }


    }
}
