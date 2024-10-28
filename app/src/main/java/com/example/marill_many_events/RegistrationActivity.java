package com.example.marill_many_events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private Button buttonRegister;
    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Variable to hold the device ID
    private ImageView profilePicture;
    private Uri profilePictureUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this matches your layout XML file name
        deviceId = getIntent().getStringExtra("deviceId"); // Ensure you pass this from MainActivity
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");


        // Initialize UI elements
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = findViewById(R.id.textInputLayoutMobile);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        profilePicture = findViewById(R.id.profile_picture);
        buttonRegister = findViewById(R.id.buttonRegister); // Ensure you add this button in your layout XML


        profilePicture.setOnClickListener(v -> openPhotoPicker());

        // Set click listener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registerUser();
                }
            }
        });
    }

    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);

    }


    private final ActivityResultLauncher<Intent> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                profilePictureUri = data.getData();
                                // Display the selected image in a circular shape using Glide
                                if (profilePictureUri != null) {
                                    Glide.with(this)
                                            .load(profilePictureUri)
                                            .transform(new CircleCrop()) // Apply circular cropping
                                            .into(profilePicture); // Display in ImageView
                                }
                            }
                        }
                    });


        // Validate user inputs
    private boolean validateInputs() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            return false;
        } else {
            textInputLayoutName.setError(null); // Clear error
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Valid email is required");
            return false;
        } else {
            textInputLayoutEmail.setError(null); // Clear error
        }

        if (!mobile.isEmpty() && mobile.length() < 10) {
            textInputLayoutMobile.setError("Valid mobile number is required");
            return false;
        } else {
            textInputLayoutMobile.setError(null); // Clear error
        }

        return true;
    }


    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        // Upload the image to Firebase Storage
        if (profilePictureUri != null) {
            StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");

            fileReference.putFile(profilePictureUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create a user document in Firestore with the image URL and other fields
                        User user = new User(name, email, mobile, uri.toString());
                        firestore.collection("users").document(deviceId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    // Navigate to the next activity if needed
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Failed to register user.", Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Image upload failed.", Toast.LENGTH_SHORT).show());
        } else {
            // Handle case where no image was selected
            Toast.makeText(this, "Please select a profile picture.", Toast.LENGTH_SHORT).show();
        }
    }
}
