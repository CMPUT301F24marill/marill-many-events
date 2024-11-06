package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CreateEventFragment extends Fragment implements EventsCallback {

    private TextView NameField, waitlistText;
    private ListView waitlistList, registeredList;
    private ArrayList<Event> waitlistdataList;
    private ArrayList<Event> registereddataList;
    private EventyArrayAdapter waitlistAdapter, registeredAdapter;
    private Button createButton;

    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    private FirebaseEvents firebaseEvents;

    public CreateEventFragment() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        storageReference = identity.getStorage().getReference("event_posters");
        firebaseEvents = new FirebaseEvents(firestore, storageReference, deviceId, this);
    }

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater           The LayoutInflater used to inflate the view.
     * @param container          The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     * @return The view for this fragment.
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_createevent, container, false);
    }

    /**
     * Initializes UI elements and sets up click listeners.
     *
     * @param view              The view returned by onCreateView.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NameField = view.findViewById(R.id.NameField);
        createButton = view.findViewById(R.id.create);


        createButton.setOnClickListener(v-> {
                Event event = new Event(null, NameField.getText().toString().trim(), null, null, null, null, false);
                firebaseEvents.createEvent(event);
                });

//        registereddataList = new ArrayList<Event>();
//        registeredAdapter = new EventyArrayAdapter(getContext(), registereddataList);
//        registeredList.setAdapter(registeredAdapter);
//
//        waitlistdataList = new ArrayList<Event>();
//        waitlistAdapter = new EventyArrayAdapter(getContext(), waitlistdataList);
//        waitlistList.setAdapter(waitlistAdapter);
    }


    public void onEventCreate(String documentID){}
    public void onEventDelete(){}
    public void joinEvent(){}
    public void getEvent(Event event){}
}
