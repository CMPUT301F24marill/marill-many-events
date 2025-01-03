package com.example.marill_many_events.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;


public class WaitlistFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener{

    private Event currentEvent;
    private RecyclerView waitlistList;
    private RecyclerView pendinglist;

    private EventyArrayAdapter eventAdapter;
    private EventyArrayAdapter pendingAdapter;
    private List<Event> eventItemList;
    private List<Event> pendingEventsList;
    private HomePageActivity parentActivity;
    private EventViewModel eventViewModel;
    private User user;

    ScanOptions options = new ScanOptions();

    private FirebaseEvents firebaseEvents;
    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    DocumentReference userReference;
    //private onLeaveListener listener;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public WaitlistFragment() {
        // Required empty public constructor
    }
    /**
     * Registers a QR code scan result handler using an `ActivityResultLauncher`.
     */
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

    /**
     * Attaches the fragment to its parent context and ensures the context implements the `Identity` interface.
     *
     * @param context The context to attach the fragment to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (HomePageActivity) getActivity();
        // Make sure the activity implements the required interface
        if (context instanceof Identity) {
            identity = (Identity) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement Identity Interface");
        }
    }
    /**
     * Called when the fragment becomes visible to the user.
     * Logs the visibility state.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentLifecycle", "Fragment is now visible.");
    }
    /**
     * Inflates the fragment's layout, initializes UI components, and sets up RecyclerViews for waitlisted and pending events.
     *
     * @param inflater The LayoutInflater used to inflate the layout.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        userReference = firestore.collection("users").document(deviceId);

        eventViewModel.setUserReference(userReference);
        eventViewModel.setFirebaseStorage(identity.getStorage());
        eventViewModel.setFirebaseReference(firestore);

        getUser();


        FloatingActionButton scanButton = view.findViewById(R.id.scan);

        scanButton.setOnClickListener(v -> {
            // Launch the QR scanner using the ActivityResultLauncher
            Intent intent = new Intent(getActivity(), com.journeyapps.barcodescanner.CaptureActivity.class);
            qrCodeLauncher.launch(options);
        });

        // Initialize RecyclerView and CardAdapter
        waitlistList = view.findViewById(R.id.waitlist_list);
        waitlistList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        eventItemList = new ArrayList<Event>();

        // Initialize the adapter and set it to RecyclerView
        eventAdapter = new EventyArrayAdapter(eventItemList, this, true);
        waitlistList.setAdapter(eventAdapter);


        // Initialize RecyclerView and CardAdapter
        pendinglist = view.findViewById(R.id.pending_list);
        TextView pendinglabel = view.findViewById(R.id.pending_label);

        pendinglist.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        pendingEventsList = new ArrayList<Event>();

        // Initialize the adapter and set it to RecyclerView
        pendingAdapter = new EventyArrayAdapter(pendingEventsList, this, true);
        pendinglist.setAdapter(pendingAdapter);



        eventViewModel.getUserWaitlist();
        eventViewModel.getUserPendinglist();


        eventViewModel.getUserPendingList().observe(getViewLifecycleOwner(), updatedList ->{
            pendinglist.setVisibility(View.VISIBLE);
            pendinglabel.setVisibility(View.VISIBLE);
            pendingEventsList.clear();
            pendingEventsList.addAll(updatedList);
            pendingAdapter.notifyDataSetChanged();
        });

        eventViewModel.getUserWaitList().observe(getViewLifecycleOwner(), updatedList -> {
            eventItemList.clear(); // Clear the old list
            eventItemList.addAll(updatedList); // Add the updated list
            eventAdapter.notifyDataSetChanged(); // Notify the adapter of the changes
        });

        return view;
    }

    /**
     * Fetches event details from Firestore using the scanned QR code.
     *
     * @param eventID The ID of the event document.
     */
    public void getEvent(String eventID){
        firestore.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = document.toObject(Event.class);
                            eventViewModel.setSelectedEvent(event);
                            Log.d("FragmentLifecycle", "Opening details.");
                            showEventDetails();                        }
                    }
                });
    }

    /**
     * Removes an event from the user's list.
     *
     * @param event The event to remove.
     */
    public void onDeleteClick(Event event){
        Log.d("FragmentLifecycle", "Deleting Event.");
        eventViewModel.leaveList(event);
    }

    /**
     * Navigates to the EventDetailsFragment to display the details of a selected event.
     */
    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Handles the click event for an event item in the RecyclerView.
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
     * Fetches the current user's details from Firestore and updates the ViewModel.
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