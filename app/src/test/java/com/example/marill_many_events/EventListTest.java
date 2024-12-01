package com.example.marill_many_events;

import com.example.marill_many_events.models.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

public class EventListTest {

    private ArrayList<Event> eventItemList;
    private Event mockEvent;
    /**
     * create a mock list for my eventItemList
     * @return ArrayList<Event>
     */
    public ArrayList<Event> MockEventList(){
        eventItemList = new ArrayList<Event>(); return eventItemList;
    }
    /**
     * create a mock event
     * @return Event
     */
    public Event MockEvent(){
        mockEvent = new  Event("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae"
                , "Event1", null, null, null, 1, false, null,53,-113);
        return mockEvent;
    }

    /**
     * Add an event to the Event List,
     * assert entrant list size increased*/
    @Test
    public void addEventTest(){
        eventItemList = MockEventList();
        mockEvent = MockEvent();
        assertEquals(eventItemList.size(),0);
        eventItemList.add(mockEvent);
        assertEquals(eventItemList.size(),1);
    }

    /**
     * remove an event to the Event List,
     * assert entrant list decreased */
    @Test
    public void removeEventTest(){
        eventItemList = MockEventList();
        mockEvent = MockEvent();
        assertEquals(eventItemList.size(),0);
        eventItemList.add(mockEvent);
        assertEquals(eventItemList.size(),1);
        eventItemList.remove(mockEvent);
        assertEquals(eventItemList.size(),0);
    }

    /**
     * Add an event to the Event List,
     * assert the added event is there */
    @Test
    public void HasEventTest(){
        eventItemList = MockEventList();
        mockEvent = MockEvent();
        eventItemList.add(mockEvent);
        assertTrue(eventItemList.contains(mockEvent));
    }
    /**
     * Remove an event to the Event List,
     * assert the removed event is no longer */
    @Test
    public void NoLongerHasEventTest(){
        eventItemList = MockEventList();
        mockEvent = MockEvent();
        eventItemList.add(mockEvent);
        eventItemList.remove(mockEvent);
        assertFalse(eventItemList.contains(mockEvent));
    }
}
