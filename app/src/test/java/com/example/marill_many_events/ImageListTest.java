package com.example.marill_many_events;


import com.example.marill_many_events.models.ImageList;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.User; // Ensure this import is present

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ImageListTest {

    private ImageList imageList;
    private Entrant entrant1;
    private Entrant entrant2;

    @Before
    public void setUp() {
        imageList = new ImageList();
        entrant1 = new Entrant("Waitlist", new User("John Doe", "john@example.com", "1234567890", "https://example.com/john.jpg"));
        entrant2 = new Entrant("Enrolled", new User("Jane Smith", "jane@example.com", "0987654321", "https://example.com/jane.jpg"));
    }

    @Test
    public void testAddEntrant() {
        imageList.addEntrant(entrant1);
        List<Entrant> entrants = imageList.getEntrants();
        assertFalse(entrants.isEmpty());
        assertEquals(1, entrants.size());
        assertEquals(entrant1, entrants.get(0));
    }

    @Test
    public void testRemoveEntrant() {
        imageList.addEntrant(entrant1);
        imageList.addEntrant(entrant2);
        imageList.removeEntrant(entrant1);
        List<Entrant> entrants = imageList.getEntrants();
        assertFalse(entrants.isEmpty());
        assertEquals(1, entrants.size());
        assertEquals(entrant2, entrants.get(0));
    }

    @Test
    public void testClearList() {
        imageList.addEntrant(entrant1);
        imageList.addEntrant(entrant2);
        imageList.clearList();
        List<Entrant> entrants = imageList.getEntrants();
        assertTrue(entrants.isEmpty());
    }

    @Test
    public void testGetEntrants() {
        imageList.addEntrant(entrant1);
        imageList.addEntrant(entrant2);
        List<Entrant> entrants = imageList.getEntrants();
        assertEquals(2, entrants.size());
        assertEquals(entrant1, entrants.get(0));
        assertEquals(entrant2, entrants.get(1));
    }
}
