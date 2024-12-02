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

public class EntrantsDrawFragment extends Fragment {

    private static final String TAG = "EntrantsDrawFragment";

    private String eventDocumentId;
    private FirebaseFirestore db;

    public EntrantsDrawFragment() {
        // Required empty public constructor
    }

    public static EntrantsDrawFragment newInstance(String eventDocumentId) {
        EntrantsDrawFragment fragment = new EntrantsDrawFragment();
        Bundle args = new Bundle();
        args.putString("eventDocumentId", eventDocumentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate a simple layout (can be an empty layout since we're not displaying anything)
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        if (getArguments() != null) {
            eventDocumentId = getArguments().getString("eventDocumentId");
            performDraw();
        }

        return view;
    }

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


