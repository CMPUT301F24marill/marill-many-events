package com.example.marill_many_events;

import com.example.marill_many_events.models.Event;
/**
 * Interface for handling callback events related to Firebase operations on events.
 */
public interface EventsCallback {
    /**
     * Called when an event is successfully created in Firebase.
     *
     * @param documentID The document ID of the newly created event.
     */
    void onEventCreate(String documentID);
    /**
     * Called when event details are retrieved from Firebase.
     *
     * @param event The retrieved event object.
     */
    void getEvent(Event event);
    /**
     * Called when the event poster URL is successfully uploaded to Firebase Storage.
     *
     * @param url The download URL of the uploaded poster.
     */
    void onPosterUpload(String url);

}
