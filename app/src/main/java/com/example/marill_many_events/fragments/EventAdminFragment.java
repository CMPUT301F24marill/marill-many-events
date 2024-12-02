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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
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
 * EventAdminFragment displays all events as a list. The events can be either the user's waitlist
 * or the organizer's created events. It allows the admin to interact with the events, such as viewing
 * details, deleting events, or deleting associated hash data.
 */
public class EventAdminFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener, AdminEventDetailsFragment.OnItemClickListener{

    private Event currentEvent;
    private RecyclerView waitlistList;
    private EventyArrayAdapter eventAdapter;
    private List<Event> eventItemList;

    private FirebaseFirestore firestore;
    private Identity identity;
    private CollectionReference user;
    private EventViewModel eventViewModel;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public EventAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the fragment is attached to its context (activity).
     * This method ensures that the activity hosting the fragment implements the necessary interface
     * to provide the required functionality.
     *
     * @param context The context to which the fragment is attached.
     * @throws ClassCastException if the context does not implement the required Identity interface.
     */
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
    /**
     * Called when the fragment is resumed and becomes visible to the user.
     * This method triggers the loading of events by calling the getEvents method.
     * It also logs a message indicating that the fragment is now visible.
     */
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
    /**
     * Called to create the view for this fragment.
     * This method inflates the layout and initializes various components,
     * including setting up the Firestore references, the ViewModel, and handling UI elements.
     *
     * @param inflater The LayoutInflater object to inflate the view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState A Bundle containing saved state information (if any) from a previous instance of the fragment.
     * @return The root view of the fragment's layout.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        firestore = identity.getFirestore();
        user = firestore.collection("events");

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        eventViewModel.setFirebaseStorage(identity.getStorage());
        eventViewModel.setFirebaseReference(firestore);

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
     * Retrieves all events from Firestore and populates the event list.
     * This method fetches event data from Firestore and updates the adapter with the fetched events.
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
     * Adds an event to the list of events and notifies the adapter to update the UI.
     *
     * @param event The event to be added to the list.
     */
    public void addToItemList(Event event){
        if (!eventItemList.contains(event)) {
            eventItemList.add(event);
        }
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Removes an event from the list and notifies the adapter to update the UI.
     *
     * @param event The event to be removed from the list.
     */
    public void removeItemfromList(Event event){
        if (eventItemList.contains(event)) {
            eventItemList.remove(event);
        }
        eventAdapter.notifyDataSetChanged();
    }
    /**
     * Retrieves the current event.
     *
     * @return The current event selected by the user.
     */
    public Event getCurrentEvent(){
        return currentEvent;
    }
    /**
     * Shows the details of the selected event by navigating to the event details fragment.
     */
    public void showEventDetails(){
        AdminEventDetailsFragment eventDetailsFragment = new AdminEventDetailsFragment(this);
        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
        Log.d("S","showing event details");
    }

    /**
     * Delete an event as admin
     * @param event: event to be deleted
     */
    public void onDeleteClick(Event event ) {
        DocumentReference eventDoc = user.document(event.getFirebaseID());
        Log.d("S", "event id: "+event.getFirebaseID());
        eventDoc.delete()
                .addOnSuccessListener(aVoid -> {
                    //remove from local list
                    removeItemfromList(event);
                    Log.d("Firebase", "Event deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "failed to delete event: " + e.getMessage());
                });
    }

    /**
     * Delete hash data
     * @param event: event who's hash data is to be removed
     */
    public void onDeleteHashDataClick(Event event ) {
        DocumentReference eventDoc = user.document(event.getFirebaseID());
        Log.d("S", "event id: "+event.getFirebaseID());
        eventDoc.update("qrcode", null)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Hash data deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "failed to delete Hashdata: " + e.getMessage());
                });
    }
    /**
     * Handles the event when an item (event) is clicked in the list.
     * Sets the selected event in the ViewModel and opens the event details screen.
     *
     * @param event The event that was clicked.
     */
    @Override
    public void onItemClick(Event event) {
        eventViewModel.setSelectedEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();
    }
}