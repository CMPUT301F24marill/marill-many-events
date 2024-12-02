package com.example.marill_many_events.models;

/**
 * This is a class for a notification
 * sentToID is the id where the notification is being sent to
 * content is the info inside being sent
*/

public class Notification {

    String sentToId;
    String content;

    public Notification() {
        // This constructor is needed by Firebase or Gson
    }

    public Notification(String sentToId, String content) {
        this.sentToId = sentToId;
        this.content = content;
    }

    public String getSentToId() {
        return sentToId;
    }

    public void setSentToId(String sentToId) {
        this.sentToId = sentToId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
