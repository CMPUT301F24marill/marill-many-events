package com.example.marill_many_events;

import com.example.marill_many_events.models.User;

public interface UserCallback {
    void onUserloaded(User user);
    void onUserUpdated();
    void onRegistered();
}
