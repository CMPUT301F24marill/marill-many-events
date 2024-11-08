package com.example.marill_many_events.models;

import java.util.ArrayList;
import java.util.List;

public class Facility{
    private List<Event> Events;

    String Name;
    String Location;

    public Facility(String name, String location){
        this.Events = new ArrayList<>();
        this.Name = name;
        this.Location = location;
    }

    public String getLocation() {
        return this.Location;
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

    public String getName(){ return this.Name; }
    
}
