package com.example.marill_many_events;

import com.example.marill_many_events.models.FirebaseNotifs;
import com.example.marill_many_events.models.Notification;

import java.util.List;

public interface NotificationCallback {
    void getNotification(Notification notification);
    void notificationCreated();
    void notificationsRecieved(List<Notification> notifications);
    void onError(Exception e);

}
