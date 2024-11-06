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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateEventFragment extends Fragment implements EventsCallback {

    //Views
    private TextView NameField, datePickerStart, datePickerEnd, capacityField;
    private ListView waitlistList, registeredList;
    private ArrayList<Event> waitlistdataList;
    private ArrayList<Event> registereddataList;
    private EventyArrayAdapter waitlistAdapter, registeredAdapter;
    private Button createButton;

    //Variables
    Date startDate;
    Date endDate;

    //Data Storage
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

        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);


        datePicker(datePickerStart, true);
        datePicker(datePickerEnd,false);

        createButton.setOnClickListener(v-> {
                int capacity = Integer.parseInt(capacityField.getText().toString().trim());
                String eventname = NameField.getText().toString().trim();
                Event event = new Event(null, eventname, null, startDate, endDate, capacity, false);
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


    public void datePicker(TextView view, boolean isStartDate) {

        view.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

            // Show the date picker dialog
            datePicker.show(getParentFragmentManager(), "DATE_PICKER");

            // Handle the date selection
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Date selectedDate = new Date(selection);
                view.setText(datePicker.getHeaderText()); // Update the TextView with the selected date
                if(isStartDate) startDate = selectedDate; // Crude way to avoid more handlers, if boolean flag is true then this is a start date
                else endDate = selectedDate;
            });
        });
    }
}
