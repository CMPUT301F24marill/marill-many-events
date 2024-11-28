package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all events as a list, events can either be user's waitlist or organizer's created events
 */
public class OrgEventsFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener {


    private Event currentEvent;

    private RecyclerView waitlistList;
    private EventyArrayAdapter eventAdapter;
    private List<Event> eventItemList;
    FirebaseFacilityRegistration firebaseFacilityRegistration;


    ScanOptions options = new ScanOptions();


    private FirebaseEvents firebaseEvents;
    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    CollectionReference events;
    Facility facility;


    public OrgEventsFragment() {
        // Required empty public constructor
    }

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
        events = firestore.collection("events");
        //firebaseFacilityRegistration.getFacility(deviceId);

        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        TextView titleView = view.findViewById(R.id.waitlist_label);
        //titleView.setText(facility.getName());
        titleView.setText("My Events");
        FloatingActionButton createEvent = view.findViewById(R.id.scan);
        createEvent.setImageResource(R.drawable.plus);


        createEvent.setOnClickListener(v-> createEvent());

        // Initialize RecyclerView and CardAdapter
        waitlistList = view.findViewById(R.id.waitlist_list);
        waitlistList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        eventItemList = new ArrayList<Event>();

        // Initialize the adapter and set it to RecyclerView
        eventAdapter = new EventyArrayAdapter(eventItemList, this);
        waitlistList.setAdapter(eventAdapter);
        //eventAdapter.hideLeaveButton();

        return view;
    }

    public void getUserEvents() {
        eventItemList.clear();

        // Fetch the facility document using the deviceId
        firestore.collection("facilities")
                .document(deviceId) // Use the deviceId to get the specific facility document
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
        events.document(eventId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot eventDoc = task.getResult();
                        if (eventDoc.exists()) {
                            Event event = eventDoc.toObject(Event.class);
                            if (event != null) {
                                addToItemList(event);
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

    public void addToItemList(Event event){
        if (!eventItemList.contains(event)) {
            eventItemList.add(event);
        }
        eventAdapter.notifyDataSetChanged();
    }

    public void createEvent(){
        CreateEventFragment createEventFragment = new CreateEventFragment();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createEventFragment)
                .addToBackStack(null)
                .commit();
    }


    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(Event event) {
        HomePageActivity parentActivity = (HomePageActivity) getActivity();
        parentActivity.setCurrentEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();
    }

    public void onDeleteClick(Event event){

    }

}
