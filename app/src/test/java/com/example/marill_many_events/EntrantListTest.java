package com.example.marill_many_events;

import com.example.marill_many_events.models.EntrantList;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.User;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class EntrantListTest {

    private EntrantList entrantList;
    private Entrant entrant1;
    private Entrant entrant2;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        entrantList = new EntrantList();
        user1 = new User(); // Modify if User class requires parameters
        user2 = new User(); // Modify if User class requires parameters
        entrant1 = new Entrant("Waitlist", user1);
        entrant2 = new Entrant("Enrolled", user2);
        entrantList.addEntrant(entrant1);
        entrantList.addEntrant(entrant2);
    }

    @Test
    public void testAddEntrant() {
        Entrant newEntrant = new Entrant("Invited", new User());
        entrantList.addEntrant(newEntrant);
        List<Entrant> entrants = entrantList.getStatusEntrantsList("Invited");
        assertFalse(entrants.isEmpty());
        assertEquals(1, entrants.size());
        assertEquals("Invited", entrants.get(0).getStatus());
    }

    @Test
    public void testSetEntrantStatus() {
        entrantList.setEntrantStatus(user1, "Cancelled");
        List<Entrant> updatedEntrants = entrantList.getStatusEntrantsList("Cancelled");
        assertFalse(updatedEntrants.isEmpty());
        assertEquals(1, updatedEntrants.size());
        assertEquals("Cancelled", updatedEntrants.get(0).getStatus());
    }

    @Test
    public void testGetStatusEntrantsList() {
        List<Entrant> enrolledEntrants = entrantList.getStatusEntrantsList("Enrolled");
        assertNotNull(enrolledEntrants);
        assertEquals(1, enrolledEntrants.size());
        assertEquals("Enrolled", enrolledEntrants.get(0).getStatus());
    }

    @Test
    public void testRemoveEntrant() {
        entrantList.removeEntrant(user2);
        List<Entrant> entrantsAfterRemoval = entrantList.getStatusEntrantsList("Enrolled");
        assertTrue(entrantsAfterRemoval.isEmpty());
    }
}
