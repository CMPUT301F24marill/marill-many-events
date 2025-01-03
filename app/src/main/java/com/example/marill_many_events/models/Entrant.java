package com.example.marill_many_events.models;

import com.example.marill_many_events.models.User;
import com.google.firebase.firestore.DocumentReference;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

/**
 * Entrant represents an individual participating in events.
 * It holds the user's profile, status, geolocation, and a list of events the user is associated with.\
 */
public class Entrant {
    String status;
    Geolocation Geo;
    DocumentReference reference;
    //private String deviceId;

    private List<Event> Events = new ArrayList<>();
    User user;

    // No-argument constructor
    public Entrant() {
        // Initialize default values if necessary
        this.Geo = new Geolocation(null, null);
    }

    /**
     * Constructs an Entrant with a specified status and user profile.
     *
     * @param status The status of the entrant (e.g., registered, waitlisted).
     * @param user   The user profile associated with this entrant.
     */
    public Entrant(String status, User user) {
        // Initialize user profile
        this.status = status;
        this.user = user;
        this.Geo = new Geolocation(null, null);
    }

    /**
     * Set the location coordinates for the entrant
     *
     * @param x_cord x coordinate
     * @param y_cord y coordinate
     */
    public void setLocation(Float x_cord, Float y_cord){
        this.Geo.setX_cord(x_cord);
        this.Geo.setY_cord(y_cord);
    }

    /**
     * Retrieves the status of the entrant.
     *
     * @return The status of the entrant.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the entrant.
     *
     * @param status The new status of the entrant.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the user profile associated with this entrant.
     *
     * @return The user profile.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user profile for this entrant.
     *
     * @param user The new user profile.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the list of events associated with this entrant.
     *
     * @return A list of events.
     */
    public List<Event> getEvents() {
        return Events;
    }

    /**
     * Adds an event to the entrant's list of events.
     *
     * @param event The event to be added.
     */
    public void addEvents(Event event){
        this.Events.add(event);
    }
//
//    /**
//     * Retrieves the deviceId of the entrant.
//     *
//     * @return The deviceId of the entrant.
//     */
//    public String getDeviceId() {
//        return deviceId;
//    }
//
//    /**
//     * Sets the deviceId of the entrant.
//     *
//     * @param deviceId The new deviceId of the entrant.
//     */
//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }
    /**
     * Gets the Firestore document reference associated with this object.
     * The document reference is used to uniquely identify and interact with
     * the Firestore document related to this object.
     *
     * @return The Firestore document reference.
     */
    public DocumentReference getReference() {
        return reference;
    }
    /**
     * Sets the Firestore document reference for this object.
     * This method allows associating the object with a specific Firestore document.
     *
     * @param reference The Firestore document reference to associate with this object.
     */
    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }
}