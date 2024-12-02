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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;
import com.example.marill_many_events.models.User;
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
 * A fragment that displays a list of events created by the organizer.
 * Organizers can view event details, create new events, and delete events.
 */
public class OrgEventsFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener, FacilityCallback {


    private Event currentEvent;

    private RecyclerView waitlistList;
    private EventyArrayAdapter eventAdapter;
    private List<Event> eventItemList;
    FirebaseFacilityRegistration firebaseFacilityRegistration;
    private EventViewModel eventViewModel;
    DocumentReference userReference;
    private User user;

    ScanOptions options = new ScanOptions();


    private FirebaseFirestore firestore;
    private String deviceId;

    private StorageReference storageReference;
    private Identity identity;
    CollectionReference events;
    CollectionReference facility;
    private List<String> facilityEvents;

    /**
     * Default constructor for OrgEventsFragment.
     * Required to ensure proper fragment instantiation.
     */
    public OrgEventsFragment() {
        // Required empty public constructor
    }
    /**
     * Attaches the fragment to the activity and checks if the activity implements the Identity interface.
     *
     * @param context The context to attach the fragment to.
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
     * Called when the fragment is resumed. Fetches the organizer's events.
     */
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
    /**
     * Inflates the layout for this fragment, initializes UI components, and sets up the event list.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        events = firestore.collection("events");
        facility = firestore.collection("facilities");
        userReference = firestore.collection("users").document(deviceId);

        eventViewModel.setUserReference(userReference);
        eventViewModel.setFirebaseStorage(identity.getStorage());
        eventViewModel.setFirebaseReference(firestore);
        getUser();


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
        eventAdapter = new EventyArrayAdapter(eventItemList, this, false);
        waitlistList.setAdapter(eventAdapter);
        //eventAdapter.hideLeaveButton();

        eventViewModel.getUserOwnedEvents();

        eventViewModel.getUserOwnedList().observe(getViewLifecycleOwner(), updatedList -> {
            eventItemList.clear();
            eventItemList.addAll(updatedList); // Add the updated list
            eventAdapter.notifyDataSetChanged(); // Notify the adapter of the changes
        });

        return view;
    }
    /**
     * Fetches the user's events from Firestore.
     */
    public void getUserEvents() {

    }



//    public void addToItemList(Event event){
//        if (!eventItemList.contains(event)) {
//            eventItemList.add(event);
//            Log.d("FragmentLifecycle", event.getFirebaseID());
//
//        }
//        eventAdapter.notifyDataSetChanged();
//    }
    /**
     * Navigates to the CreateEventFragment to create a new event.
     */
    public void createEvent(){
        CreateEventFragment createEventFragment = new CreateEventFragment();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createEventFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Navigates to the EventDetailsFragment to display event details.
     */
    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

//    @Override
//    public void onItemClick(Event event) {
//        HomePageActivity parentActivity = (HomePageActivity) getActivity();
//        parentActivity.setCurrentEvent(event);
//        Log.d("FragmentLifecycle", "Opening details.");
//        showEventDetails();
//    }
    /**
     * Handles item click events to view event details.
     *
     * @param event The clicked event.
     */
    @Override
    public void onItemClick(Event event) {
        eventViewModel.setSelectedEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();
    }


    /**
     * Deletes an event from Firestore.
     *
     * @param event The event to delete.
     */
    public void onDeleteClick(Event event){
        if(event != null)
            eventViewModel.deleteEvent(event);
    }

    /**
     * Handles the callback when a facility is loaded.
     *
     * @param facility The loaded facility.
     */
    @Override
    public void onFacilityLoaded(Facility facility) {
    }

    /**
     * Handles the callback when a facility is registered.
     */
    @Override
    public void onFacilityRegistered() {

    }
    /**
     * Handles the callback when a facility is updated.
     */
    @Override
    public void onFacilityUpdated() {

    }
    /**
     * Fetches the current user's data from Firestore and updates the ViewModel.
     */
    public void getUser(){
        userReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                eventViewModel.setCurrentUser(user);
            } else {
                Log.d("Firestore", "No such user");
            }
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Error getting user: ", e);
        });
    }
}
