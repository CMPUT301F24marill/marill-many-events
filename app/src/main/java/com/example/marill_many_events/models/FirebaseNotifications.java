package com.example.marill_many_events.models;

import static androidx.fragment.app.FragmentManager.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.marill_many_events.NotificationCallback;
import com.example.marill_many_events.activities.HomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appcheck.internal.util.Logger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseNotifications {
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String deviceId;
    private NotificationCallback notificationCallback;

    public FirebaseNotifications(FirebaseFirestore firestore, StorageReference storageReference, String deviceId, NotificationCallback notificationCallback) {
        this.firestore = firestore;
        this.storageReference = storageReference;
        this.deviceId = deviceId;
        this.notificationCallback = notificationCallback;
    }

    /**
     * Loads Notification details from Firestore.
     */
    public void getNotificationDetails(String documentID) {
        firestore.collection("notifications").document(documentID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Notification notification = document.toObject(Notification.class);
                            notificationCallback.getNotification(notification); // Invoke the interface once user resource is ready
                        }
                    }
                });
    }

    /**
     * Create a notification
     */
    public void createNotification(String sentTo,String title, String content) {
        Notification notification = new Notification(sentTo, title, content);
//        firestore.collection("notifications").document(sentTo)
//                .set(notification).addOnSuccessListener(v->{
//                    notificationCallback.notificationCreated();
//                });

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("sentTo", sentTo);
        notificationData.put("title", title);
        notificationData.put("content", content);

        firestore.collection("notifications")
                .add(notificationData)
                .addOnSuccessListener(aVoid -> {
                    //add success metric
                }).addOnFailureListener(e -> Log.e(Logger.TAG, "Error creating notification", e));

        //

    }

    /**
     * Delete a notification, called after viewing a notification
     * @param notificationId the documentID of the notification to be deleted
     */
    public void deleteNotification(String notificationId){
        firestore.collection("notification").document(notificationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // success metric
                })
                .addOnFailureListener(e -> Log.e(Logger.TAG, "Error deleting notification", e));
    }

    /**
     * Query notification for specific user
     */
    public void getUsersNotifications(String sentTo){
        firestore.collection("notifications").whereEqualTo("sentToId", sentTo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Notification> notifications = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Notification notification = document.toObject(Notification.class);
                            notifications.add(notification);
                        }
                        notificationCallback.notificationsRecieved(notifications);
                    } else {
                        notificationCallback.onError(task.getException());
                    }
                });
    }
}



