package com.example.marill_many_events.activities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseUsers;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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
    private FusedLocationProviderClient fusedLocation;

    GeoPoint current_geo;

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

        fusedLocation = LocationServices.getFusedLocationProviderClient(this);
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
    /**
     * Called when the waitlist navigation item is selected. Replaces the current fragment with
     * the {@link WaitlistFragment}.
     */
    public void onwaitlistSelected(){
        // Open the eventfragment when profile is selected
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        WaitlistFragment waitlistFragment = new WaitlistFragment();
        Log.d(TAG, "onHomeSelected called with deviceId: " + deviceId);
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
    /**
     * Returns the device ID associated with the user or facility.
     */
    @Override
    public String getdeviceID(){
        return deviceId;
    }
    /**
     * Returns the Firebase Storage instance for managing files.
     */
    @Override
    public FirebaseStorage getStorage(){
        return firebaseStorage;
    }
    /**
     * Returns the Firestore instance for database interactions.
     */
    @Override
    public FirebaseFirestore getFirestore(){
        return firestore;
    }
    /**
     * Gets the current event.
     */
    public Event getCurrentEvent(){
        return currentEvent;
    }
    /**
     * Sets the current event.
     */
    public void setCurrentEvent(Event event){
        currentEvent = event;
    }
    /**
     * Sets the flag to indicate whether the user is viewing organization events.
     */
    public void setOrgList(boolean orgList){
        isOrgList = orgList;
    }
    /**
     * Gets the flag indicating whether the user is viewing organization events.
     */
    public boolean getOrgList(){
        return isOrgList;
    }

    /**
     * Sets the event document ID.
     */
    public void setEventDocumentId(String eventDocumentId) {
        this.eventDocumentId = eventDocumentId;
    }
    /**
     * Gets the event document ID.
     */
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

    /** check permissions if location permissions are accepted
     *
     */
    public void checkLocationPerms(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            getLocation();
            Log.d("Location Permission", "Location permission accepted.");
        }
    }


    /** get if location permissions are accepted
     *
     */
    public boolean getLocationPerms(){
        return !(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    /** get location and put it in current_geo
     *
     */
    public void getLocation(){
        Task<Location> locationResult = fusedLocation.getLastLocation();
        locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Use the location here (latitude and longitude)
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    current_geo = new GeoPoint(latitude, longitude);
                }
            }
        });
    }

    public GeoPoint getCurrent_geo() {
        return current_geo;
    }

    public void setCurrent_geo(GeoPoint current_geo) {
        this.current_geo = current_geo;
    }
}
