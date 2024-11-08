package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.os.Bundle;
import android.util.Log;

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


/**
 * HomePageActivity serves as the main activity for the application, managing the
 * display of the navigation bar and handling user profile interactions.
 * It implements the {@link NavbarListener} interface to respond to navigation events.
 */
public class HomePageActivity extends AppCompatActivity implements NavbarListener {

    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here

    /**
     * Called when the activity is starting. Initializes the activity, sets up the layout,
     * and loads the initial navigation bar fragment.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the most recent
     *                           data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        firestore = FirebaseFirestore.getInstance();

        Log.d("HomePageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());

        // Set up NavbarFragment
        NavbarFragment navbarFragment = new NavbarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbarFragment)
                .commit();
    }


    /**
     * Handles the event when the home option is selected from the navigation bar.
     * This method opens the {@link EventFragment} and passes the device ID to it.
     */
    public void onHomeSelected(){
        // Open the eventfragment when profile is selected
        EventFragment eventFragment = new EventFragment();
        Log.d(TAG, "onHomeSelected called with deviceId: " + deviceId);

        // Pass deviceId to the fragment
        Bundle args = new Bundle();
        args.putString("deviceId", deviceId);
        eventFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();

        // Log visibility status for debugging
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        FrameLayout navbarContainer = findViewById(R.id.navbar_container);
        Log.d(TAG, "Fragment Container Visibility: " + fragmentContainer.getVisibility());
        Log.d(TAG, "NavbarFragment Container Visibility: " + navbarContainer.getVisibility());
    }

    /**
     * Handles the event when the menu option is selected from the navigation bar.
     * This method opens the {@link MenuFragment} as a placeholder for the menu section.
     */
    public void onMenuSelected(){
        MenuFragment menuFragment = new MenuFragment();

        // Log the event for debugging purposes
        Log.d(TAG, "onMenuSelected called");

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .commit();
    }

    /**
     * Handles the event when the profile option is selected from the navigation bar.
     * Opens the RegistrationFragment and passes the device ID as an argument.
     */
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
                .replace(R.id.fragment_container, registrationFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();


        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        FrameLayout navbarContainer = findViewById(R.id.navbar_container);
        Log.d(TAG, "Fragment Container Visibility: " + fragmentContainer.getVisibility());
        Log.d(TAG, "NavbarFragment Container Visibility: " + navbarContainer.getVisibility());


    }
}
