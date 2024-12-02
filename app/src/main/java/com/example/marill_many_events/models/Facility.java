package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a Facility that can host events, including its name, location, and a list of events.
 */
public class Facility{
    private List<Event> Events;

    String facilityName;
    String Location;

    String id;
    /**
     * Default no-argument constructor.
     * Required by Firestore for deserialization.
     */

    // No-argument constructor (needed by Firestore)
    public Facility() {
        // Firebase needs a no-argument constructor
    }
    /**
     * Constructs a new Facility with the specified name and location.
     *
     * @param facilityName The name of the facility.
     * @param location     The location of the facility.
     */
    public Facility(String facilityName, String location){
        this.Events = new ArrayList<>();
        this.facilityName = facilityName;
        this.Location = location;
    }
    /**
     * Retrieves the location of the facility.
     *
     * @return The facility's location as a {@link String}.
     */
    public String getLocation() {
        return Location;
    }
    /**
     * Sets the location of the facility.
     *
     * @param location The new location of the facility.
     */
    public void setLocation(String location) {
        this.Location = location;
    }
    /**
     * Retrieves the list of events associated with this facility.
     *
     * @return A {@link List} of {@link Event} objects.
     */
    public List<Event> getEvents() {
        return Events;
    }
    /**
     * Clears all events associated with this facility.
     * Typically called when the facility is being destroyed or reset.
     */
    public void onDestroy(){
        this.Events.clear();
    }
    /**
     * Retrieves the name of the facility.
     *
     * @return The facility's name as a {@link String}.
     */
    public String getFacilityName(){ return facilityName; }
    /**
     * Retrieves the unique identifier of the facility.
     *
     * @return The facility's ID as a {@link String}.
     */
    public String getId() {
        return id;
    }
    /**
     * Sets the unique identifier of the facility.
     *
     * @param id The new ID for the facility.
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
