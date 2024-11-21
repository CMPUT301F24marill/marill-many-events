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
    private ArrayList<String> events;
    private ArrayList<DocumentReference> waitList;
    private boolean isOrganizer;

    /**
     * Default constructor for User.
     */
    public User() {
        // Default constructor
    }

    public User(String name, String email, String phone, String profilePictureUrl, ArrayList<DocumentReference> waitList) {
        // Initialize user profile
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
        this.waitList = waitList;
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

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }


    public ArrayList<String> getEvents() {
        return events;
    }

    public void addEvent(String event) {
        this.events.add(event);
    }

    public ArrayList<DocumentReference> getwaitList() {
        return waitList;
    }

    public void addToWaitList(DocumentReference event) {
        this.waitList.add(event);
    }

}
