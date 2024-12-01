package com.example.marill_many_events.models;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.marill_many_events.UserCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class RegistrationViewModel extends ViewModel implements UserCallback {
    private final FirebaseUsers firebaseUsers;

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEditModeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Uri> profilePictureUriLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> profilePictureUrlLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> registrationComplete = new MutableLiveData<>();

    public RegistrationViewModel(FirebaseFirestore firestore, FirebaseStorage firebaseStorage, String deviceId) {
        this.firebaseUsers = new FirebaseUsers(firestore, firebaseStorage, deviceId, this);
        isEditModeLiveData.setValue(false);
        profilePictureUriLiveData.setValue(null);
        profilePictureUrlLiveData.setValue(null);
        loadUserDetails();
    }

    // Observables
    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<Boolean> isEditMode() {
        return isEditModeLiveData;
    }

    public LiveData<Uri> getProfilePictureUri() {
        return profilePictureUriLiveData;
    }

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

    public void registerUser(String name, String email, String phone, Uri profilePictureUri) {
        firebaseUsers.registerUser(name, email, phone, profilePictureUri);
    }

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

    public void onPhotoSelected(Uri uri) {
        profilePictureUriLiveData.setValue(uri);
    }

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
