package com.example.marill_many_events;

import com.example.marill_many_events.models.Geolocation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeolocationTest {

    private Geolocation geolocation;

    @Before
    public void setUp() {
        geolocation = new Geolocation(10.5f, 20.5f);
    }

    @Test
    public void testGetX_cord() {
        assertEquals(10.5f, geolocation.getX_cord(), 0.0f);
    }

    @Test
    public void testSetX_cord() {
        geolocation.setX_cord(15.5f);
        assertEquals(15.5f, geolocation.getX_cord(), 0.0f);
    }

    @Test
    public void testGetY_cord() {
        assertEquals(20.5f, geolocation.getY_cord(), 0.0f);
    }

    @Test
    public void testSetY_cord() {
        geolocation.setY_cord(25.5f);
        assertEquals(25.5f, geolocation.getY_cord(), 0.0f);
    }
}
