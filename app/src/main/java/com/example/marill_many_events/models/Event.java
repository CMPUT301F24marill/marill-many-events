package com.example.marill_many_events.models;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
/**
 * Represents an event with properties such as name, location, capacity, and other metadata.
 * The event may include geographical verification for entrants and maintains a list of participants.
 */
public class Event{
    ArrayList<DocumentReference> entrants;
    String Name;
    String Location;
    String ImageURL;
    String FirebaseID;
    String QRcode;
    Date startDate;
    Date drawDate;
    String facilityID;

    Integer capacity;
    boolean checkGeo;
    /**
     * Default constructor required by Firebase or Gson for object mapping.
     */
    public Event() {
        // This constructor is needed by Firebase or Gson
    }
    /**
     * Constructs an Event object with specified properties.
     *
     * @param imageURL   URL of the event poster image.
     * @param name       Name of the event.
     * @param location   Location of the event.
     * @param startDate  Start date of the event.
     * @param drawDate   Date for entrant selection or draw.
     * @param capacity   Maximum capacity for the event.
     * @param checkGeo   Whether the event requires geographical verification.
     * @param FirebaseID Unique identifier for the event in Firebase.
     * @param facilityID Unique identifier for the facility associated with the event.
     */
    public Event(String imageURL, String name, String location, Date startDate, Date drawDate, @Nullable Integer capacity, boolean checkGeo, String FirebaseID, String facilityID){
        this.Name = name;
        this.ImageURL = imageURL;
        this.Location = location;
        this.startDate = startDate;
        this.drawDate = drawDate;
        this.FirebaseID = FirebaseID;
        this.QRcode = FirebaseID;
        this.capacity = capacity;
        this.entrants = new ArrayList<DocumentReference>();
        this.checkGeo = checkGeo;
        this.facilityID = facilityID;
    }


//    /**
//     * Adds a new entrant to the list and sets their initial status as 'Waitlist'.
//     * Optionally sets the location coordinates if the event requires geographical verification.
//     *
//     * @param user   The {@link User} to be added as an entrant.
//     * @param x_cord The x-coordinate of the user's location.
//     * @param y_cord The y-coordinate of the user's location.
//     */
//    public void addEntrant(User user, float x_cord, float y_cord){
//        Entrant entrant = new Entrant("Waitlist", user);
//        if(this.checkGeo){
//            //entrant location is set if event checks for geo, else the coordinates within the entrant will remain null
//            entrant.setLocation(x_cord, y_cord);
//        }
//        this.entrants.addEntrant(entrant);
//    }
//
//    /**
//     * Updates the status of an entrant in the event.
//     *
//     * @param user   The {@link User} whose status needs to be updated.
//     * @param Status The new status to be applied (e.g., Waitlist, Cancelled, Enrolled, Invited).
//     */
//    public void setEntrantStatus(User user, String Status){
//        this.entrants.setEntrantStatus(user, Status);
//    }

    /**
     * Retrieves the image URL of the event.
     *
     * @return The image URL as a {@link String}.
     */
    public String getImageURL(){
        return this.ImageURL;
    }

    /**
     * set event poster
     *
     * @param url for event poster
     */
    public void setImageURL(String url){
        this.ImageURL = url;
    }

    // Getter and setter methods for event properties

    /**
     * Retrieves the capacity of the event.
     *
     * @return The event's capacity as an {@link Integer}.
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity for the event.
     *
     * @param capacity The new capacity.
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    /**
     * Retrieves the draw date of the event.
     *
     * @return The draw date as a {@link Date}.
     */
    public Date getDrawDate() {
        return drawDate;
    }
    /**
     * Sets the draw date for the event.
     *
     * @param drawDate The new draw date.
     */
    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }
    /**
     * Retrieves the start date of the event.
     *
     * @return The start date as a {@link Date}.
     */
    public Date getStartDate() {
        return startDate;
    }
    /**
     * Sets the start date for the event.
     *
     * @param startDate The new start date.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the location of the event.
     *
     * @return The event location as a {@link String}.
     */
    public String getLocation() {
        return Location;
    }

    /**
     * Sets the location of the event.
     *
     * @param location The new event location.
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * Retrieves the name of the event.
     *
     * @return The event name as a {@link String}.
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the name of the event.
     *
     * @param name The new event name.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Checks if the event requires geographical verification for entrants.
     *
     * @return True if geographical verification is required; otherwise, false.
     */
    public boolean isCheckGeo() {
        return checkGeo;
    }

    /**
     * Sets whether the event should check geographical location for entrants.
     *
     * @param checkGeo A boolean indicating whether to check geo-location.
     */
    public void setCheckGeo(boolean checkGeo) {
        this.checkGeo = checkGeo;
    }
    /**
     * Retrieves the Firebase ID of the event.
     *
     * @return The Firebase ID as a {@link String}.
     */
    public String getFirebaseID() {
        return this.FirebaseID;
    }
    /**
     * Sets the Firebase ID for the event.
     *
     * @param FirebaseID The unique Firebase identifier for the event.
     */
    public void setFirebaseID(String FirebaseID) {
        this.FirebaseID = FirebaseID;
    }
    /**
     * Retrieves the QR code of the event.
     *
     * @return The QR code as a {@link String}.
     */
    public String getQRcode() {
        return this.QRcode;
    }
    /**
     * Sets the QR code for the event.
     *
     * @param FirebaseID The Firebase ID to be used as the QR code.
     */
    public void setQRcode(String FirebaseID) {
        this.QRcode = FirebaseID;
    }
    /**
     * Retrieves the facility ID associated with the event.
     *
     * @return The facility ID as a {@link String}.
     */
    public String getFacilityID() {
        return facilityID;
    }
    /**
     * Sets the facility ID for the event.
     *
     * @param facilityID The unique facility identifier.
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

}
