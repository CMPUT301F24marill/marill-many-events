package com.example.marill_many_events.activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.provider.Settings;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.RegistrationFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

/**
 * MainActivity checks for an existing user registration based on the device ID
 * and navigates the user to the appropriate screen (e.g., home or registration).
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Tag for logging
    private static final int REQUEST_CODE_REGISTER = 1;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    public String deviceId;

    Fragment registrationFragment = new RegistrationFragment();
    Bundle args = new Bundle();


    private ActivityResultLauncher<Intent> registrationActivityLauncher;

    /**
     * For notification perms
     */
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(MainActivity.this, "Post notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Post notification permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });


    /**
     * Called when the app is starting. Initializes the UI components, sets up the
     * Firestore instance, retrieves the device ID, and sets the login button's click listener.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the unique device ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize the login button
        Button logInButton = findViewById(R.id.loginButton);

        // Set up Firestore instance and reference to the 'users' collection
        firestore = FirebaseFirestore.getInstance();
        usersRef = firestore.collection("users");

        // Register the activity result launcher for the registration activity
        registrationActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                            checkDeviceId(deviceId); // attempt login after registration is successful
                    }
                });

        //set up notification builder

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test")
                .setContentTitle("test title")
                .setContentText("Example Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_marill);


        NotificationManager notificationManager = getSystemService(NotificationManager.class);


        // Set up the login button to navigate to the actual login page
        logInButton.setOnClickListener(v -> {
            checkDeviceId(deviceId);

            //After logging in, get notifications
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "content";
                    String description = "Example Notification";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("test", name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);

                    notificationManager.notify(10, builder.build());
                }
            }
        });
        
    }

    /**
     * Checks if a user with the provided device ID exists in the Firestore database.
     * If the user exists, navigates to the HomePageActivity; otherwise, it starts the RegistrationActivity.
     *
     * @param deviceId The unique device ID used to identify the user in the database.
     */

    private void checkDeviceId(String deviceId) {
        // Pass the device ID to the RegistrationFragment
        args.putString("deviceId", deviceId); // Pass the device ID
        registrationFragment.setArguments(args);

        // Query the Firestore database for the user document with the given device ID
        usersRef.document(deviceId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User exists, navigate to HomePageActivity
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
                        // Log an error if the task fails
                        Log.e(TAG, "Error checking device ID", task.getException());
                    }
                });
    }
}