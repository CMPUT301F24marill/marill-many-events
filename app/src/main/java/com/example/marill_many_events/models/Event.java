package com.example.marill_many_events.models;

import java.text.DateFormat;
import java.util.List;

/**
 * Event represents an event that users can register for. It includes details such as the name,
 * location, start date, and a list of entrants. It also has an option to limit the number of entrants
 * and verify geographical locations.
 */
public class Event {
    EntrantList entrants;
    String Name;
    String Location;
    String ImageURL;
    QRcode eventQRcode;
    DateFormat startDate;
    DateFormat drawDate;

    boolean limitEntrants;
    Integer capacity;
    boolean checkGeo;

    /**
     * Initializes an event with the specified parameters.
     *
     * @param imageURL      The URL of the event image.
     * @param name          The name of the event.
     * @param location      The location of the event.
     * @param startDate     The start date of the event.
     * @param drawDate      The draw date of the event.
     * @param limitEntrants Indicates if the number of entrants should be limited.
     * @param capacity      The capacity of the event (if entrants are limited).
     * @param checkGeo      Indicates if geographical verification is required.
     */
    public void event(String imageURL, String name, String location, DateFormat startDate, DateFormat drawDate, boolean limitEntrants, Integer capacity, boolean checkGeo){
        this.entrants = new EntrantList();
        this.ImageURL = imageURL;
        this.eventQRcode = new QRcode();

        this.Name = name;
        this.Location = location;
        this.startDate = startDate;
        this.drawDate = drawDate;
        this.limitEntrants = limitEntrants;
        if(limitEntrants){
            this.capacity = capacity;
        }
        else{
            this.capacity = 0;
        }
        this.checkGeo = checkGeo;
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

    /**
     * Checks if the event has a limit on the number of entrants.
     *
     * @return True if the event limits entrants; otherwise, false.
     */
    public boolean isLimitEntrants() {
        return limitEntrants;
    }

    /**
     * Sets whether the event should limit the number of entrants.
     *
     * @param limitEntrants A boolean indicating whether to limit entrants.
     */
    public void setLimitEntrants(boolean limitEntrants) {
        this.limitEntrants = limitEntrants;
    }

    /**
     * Retrieves the draw date of the event.
     *
     * @return The draw date as a {@link DateFormat}.
     */
    public DateFormat getDrawDate() {
        return drawDate;
    }

    /**
     * Sets the draw date for the event.
     *
     * @param drawDate The new draw date.
     */
    public void setDrawDate(DateFormat drawDate) {
        this.drawDate = drawDate;
    }

    /**
     * Retrieves the start date of the event.
     *
     * @return The start date as a {@link DateFormat}.
     */
    public DateFormat getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date for the event.
     *
     * @param startDate The new start date.
     */
    public void setStartDate(DateFormat startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the QR code associated with the event.
     *
     * @return The event's {@link QRcode}.
     */
    public QRcode getEventQRcode() {
        return eventQRcode;
    }

    /**
     * Sets the QR code for the event.
     *
     * @param eventQRcode The new {@link QRcode} object.
     */
    public void setEventQRcode(QRcode eventQRcode) {
        this.eventQRcode = eventQRcode;
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

}
