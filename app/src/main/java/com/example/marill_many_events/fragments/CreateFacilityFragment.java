package com.example.marill_many_events.fragments;

import android.graphics.text.TextRunShaper;
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

import com.example.marill_many_events.R;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;

/**
 * CreateFacilityFragment represents a fragment that allow non-organizer users
 * to create facility profile.
 */
public class CreateFacilityFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextLocation;
    private Button buttonCreate;

    FirebaseFacilityRegistration firebaseFacilityRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_facility, container, false);  // inflate the layout

        // initialize the views
        editTextName = view.findViewById(R.id.editTextFacilityName);
        editTextLocation = view.findViewById(R.id.editTextFacilityLocation);
        buttonCreate = view.findViewById(R.id.buttonCreateFacility);

        // set up buttonCreate onclick listener
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    firebaseFacilityRegistration.registerFacility(
                            editTextName.getText().toString().trim(),
                            editTextLocation.getText().toString().trim());
                }
            }
        });

        return view;
    }

    /**
     * Validate the inputs from createFacilityFragment view.
     *
     * @return A boolean indicating if the input is valid.
     */
    private boolean validateInputs() {
        String name = editTextName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            return false;
        } else {
            editTextName.setError(null); // Clear error
        }

        if (location.isEmpty()) {
            editTextLocation.setError("Valid email is required");
            return false;
        } else {
            editTextLocation.setError(null); // Clear error
        }

        return true;
    }
}

