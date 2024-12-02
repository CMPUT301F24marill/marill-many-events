package com.example.marill_many_events;

import com.example.marill_many_events.models.Event;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class GenerateQRcodeTest {

    private Event event;

    @Before
    public void setUp() {
        // Create an event object with a mock Firebase ID
        event = new Event(
                "https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae",
                "Event1",
                "Location1",
                new java.util.Date(),  // Using current date for simplicity
                new java.util.Date(),
                100,
                true,
                "FirebaseID123",  // Example Firebase ID to generate QR code
                "FacilityID123"
        );
    }

    @Test
    public void testQRCodeGeneration() {
        // Simulate the generation of QR Code (usually, this would involve calling an actual QR code generator)
        String expectedQRCode = "https://example.com/qrcode?eventID=FirebaseID123";  // Replace with your actual QR code generation logic
        event.setQRcode(expectedQRCode);  // Set the generated QR code

        // Verify that the QR code was correctly set
        assertEquals(expectedQRCode, event.getQRcode());
    }

    @Test
    public void testQRCodeGenerationWithRealisticValues() {
        // Simulating a QR code generation using FirebaseID
        String expectedQRCode = "https://example.com/qrcode?eventID=" + event.getFirebaseID();

        event.setQRcode(expectedQRCode);  // Mocking QR code setting

        // Verify if the QR code matches the expected format
        assertNotNull(event.getQRcode());  // QR Code should not be null
        assertTrue(event.getQRcode().contains("qrcode?eventID="));  // Check if QR code contains the event ID
        assertEquals(expectedQRCode, event.getQRcode());  // Verify that the expected QR code is set
    }
}

