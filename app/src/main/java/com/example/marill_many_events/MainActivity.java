package com.example.marill_many_events;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_REGISTER = 1;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;

    private ActivityResultLauncher<Intent> registrationActivityLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Button logInButton = findViewById(R.id.loginButton);


        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection("users");

        registrationActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Get user info from the RegistrationActivity result

                        String name = result.getData().getStringExtra("name");
                        String email = result.getData().getStringExtra("email");
                        String mobile = result.getData().getStringExtra("mobile");

                        // Create a new user map to add to Firestore

                        // Add the user to Firestore under their device ID
//                        usersRef.document(deviceId).set(newUser)
//                                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully"))
//                                .addOnFailureListener(e -> Log.e(TAG, "Error adding user", e));
                    }
                });


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
        usersRef.document(deviceId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                            intent.putExtra("deviceId", deviceId);
                            startActivity(intent);; // Use the launcher to start RegistrationActivity
                        } else {
                            // Device ID does not exist, navigate to register activity
                            Log.d(TAG, "Device ID not found. Redirecting to RegistrationActivity.");
                            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                            intent.putExtra("deviceId", deviceId);
                            registrationActivityLauncher.launch(intent); // Use the launcher to start RegistrationActivity
                        }
                    } else {
                        Log.e(TAG, "Error checking device ID", task.getException());
                    }
                });
    }
}