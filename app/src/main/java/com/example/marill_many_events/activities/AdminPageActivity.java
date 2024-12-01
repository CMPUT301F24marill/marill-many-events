package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.NavbarListener;
import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.AdminNavbarFragment;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.example.marill_many_events.fragments.EventAdminFragment;
import com.example.marill_many_events.fragments.EventFragment;
import com.example.marill_many_events.fragments.FacilitiesAdminFragment;
import com.example.marill_many_events.fragments.ImagesAdminFragment;
import com.example.marill_many_events.fragments.NavbarFragment;
import com.example.marill_many_events.fragments.OrgEventsFragment;
import com.example.marill_many_events.fragments.ProfilesAdminFragment;
import com.example.marill_many_events.fragments.RegistrationFragment;
import com.example.marill_many_events.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


/**
 * HomePageActivity serves as the main activity for the application, managing the
 * display of the navigation bar and handling user profile interactions.
 * It implements the {@link NavbarListener} interface to respond to navigation events.
 */
public class AdminPageActivity extends AppCompatActivity implements NavbarListener, Identity{

    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here
    private FirebaseStorage firebaseStorage; // Firebase Storage for images
    private boolean isOrgList;

    private Event currentEvent;

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
        firebaseStorage = FirebaseStorage.getInstance();

        Log.d("AdminPageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());

        // Set up NavbarFragment
        AdminNavbarFragment navbarFragment = new AdminNavbarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbarFragment)
                .commit();

        EventAdminFragment eventFragment = new EventAdminFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    /**
     * Check if the deviceId exists in Firestore's "facilities" collection.
     * Opens CreateEventFragment if the device ID exists, otherwise opens CreateFacilityFragment.
     */
    public void checkAndOpenFragment() {
        firestore.collection("facilities").document(deviceId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            Log.d(TAG, "Device ID exists in facilities. Opening CreateEventFragment.");
                            OrgEventsFragment orgEventsFragment = new OrgEventsFragment();

                            // Replace the current fragment in the container with MenuFragment
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, orgEventsFragment)
                                    .commit();

                        } else {
                            Log.d(TAG, "Device ID does not exist in facilities. Opening CreateFacilityFragment.");
                            CreateFacilityFragment createFacilityFragment = new CreateFacilityFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, createFacilityFragment)
                                    .addToBackStack(null)
                                    .commit();

                        }
                    }
                });
    }

    /**
     * Called when the gear icon is pressed. returns to HomePageActivity
     */
    public void openAdmin() {
        Intent intent = new Intent(AdminPageActivity.this, HomePageActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }

    /**
     * Called when the home navigation item is selected. Replaces the current fragment with
     * {@link EventFragment} and passes the device ID as an argument.
     */
    public void onFacilitiesSelected(){
        // Open the RegistrationFragment when profile is selected
        FacilitiesAdminFragment eventsFragment = new FacilitiesAdminFragment();

        Log.d(TAG, "onImageselected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventsFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    /**
     * Called when the menu navigation item is selected. Calls checkAndOpenFragment()
     */
    public void onImagesSelected(){
        // Open the RegistrationFragment when profile is selected
        ImagesAdminFragment eventsFragment = new ImagesAdminFragment();

        Log.d(TAG, "onImageselected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventsFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    /**
     * Called when the profiles navigation item is selected. Replaces the current fragment with
     * {@link RegistrationFragment} and passes the device ID as an argument.
     */
    @Override
    public void onEventsSelected() {
        // Open the RegistrationFragment when profile is selected
        EventAdminFragment eventsFragment = new EventAdminFragment();

        Log.d(TAG, "onEventselected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventsFragment) // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();

    }

    /**
     * Called when the profiles navigation item is selected. Replaces the current fragment with
     * {@link RegistrationFragment} and passes the device ID as an argument.
     */
    @Override
    public void onProfilesSelected() {
        // Open the RegistrationFragment when profile is selected
        ProfilesAdminFragment profilesFragment = new ProfilesAdminFragment();

        Log.d(TAG, "onProfilesSelected called with deviceId: " + deviceId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profilesFragment) // replace the fragment already in fragment_container
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

    public Event getCurrentEvent(){
        return currentEvent;
    }

    public void setCurrentEvent(Event event){
        currentEvent = event;
    }

    @Override
    public void onProfileSelected() {}
    @Override
    public void onHomeSelected() {}
    @Override
    public void onMenuSelected() {}
}
