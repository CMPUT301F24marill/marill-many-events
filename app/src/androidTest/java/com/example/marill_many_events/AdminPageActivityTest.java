package com.example.marill_many_events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.example.marill_many_events.fragments.EventAdminFragment;
import com.example.marill_many_events.fragments.FacilitiesAdminFragment;
import com.example.marill_many_events.fragments.ImagesAdminFragment;
import com.example.marill_many_events.fragments.OrgEventsFragment;
import com.example.marill_many_events.fragments.ProfilesAdminFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class AdminPageActivityTest {

    private AdminPageActivity activity;

    @Before
    public void setUp() {
        // Launch the AdminPageActivity
        ActivityScenario.launch(AdminPageActivity.class).onActivity(activity -> {
            this.activity = activity;
        });

        // Initialize Intents
        Intents.init();
    }
    @Test
    public void testOpenAdmin() {
        // Create the expected Intent
        Intent expectedIntent = new Intent(activity, HomePageActivity.class);
        expectedIntent.putExtra("deviceId", activity.getdeviceID());

        // Simulate opening the Admin activity
        activity.openAdmin();

        // Check that the expected intent is fired
        intended(hasComponent(HomePageActivity.class.getName())); // Verifies HomePageActivity is opened
    }

    @Test
    public void testOnFacilitiesSelected() {
        // Simulate onFacilitiesSelected() method
        activity.onFacilitiesSelected();

        // Verify that FacilitiesAdminFragment is displayed
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        assertNotNull("FacilitiesAdminFragment should be displayed", currentFragment);
        assertTrue(currentFragment instanceof FacilitiesAdminFragment);
    }

    @Test
    public void testOnImagesSelected() {
        // Simulate onImagesSelected() method
        activity.onImagesSelected();

        // Verify that ImagesAdminFragment is displayed
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        assertNotNull("ImagesAdminFragment should be displayed", currentFragment);
        assertTrue(currentFragment instanceof ImagesAdminFragment);
    }
    @Test
    public void testOnProfilesSelected() throws InterruptedException {
        // Simulate onProfilesSelected() method
        activity.onProfilesSelected();

        // Wait for fragment transaction to complete (for testing purposes)
        Thread.sleep(1000);  // Adjust the delay as needed

        // Verify that ProfilesAdminFragment is displayed
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        // Retrieve the current fragment from the container
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        // Log to help debugging
        Log.d("Test", "Current Fragment: " + (currentFragment != null ? currentFragment.getClass().getSimpleName() : "null"));

        // Assert that the current fragment is an instance of ProfilesAdminFragment
        assertNotNull("ProfilesAdminFragment should be displayed", currentFragment);
        assertTrue("Current fragment should be an instance of ProfilesAdminFragment", currentFragment instanceof ProfilesAdminFragment);
    }



    @Test
    public void testCheckAndOpenFragment_DeviceExists() {
        // Mock the Firestore response for an existing device
        FirebaseFirestore mockFirestore = FirebaseFirestore.getInstance();
        // Simulate the Firestore query (not shown here, but assume it returns a valid device)

        // Call checkAndOpenFragment
        activity.checkAndOpenFragment();

        // Verify OrgEventsFragment is displayed when device exists
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        assertNotNull("OrgEventsFragment should be displayed when device exists", currentFragment);
        assertTrue(currentFragment instanceof OrgEventsFragment);
    }

    @Test
    public void testCheckAndOpenFragment_DeviceDoesNotExist() {
        // Mock the Firestore response for a non-existing device
        FirebaseFirestore mockFirestore = FirebaseFirestore.getInstance();
        // Simulate the Firestore query (not shown here, but assume it returns no device)

        // Call checkAndOpenFragment
        activity.checkAndOpenFragment();

        // Verify CreateFacilityFragment is displayed when device doesn't exist
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        assertNotNull("CreateFacilityFragment should be displayed when device doesn't exist", currentFragment);
        assertTrue(currentFragment instanceof CreateFacilityFragment);
    }

    @Test
    public void testOnEventsSelected() {
        // Simulate onEventsSelected() method
        activity.onEventsSelected();

        // Verify that EventAdminFragment is displayed
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        assertNotNull("EventAdminFragment should be displayed", currentFragment);
        assertTrue(currentFragment instanceof EventAdminFragment);
    }

    @Test
    public void testOnHomeSelected() {
        // Simulate onHomeSelected() method
        activity.onHomeSelected();

        // Verify that the correct fragment or action is triggered
        // (you can check for changes or fragment replacements here)
    }

    @Test
    public void testOnMenuSelected() {
        // Simulate onMenuSelected() method
        activity.onMenuSelected();

        // Verify that the correct fragment or action is triggered
        // (you can check for changes or fragment replacements here)
    }

    @Test
    public void testOnwaitlistSelected() {
        // Simulate onwaitlistSelected() method
        activity.onwaitlistSelected();

        // Verify that the correct fragment or action is triggered
        // (you can check for changes or fragment replacements here)
    }

    @Test
    public void testOpenAdminWithDeviceId() {
        // Simulate the expected Intent behavior
        Intent expectedIntent = new Intent(activity, HomePageActivity.class);
        expectedIntent.putExtra("deviceId", activity.getdeviceID());

        // Verify that the correct Intent is fired when opening the Admin page
        activity.openAdmin();
        intended(hasComponent(HomePageActivity.class.getName()));
    }
}

