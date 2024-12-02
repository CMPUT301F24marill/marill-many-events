package com.example.marill_many_events.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

    private static final String TAG = "MainActivity"; // Tag for logging
    private static final int REQUEST_CODE_REGISTER = 1;
    private FirebaseFirestore firestore;
    private CollectionReference usersRef;
    public String deviceId;

    Fragment registrationFragment = new RegistrationFragment();
    Bundle args = new Bundle();


    private ActivityResultLauncher<Intent> registrationActivityLauncher;

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


        // Set up the login button to navigate to the actual login page
        logInButton.setOnClickListener(v -> {
            checkDeviceId(deviceId);
        });
        createNotificationChannel();


        //notification set up
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(1, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "name", importance);
//            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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