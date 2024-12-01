package com.example.marill_many_events.fragments;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.FacilityCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.FirebaseFacilityRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * CreateFacilityFragment represents a fragment that allow non-organizer users
 * to create facility profile.
 */
public class CreateFacilityFragment extends Fragment implements FacilityCallback {

    private EditText editTextName;
    private EditText editTextLocation;
    private Button buttonCreate;
    private Button buttonDelete;

    private Identity identity;
    public FirebaseFirestore firestore;

    public String facilityId;
    private boolean isEditMode = false;

    private Facility facility;

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
        return inflater.inflate(R.layout.fragment_create_facility, container, false);  // Inflate the layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initialize the views
        editTextName = view.findViewById(R.id.editTextFacilityName);
        editTextLocation = view.findViewById(R.id.editTextFacilityLocation);
        buttonCreate = view.findViewById(R.id.buttonCreateFacility);

        buttonDelete = view.findViewById(R.id.buttonDeleteFacility);
        // show if facility registered, hide if not
        firestore.collection("facilities").document(facilityId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            Log.d(TAG, "Device ID exists in facilities. Showing facility deletion button.");
                            buttonDelete.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "Device ID does not exist in facilities. Hiding facility deletion button.");
                            buttonDelete.setVisibility(View.GONE);
                        }
                    }
                });

        // Attempt to load existing facility details (if any)
        firebaseFacilityRegistration.loadFacilityDetails();

        // Set up buttonCreate onclick listener
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    if (isEditMode) {
                        // Update the existing facility
                        firebaseFacilityRegistration.updateFacility(
                                editTextName.getText().toString().trim(),
                                editTextLocation.getText().toString().trim());
                    } else {
                        // Register a new facility
                        firebaseFacilityRegistration.registerFacility(
                                editTextName.getText().toString().trim(),
                                editTextLocation.getText().toString().trim());
                    }
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirm deletion before proceeding
                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Delete Facility")
                        .setMessage("Are you sure you want to delete this facility and all its events?")
                        .setPositiveButton("Yes", (dialog, which) -> deleteFacility())
                        .setNegativeButton("No", null)
                        .show();
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
            editTextLocation.setError("Valid location is required");
            return false;
        } else {
            editTextLocation.setError(null); // Clear error
        }

        return true;
    }

    public void deleteFacility() {
        firestore.collection("facilities").document(facilityId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the list of events associated with the facility as an ArrayList
                        ArrayList<String> eventIds = (ArrayList<String>) documentSnapshot.get("events");

                        if (eventIds != null && !eventIds.isEmpty()) {
                            // Delete all events associated with this facility
                            for (String eventId : eventIds) {
                                firestore.collection("events").document(eventId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Event " + eventId + " deleted successfully"))
                                        .addOnFailureListener(e -> Log.e(TAG, "Error deleting event " + eventId, e));
                            }
                        }

                        // Delete the facility document after events are deleted
                        firestore.collection("facilities").document(facilityId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Facility deleted successfully", Toast.LENGTH_SHORT).show();

                                    // redirect to personal profile
                                    HomePageActivity parentActivity = (HomePageActivity) getActivity();

                                    if (parentActivity != null) {
                                        parentActivity.onProfileSelected();
                                    }
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error deleting facility", e));
                    } else {
                        Toast.makeText(getActivity(), "Facility does not exist.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching facility details", e));
    }



    /**
     * This callback is triggered when the facility data is loaded from Firestore.
     *
     * @param returnedFacility The facility object retrieved from Firestore.
     */
    @Override
    public void onFacilityLoaded(Facility returnedFacility) {
        if (returnedFacility != null) {
            facility = returnedFacility;
            isEditMode = true;

            // Set the edit mode UI
            editTextName.setText(facility.getFacilityName());
            editTextLocation.setText(facility.getLocation());
            buttonCreate.setText("Save"); // Change button text to "Save"
        } else {
            // If no facility exists, show a message and continue to registration
            Toast.makeText(getActivity(), "Facility not found. You can register.", Toast.LENGTH_SHORT).show();
            isEditMode = false;
            buttonCreate.setText("Create"); // Change button text to "Create"
        }
    }

    /**
     * Callback when a facility has been updated successfully.
     */
    @Override
    public void onFacilityUpdated() {
        // Reload the facility details after updating
        firebaseFacilityRegistration.loadFacilityDetails();
    }

    /**
     * Once a facility is registered, navigate to the home page activity.
     */
    @Override
    public void onFacilityRegistered() {
        HomePageActivity homePageActivity = (HomePageActivity) getActivity();
        homePageActivity.checkAndOpenFragment();
    }
}

