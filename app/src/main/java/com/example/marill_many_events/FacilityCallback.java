package com.example.marill_many_events;

import com.example.marill_many_events.models.Facility;

public interface FacilityCallback {
    void onFacilityLoaded(Facility facility);
    void onFacilityUpdated();
    void onFacilityRegistered();
}
