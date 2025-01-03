package com.example.marill_many_events.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.WaitlistFragment;

/**
 * EventsActivity is responsible for displaying selected event data.
 * It extends {@link AppCompatActivity} and handles the initialization and
 * display of the WaitlistFragment, passing data as needed.
 */
public class EventsActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. Initializes the activity, sets up the layout,
     * and loads the {@link WaitlistFragment} if no previous state is saved.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this layout exists

        // Get the deviceId passed with the intent that started this activity.
        String deviceId = getIntent().getStringExtra("deviceId");

        // Load the WaitlistFragment if there is no saved instance state.
        if (savedInstanceState == null) {
            WaitlistFragment myFragment = new WaitlistFragment(); // Replace with your Fragment class
            Bundle args = new Bundle();
            args.putString("deviceId", deviceId); // Put the deviceId in the Bundle
            myFragment.setArguments(args);

            // Replace the fragment container with the WaitlistFragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myFragment)
                    .commit();

        }
    }
}
