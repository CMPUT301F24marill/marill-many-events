package com.example.marill_many_events.models;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.marill_many_events.UserCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
/**
 * ViewModel for managing user registration and profile updates.
 * Communicates with Firebase services to load, register, and update user details.
 */

public class RegistrationViewModel extends ViewModel implements UserCallback {
    private final FirebaseUsers firebaseUsers;

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEditModeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Uri> profilePictureUriLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> profilePictureUrlLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> registrationComplete = new MutableLiveData<>();
    /**
     * Constructor for initializing Firebase services and loading user details.
     *
     * @param firestore     Instance of FirebaseFirestore for database operations.
     * @param firebaseStorage Instance of FirebaseStorage for file storage operations.
     * @param deviceId      Unique device ID or identifier for the user.
     */
    public RegistrationViewModel(FirebaseFirestore firestore, FirebaseStorage firebaseStorage, String deviceId) {
        this.firebaseUsers = new FirebaseUsers(firestore, firebaseStorage, deviceId, this);
        isEditModeLiveData.setValue(false);
        profilePictureUriLiveData.setValue(null);
        profilePictureUrlLiveData.setValue(null);
        loadUserDetails();
    }
    /**
     * Provides LiveData for the current user.
     *
     * @return LiveData object containing the user details.
     */
    // Observables
    public LiveData<User> getUser() {
        return userLiveData;
    }
    /**
     * Provides LiveData for the edit mode status.
     *
     * @return LiveData object containing the edit mode status (true or false).
     */
    public LiveData<Boolean> isEditMode() {
        return isEditModeLiveData;
    }
    /**
     * Provides LiveData for the selected profile picture URI.
     *
     * @return LiveData object containing the URI of the selected profile picture.
     */
    public LiveData<Uri> getProfilePictureUri() {
        return profilePictureUriLiveData;
    }
    /**
     * Provides LiveData for the profile picture URL.
     *
     * @return LiveData object containing the URL of the profile picture.
     */
    public LiveData<String> getProfilePictureUrl() {
        return profilePictureUrlLiveData;
    }

    public LiveData<String> getMessage() {
        return messageLiveData;
    }

    // Public API for Fragment
    public void loadUserDetails() {
        firebaseUsers.loadUserDetails();
    }
    /**
     * Registers a new user with the provided details.
     *
     * @param name             User's name.
     * @param email            User's email address.
     * @param phone            User's phone number.
     * @param profilePictureUri URI of the profile picture.
     */
    public void registerUser(String name, String email, String phone, Uri profilePictureUri) {
        firebaseUsers.registerUser(name, email, phone, profilePictureUri);
    }
    /**
     * Updates an existing user's details.
     *
     * @param name             User's name.
     * @param email            User's email address.
     * @param phone            User's phone number.
     * @param profilePictureUri URI of the profile picture.
     */

    public void updateUser(String name, String email, String phone, Uri profilePictureUri) {
        firebaseUsers.updateUser(name, email, phone, profilePictureUri, false);
    }

    public LiveData<Boolean> isRegistrationComplete() {
        return registrationComplete;
    }

    public void completeRegistration() {
        registrationComplete.setValue(true);
    }

    public void onPhotoDeleted() {
        firebaseUsers.deleteProfilePicture();
        profilePictureUriLiveData.setValue(null);
        profilePictureUrlLiveData.setValue(null);
    }
    /**
     * Sets the selected profile picture URI.
     *
     * @param uri The URI of the selected profile picture.
     */
    public void onPhotoSelected(Uri uri) {
        profilePictureUriLiveData.setValue(uri);
    }
    /**
     * Handles the callback when user details are successfully loaded.
     *
     * @param returnedUser The loaded user details.
     */
    // Callbacks
    @Override
    public void onUserloaded(User returnedUser) {
        if (returnedUser != null) {
            userLiveData.setValue(returnedUser);
            isEditModeLiveData.setValue(true);
            profilePictureUrlLiveData.setValue(returnedUser.getProfilePictureUrl());
        } else {
            messageLiveData.setValue("User not found. You can register.");
        }
    }

    @Override
    public void onUserUpdated() {
        messageLiveData.setValue("User updated successfully.");
        loadUserDetails();
    }

    @Override
    public void onRegistered() {
        messageLiveData.setValue("Registration successful.");
        completeRegistration();
    }
}
