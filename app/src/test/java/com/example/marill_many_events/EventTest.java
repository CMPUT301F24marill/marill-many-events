package com.example.marill_many_events;

import com.example.marill_many_events.models.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventTest {

    private Event event;

    @Before
    public void setUp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Using SimpleDateFormat to create specific date format
        Date startDate = null;
        Date drawDate = null;
        try {
            startDate = dateFormat.parse("2025-01-01");  // Example start date
            drawDate = dateFormat.parse("2025-01-02");   // Example draw date
        } catch (Exception e) {
            e.printStackTrace();  // Handle date parsing exception if needed
        }

        // Creating the Event object with valid Date parameters
        event = new Event(
                "https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae",
                "Event1",
                "Location1",  // Assuming a valid location
                startDate,     // Valid startDate
                drawDate,      // Valid drawDate
                100,           // Capacity as Integer
                true,          // Geo-check flag as boolean
                "FirebaseID123",  // Example Firebase ID
                "FacilityID123"   // Example Facility ID
        );
    }

    @Test
    public void testGetImageURL() {
        assertEquals("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae", event.getImageURL());
    }

    @Test
    public void testSetImageURL() {
        event.setImageURL("https://newexample.com/newimage.jpg");
        assertEquals("https://newexample.com/newimage.jpg", event.getImageURL());
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
    public void testGetLocation() {
        assertEquals("Location1", event.getLocation());
    }

    @Test
    public void testSetLocation() {
        event.setLocation("New Location");
        assertEquals("New Location", event.getLocation());
    }

    @Test
    public void testGetStartDate() {
        assertNotNull(event.getStartDate());
    }

    @Test
    public void testSetStartDate() {
        Date newStartDate = new Date();
        event.setStartDate(newStartDate);
        assertEquals(newStartDate, event.getStartDate());
    }

    @Test
    public void testGetDrawDate() {
        assertNotNull(event.getDrawDate());
    }

    @Test
    public void testSetDrawDate() {
        Date newDrawDate = new Date();
        event.setDrawDate(newDrawDate);
        assertEquals(newDrawDate, event.getDrawDate());
    }

    @Test
    public void testGetFirebaseID() {
        assertEquals("FirebaseID123", event.getFirebaseID());
    }

    @Test
    public void testSetFirebaseID() {
        event.setFirebaseID("NewFirebaseID123");
        assertEquals("NewFirebaseID123", event.getFirebaseID());
    }

    @Test
    public void testGetQRcode() {
        assertEquals("FirebaseID123", event.getQRcode());
    }

    @Test
    public void testSetQRcode() {
        event.setQRcode("NewQRcode123");
        assertEquals("NewQRcode123", event.getQRcode());
    }

    @Test
    public void testGetFacilityID() {
        assertEquals("FacilityID123", event.getFacilityID());
    }

    @Test
    public void testSetFacilityID() {
        event.setFacilityID("NewFacilityID123");
        assertEquals("NewFacilityID123", event.getFacilityID());
    }
}

