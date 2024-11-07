package com.example.marill_many_events.models;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFacilityRegistration {

    private FirebaseFirestore firestore;
    private String facilityId;
    private FacilityCallback callback;

    public FirebaseFacilityRegistration(FirebaseFirestore firestore, String facilityId, FacilityCallback callback) {
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
                .set(facility);
    }



}
