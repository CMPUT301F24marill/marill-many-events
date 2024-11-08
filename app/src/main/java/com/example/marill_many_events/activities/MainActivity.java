package com.example.marill_many_events.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.RegistrationFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

/**
 * MainActivity checks for an existing user registration based on the device ID
 * and navigates the user to the appropriate screen (e.g., home or registration).
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_REGISTER = 1;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    public String deviceId;

    Fragment registrationFragment = new RegistrationFragment();
    Bundle args = new Bundle();


    private ActivityResultLauncher<Intent> registrationActivityLauncher;

    /**
     * Called when the activity is starting. Initializes the UI components, sets up the
     * Firestore instance, retrieves the device ID, and sets the login button's click listener.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button logInButton = findViewById(R.id.loginButton);


        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection("users");

        registrationActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                            checkDeviceId(deviceId); // attempt login after registration is successful
                    }
                });


        // Set up the login button to navigate to the actual login page
        logInButton.setOnClickListener(v -> {
            checkDeviceId(deviceId);
        });


    }

    /**
     * Checks if a user with the provided device ID exists in the Firestore database.
     * If the user exists, navigates to the HomePageActivity; otherwise, it starts the RegistrationActivity.
     *
     * @param deviceId The unique device ID used to identify the user in the database.
     */

    private void checkDeviceId(String deviceId) {
        args.putString("deviceId", deviceId); // Pass the device ID
        registrationFragment.setArguments(args);

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