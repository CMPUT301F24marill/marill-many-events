package com.example.marill_many_events.models;

import android.util.Log;

import com.example.marill_many_events.NotificationCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FirebaseNotifs {
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String deviceId;
    private NotificationCallback notificationCallback;

    public FirebaseNotifs(FirebaseFirestore firestore, StorageReference storageReference, String deviceId, NotificationCallback notificationCallback) {
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
    public void createNotification(String sentTo, String content) {
        Notification notification = new Notification(sentTo, content);
        firestore.collection("notifications").document(sentTo)
                .set(notification).addOnSuccessListener(v->{
                    notificationCallback.notificationCreated();
                });
    }

    /**
     * Delete a notification
     */
    public void deleteNotification(){

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
                            notifications.add(notification); } notificationCallback.notificationsRecieved(notifications);
                    } else {
                        notificationCallback.onError(task.getException());
                    }
                });
    }
}



