package com.example.marill_many_events.models;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
/**
 * Handles Firebase Firestore operations related to facility registration and management.
 */
public class FirebaseFacilityRegistration {

    private FirebaseFirestore firestore;
    private String facilityId;
    private FacilityCallback callback;
    /**
     * Constructor for FirebaseFacilityRegistration.
     *
     * @param firestore  The Firestore instance to use for database operations.
     * @param facilityId The unique ID of the facility being managed.
     * @param callback   The callback interface for facility-related operations.
     */
    public FirebaseFacilityRegistration(FirebaseFirestore firestore,  String facilityId, FacilityCallback callback) {
        this.firestore = firestore;
        this.facilityId = facilityId;
        this.callback = callback;
    }

    /**
     * Registers a new facility in Firestore.
     *
     * @param Name     The name of the facility.
     * @param location The location of the facility.
     */
    public void registerFacility(String Name, String location) {
        Facility facility = new Facility(Name, location);
        firestore.collection("facilities").document(facilityId)
                .set(facility).addOnSuccessListener(v->{
                    callback.onFacilityRegistered();
                });
    }
    /**
     * Updates the facility's details in Firestore.
     *
     * @param name     The new name of the facility.
     * @param location The new location of the facility.
     */
    public void updateFacility(String name, String location) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("facilityName", name);
        userUpdates.put("location", location);

        firestore.collection("facilities").document(facilityId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    callback.onFacilityUpdated();
                    //Toast.makeText(getActivity(), "facility details updated successfully!", Toast.LENGTH_SHORT).show();
                });
    }
    /**
     * Loads the details of a facility from Firestore.
     * If the facility exists, its details are passed to the callback.
     * If the facility does not exist, the callback receives a null value.
     */
    public void loadFacilityDetails() {
        firestore.collection("facilities")
                .document(facilityId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the facility object from the document
                        Facility facility = documentSnapshot.toObject(Facility.class);

                        // Pass the facility object to the callback
                        callback.onFacilityLoaded(facility);
                    } else {
                        // Handle the case where the document doesn't exist
                        callback.onFacilityLoaded(null);
                    }
                });
    }



}
