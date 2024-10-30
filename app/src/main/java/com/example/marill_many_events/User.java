package com.example.marill_many_events;

public class User {
    public String name;
    public String email;
    public String phone;
    public String profilePictureUrl;

    public User() {
        // Default constructor
    }

    public User(String name, String email, String phone, String profilePictureUrl) {
        // Initialize user profile
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
    }
}
