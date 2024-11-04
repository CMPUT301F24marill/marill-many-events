package com.example.marill_many_events;

public class Geolocation {
    Float x_cord;
    Float y_cord;
    Entrant entrant;

    public Geolocation(Float x, Float y){
        this.x_cord = x;
        this.y_cord = y;
    }

    public Float getX_cord() {
        return x_cord;
    }

    public void setX_cord(Float x_cord) {
        this.x_cord = x_cord;
    }

    public Float getY_cord() {
        return y_cord;
    }

    public void setY_cord(Float y_cord) {
        this.y_cord = y_cord;
    }
}
