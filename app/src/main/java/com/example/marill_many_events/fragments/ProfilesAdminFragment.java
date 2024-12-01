package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all events as a list, events can either be user's waitlist or organizer's created events
 */
public class ProfilesAdminFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener{

    private Event currentEvent;
    private RecyclerView waitlistList;
    private EventyArrayAdapter eventAdapter;
    private List<Event> eventItemList;


    ScanOptions options = new ScanOptions();

    private FirebaseEvents firebaseEvents;
    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    DocumentReference user;
    //private onLeaveListener listener;

    /**
     * Default constructor for EventFragment.
     * Required to ensure proper fragment instantiation.
     */
    public ProfilesAdminFragment() {
        // Required empty public constructor
    }

    final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(getContext(), "Scan canceled", Toast.LENGTH_LONG).show();
                } else {
                    String scannedData = result.getContents();
                    Toast.makeText(getContext(), "Scanned: " + scannedData, Toast.LENGTH_LONG).show();
                    getEvent(scannedData);
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure the activity implements the required interface
        if (context instanceof Identity) {
            identity = (Identity) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement Identity Interface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserEvents();
        Log.d("FragmentLifecycle", "Fragment is now visible.");

        //addToItemList( new Event("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae"
        //        , "Event1", null, null, null, 1, false, null));
        //addToItemList( new Event("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae"
        //       , "Event9001", null, null, null, 1, false, null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        user = firestore.collection("users").document(deviceId);


        View view = inflater.inflate(R.layout.fragment_eventlist_admin, container, false);

        ImageView gearButton = view.findViewById(R.id.admin_gear);

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText(getString(R.string.lbl_all_Profiles));

        gearButton.setOnClickListener(v -> {
            AdminPageActivity parentActivity = (AdminPageActivity) getActivity();
            if (parentActivity != null) {
                // navigate to AdminPageActivity
                parentActivity.openAdmin();
            }
        });

        // Initialize RecyclerView and CardAdapter
        waitlistList = view.findViewById(R.id.waitlist_list);
        waitlistList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        eventItemList = new ArrayList<Event>();

        // Initialize the adapter and set it to RecyclerView
        eventAdapter = new EventyArrayAdapter(eventItemList, this, true);
        waitlistList.setAdapter(eventAdapter);

        return view;
    }

    /**
     * Get event details from a qr code of its firebase reference
     */
    public void getEvent(String eventID){
        firestore.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = document.toObject(Event.class);
                            registerUser(eventID);
                        }
                    }
                });
    }

    /**
     * Register a user to an event by atomically adding user to event's waitlist and event to user's events
     * @param eventID the document reference for the event in firebase
     */
    public void registerUser(String eventID){ // Register the current deviceID (user) to the given event by writing to the user and event a reference to each other
        WriteBatch batch = firestore.batch();
        DocumentReference eventUsers = firestore.collection("events").document(eventID);

        batch.update(user, "waitList", FieldValue.arrayUnion(eventUsers));
        batch.update(eventUsers, "waitList", FieldValue.arrayUnion(user));

        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    firestore.collection("events").document(eventID).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Event newEvent = documentSnapshot.toObject(Event.class);
                                    if (newEvent != null) {
                                        addToItemList(newEvent); // Add directly to the list
                                    }
                                }
                            });
                    Toast.makeText(getContext(), "Item added to the list!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding item to the list", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Get all of the events that a user is registered in and populate the adapter
     */
    public void getUserEvents(){
        eventItemList.clear();
        user.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the array of DocumentReferences
                        List<DocumentReference> docRefs = (List<DocumentReference>) documentSnapshot.get("waitList");

                        if (docRefs != null) {
                            // Iterate through the list of DocumentReferences
                            for (DocumentReference reference : docRefs) {
                                // Fetch each document using the DocumentReference
                                reference.get()
                                        .addOnSuccessListener(innerDoc -> {
                                            if (innerDoc.exists()) {
                                                Event eventIter = innerDoc.toObject(Event.class);
                                                addToItemList(eventIter);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle error in fetching the referenced document
                                            Toast.makeText(getContext(), "Error fetching referenced document", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    } else {
                        // Document doesn't exist
                        Toast.makeText(getContext(), "Document not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error in retrieving the document
                    Toast.makeText(getContext(), "Error getting document", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Add en event to the list
     */
    public void addToItemList(Event event){
        if (!eventItemList.contains(event)) {
            eventItemList.add(event);
        }
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Remove an item from the list
     */
    public void removeItemfromList(Event event){
        if (eventItemList.contains(event)) {
            eventItemList.remove(event);
        }
    }

    public Event getCurrentEvent(){
        return currentEvent;
    }

    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Leave an event as a user
     */
    public void onDeleteClick(Event event){
            // Register the current deviceID (user) to the given event by writing to the user and event a reference to each other
            WriteBatch batch = firestore.batch();
            DocumentReference eventUsers = firestore.collection("events").document(event.getQRcode());

            batch.update(user, "waitList", FieldValue.arrayRemove(eventUsers));
            batch.update(eventUsers, "waitList", FieldValue.arrayRemove(user));

            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        firestore.collection("events").document(event.getQRcode()).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Event newEvent = documentSnapshot.toObject(Event.class);
                                        if (newEvent != null) {
                                            removeItemfromList(newEvent); // Remove from list
                                            getUserEvents();
                                        }
                                    }
                                });
                        Toast.makeText(getContext(), "Left the event!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error leaving the event", Toast.LENGTH_SHORT).show();
                    });
    }


    @Override
    public void onItemClick(Event event) {
        HomePageActivity parentActivity = (HomePageActivity) getActivity();
        parentActivity.setCurrentEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();
    }
}