package com.example.marill_many_events.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.UserCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;
import java.util.UUID;

public class FirebaseEvents {
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private String deviceId;
    private EventsCallback eventsCallback;

    public FirebaseEvents(FirebaseFirestore firestore, StorageReference storageReference, String deviceId, EventsCallback eventsCallback) {
        this.firestore = firestore;
        this.storageReference = storageReference;
        this.deviceId = deviceId;
        this.eventsCallback = eventsCallback;
    }

    /**
     * Loads Event details from Firestore.
     */
    public void getEventDetails(String documentID) {
        firestore.collection("events").document(documentID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = document.toObject(Event.class);
                            eventsCallback.getEvent(event); // Invoke the interface once user resource is ready
                        }
                    }
                });
    }

    /**
     * Create an event.
     */
    public void createEvent(Event event) {
        firestore.collection("events") // "events" is the name of your collection
                .add(event) // This automatically generates a document ID
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Event added with ID: " + documentReference.getId());
                    eventsCallback.onEventCreate(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding event", e);
                });
    }


    /**
     * Registers a new user and uploads the profile picture to Firebase Storage if provided.
     */

//    public void registerUser(String name, String email, String phone, Uri profilePictureUri) {
//        User user = new User(name, email, phone, null); // Register without profile picture
//        firestore.collection("users").document(deviceId)
//                .set(user)
//                .addOnSuccessListener(aVoid -> {
//                    uploadProfilePicture(profilePictureUri);
//                    callback.onRegistered();
//                });
//        //.addOnFailureListener(e -> Toast.makeText(,"Failed to register user.", Toast.LENGTH_SHORT).show());
//    }


    /**
     * Deletes the current profile picture from Firebase Storage and removes the download link reference from Firestore.
     */
//    public void deleteProfilePicture() {
//        // Delete profile picture from Firebase Storage
//        StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");
//        fileReference.delete()
//                .addOnSuccessListener(aVoid -> {
//                    //Toast.makeText(getActivity(), "Profile picture deleted!", Toast.LENGTH_SHORT).show();
//
//                    // Remove profile picture URL from Firestore
//                    firestore.collection("users").document(deviceId)
//                            .update("profilePictureUrl", null)  // Set URL to null
//                            .addOnSuccessListener(aVoid2 -> {
//                                //Toast.makeText(getActivity(), "Profile picture reference removed from Firestore.", Toast.LENGTH_SHORT).show();
//                                //profilePictureUrl = null;
//                                //loadProfilewithGlide(null,null);
//                                callback.onUserUpdated();
//                            });
//                    //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to remove picture reference from Firestore.", Toast.LENGTH_SHORT).show());
//                });
//        //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete profile picture.", Toast.LENGTH_SHORT).show());
//    }


    /**
     * Upload poster picture to firebase storage and get the download url.
     */
    public void uploadPoster(Uri profilePictureUri) {
        String filename = generateRandomFilename();
        if (profilePictureUri != null) {
            StorageReference fileReference = storageReference.child("eventposters/" + filename + ".jpg");
            fileReference.putFile(profilePictureUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        //updateProfilePictureUrl(uri.toString());
                        String posterurl = uri.toString();
                        eventsCallback.onPosterUpload(posterurl);
                    }));
            //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Upload profile picture URL to firestore under a user's profilepictureurl field.
     */
//    public void updateProfilePictureUrl(String profilePictureUrl) {
//        firestore.collection("users").document(deviceId)
//                .update("profilePictureUrl", profilePictureUrl);
//        //.addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Profile picture updated!", Toast.LENGTH_SHORT).show())
//        //.addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update profile picture.", Toast.LENGTH_SHORT).show());
//    }


    public void deleteUser(){
        firestore.collection("users").document(deviceId).delete();
    }

    public String generateRandomFilename() {    // https://stackoverflow.com/questions/5126559/android-create-unique-string-for-file-name
        long timestamp = System.currentTimeMillis();
        return "image_" + timestamp + "_" + UUID.randomUUID().toString() + ".jpg"; // Add your desired extension
    }


}