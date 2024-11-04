package com.example.marill_many_events;

import java.util.ArrayList;
import java.util.List;

public class Facility {
    private List<Event> Events;

    String location;

    public Facility(String location){
        this.Events = new ArrayList<>();
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Event> getEvents() {
        return Events;
    }

    public void onDestroy(){
        this.Events.clear();
    }
    
}
