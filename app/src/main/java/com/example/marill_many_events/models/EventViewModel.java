package com.example.marill_many_events.models;

import static androidx.test.InstrumentationRegistry.getContext;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.marill_many_events.EventsCallback;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel implements EventsCallback {

    private final MutableLiveData<Event> selectedEvent = new MutableLiveData<>();
    private User currentUser;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference eventStorageReference;
    private FirebaseStorage firebaseStorage;
    private DocumentReference userReference;
    private final MutableLiveData<List<Event>> userEventList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> userOwnedList = new MutableLiveData<>(new ArrayList<>());



    public LiveData<List<Event>> getUserEventList() {
        return userEventList;
    }
    public LiveData<List<Event>> getUserOwnedList() {
        return userOwnedList;
    }

    /**
     * Adds a new event to the current event list and updates the LiveData.
     *
     * @param event The event to add.
     */
    private void addToEventList(Event event) {
        List<Event> currentList = userEventList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(event);
        userEventList.setValue(currentList); // Trigger observers
    }

    /**
     * Remove an event from the current event list and updates the LiveData.
     *
     * @param event The event to remove.
     */
    private void removeFromEventList(Event event) {
        List<Event> currentList = userEventList.getValue();
        if (currentList != null && currentList.contains(event)) {
            currentList.remove(event);
            userEventList.setValue(currentList); // Trigger observers
        }
    }

    /**
     * Adds a new event to the current event list and updates the LiveData.
     *
     * @param event The event to add.
     */
    private void addToOwnedList(Event event) {
        List<Event> currentList = userOwnedList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(event);
        userOwnedList.setValue(currentList); // Trigger observers
    }

    /**
     * Remove an event from the current event list and updates the LiveData.
     *
     * @param event The event to remove.
     */
    private void removeFromOwnedList(Event event) {
        List<Event> currentList = userOwnedList.getValue();
        if (currentList != null && currentList.contains(event)) {
            currentList.remove(event);
            userOwnedList.setValue(currentList); // Trigger observers
        }
    }


    /**
     * Fetch all events the user is registered in and update LiveData.
     */
    public void getUserEvents() {
        userEventList.setValue(new ArrayList<>()); // Clear the current list
        userReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<DocumentReference> docRefs = (List<DocumentReference>) documentSnapshot.get("waitList");

                        if (docRefs != null) {
                            for (DocumentReference reference : docRefs) {
                                reference.get()
                                        .addOnSuccessListener(innerDoc -> {
                                            if (innerDoc.exists()) {
                                                Event event = innerDoc.toObject(Event.class);
                                                addToEventList(event);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle error fetching the event document
                                            Toast.makeText(getContext(), "Error fetching referenced document", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Document not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error retrieving the user document
                    Toast.makeText(getContext(), "Error getting document", Toast.LENGTH_SHORT).show();
                });
    }

    public void getUserOwnedEvents() {
        userOwnedList.setValue(new ArrayList<>()); // Clear the current list

        // Fetch the facility document using the deviceId
        firebaseFirestore.collection("facilities")
                .document(userReference.getId()) // Use the deviceId to get the specific facility document
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot facilityDoc = task.getResult();
                        if (facilityDoc.exists()) {
                            // Extract event IDs from the 'events' field
                            List<String> eventIds = (List<String>) facilityDoc.get("events");

                            if (eventIds != null) {
                                // Loop through the event IDs and fetch the corresponding events from the "events" collection
                                for (String eventId : eventIds) {
                                    fetchEventDetails(eventId);
                                }
                            } else {
                                Toast.makeText(getContext(), "No events found in the facility", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Facility document not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error retrieving facility", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error getting facility", Toast.LENGTH_SHORT).show();
                });
    }


    public void fetchEventDetails(String eventId) {
        // Fetch the event document from the "events" collection using the eventId
        firebaseFirestore.collection("events").document(eventId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDoc = task.getResult();
                        if (eventDoc.exists()) {
                            Event event = eventDoc.toObject(Event.class);
                            if (event != null) {
                                addToOwnedList(event);
                            }
                        } else {
                            Log.d("Event Fetch", "Event not found: " + eventId);
                        }
                    } else {
                        Log.d("Event Fetch", "Error fetching event: " + eventId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Event Fetch", "Error getting event document: " + e.getMessage());
                });
    }

    /**
     * Sets the currently selected event.
     *
     * @param event The event to be set as selected.
     */
    public void setSelectedEvent(Event event) {
        selectedEvent.setValue(event);
    }

    /**
     * Retrieves the currently selected event as a LiveData object.
     *
     * @return LiveData containing the selected event.
     */
    public LiveData<Event> getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user to be set as current.
     */
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the DocumentReference for the selected event.
     *
     * @param firebaseFirestore The DocumentReference for the event.
     */
    public void setFirebaseReference(FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    /**
     * Sets the DocumentReference for the selected event.
     *
     */
    public FirebaseFirestore getFirebaseReference() {
        return firebaseFirestore;
    }


    /**
     * Retrieves the DocumentReference for the selected event.
     *
     * @return The DocumentReference for the event.
     */
    public DocumentReference getEventDocumentReference() {
        return firebaseFirestore.collection("events").document(selectedEvent.getValue().getFirebaseID());
    }

    /**
     * Sets the StorageReference for the selected event.
     *
     * @param storageReference The StorageReference for the event.
     */
    public void setEventStorageReference(StorageReference storageReference) {
        this.eventStorageReference = storageReference;
    }

    /**
     * Retrieves the StorageReference for the selected event.
     *
     * @return The StorageReference for the event.
     */
    public StorageReference getEventStorageReference() {
        return eventStorageReference;
    }

    /**
     * Updates the status of a specific entrant for the selected event.
     *
     * @param user   The user whose status needs to be updated.
     * @param status The new status to be applied.
     */
    public void updateEntrantStatus(User user, String status) {
        Event event = selectedEvent.getValue();
        if (event != null) {
            event.setEntrantStatus(user, status);
            selectedEvent.setValue(event); // Trigger LiveData observers
        }
    }

    /**
     * Adds a new entrant to the currently selected event.
     *
     * @param user   The user to be added as an entrant.
     * @param xCord  The x-coordinate of the user's location.
     * @param yCord  The y-coordinate of the user's location.
     */
    public void addEntrantToEvent(User user, float xCord, float yCord) {
        Event event = selectedEvent.getValue();
        if (event != null) {
            event.addEntrant(user, xCord, yCord);
            selectedEvent.setValue(event); // Trigger LiveData observers
        }
    }

    /**
     * Register a user to an event by atomically adding user to event's waitlist and event to user's events
     */
    public void registerUser(){ // Register the current deviceID (user) to the given event by writing to the user and event a reference to each other
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(getSelectedEvent().getValue().FirebaseID);

        batch.update(userReference, "waitList", FieldValue.arrayUnion(eventUsers));
        batch.update(eventUsers, "waitList", FieldValue.arrayUnion(userReference));

        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    firebaseFirestore.collection("events").document(getSelectedEvent().getValue().FirebaseID).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        addToEventList(newEvent); // Add directly to the list
                                    }
                                }
                            });
                    //Toast.makeText(getContext(), "Item added to the list!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error adding item to the list", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Leave an event as a user
     */
    public void leaveEvent(Event event){
        // Leave an event as a user
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(event.getFirebaseID());

        batch.update(userReference, "waitList", FieldValue.arrayRemove(eventUsers));
        batch.update(eventUsers, "waitList", FieldValue.arrayRemove(userReference));

        batch.commit() // remove event from user and user from event atomically
                .addOnSuccessListener(aVoid -> {
                    firebaseFirestore.collection("events").document(event.getFirebaseID()).get() // get the present event's firebase id from its local copy
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        removeFromEventList(event); // Remove from list
                                    }
                                }
                            });
                    //Toast.makeText(getContext(), "Left the event!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error leaving the event", Toast.LENGTH_SHORT).show();
                });
    }

    public void deleteEvent(Event event){
        if(event != null) {
            firebaseFirestore.collection("events") // "events" is the name of your collection
                    .document(event.getFirebaseID())
                    .delete()
                    .addOnSuccessListener(documentReference -> {
                        removeFromOwnedList(event);
                        Log.d("Firestore", "Event deleted");
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error adding event", e);
                    });
        }
    }

    // Implemented EventsCallback methods
    public void onEventCreate(String documentID) {
        // Custom logic for event creation
    }

    public void onEventDelete() {
        // Custom logic for event deletion
    }

    public void joinEvent() {
        // Custom logic for joining an event
    }

    public void getEvent(Event event) {
        setSelectedEvent(event);
    }

    public void onPosterUpload(String url) {
        // Custom logic for handling poster uploads
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public void setFirebaseStorage(FirebaseStorage firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

    public DocumentReference getUserReference() {
        return userReference;
    }

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
    }

    public boolean userOwnsEvent(){
        // Get the current value of userEventList
        List<Event> currentList = getUserOwnedList().getValue();
        // Check if the list is not null and contains the event

        return currentList != null && getSelectedEvent().getValue() != null && currentList.contains(getSelectedEvent().getValue());
    }
}
