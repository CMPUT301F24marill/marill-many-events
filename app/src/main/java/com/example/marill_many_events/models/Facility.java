package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.List;

public class Facility{
    private List<Event> Events;

    String facilityName;
    String Location;

    String id;

    // No-argument constructor (needed by Firestore)
    public Facility() {
        // Firebase needs a no-argument constructor
    }

    public Facility(String facilityName, String location){
        this.Events = new ArrayList<>();
        this.facilityName = facilityName;
        this.Location = location;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public List<Event> getEvents() {
        return Events;
    }

    public void onDestroy(){
        this.Events.clear();
    }

    public String getFacilityName(){ return facilityName; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
