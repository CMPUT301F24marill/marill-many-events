package com.example.marill_many_events.fragments;

import static androidx.test.platform.app.InstrumentationRegistry.getArguments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private TextView registrationText, waitlistText;
    private ListView waitlistList, registeredList;
    private ArrayList<Event> waitlistdataList;
    private ArrayList<Event> registereddataList;
    private EventyArrayAdapter waitlistAdapter, registeredAdapter;

    private String deviceId; // Variable to hold the device ID
    private FirebaseFirestore firestore; // Firestore instance
    private StorageReference storageReference;

    private boolean isEditMode = false; // Flag to indicate edit mode

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = getArguments() != null ? getArguments().getString("deviceId") : null; // Get device ID from arguments
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     * Initializes UI elements and sets up click listeners.
     *
     * @param view              The view returned by onCreateView.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        registereddataList = new ArrayList<Event>();
        registeredAdapter = new EventyArrayAdapter(getContext(), registereddataList);
        registeredList.setAdapter(registeredAdapter);

        waitlistdataList = new ArrayList<Event>();
        waitlistAdapter = new EventyArrayAdapter(getContext(), waitlistdataList);
        waitlistList.setAdapter(waitlistAdapter);
    }
}
