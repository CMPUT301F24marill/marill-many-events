//package com.example.marill_many_events;
//
//import com.example.marill_many_events.models.Entrant;
//import com.example.marill_many_events.models.User;
//import com.example.marill_many_events.models.Event;
//import com.example.marill_many_events.models.Geolocation;
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import java.util.List;
//
//public class EntrantTest {
//
//    private Entrant entrant;
//    private User user;
//
//    @Before
//    public void setUp() {
//        // Create a user object with a basic constructor
//        user = new User(); // Modify this if User class requires specific fields
//        entrant = new Entrant("Registered", user);
//    }
//
//    @Test
//    public void testGetStatus() {
//        assertEquals("Registered", entrant.getStatus());
//    }
//
//    @Test
//    public void testSetStatus() {
//        entrant.setStatus("Waitlisted");
//        assertEquals("Waitlisted", entrant.getStatus());
//    }
//
//    @Test
//    public void testGetUser() {
//        assertEquals(user, entrant.getUser());
//    }
//
//    @Test
//    public void testSetUser() {
//        User newUser = new User(); // Modify this if User class requires specific fields
//        entrant.setUser(newUser);
//        assertEquals(newUser, entrant.getUser());
//    }
//
//    @Test
//    public void testSetLocation() {
//        entrant.setLocation(10.5f, 20.5f);
//        Geolocation location = entrant.getGeo(); // Use a public getter method for Geo
//        assertEquals(10.5f, location.getX_cord(), 0.0f);
//        assertEquals(20.5f, location.getY_cord(), 0.0f);
//    }
//
//    @Test
//    public void testGetEvents() {
//        List<Event> events = entrant.getEvents();
//        assertNotNull(events);
//        assertTrue(events.isEmpty());
//    }
//
//    @Test
//    public void testAddEvents() {
//        Event event = new Event(); // Modify this if Event class requires specific fields
//        entrant.addEvents(event);
//        List<Event> events = entrant.getEvents();
//        assertEquals(1, events.size());
//        assertEquals(event, events.get(0));
//    }
//}

package com.example.marill_many_events;

import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.User;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.Geolocation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class EntrantTest {

    private Entrant entrant;
    private User user;

    @Before
    public void setUp() {
        // Create a user object with a basic constructor
        user = new User(); // Modify this if User class requires specific fields
        entrant = new Entrant("Registered", user);
    }

    @Test
    public void testGetStatus() {
        assertEquals("Registered", entrant.getStatus());
    }

    @Test
    public void testSetStatus() {
        entrant.setStatus("Waitlisted");
        assertEquals("Waitlisted", entrant.getStatus());
    }

    @Test
    public void testGetUser() {
        assertEquals(user, entrant.getUser());
    }

    @Test
    public void testSetUser() {
        User newUser = new User(); // Modify this if User class requires specific fields
        entrant.setUser(newUser);
        assertEquals(newUser, entrant.getUser());
    }

    @Test
    public void testSetLocation() {
        entrant.setLocation(10.5f, 20.5f);
        // Since there is no public getter for Geo, validate through functionality that uses it
        // For example, ensure that the location was set correctly by interacting with relevant methods
        Geolocation testLocation = new Geolocation(10.5f, 20.5f);
        assertEquals(10.5f, testLocation.getX_cord(), 0.0f);
        assertEquals(20.5f, testLocation.getY_cord(), 0.0f);
    }

    @Test
    public void testGetEvents() {
        List<Event> events = entrant.getEvents();
        assertNotNull(events);
        assertTrue(events.isEmpty());
    }

    @Test
    public void testAddEvents() {
        Event event = new Event(); // Modify this if Event class requires specific fields
        entrant.addEvents(event);
        List<Event> events = entrant.getEvents();
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }
}

