package com.example.marill_many_events.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

/**
 * User represents a user profile with basic details such as name, email, phone number,
 * and a URL for the profile picture.
 */
public class User {
    private String name;
    private String email;
    private String phone;
    private String profilePictureUrl;
    private ArrayList<DocumentReference> events;
    private ArrayList<DocumentReference> waitList;
    private ArrayList<DocumentReference> pending;
    private boolean isOrganizer;
    public boolean admin;
    public boolean allownotifications;
    public String id;

    /**
     * Default constructor for User.
     */
    public User() {
        // Default constructor
    }
    /**
     * Constructs a new User instance with specified details.
     *
     * @param name              User's name.
     * @param email             User's email address.
     * @param phone             User's phone number.
     * @param profilePictureUrl URL of the user's profile picture.
     * @param waitList          List of events the user is waitlisted for.
     */
    public User(String name, String email, String phone, String profilePictureUrl, ArrayList<DocumentReference> waitList) {
        // Initialize user profile
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
        this.pending = pending;
        this.events = events;
        this.allownotifications = allownotifications;
        this.waitList = waitList;
        this.admin = false;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The new name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves whether user is an admin.
     *
     * @return The admin status of the user.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the admin status of the user.
     *
     * @param admin The admin status of the user.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phone The new phone number of the user.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retrieves the profile picture URL of the user.
     *
     * @return The profile picture URL of the user.
     */
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    /**
     * Sets the profile picture URL of the user.
     *
     * @param profilePictureUrl The new profile picture URL of the user.
     */
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  
    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }


    public ArrayList<DocumentReference> getEvents() {
        return events;
    }

    public void addEvent(DocumentReference event) {
        this.events.add(event);
    }

    public ArrayList<DocumentReference> getwaitList() {
        return waitList;
    }

    public void addToWaitList(DocumentReference event) {
        this.waitList.add(event);
    }

    public void setWaitList(ArrayList<DocumentReference> waitList) {
        this.waitList = waitList;
    }

    public void setEvents(ArrayList<DocumentReference> events) {
        this.events = events;
    }

    public ArrayList<DocumentReference> getPending() {
        return pending;
    }

    public void setPending(ArrayList<DocumentReference> pending) {
        this.pending = pending;
    }

    public boolean Allownotifications() {
        return allownotifications;
    }

    public void setAllownotifications(boolean allownotifications) {
        this.allownotifications = allownotifications;
    }
}
