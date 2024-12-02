package com.example.marill_many_events.models;

/**
 * This is a class for a notification
 * sentToID is the id where the notification is being sent to
 * content is the info inside being sent
*/

public class Notification {

    String sentToId;
    String title;
    String content;


    /**
     * This constructor is needed by Firebase or Gson
     */
    public Notification() {
        // This constructor is needed by Firebase or Gson
    }


    /**
     * Default constructor for User.
     * @param sentToId The id of the user the notification is being sent to
     *
     * @param title The title of the notification
     *
     * @param content The content of the notification
     *
     */
    public Notification(String sentToId, String title, String content) {
        this.sentToId = sentToId;
        this.title = title;
        this.content = content;
    }

    /**
     * Retrieves the id of the receiver of a notification.
     *
     * @return The receivers id as a {@link String}.
     */
    public String getSentToId() {
        return sentToId;
    }

    /**
     * Sets the id of a receiver for the notification.
     *
     * @param sentToId The id of the receiver of a notification
     */
    public void setSentToId(String sentToId) {
        this.sentToId = sentToId;
    }

    /**
     * Retrieves the title of a notification.
     *
     * @return The notification title as a {@link String}.
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets the title for the notification.
     *
     * @param title The content of the notification.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the content of a notification.
     *
     * @return The notification content as a {@link String}.
     */
    public String getContent() {
        return content;
    }


    /**
     * Sets the content for the notification.
     *
     * @param content The content of the notification.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
