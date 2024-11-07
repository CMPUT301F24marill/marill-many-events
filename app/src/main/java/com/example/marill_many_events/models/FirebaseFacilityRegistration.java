package com.example.marill_many_events.models;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFacilityRegistration {

    private FirebaseFirestore firestore;
    private String facilityId;

    public FirebaseFacilityRegistration(FirebaseFirestore firestore, String facilityId) {
        this.firestore = firestore;
        this.facilityId = facilityId;
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
