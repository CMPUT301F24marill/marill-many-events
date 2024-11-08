package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Facility represents a physical or virtual location where events take place.
 * It holds a list of {@link Event} objects and provides methods to manage these events
 * and the location of the facility.
 */
public class Facility {
    private List<Event> Events;

    String location;

    /**
     * Constructs a Facility with a specified location.
     *
     * @param location The location of the facility.
     */
    public Facility(String location){
        this.Events = new ArrayList<>();
        this.location = location;
    }

    /**
     * Retrieves the location of the facility.
     *
     * @return The location of the facility.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the facility.
     *
     * @param location The new location of the facility.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the list of events associated with this facility.
     *
     * @return A {@link List} of {@link Event} objects linked to the facility.
     */
    public List<Event> getEvents() {
        return Events;
    }

    /**
     * Clears all events associated with this facility.
     * This method should be called when the facility is being destroyed or no longer used.
     */
    public void onDestroy(){
        this.Events.clear();
    }
    
}
