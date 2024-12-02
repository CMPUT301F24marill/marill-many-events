package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.example.marill_many_events.fragments.JoinedEventsFragment;
import com.example.marill_many_events.fragments.WaitlistFragment;
import com.example.marill_many_events.fragments.NavbarFragment;
import com.example.marill_many_events.NavbarListener;
import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.OrgEventsFragment;
import com.example.marill_many_events.fragments.RegistrationFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseUsers;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


/**
 * HomePageActivity serves as the main activity for the application, managing the
 * display of the navigation bar and handling user profile interactions.
 * It implements the {@link NavbarListener} interface to respond to navigation events.
 */
public class HomePageActivity extends AppCompatActivity implements NavbarListener, Identity {

    private FirebaseStorage firebaseStorage; // Firebase Storage for images
    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Store deviceId here
    private FirebaseUsers firebaseUsers;

    private boolean isOrgList;
    private String eventDocumentId;

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
        //firebaseUsers= new FirebaseUsers(firestore, firebaseStorage, deviceId, this);
        //firebaseUsers.loadUserDetails();



        Log.d("HomePageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());

        // Set up NavbarFragment
        NavbarFragment navbarFragment = new NavbarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbarFragment)
                .commit();

        JoinedEventsFragment joinedEventsFragment = new JoinedEventsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, joinedEventsFragment) // replace the fragment already in fragment_container
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
     * Called when the facility management button is pressed. Opens the respective fragment
     */
    public void openFacilityProfile() {
        CreateFacilityFragment createFacilityFragment = new CreateFacilityFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createFacilityFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when the gear icon is pressed. Opens the admin activity
     */
    public void openAdmin() {
        Intent intent = new Intent(HomePageActivity.this, AdminPageActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }

    /**
     * Called when the home navigation item is selected. Replaces the current fragment with
     * {@link WaitlistFragment} and passes the device ID as an argument.
     */
    public void onHomeSelected(){
        // Open the eventfragment when profile is selected
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        JoinedEventsFragment joinedEventsFragment = new JoinedEventsFragment();
        Log.d(TAG, "onHomeSelected called with deviceId: " + deviceId);
        isOrgList = false;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, joinedEventsFragment, "user events") // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    public void onwaitlistSelected(){
        // Open the eventfragment when profile is selected
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        WaitlistFragment waitlistFragment = new WaitlistFragment();
        Log.d(TAG, "onwailistSelected called with deviceId: " + deviceId);
        isOrgList = false;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, waitlistFragment, "user events") // replace the fragment already in fragment_container
                .addToBackStack(null) // add to back stack
                .commit();
    }

    /**
     * Called when the menu navigation item is selected. Calls checkAndOpenFragment()
     */
    public void onMenuSelected(){
        // Log the event for debugging purposes
        Log.d(TAG, "onMenuSelected called");

        checkAndOpenFragment();
    }

    /**
     * Called when the profile navigation item is selected. Replaces the current fragment with
     * {@link RegistrationFragment} and passes the device ID as an argument.
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

    public Event getCurrentEvent(){
        return currentEvent;
    }

    public void setCurrentEvent(Event event){
        currentEvent = event;
    }

    public void setOrgList(boolean orgList){
        isOrgList = orgList;
    }

    public boolean getOrgList(){
        return isOrgList;
    }


    public void setEventDocumentId(String eventDocumentId) {
        this.eventDocumentId = eventDocumentId;
    }
    public String getEventDocumentId() {
        return eventDocumentId;
    }

    @Override
    public void onFacilitiesSelected() {}
    @Override
    public void onImagesSelected() {}
    @Override
    public void onEventsSelected() {}
    @Override
    public void onProfilesSelected() {}
}
