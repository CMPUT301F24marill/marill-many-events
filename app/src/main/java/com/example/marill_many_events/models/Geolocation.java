package com.example.marill_many_events.models;

/**
 * Geolocation represents the geographical coordinates (x, y) of an entrant.
 * It stores and provides access to the x and y coordinates.
 */
public class Geolocation {
    Float x_cord;
    Float y_cord;
    Entrant entrant;

    /**
     * Constructs a Geolocation object with specified x and y coordinates.
     *
     * @param x The x-coordinate of the geolocation.
     * @param y The y-coordinate of the geolocation.
     */
    public Geolocation(Float x, Float y){
        this.x_cord = x;
        this.y_cord = y;
    }

    /**
     * Retrieves the x-coordinate of the geolocation.
     *
     * @return The x-coordinate.
     */
    public Float getX_cord() {
        return x_cord;
    }

    /**
     * Sets the x-coordinate of the geolocation.
     *
     * @param x_cord The new x-coordinate.
     */
    public void setX_cord(Float x_cord) {
        this.x_cord = x_cord;
    }

    /**
     * Retrieves the y-coordinate of the geolocation.
     *
     * @return The y-coordinate.
     */
    public Float getY_cord() {
        return y_cord;
    }

    /**
     * Sets the y-coordinate of the geolocation.
     *
     * @param y_cord The new y-coordinate.
     */
    public void setY_cord(Float y_cord) {
        this.y_cord = y_cord;
    }
}
