package com.example.marill_many_events;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private User myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String deviceId = getIntent().getStringExtra("deviceId");

        db = FirebaseFirestore.getInstance();

        // Fetch user data and pass the profile image URL to ProfileButton
        getUserData(deviceId, user -> {
            if (user != null) {
                myData = user; // Store the user data

                // Set up bottom navigation after user data is retrieved
                setupBottomNavigation();
                loadInitialFragment(); // Load the initial fragment

                // Update the profile icon in the bottom navigation
                updateProfileIcon(myData.profilePictureUrl);
            } else {
                Log.d("FirestoreData", "No data found or an error occurred.");
            }
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Handle fragment selection based on the navigation item selected
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new Homebutton(); // Home button fragment
                } else if (item.getItemId() == R.id.nav_menu) {
                    selectedFragment = new MenuButton(); // Menu button fragment
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Pass the profile picture URL to the ProfileButton fragment
                    selectedFragment = ProfileButton.newInstance(myData != null ? myData.profilePictureUrl : null);
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });
    }

    private void loadInitialFragment() {
        // Load the default selected fragment (home) when the activity starts
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Homebutton())
                .commit();
    }

    private void updateProfileIcon(String profileImageUrl) {
        Glide.with(this)
                .asBitmap() // Load as Bitmap to set as icon
                .load(profileImageUrl)
                .placeholder(R.drawable.default_profile) // Placeholder image
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the Bitmap as the icon for the profile navigation item
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setIcon(new BitmapDrawable(getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if needed
                    }
                });
    }


    public interface FirestoreCallback {
        void onCallback(User myData);
    }

    private void getUserData(String userId, FirestoreCallback callback) {
        db.collection("users")
                .document(userId) // Use userId passed to the method
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        // Manually retrieve each field
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        String profilePictureUrl = document.getString("profilePictureUrl");

                        // Create User object using the retrieved data
                        User myData = new User(name, email, phone, profilePictureUrl);
                        callback.onCallback(myData); // Return the data through the callback
                    } else {
                        Log.d("FirestoreData", "Document does not exist.");
                        callback.onCallback(null); // Handle the case where the document doesn't exist
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("FirestoreData", "Error getting document", e);
                    callback.onCallback(null); // Handle the error case
                });
    }
}
