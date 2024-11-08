package com.example.marill_many_events;


import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.User;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.QRcode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EventTest {

    private Event event;
    private User user;

    @Before
    public void setUp() {
        DateFormat startDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat drawDate = new SimpleDateFormat("yyyy-MM-dd");
        event = new Event(); // Replace with a suitable constructor or initialization logic if needed
        event.event("https://example.com/image.jpg", "Sample Event", "New York", startDate, drawDate, true, 100, true);
        user = new User(); // Modify if the User class requires parameters
    }

    @Test
    public void testGetImageURL() {
        assertEquals("https://example.com/image.jpg", event.getImageURL());
    }

    @Test
    public void testSetImageURL() {
        event.setImageURL("https://newexample.com/newimage.jpg");
        assertEquals("https://newexample.com/newimage.jpg", event.getImageURL());
    }

    @Test
    public void testGetName() {
        assertEquals("Sample Event", event.getName());
    }

    @Test
    public void testSetName() {
        event.setName("Updated Event Name");
        assertEquals("Updated Event Name", event.getName());
    }

    @Test
    public void testGetLocation() {
        assertEquals("New York", event.getLocation());
    }

    @Test
    public void testSetLocation() {
        event.setLocation("Los Angeles");
        assertEquals("Los Angeles", event.getLocation());
    }

    @Test
    public void testGetStartDate() {
        assertNotNull(event.getStartDate());
    }

    @Test
    public void testSetStartDate() {
        DateFormat newStartDate = new SimpleDateFormat("yyyy-MM-dd");
        event.setStartDate(newStartDate);
        assertEquals(newStartDate, event.getStartDate());
    }

    @Test
    public void testGetDrawDate() {
        assertNotNull(event.getDrawDate());
    }

    @Test
    public void testSetDrawDate() {
        DateFormat newDrawDate = new SimpleDateFormat("yyyy-MM-dd");
        event.setDrawDate(newDrawDate);
        assertEquals(newDrawDate, event.getDrawDate());
    }

    @Test
    public void testAddEntrant() {
        event.addEntrant(user, 40.7128f, 74.0060f);
        List<Entrant> entrants = event.getStatusEntrants("Waitlist");
        assertFalse(entrants.isEmpty());
        assertEquals(1, entrants.size());
        assertEquals(user, entrants.get(0).getUser());
    }

    @Test
    public void testSetEntrantStatus() {
        event.addEntrant(user, 40.7128f, 74.0060f);
        event.setEntrantStatus(user, "Enrolled");
        List<Entrant> entrants = event.getStatusEntrants("Enrolled");
        assertFalse(entrants.isEmpty());
        assertEquals(1, entrants.size());
        assertEquals("Enrolled", entrants.get(0).getStatus());
    }

    @Test
    public void testGetCapacity() {
        assertEquals(100, (int) event.getCapacity());
    }

    @Test
    public void testSetCapacity() {
        event.setCapacity(200);
        assertEquals(200, (int) event.getCapacity());
    }

    @Test
    public void testIsLimitEntrants() {
        assertTrue(event.isLimitEntrants());
    }

    @Test
    public void testSetLimitEntrants() {
        event.setLimitEntrants(false);
        assertFalse(event.isLimitEntrants());
    }

    @Test
    public void testIsCheckGeo() {
        assertTrue(event.isCheckGeo());
    }

    @Test
    public void testSetCheckGeo() {
        event.setCheckGeo(false);
        assertFalse(event.isCheckGeo());
    }

    @Test
    public void testGetEventQRcode() {
        assertNotNull(event.getEventQRcode());
    }

    @Test
    public void testSetEventQRcode() {
        QRcode newQRCode = new QRcode();
        event.setEventQRcode(newQRCode);
        assertEquals(newQRCode, event.getEventQRcode());
    }
}
