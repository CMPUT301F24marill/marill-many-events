package com.example.marill_many_events;

import com.example.marill_many_events.models.Facility;
/**
 * Interface for handling callback events related to Firebase operations on facilities.
 */
public interface FacilityCallback {
    /**
     * Called when facility details are successfully loaded from Firebase.
     *
     * @param facility The loaded facility object. If the facility does not exist, this will be null.
     */
    void onFacilityLoaded(Facility facility);
    /**
     * Called when facility details are successfully updated in Firebase.
     */
    void onFacilityUpdated();
    /**
     * Called when facility details are successfully updated in Firebase.
     */
    void onFacilityRegistered();
}
