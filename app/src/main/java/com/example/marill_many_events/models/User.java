package com.example.marill_many_events.models;

public class User {
    public String name;
    public String email;
    public String phone;
    public String profilePictureUrl;
    public boolean hasFacility;

    public User() {
        // Default constructor
    }

    public User(String name, String email, String phone, String profilePictureUrl) {
        // Initialize user profile
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
        this.hasFacility = false;  // initialize user status with normal entrant
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean getFacilityStatus() { return  hasFacility; }

    public void setFacilityStatus(boolean status) { this.hasFacility = status; }
}
