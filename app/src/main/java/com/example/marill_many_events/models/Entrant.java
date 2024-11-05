package com.example.marill_many_events.models;

import com.example.marill_many_events.User;

import java.util.ArrayList;
import java.util.List;

public class Entrant {
    String status;
    Geolocation Geo;

    private List<Event> Events = new ArrayList<>();
    com.example.marill_many_events.User user;

    public Entrant(String status, com.example.marill_many_events.User user) {
        // Initialize user profile
        this.status = status;
        this.user = user;
        this.Geo = new Geolocation(null, null);
    }

    /**
     * Set the location
     *
     * @param x_cord x coordinate
     * @param y_cord y coordinate
     */
    public void setLocation(Float x_cord, Float y_cord){
        this.Geo.setX_cord(x_cord);
        this.Geo.setY_cord(y_cord);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public com.example.marill_many_events.User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getEvents() {
        return Events;
    }

    public void addEvents(Event event){
        this.Events.add(event);
    }
}
