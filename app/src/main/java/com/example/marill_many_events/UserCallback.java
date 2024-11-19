package com.example.marill_many_events;

import com.example.marill_many_events.models.User;

/**
 * UserCallback is an interface for handling user-related events and callbacks,
 * such as loading, updating, and registering a user.
 */
public interface UserCallback {

    /**
     * Called when a user is successfully loaded.
     *
     * @param user The {@link User} object that has been loaded.
     */
    void onUserloaded(User user);
    /**
     * Called when a user is successfully updated.
     */
    void onUserUpdated();
    /**
     * Called when a user is successfully registered.
     */
    void onRegistered();
}
