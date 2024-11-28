package com.example.marill_many_events.models;

import android.os.Parcelable;

import com.example.marill_many_events.models.User;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class Event{
    //private String eventDocumentId;
    EntrantList entrants;
    String Name;
    String Location;
    String ImageURL;
    String QRcode;
    Date startDate;
    Date drawDate;

    Integer capacity;
    boolean checkGeo;

    public Event() {
        // This constructor is needed by Firebase or Gson
    }

    public Event(String imageURL, String name, String location, Date startDate, Date drawDate, Integer capacity, @Nullable boolean checkGeo, String QRcode){
        this.entrants = new EntrantList();
        this.ImageURL = imageURL;
        this.QRcode = QRcode;
        this.capacity = capacity;
        this.Name = name;
        this.Location = location;
        this.startDate = startDate;
        this.drawDate = drawDate;
        this.checkGeo = checkGeo;
        //this. eventDocumentId = eventDocumentId; //, String eventDocumentId
    }


    /**
     * Retrieves a list of entrants with a specified status.
     *
     * @param Status The status to filter entrants by (e.g., Waitlist, Cancelled, Enrolled, Invited).
     * @return A list of {@link Entrant} objects matching the specified status.
     */
    public List<Entrant> getStatusEntrants(String Status){
        return this.entrants.getStatusEntrantsList(Status);
    }

    /**
     * Adds a new entrant to the list and sets their initial status as 'Waitlist'.
     * Optionally sets the location coordinates if the event requires geographical verification.
     *
     * @param user   The {@link User} to be added as an entrant.
     * @param x_cord The x-coordinate of the user's location.
     * @param y_cord The y-coordinate of the user's location.
     */
    public void addEntrant(User user, float x_cord, float y_cord){
        Entrant entrant = new Entrant("Waitlist", user);
        if(this.checkGeo){
            //entrant location is set if event checks for geo, else the coordinates within the entrant will remain null
            entrant.setLocation(x_cord, y_cord);
        }
        this.entrants.addEntrant(entrant);
    }

    /**
     * Updates the status of an entrant in the event.
     *
     * @param user   The {@link User} whose status needs to be updated.
     * @param Status The new status to be applied (e.g., Waitlist, Cancelled, Enrolled, Invited).
     */
    public void setEntrantStatus(User user, String Status){
        this.entrants.setEntrantStatus(user, Status);
    }

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

    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    public Date getStartDate() {
        return startDate;
    }

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

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }
//
//    public void setEventDocumentId(String eventDocumentId) {
//        this.eventDocumentId = eventDocumentId;
//    }
//
//    public String getEventDocumentId() {
//        return eventDocumentId;
//    }
}
