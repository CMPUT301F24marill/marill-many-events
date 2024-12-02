package com.example.marill_many_events.models;

import static android.content.ContentValues.TAG;
import static androidx.test.InstrumentationRegistry.getContext;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.marill_many_events.EventsCallback;
import com.google.firebase.appcheck.internal.util.Logger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventViewModel extends ViewModel implements EventsCallback {

    private final MutableLiveData<Event> selectedEvent = new MutableLiveData<>();
    private User currentUser;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference eventStorageReference;
    private FirebaseStorage firebaseStorage;
    private DocumentReference userReference;
    private final MutableLiveData<List<Event>> userWaitList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> userOwnedList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> userEventList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> userPendingList = new MutableLiveData<>(new ArrayList<>());


    public LiveData<List<Event>> getUserPendingList() {
        return userPendingList;
    }

    public LiveData<List<Event>> getUserEventList() {
        return userEventList;
    }

    public LiveData<List<Event>> getUserWaitList() {
        return userWaitList;
    }
    public LiveData<List<Event>> getUserOwnedList() {
        return userOwnedList;
    }

    /**
     * Adds a new event to the current event list and updates the LiveData.
     *
     * @param event The event to add.
     */
    private void addToPendingList(Event event) {
        List<Event> currentList = userPendingList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(event);
        userPendingList.setValue(currentList); // Trigger observers
    }

    /**
     * Remove an event from the current event list and updates the LiveData.
     *
     * @param event The event to remove.
     */
    private void removeFromPendingList(Event event) {
        List<Event> currentList = userPendingList.getValue();
        if (currentList != null && currentList.contains(event)) {
            currentList.remove(event);
            userPendingList.setValue(currentList); // Trigger observers
        }
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
    private void addToWaitList(Event event) {
        List<Event> currentList = userWaitList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(event);
        userWaitList.setValue(currentList); // Trigger observers
    }

    /**
     * Remove an event from the current event list and updates the LiveData.
     *
     * @param event The event to remove.
     */
    private void removeFromWaitList(Event event) {
        List<Event> currentList = userWaitList.getValue();
        if (currentList != null && currentList.contains(event)) {
            currentList.remove(event);
            userWaitList.setValue(currentList); // Trigger observers
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

    public void leaveList(Event event){
        if(getUserPendingList().getValue().contains(event)){
            setSelectedEvent(event);
            rejectEvent();
        }
        else if(getUserWaitList().getValue().contains(event)){
            setSelectedEvent(event);
            leaveWaitlist();
        }
        else if(getUserEventList().getValue().contains(event)){
            setSelectedEvent(event);
            leaveEvent();
        }
    }

    public void getUserEventlist() {
        userEventList.setValue(new ArrayList<>()); // Clear the current list
        userReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<DocumentReference> docRefs = (List<DocumentReference>) documentSnapshot.get("events");

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


    /**
     * Fetch all events the user is registered in and update LiveData.
     */
    public void getUserWaitlist() {
        userWaitList.setValue(new ArrayList<>()); // Clear the current list
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
                                                addToWaitList(event);
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

    /**
     * Fetch all events the user is registered in and update LiveData.
     */
    public void getUserPendinglist() {
        userPendingList.setValue(new ArrayList<>()); // Clear the current list
        userReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<DocumentReference> docRefs = (List<DocumentReference>) documentSnapshot.get("pending");

                        if (docRefs != null) {
                            for (DocumentReference reference : docRefs) {
                                reference.get()
                                        .addOnSuccessListener(innerDoc -> {
                                            if (innerDoc.exists()) {
                                                Event event = innerDoc.toObject(Event.class);
                                                addToPendingList(event);
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
                                        addToWaitList(newEvent); // Add directly to the list
                                    }
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error adding item to the list", Toast.LENGTH_SHORT).show();
                });
    }

    public void enterUser(){ // Add the current user as an entrant to the selected event
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(getSelectedEvent().getValue().FirebaseID);


        batch.update(userReference, "pending", FieldValue.arrayRemove(eventUsers)); // remove the event from the user's pending events
        batch.update(eventUsers, "pending", FieldValue.arrayRemove(userReference)); // remove the event from the user's pending events

        batch.update(userReference, "events", FieldValue.arrayUnion(eventUsers));

        batch.update(eventUsers, "entrants", FieldValue.arrayUnion(userReference));




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
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error adding item to the list", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Leave an event as a user
     */
    public void leaveEvent(){
        // Leave an event as a user
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID());

        batch.update(userReference, "events", FieldValue.arrayRemove(eventUsers));
        batch.update(eventUsers, "entrants", FieldValue.arrayRemove(userReference));
        batch.update(eventUsers, "cancelled", FieldValue.arrayUnion(userReference));

        batch.commit() // remove event from user and user from event atomically
                .addOnSuccessListener(aVoid -> {
                    firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID()).get() // get the present event's firebase id from its local copy
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        removeFromWaitList(getSelectedEvent().getValue()); // Remove from list
                                    }
                                }
                            });
                    //Toast.makeText(getContext(), "Left the event!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error leaving the event", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Leave an event as a user
     */
    public void rejectEvent(){
        // Leave an event as a user
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID());

        batch.update(userReference, "pending", FieldValue.arrayRemove(eventUsers)); // remove the event from the user's pending events
        //batch.update(eventUsers, "entrants", FieldValue.arrayRemove(userReference));
        batch.update(eventUsers, "pending", FieldValue.arrayRemove(userReference)); // remove the user from the event's pending list
        batch.update(eventUsers, "cancelled", FieldValue.arrayUnion(userReference)); // add the user to the event's cancelled list

        batch.commit() // remove event from user and user from event atomically
                .addOnSuccessListener(aVoid -> {
                    firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID()).get() // get the present event's firebase id from its local copy
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        removeFromPendingList(getSelectedEvent().getValue()); // Remove from list
                                    }
                                }
                            });
                    //Toast.makeText(getContext(), "Left the event!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext(), "Error leaving the event", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Leave an event as a user
     */
    public void leaveWaitlist(){
        // Leave an event as a user
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID());

        batch.update(userReference, "waitList", FieldValue.arrayRemove(eventUsers));
        batch.update(eventUsers, "waitList", FieldValue.arrayRemove(userReference));

        batch.commit() // remove event from user and user from event atomically
                .addOnSuccessListener(aVoid -> {
                    firebaseFirestore.collection("events").document(getSelectedEvent().getValue().getFirebaseID()).get() // get the present event's firebase id from its local copy
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        removeFromWaitList(getSelectedEvent().getValue()); // Remove from list
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

            firebaseFirestore.collection("facilities").document(event.getFacilityID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the current events list
                            ArrayList<String> events = (ArrayList<String>) documentSnapshot.get("events");
                            if (events != null && events.contains(event.getFirebaseID())) {
                                events.remove(event.getFirebaseID()); // Remove the eventId

                                // Update the document with the modified list
                                firebaseFirestore.collection("facilities").document(event.getFacilityID())
                                        .update("events", events)
                                        .addOnSuccessListener(aVoid -> Log.d(Logger.TAG, "Event " + event.getFirebaseID() + " removed successfully"))
                                        .addOnFailureListener(e -> Log.e(Logger.TAG, "Error removing event " + event.getFirebaseID(), e));
                            } else {
                                Log.d(Logger.TAG, "Event " + event.getFirebaseID() + " not found in the list.");
                            }
                        } else {
                            Log.d(Logger.TAG, "Facility document does not exist.");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(Logger.TAG, "Error retrieving facility document", e));

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

    public void performDraw() {
        firebaseFirestore = FirebaseFirestore.getInstance();


        if (getSelectedEvent().getValue().getFirebaseID() == null || getSelectedEvent().getValue().getFirebaseID().isEmpty()) {
            Log.e(TAG, "Event Document ID is null or empty.");
            return;
        }

        
        // Reference to the event document
        DocumentReference eventDocRef = getEventDocumentReference();

        // Fetch the event document from Firestore
        eventDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the array of DocumentReferences
                        ArrayList<DocumentReference> entrantRefs = (ArrayList<DocumentReference>) documentSnapshot.get("waitList");

                        if (entrantRefs != null) {
                            Log.d("Firestore", "Entrants: " + entrantRefs);
                            getEventCapacityAndSelectEntrants(entrantRefs);
                        } else {
                            Log.d("Firestore", "No entrants found.");
                        }
                    } else {
                        Log.d("Firestore", "Document does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error retrieving document", e);
                });
    }

    private void getEventCapacityAndSelectEntrants(List<DocumentReference> waitListRefs) {
        // Fetch the event's capacity from Firestore

        int capacity = getSelectedEvent().getValue().capacity != null ? getSelectedEvent().getValue().capacity : 100; // Default capacity if null

        // Select random entrants based on capacity
        List<DocumentReference> selectedEntrantRefs = selectRandomEntrants(waitListRefs, capacity);

        // Store selected entrants in Firestore
        storeSelectedEntrants(selectedEntrantRefs, waitListRefs);

    }

    private List<DocumentReference> selectRandomEntrants(List<DocumentReference> entrantRefs, int numberOfEntrantsToSelect) {
        if (entrantRefs.isEmpty()) {
            Log.w(TAG, "The entrant references list is empty.");
            return new ArrayList<>();
        }

        if (numberOfEntrantsToSelect >= entrantRefs.size()) {
            Log.w(TAG, "Requested number of entrants exceeds or equals the available entrants. Returning all entrants.");
            return entrantRefs;
        }

        List<DocumentReference> shuffledEntrantRefs = new ArrayList<>(entrantRefs);
        Collections.shuffle(shuffledEntrantRefs);
        return new ArrayList<DocumentReference>(shuffledEntrantRefs.subList(0, numberOfEntrantsToSelect));
    }

    private void storeSelectedEntrants(List<DocumentReference> selectedEntrantRefs, List<DocumentReference> allEntrantRefs) {
        if (selectedEntrantRefs.isEmpty()) {
            Log.w(TAG, "No entrants to store.");
            return;
        }

        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference eventUsers = getEventDocumentReference();
        String notification = "Invited to" + getSelectedEvent().getValue().getName();
        String rejection = "Draw failed for" + getSelectedEvent().getValue().getName();

        List<DocumentReference> cancelled = new ArrayList<>(allEntrantRefs);
        cancelled.removeAll(selectedEntrantRefs);

        for (DocumentReference entrantRef : cancelled) {
            batch.update(entrantRef, "waitList", FieldValue.arrayRemove(eventUsers)); // remove from user's waitList
            batch.update(entrantRef, "notifications", FieldValue.arrayUnion(rejection)); // add rejection to user's notification stack
        }

        for (DocumentReference entrantRef : selectedEntrantRefs) {
            batch.update(entrantRef, "pending", FieldValue.arrayUnion(eventUsers)); // add to user's events
            batch.update(entrantRef, "waitList", FieldValue.arrayRemove(eventUsers)); // remove from user's waitList
            batch.update(entrantRef, "notifications", FieldValue.arrayUnion(notification)); // add to user's notification stack
        }




        batch.update(eventUsers, "pending", FieldValue.arrayUnion(selectedEntrantRefs.toArray())); // add to events entrants

        for (DocumentReference ref : selectedEntrantRefs) {
            batch.update(eventUsers, "waitList", FieldValue.arrayRemove(ref)); // remove from event's waitlist
        }



        batch.commit() // remove event from user and user from event atomically
                .addOnSuccessListener(aVoid -> {
                    Log.d("BatchWrite", "Invited Users");

                });

    }
}
