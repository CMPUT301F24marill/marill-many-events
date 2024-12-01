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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all events as a list, events can either be user's waitlist or organizer's created events
 */
public class EventAdminFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener{

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
    private CollectionReference user;
    //private onLeaveListener listener;

    /**
     * Default constructor for EventFragment.
     * Required to ensure proper fragment instantiation.
     */
    public EventAdminFragment() {
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
        getEvents();
        Log.d("FragmentLifecycle", "Fragment is now visible.");

        //addToItemList( new Event("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae"
        //        , "Event1", null, null, null, 1, false, null));
        //addToItemList( new Event("https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o/event_posters%2Feventposters%2Fimage_1730935799965_05ae8f93-85df-4308-aa48-cdd23874342a.jpg.jpg?alt=media&token=81e266fb-bc73-4489-9f10-8f893e3260ae"
        //       , "Event9001", null, null, null, 1, false, null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        firestore = identity.getFirestore();
        user = firestore.collection("events");


        View view = inflater.inflate(R.layout.fragment_eventlist_admin, container, false);

        ImageView gearButton = view.findViewById(R.id.admin_gear);

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText(getString(R.string.lbl_all_Events));

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
     * Get all of the events and populate the adapter
     */
    public void getEvents(){
        eventItemList.clear();
        user.get()
                .addOnCompleteListener(documentSnapshot -> {
                    if (documentSnapshot.isSuccessful()) {
                        QuerySnapshot docRefs = documentSnapshot.getResult();
                        for (DocumentSnapshot reference : docRefs) {
                            // Fetch each document using the DocumentReference
                            Event eventIter = reference.toObject(Event.class);
                            if(eventIter != null){
                                eventIter.setID(reference.getId());
                                addToItemList(eventIter);
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
        eventAdapter.notifyDataSetChanged();
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
     * Delete an event as admin
     * @param event: event to be deleted
     */
    public void onDeleteClick(Event event ) {
        DocumentReference eventDoc = user.document(event.getID());
        Log.d("S", "event id: "+event.getID());
        eventDoc.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "attempting Event delete");
                    //remove from local list
                    removeItemfromList(event);
                    Log.d("Firebase", "Event deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "failed to delete event: " + e.getMessage());
                });
    }

    @Override
    public void onItemClick(Event event) {
        /*HomePageActivity parentActivity = (HomePageActivity) getActivity();
        parentActivity.setCurrentEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();*/
    }
}