package com.example.marill_many_events;

import com.example.marill_many_events.models.Event;

public interface EventsCallback {
    void onEventCreate(String documentID);
    void getEvent(Event event);
    void onPosterUpload(String url);

}
