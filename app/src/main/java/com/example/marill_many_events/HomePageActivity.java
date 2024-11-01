package com.example.marill_many_events;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity implements NavbarListener {

    private FirebaseFirestore db;
    private String deviceId; // Store deviceId here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deviceId = getIntent().getStringExtra("deviceId"); // Retrieve deviceId

        db = FirebaseFirestore.getInstance();

        PlaceholderFragment placeholderFragment = new PlaceholderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, placeholderFragment)
                .commit();

        Log.d("HomePageActivity", "Fragment Container Visibility: " + findViewById(R.id.fragment_container).getVisibility());




        // Set up Navbar
        Navbar navbar = new Navbar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navbar_container, navbar)
                .commit();


    }

    @Override
    public void onProfileSelected(String deviceId) {
        // Open the RegistrationFragment when profile is selected
        RegistrationFragment registrationFragment = new RegistrationFragment();


        Log.d(TAG, "onProfileSelected called with deviceId: " + deviceId);

        // Pass deviceId to the fragment
        Bundle args = new Bundle();
        args.putString("deviceId", deviceId);
        registrationFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, registrationFragment)
                .addToBackStack(null) // Optional: add to back stack
                .commit();


        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        FrameLayout navbarContainer = findViewById(R.id.navbar_container);
        Log.d(TAG, "Fragment Container Visibility: " + fragmentContainer.getVisibility());
        Log.d(TAG, "Navbar Container Visibility: " + navbarContainer.getVisibility());


    }
}
