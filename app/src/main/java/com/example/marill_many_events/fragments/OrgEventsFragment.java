package com.example.marill_many_events.fragments;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class OrgEventsFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener {


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
    CollectionReference events;


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


        View view = inflater.inflate(R.layout.home, container, false);

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

        return view;
    }

//    public void getEvent(String eventID){
//        firestore.collection("events").document(eventID)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Event event = document.toObject(Event.class);
//                            registerUser(eventID);
//                        }
//                    }
//                });
//    }

//    public void registerUser(String eventID){ // Register the current deviceID (user) to the given event by writing to the user and event a reference to each other
//        WriteBatch batch = firestore.batch();
//        DocumentReference eventUsers = firestore.collection("events").document(eventID);
//
//        batch.update(user, "waitList", FieldValue.arrayUnion(eventUsers));
//        batch.update(eventUsers, "waitList", FieldValue.arrayUnion(user));
//
//        batch.commit()
//                .addOnSuccessListener(aVoid -> {
//                    firestore.collection("events").document(eventID).get()
//                            .addOnSuccessListener(documentSnapshot -> {
//                                if (documentSnapshot.exists()) {
//                                    Event newEvent = documentSnapshot.toObject(Event.class);
//                                    if (newEvent != null) {
//                                        addToItemList(newEvent); // Add directly to the list
//                                    }
//                                }
//                            });
//                    Toast.makeText(getContext(), "Item added to the list!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getContext(), "Error adding item to the list", Toast.LENGTH_SHORT).show();
//                });
//    }

    public void getUserEvents(){
        eventItemList.clear();
        events.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the array of DocumentReferences
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                addToItemList(document.toObject(Event.class));
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

}
