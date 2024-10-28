package com.example.marill_many_events;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private Button buttonRegister;
    private FirebaseFirestore firestore; // Firestore instance
    private String deviceId; // Variable to hold the device ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Ensure this matches your layout XML file name
        deviceId = getIntent().getStringExtra("deviceId"); // Ensure you pass this from MainActivity
        firestore = FirebaseFirestore.getInstance();


        // Initialize UI elements
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = findViewById(R.id.textInputLayoutMobile);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        buttonRegister = findViewById(R.id.buttonRegister); // Ensure you add this button in your layout XML

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

        if (mobile.isEmpty() || mobile.length() < 10) {
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

        // Create a Map to hold the user data
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("mobile", mobile);

        // Add a new document with the deviceId as the document name
        firestore.collection("users")
                .document(deviceId) // Use the deviceId as the document ID
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to another activity after successful registration
                            // Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                            // startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
