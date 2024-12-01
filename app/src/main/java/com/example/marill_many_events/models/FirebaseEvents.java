package com.example.marill_many_events.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.UserCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * Manage firebase operations for events
 *
 */
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
                    updateQR(documentReference.getId());

                    addEventToFacility(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding event", e);
                });
    }


    /**
     * Registers a new user and uploads the profile picture to Firebase Storage if provided.
     */

    public void updateQR(String documentID) {
        firestore.collection("events") // "events" is the name of your collection
                .document(documentID) // This automatically generates a document ID
                .update("qrcode", documentID)
                .addOnSuccessListener(documentReference -> {
                    eventsCallback.onEventCreate(documentID);
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding event", e);
                });
    }

    /**
     * Adds the event ID to the facility's event list.
     *
     * @param eventId The ID of the newly created event.
     */
    private void addEventToFacility(String eventId) {
        firestore.collection("facilities") // "facilities" is the name of your facilities collection
                .document(deviceId) // Replace facilityId with the current facility's document ID
                .update("events", FieldValue.arrayUnion(eventId)) // Add the eventId to the eventList array
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Event added to facility's event list successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error updating facility's event list", e);
                });
    }

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
    /**
     * Updates the geolocation fields for an existing event in Firestore.
     *
     * @param eventId   The ID of the event to update.
     * @param latitude  The latitude of the event location.
     * @param longitude The longitude of the event location.
     */
    public void updateEventGeolocation(String eventId, double latitude, double longitude) {
        Map<String, Object> geolocationData = new HashMap<>();
        geolocationData.put("latitude", latitude);
        geolocationData.put("longitude", longitude);

        firestore.collection("events")
                .document(eventId)
                .update(geolocationData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Geolocation updated successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating geolocation", e));
    }

    /**
     * Deletes a user document from Firestore.
     */
    public void deleteUser(){
        firestore.collection("users").document(deviceId).delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User deleted successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting user", e));
    }

    /**
     * Generates a random filename for uploaded files.
     *
     * @return A unique filename.
     */
    public String generateRandomFilename() {    // https://stackoverflow.com/questions/5126559/android-create-unique-string-for-file-name
        long timestamp = System.currentTimeMillis();
        return "image_" + timestamp + "_" + UUID.randomUUID().toString() + ".jpg"; // Add your desired extension
    }


}
