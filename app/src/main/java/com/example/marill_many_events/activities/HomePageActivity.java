package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.os.Bundle;
import android.util.Log;

import com.example.marill_many_events.fragments.NavbarFragment;
import com.example.marill_many_events.NavbarListener;
import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.RegistrationFragment;

import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity implements NavbarListener {

    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    private String deviceId; // Store deviceId here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection("users");

        Log.d("HomePageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());


        // Set up NavbarFragment
        NavbarFragment navbarFragment = new NavbarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbarFragment)
                .commit();


    }

    @Override
    public void onProfileSelected() {
        // Open the RegistrationFragment when profile is selected
        RegistrationFragment registrationFragment = new RegistrationFragment();


        Log.d(TAG, "onProfileSelected called with deviceId: " + deviceId);

        // Pass deviceId to the fragment
        Bundle args = new Bundle();
        args.putString("deviceId", deviceId);
        registrationFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registrationFragment)
                .addToBackStack(null) // Optional: add to back stack
                .commit();


        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        FrameLayout navbarContainer = findViewById(R.id.navbar_container);
        Log.d(TAG, "Fragment Container Visibility: " + fragmentContainer.getVisibility());
        Log.d(TAG, "NavbarFragment Container Visibility: " + navbarContainer.getVisibility());


    }
}
