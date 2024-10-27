package com.example.marill_many_events;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Button logInButton = findViewById(R.id.loginButton);
        //Button registerButton = findViewById(R.id.registerButton);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Set up the login button to navigate to the actual login page
        logInButton.setOnClickListener(v -> {
            checkDeviceId(deviceId);
        });


        // Set up the register button to navigate to the registration page
//        registerButton.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//            startActivity(intent);
//        });

    }


    private void checkDeviceId(String deviceId) {
        mDatabase.child(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Device ID exists, navigate to home page
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class); // Replace with your home activity
                    startActivity(intent);
                } else {
                    // Device ID does not exist, navigate to register activity
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // Replace with your register activity
                    startActivity(intent);
                }
                finish(); // Optional: Call finish() to remove this activity from the back stack
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                // Handle possible errors.
            }
        });
    }
}