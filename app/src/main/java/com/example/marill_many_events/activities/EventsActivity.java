package com.example.marill_many_events.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.EventFragment;
import com.example.marill_many_events.fragments.RegistrationFragment;

/**
 *  Shows selected event data
 */
public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this layout exists

        String deviceId = getIntent().getStringExtra("deviceId");

        // Load the Fragment
        if (savedInstanceState == null) {
            EventFragment myFragment = new EventFragment(); // Replace with your Fragment class
            Bundle args = new Bundle();
            args.putString("deviceId", deviceId); // Put the deviceId in the Bundle
            myFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myFragment)
                    .commit();

        }
    }
}
