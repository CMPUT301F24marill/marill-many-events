package com.example.marill_many_events.models;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class FirebaseFacilityRegistration {

    private FirebaseFirestore firestore;
    private String facilityId;
    private FacilityCallback callback;

    public FirebaseFacilityRegistration(FirebaseFirestore firestore,  String facilityId, FacilityCallback callback) {
        this.firestore = firestore;
        this.facilityId = facilityId;
        this.callback = callback;
    }

    /**
     * Register a new facility.
     */
    public void registerFacility(String Name, String location) {
        Facility facility = new Facility(Name, location);
        firestore.collection("facilities").document(facilityId)
                .set(facility).addOnSuccessListener(v->{
                    callback.onFacilityRegistered();
                });
    }

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
