package com.example.marill_many_events.models;

import android.net.Uri;

import com.example.marill_many_events.UserCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


/**
 * Handles firebase functions for user class
 */
public class FirebaseUsers {

    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String deviceId;
    private String profilePictureUrl;
    private boolean isEditMode;
    private UserCallback callback;

    public FirebaseUsers(FirebaseFirestore firestore, FirebaseStorage firebaseStorage, String deviceId, UserCallback callback) {
        this.firestore = firestore;
        this.storageReference = firebaseStorage.getReference();
        this.deviceId = deviceId;
        this.isEditMode = false;
        this.callback = callback;
    }

    /**
     * Loads user details from Firestore if in edit mode.
     */
    public void loadUserDetails() {
        firestore.collection("users").document(deviceId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            isEditMode = true;
                            User user = document.toObject(User.class);
                            callback.onUserloaded(user); // Invoke the interface once user resource is ready
                        }
                    }
                });
    }

    /**
     * Updates an existing user's details in Firestore and uploads a new profile picture if provided.
     *
     * @param name              The name of the user.
     * @param email             The email of the user.
     * @param phone             The phone number of the user.
     * @param profilePictureUri The URI of the new profile picture.
     * @param Admin              The admin status of the user.
     */
    public void updateUser(String name, String email,String phone, Uri profilePictureUri, boolean Admin) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("email", email);
        userUpdates.put("phone", phone);
        userUpdates.put("admin", Admin);

        firestore.collection("users").document(deviceId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    callback.onUserUpdated();
                    //Toast.makeText(getActivity(), "User details updated successfully!", Toast.LENGTH_SHORT).show();
                    uploadProfilePicture(profilePictureUri);
                });
                //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update user details.", Toast.LENGTH_SHORT).show());
    }


    /**
     * Registers a new user in Firestore and uploads a profile picture to Firebase Storage if provided.
     *
     * @param name              The name of the user.
     * @param email             The email of the user.
     * @param phone             The phone number of the user.
     * @param profilePictureUri The URI of the profile picture.
     */
    public void registerUser(String name, String email, String phone, Uri profilePictureUri) {
        User user = new User(name, email, phone, null, null); // Register without profile picture
        firestore.collection("users").document(deviceId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    uploadProfilePicture(profilePictureUri);
                    callback.onRegistered();
                });
                //.addOnFailureListener(e -> Toast.makeText(,"Failed to register user.", Toast.LENGTH_SHORT).show());
    }


    /**
     * Deletes the current profile picture from Firebase Storage and removes the download link reference from Firestore.
     */
    public void deleteProfilePicture() {
        // Delete profile picture from Firebase Storage
        StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");
        fileReference.delete()
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(getActivity(), "Profile picture deleted!", Toast.LENGTH_SHORT).show();

                    // Remove profile picture URL from Firestore
                    firestore.collection("users").document(deviceId)
                            .update("profilePictureUrl", null)  // Set URL to null
                            .addOnSuccessListener(aVoid2 -> {
                                //Toast.makeText(getActivity(), "Profile picture reference removed from Firestore.", Toast.LENGTH_SHORT).show();
                                //profilePictureUrl = null;
                                //loadProfilewithGlide(null,null);
                                callback.onUserUpdated();
                            });
                    //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to remove picture reference from Firestore.", Toast.LENGTH_SHORT).show());
                });
        //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete profile picture.", Toast.LENGTH_SHORT).show());
    }


    /**
     * Uploads the profile picture to Firebase Storage and updates the Firestore profile picture URL.
     *
     * @param profilePictureUri The URI of the profile picture to upload.
     */
    private void uploadProfilePicture(Uri profilePictureUri) {
        if (profilePictureUri != null) {
            StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");
            fileReference.putFile(profilePictureUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateProfilePictureUrl(uri.toString());
                        profilePictureUrl = uri.toString();
                        loadUserDetails();
                    }));
                    //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show());
        }
        loadUserDetails(); // Reload the newly saved user
    }

    /**
     * Updates the profile picture URL in Firestore.
     *
     * @param profilePictureUrl The new profile picture URL.
     */
    public void updateProfilePictureUrl(String profilePictureUrl) {
        firestore.collection("users").document(deviceId)
                .update("profilePictureUrl", profilePictureUrl);
        //.addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Profile picture updated!", Toast.LENGTH_SHORT).show())
        //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update profile picture.", Toast.LENGTH_SHORT).show());
    }

    /**
     * Deletes the user record from Firestore.
     */
    public void deleteUser(){
        firestore.collection("users").document(deviceId).delete();
    }

}



