package com.example.marill_many_events.models;

/**
 * User represents a user profile with basic details such as name, email, phone number,
 * and a URL for the profile picture.
 */
public class User {
    public String name;
    public String email;
    public String phone;
    public String profilePictureUrl;

    /**
     * Default constructor for User.
     */
    public User() {
        // Default constructor
    }

    /**
     * Constructs a User with specified details.
     *
     * @param name              The name of the user.
     * @param email             The email address of the user.
     * @param phone             The phone number of the user.
     * @param profilePictureUrl The URL of the user's profile picture.
     */
    public User(String name, String email, String phone, String profilePictureUrl) {
        // Initialize user profile
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
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
}
