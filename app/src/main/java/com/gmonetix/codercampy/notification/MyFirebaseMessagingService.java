package com.gmonetix.codercampy.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.util.CoderCampy;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gaurav Bordoloi on 3/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final static String CHANNEL_ID = "codercampy_channel_01";

    private Bitmap bitmap;
    private Bitmap icon;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);

        if (remoteMessage.getData().size() > 0) {

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");

            String image = "";
            if (remoteMessage.getData().containsKey("image"))
                image = remoteMessage.getData().get("image");

            switch (remoteMessage.getFrom().replace("/topics/", "")) {
                case CoderCampy.TOPIC_COURSE:

                    sendNotification(title, message, image);

                    break;
                case CoderCampy.TOPIC_POST:

                    sendNotification(title, message, image);

                    break;
                case CoderCampy.TOPIC_COMPULSORY:

                    sendNotification(title, message, image);

                    break;
            }

        } else {
            Log.e("TAG", "No Notification Data");
        }

    }

    private void sendNotification(String title, String message, String image) {

        if (!image.isEmpty()) {
            bitmap = getBitmapFromUrl(image);
        }

        NotificationCompat.Builder noti = new NotificationCompat.Builder(this);

        noti.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setColor(Color.RED);

        if (bitmap != null) {
            noti.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            noti.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CoderCampy Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, noti.build());

    }

    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "" + e.getMessage());
            return null;
        }
    }

}
