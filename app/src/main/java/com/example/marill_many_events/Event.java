package com.example.marill_many_events;

import java.text.DateFormat;
import java.util.List;

public class Event {
    entrantList entrants;
    String Name;
    String Location;
    Image Image;
    QRcode eventQRcode;
    DateFormat startDate;
    DateFormat drawDate;

    boolean limitEntrants;
    Integer capacity;
    boolean checkGeo;

    public void event(String imageURL, boolean imageGenerated, String name, String location, DateFormat startDate, DateFormat drawDate, boolean limitEntrants, Integer capacity, boolean checkGeo){
        this.entrants = new entrantList();
        this.Image = new Image( imageURL, imageGenerated);
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
        return this.Image.getImageURL();
    }

    /**
     * set event poster
     *
     * @param  url for event poster
     */
    public void setImageURL(String url){
        this.Image.setImageURL(url);
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public boolean isLimitEntrants() {
        return limitEntrants;
    }

    public void setLimitEntrants(boolean limitEntrants) {
        this.limitEntrants = limitEntrants;
    }

    public DateFormat getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(DateFormat drawDate) {
        this.drawDate = drawDate;
    }

    public DateFormat getStartDate() {
        return startDate;
    }

    public void setStartDate(DateFormat startDate) {
        this.startDate = startDate;
    }

    public QRcode getEventQRcode() {
        return eventQRcode;
    }

    public void setEventQRcode(QRcode eventQRcode) {
        this.eventQRcode = eventQRcode;
    }

    public com.example.marill_many_events.Image getImage() {
        return Image;
    }

    public void setImage(com.example.marill_many_events.Image image) {
        Image = image;
    }

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
