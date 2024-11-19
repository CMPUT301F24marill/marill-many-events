package com.example.marill_many_events;

import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.Event;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class FacilityTest {

    private Facility facility;
    private Event event;

    @Before
    public void setUp() {
        facility = new Facility("Central Park", "A");
        event = new Event(); // Replace with suitable initialization if needed
        facility.getEvents().add(event); // Add an event for testing purposes
    }

    @Test
    public void testGetLocation() {
        assertEquals("Central Park", facility.getLocation());
    }

    @Test
    public void testSetLocation() {
        facility.setLocation("Downtown Arena");
        assertEquals("Downtown Arena", facility.getLocation());
    }

    @Test
    public void testGetEvents() {
        List<Event> events = facility.getEvents();
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }

    @Test
    public void testOnDestroy() {
        facility.onDestroy();
        assertTrue(facility.getEvents().isEmpty());
    }
}
