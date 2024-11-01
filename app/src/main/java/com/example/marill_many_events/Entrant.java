package com.example.marill_many_events;

public class Entrant {
    String status;
    User user;

    public Entrant(String status, User user) {
        // Initialize user profile
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
