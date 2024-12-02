package com.example.marill_many_events.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * EntrantsDrawFragment is a fragment responsible for selecting a random set of entrants
 * from the event's waitlist, based on the event's capacity.
 * The fragment fetches data from Firestore, performs a random draw for entrants,
 * and updates the event's document with the selected entrants.
 */
public class EntrantsDrawFragment extends Fragment {

    private static final String TAG = "EntrantsDrawFragment";

    private String eventDocumentId;
    private FirebaseFirestore db;
    /**
     * Default constructor for the fragment.
     * Required for fragment instantiation. This constructor is empty.
     */
    public EntrantsDrawFragment() {
        // Required empty public constructor
    }
    /**
     * Creates a new instance of EntrantsDrawFragment with the given event document ID.
     *
     * @param eventDocumentId The ID of the event document in Firestore.
     * @return A new instance of EntrantsDrawFragment with the event document ID set.
     */
    public static EntrantsDrawFragment newInstance(String eventDocumentId) {
        EntrantsDrawFragment fragment = new EntrantsDrawFragment();
        Bundle args = new Bundle();
        args.putString("eventDocumentId", eventDocumentId);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Called when the fragment's view is being created. It inflates the layout for the fragment
     * and initializes the necessary operations, like fetching the event data and performing the draw.
     *
     * @param inflater The LayoutInflater object used to inflate the view.
     * @param container The parent ViewGroup to which the fragment's UI should be attached.
     * @param savedInstanceState Any saved state from a previous instance of the fragment.
     * @return The View representing the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate a simple layout (can be an empty layout since we're not displaying anything)
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        if (getArguments() != null) {
            eventDocumentId = getArguments().getString("eventDocumentId");
            performDraw();
        }

        return view;
    }
    /**
     * This method performs the draw to select entrants for the event. It fetches the event document
     * from Firestore, retrieves the waitlist, and proceeds to randomly select entrants based on event capacity.
     */
    private void performDraw() {
        db = FirebaseFirestore.getInstance();
        if (eventDocumentId == null || eventDocumentId.isEmpty()) {
            Log.e(TAG, "Event Document ID is null or empty.");
            return;
        }

        // Reference to the event document
        DocumentReference eventDocRef = db.collection("events").document(eventDocumentId);
        Log.d(TAG, "DocumentReference path: " + eventDocRef.getPath());

        // Fetch the event document from Firestore
        eventDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                Log.d(TAG, "Successfully retrieved event document.");

                // Get the waitList field from the event document
                DocumentSnapshot eventSnapshot = task.getResult();
                Object waitListRefsObj = eventSnapshot.get("waitList");
                List<?> waitListRefs = null;

                if (waitListRefsObj instanceof List) {
                    waitListRefs = (List<?>) waitListRefsObj;
                }

                if (waitListRefs != null && !waitListRefs.isEmpty()) {
                    Log.d("WaitListDebug", "Number of references in waitList: " + waitListRefs.size());

                    // Proceed with processing the fetched user references
                    getEventCapacityAndSelectEntrants(waitListRefs);
                } else {
                    Log.d("WaitListDebug", "WaitList is empty or null.");
                }

            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }
    /**
     * Fetches the event's capacity from Firestore and selects random entrants based on the capacity.
     * The selected entrants are then stored in the Firestore event document.
     *
     * @param waitListRefs The list of entrant references from the waitlist.
     */
    private void getEventCapacityAndSelectEntrants(List<?> waitListRefs) {
        // Fetch the event's capacity from Firestore
        db.collection("events").document(eventDocumentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long capacityLong = documentSnapshot.getLong("capacity");
                        int capacity = capacityLong != null ? capacityLong.intValue() : 10; // Default capacity if null

                        // Select random entrants based on capacity
                        List<?> selectedEntrantRefs = selectRandomEntrants(waitListRefs, capacity);

                        // Store selected entrants in Firestore
                        storeSelectedEntrants(selectedEntrantRefs);
                    } else {
                        Log.d(TAG, "No such event document");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting event document", e);
                });
    }

    /**
     * Selects a random subset of entrants from the waitlist based on the specified number of entrants to select.
     *
     * @param entrantRefs The list of entrant references.
     * @param numberOfEntrantsToSelect The number of entrants to randomly select.
     * @return A list of randomly selected entrants.
     */
    private List<?> selectRandomEntrants(List<?> entrantRefs, int numberOfEntrantsToSelect) {
        if (entrantRefs.isEmpty()) {
            Log.w(TAG, "The entrant references list is empty.");
            return new ArrayList<>();
        }

        if (numberOfEntrantsToSelect >= entrantRefs.size()) {
            Log.w(TAG, "Requested number of entrants exceeds or equals the available entrants. Returning all entrants.");
            return new ArrayList<>(entrantRefs);
        }

        List<?> shuffledEntrantRefs = new ArrayList<>(entrantRefs);
        Collections.shuffle(shuffledEntrantRefs);
        return new ArrayList<>(shuffledEntrantRefs.subList(0, numberOfEntrantsToSelect));
    }
    /**
     * Stores the selected entrants in Firestore by updating the 'selectedEntrants' field in the event document.
     *
     * @param selectedEntrantRefs The list of selected entrants to store in Firestore.
     */
    private void storeSelectedEntrants(List<?> selectedEntrantRefs) {
        if (selectedEntrantRefs.isEmpty()) {
            Log.w(TAG, "No entrants to store.");
            return;
        }

        // Update the selectedEntrants field in the event document
        db.collection("events").document(eventDocumentId)
                .update("selectedEntrants", selectedEntrantRefs)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Selected entrants successfully stored in Firestore.");
                    // Optionally, display a success message or navigate back
                    // For example, navigate back to the previous fragment
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> getParentFragmentManager().popBackStack());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error storing selected entrants.", e);
                });
    }
}


