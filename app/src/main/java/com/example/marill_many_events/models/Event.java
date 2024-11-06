package com.example.marill_many_events.models;

import com.example.marill_many_events.models.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class Event {
    entrantList entrants;
    String Name;
    String Location;
    String ImageURL;
    //QRcode eventQRcode;
    Date startDate;
    Date drawDate;

    Integer capacity;
    boolean checkGeo;

    public Event(String imageURL, String name, String location, Date startDate, Date drawDate, Integer capacity, @Nullable boolean checkGeo){
        this.entrants = new entrantList();
        this.ImageURL = imageURL;
        //this.eventQRcode = new QRcode();
        this.capacity = capacity;
        this.Name = name;
        this.Location = location;
        this.startDate = startDate;
        this.drawDate = drawDate;
        this.checkGeo = checkGeo;
    }


    /**
     * Gets a list of entrants under specified status
     *
     * @param Status           The status to search for: Waitlist, Cancelled, Enrolled, Invited
     * @return The list of entrants in this event under that status
     */
    public List<Entrant> getStatusEntrants(String Status){
        return this.entrants.getStatusEntrantsList(Status);
    }

    /**
     * Add entrant to list. They are added as waitlisted.
     *
     * @param user          User to add
     * @param x_cord        User x coordinate
     * @param y_cord        User y coordinate
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
     * Set the status of an entrant in the list
     *
     * @param user              The user to apply the status to
     * @param Status           The status to apply for: Waitlist, Cancelled, Enrolled, Invited
     */
    public void setEntrantStatus(User user, String Status){
        this.entrants.setEntrantStatus(user, Status);
    }

    /**
     * get event poster
     *
     * @return String for event poster
     */
    public String getImageURL(){
        return this.ImageURL;
    }

    /**
     * set event poster
     *
     * @param  url for event poster
     */
    public void setImageURL(String url){
        this.ImageURL = url;
    }

    public Integer getCapacity() {
        return capacity;
    }

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

//    public QRcode getEventQRcode() {
//        return eventQRcode;
//    }
//
//    public void setEventQRcode(QRcode eventQRcode) {
//        this.eventQRcode = eventQRcode;
//    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isCheckGeo() {
        return checkGeo;
    }

    public void setCheckGeo(boolean checkGeo) {
        this.checkGeo = checkGeo;
    }

}
