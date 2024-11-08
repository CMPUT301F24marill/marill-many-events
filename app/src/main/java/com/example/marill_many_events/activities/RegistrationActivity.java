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
 * RegistrationActivity handles the user registration process.
 * It initializes the activity layout and loads the {@link RegistrationFragment},
 * passing the device ID to the fragment for user registration purposes.
 */


public class RegistrationActivity extends AppCompatActivity implements Identity {

    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here
    private FirebaseStorage firebaseStorage; // Firebase Storage for images


    /**
     * Called when the activity is starting. This method sets up the layout and
     * loads the {@link RegistrationFragment}, passing any necessary data such as the
     * device ID for registration.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied by onSaveInstanceState.
     *                           Otherwise, it is null.
     */
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