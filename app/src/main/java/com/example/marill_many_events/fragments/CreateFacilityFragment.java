package com.example.marill_many_events.fragments;

import android.content.Context;
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

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.UserCallback;
import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * CreateFacilityFragment represents a fragment that allow non-organizer users
 * to create facility profile.
 */
public class CreateFacilityFragment extends Fragment implements FacilityCallback {

    private EditText editTextName;
    private EditText editTextLocation;
    private Button buttonCreate;

    private Identity identity;
    private FirebaseFirestore firestore;

    private String facilityId;

    FirebaseFacilityRegistration firebaseFacilityRegistration;

    public CreateFacilityFragment() {
        // Required empty public constructor
    }

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

        firebaseFacilityRegistration = new FirebaseFacilityRegistration(firestore, facilityId, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_facility, container, false);  // inflate the layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

    /**
     * @param facility
     */
    @Override
    public void onFacilityLoaded(Facility facility) {

    }

    /**
     *
     */
    @Override
    public void onFacilityUpdated() {

    }

    /**
     *
     */
    @Override
    public void onFacilityRegistered() {

    }
}

