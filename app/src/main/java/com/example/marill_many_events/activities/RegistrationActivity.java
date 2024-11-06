package com.example.marill_many_events.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.RegistrationFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * RegistrationActivity handles user registration.
 * It loads the RegistrationFragment and passes the device ID to it.
 */


public class RegistrationActivity extends AppCompatActivity implements Identity {

    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here
    private FirebaseStorage firebaseStorage; // Firebase Storage for images


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this layout exists
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();




        String deviceId = getIntent().getStringExtra("deviceId");

        // Load the Fragment
        if (savedInstanceState == null) {
            RegistrationFragment myFragment = new RegistrationFragment(); // Replace with your Fragment class
            Bundle args = new Bundle();
            args.putString("deviceId", deviceId); // Put the deviceId in the Bundle
            myFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myFragment)
                    .commit();

        }
    }

    @Override
    public String getdeviceID(){
        return deviceId;
    }

    @Override
    public FirebaseStorage getStorage(){
        return firebaseStorage;
    }

    @Override
    public FirebaseFirestore getFirestore(){
        return firestore;
    }
}