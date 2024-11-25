package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;
import com.google.firebase.firestore.FirebaseFirestore;

public class FacilityProfileFragment extends Fragment {
    private EditText editTextFacilityName, editTextFacilityLocation;
    private Button buttonSaveFacility;
    private Button buttonDeleteFacility;

    private Identity identity;
    private FirebaseFirestore firestore;
    private String facilityId;

    FirebaseFacilityRegistration firebaseFacilityRegistration;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure the activity implements the required interface
        if (context instanceof Identity) {
            identity = (Identity) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement Identity Interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        facilityId = identity.getdeviceID();
        firestore = identity.getFirestore();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_facility, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        editTextFacilityName = view.findViewById(R.id.editTextFacilityNameEdit);
        editTextFacilityLocation = view.findViewById(R.id.editTextFacilityLocationEdit);
        buttonSaveFacility = view.findViewById(R.id.buttonSaveFacility);
        buttonDeleteFacility = view.findViewById(R.id.buttonDeleteFacility);


        // Load facility data if it exists
        firebaseFacilityRegistration.loadFacilityDetails(); // Try getting an existing user

        // Set click listener for the button
        buttonSaveFacility.setOnClickListener(v -> {
            String name = editTextFacilityName.getText().toString().trim();
            String location = editTextFacilityLocation.getText().toString().trim();

            if (validateInputs()) {
                firebaseFacilityRegistration.registerFacility(
                        editTextFacilityName.getText().toString().trim(),
                        editTextFacilityLocation.getText().toString().trim());

                // refresh facility data
                firebaseFacilityRegistration.loadFacilityDetails();
            }
        });
    }

    /**
     * Validate the inputs from createFacilityFragment view.
     *
     * @return A boolean indicating if the input is valid.
     */
    private boolean validateInputs() {
        String name = editTextFacilityName.getText().toString().trim();
        String location = editTextFacilityLocation.getText().toString().trim();

        if (name.isEmpty()) {
            editTextFacilityName.setError("Name is required");
            return false;
        } else {
            editTextFacilityName.setError(null); // Clear error
        }

        if (location.isEmpty()) {
            editTextFacilityLocation.setError("Valid location is required");
            return false;
        } else {
            editTextFacilityLocation.setError(null); // Clear error
        }

        return true;
    }
}
