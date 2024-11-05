package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.os.Bundle;
import android.util.Log;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.fragments.EventFragment;
import com.example.marill_many_events.fragments.MenuFragment;
import com.example.marill_many_events.fragments.NavbarFragment;
import com.example.marill_many_events.NavbarListener;
import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.RegistrationFragment;

import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * HomePageActivity serves as the main activity for the application, managing the
 * display of the navigation bar and handling user profile interactions.
 * It implements the NavbarListener interface to respond to navigation events.
 */
public class HomePageActivity extends AppCompatActivity implements NavbarListener, Identity{

    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here
    private FirebaseStorage firebaseStorage; // Firebase Storage for images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        Log.d("HomePageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());

        // Set up NavbarFragment
        NavbarFragment navbarFragment = new NavbarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbarFragment)
                .commit();
    }


    /**
     * Handles the event when the home option is selected from the navigation bar.
     * It opens the list of events
     */
    public void onHomeSelected(){
        // Open the eventfragment when profile is selected
        EventFragment eventFragment = new EventFragment();
        Log.d(TAG, "onHomeSelected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    /**
     * Handles the event when the menu option is selected from the navigation bar.
     * It opens a placeholder.
     */
    public void onMenuSelected(){
        MenuFragment menuFragment = new MenuFragment();

        Log.d(TAG, "onMenuSelected called");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .commit();
    }

    /**
     * Handles the event when the profile option is selected from the navigation bar.
     * It opens the RegistrationFragment and passes the deviceId as an argument.
     */
    @Override
    public void onProfileSelected() {
        // Open the RegistrationFragment when profile is selected
        RegistrationFragment registrationFragment = new RegistrationFragment();


        Log.d(TAG, "onProfileSelected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registrationFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();

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
