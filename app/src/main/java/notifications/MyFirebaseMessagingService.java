package notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//package notifications;
//package com.tabian.firebasepushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServic";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Log.d(TAG, "onMessageReceived: Message Received: \n" +
                "Title: " + title + "\n" +
                "Message: " + message);

        sendNotification(title, message);
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the NotificationChannel if on Android 8.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }




    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Send the new token to your backend for registration
        sendRegistrationToServer(token);
    }

    private void handleDataMessage(Map<String, String> data) {
        // Process data payload
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        // Display notification
    }

    private void sendRegistrationToServer(String token) {
        // Implement this method to send token to your app server.
    }

}

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        // Handle FCM messages here.
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            // Handle data payload
//            handleDataMessage(remoteMessage.getData());
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            // Handle notification payload
//            handleNotification(remoteMessage.getNotification());
//        }
//    }




