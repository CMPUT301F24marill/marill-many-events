package com.example.marill_many_events.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewParticipantsFragment extends Fragment {

    private static final String TAG = "ViewParticipantsFrag";

    private String eventDocumentId;
    private FirebaseFirestore db;

    private EntrantsAdapter entrantsAdapter;
    private RecyclerView recyclerView;

    public ViewParticipantsFragment() {
        // Required empty public constructor
    }

    public static ViewParticipantsFragment newInstance(String eventDocumentId) {
        ViewParticipantsFragment fragment = new ViewParticipantsFragment();
        Bundle args = new Bundle();
        args.putString("eventDocumentId", eventDocumentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participantlist, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.participant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter
        entrantsAdapter = new EntrantsAdapter();
        recyclerView.setAdapter(entrantsAdapter);

        if (getArguments() != null) {
            eventDocumentId = getArguments().getString("eventDocumentId");
            Log.d(TAG, "Event Document ID from arguments: " + eventDocumentId);
            fetchAndDisplayParticipants();
        }

        return view;
    }

    private void fetchAndDisplayParticipants() {
        db = FirebaseFirestore.getInstance();
        if (eventDocumentId == null || eventDocumentId.isEmpty()) {
            Log.e(TAG, "Event Document ID is null or empty.");
            return;
        }

        //hard code to pass id
        //eventDocumentId = "ShcEvw5fLTiqrBJedY47";
        //Log.d(TAG, "Using hardcoded Event Document ID: " + eventDocumentId);
        //eventDocumentId = event.getQRcode();

        // Reference to the event document
        DocumentReference eventDocRef = db.collection("events").document(eventDocumentId);

        // Fetch the selectedEntrants from Firestore
        eventDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                DocumentSnapshot eventSnapshot = task.getResult();
                Log.d(TAG, "Event document fetched successfully: " + eventSnapshot.getId());

                // Get the selectedEntrants field from the event document
                Object selectedEntrantRefsObj = eventSnapshot.get("selectedEntrants");
                Log.d(TAG, "selectedEntrantRefsObj: " + selectedEntrantRefsObj);
                List<?> selectedEntrantRefs = null;

                if (selectedEntrantRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) selectedEntrantRefsObj;
                    Log.d(TAG, "selectedEntrantRefs: " + selectedEntrantRefs);
                }

                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d(TAG, "Number of references in selectedEntrants: " + selectedEntrantRefs.size());

                    // Fetch and display selected entrants
                    fetchUserDetails(selectedEntrantRefs);
                } else {
                    Log.d(TAG, "SelectedEntrants is empty or null.");
                }

            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }

    private void fetchUserDetails(List<?> userRefs) {
        Log.d(TAG, "Number of user references: " + userRefs.size());

        List<Task<DocumentSnapshot>> userFetchTasks = new ArrayList<>();

        for (Object userRefObj : userRefs) {
            Log.d(TAG, "userRefObj type: " + userRefObj.getClass().getName());
            Log.d(TAG, "userRefObj value: " + userRefObj.toString());

            if (userRefObj instanceof String) {
                Log.d(TAG, "userRefObj is a String");
                String userRefPath = (String) userRefObj;
                DocumentReference userRef = db.document(userRefPath);
                Log.d(TAG, "Fetching document from path: " + userRefPath);
                userFetchTasks.add(userRef.get());
            } else if (userRefObj instanceof DocumentReference) {
                Log.d(TAG, "userRefObj is a DocumentReference");
                DocumentReference userRef = (DocumentReference) userRefObj;
                Log.d(TAG, "Fetching document from DocumentReference: " + userRef.getPath());
                userFetchTasks.add(userRef.get());
            } else {
                Log.e(TAG, "Invalid user reference type: " + userRefObj.getClass().getName());
            }
        }

        Log.d(TAG, "Number of userFetchTasks: " + userFetchTasks.size());

        if (userFetchTasks.isEmpty()) {
            Log.e(TAG, "No valid user references to fetch.");
            return;
        }

        Tasks.whenAllSuccess(userFetchTasks)
                .addOnSuccessListener(results -> {
                    List<Entrant> entrantList = new ArrayList<>();

                    for (Object obj : results) {
                        if (obj instanceof DocumentSnapshot) {
                            DocumentSnapshot userDoc = (DocumentSnapshot) obj;
                            if (userDoc.exists()) {
                                User user = userDoc.toObject(User.class);
                                if (user != null && user.getName() != null) {
                                    Entrant entrant = new Entrant();
                                    entrant.setUser(user);
                                    entrant.setStatus("selected"); // Adjust status as needed
                                    entrantList.add(entrant);
                                } else {
                                    Log.e(TAG, "User data is null or missing name for document: " + userDoc.getId());
                                }
                            } else {
                                Log.e(TAG, "User document does not exist: " + userDoc.getId());
                            }
                        } else {
                            Log.e(TAG, "Unexpected result type: " + obj.getClass().getName());
                        }
                    }

                    Log.d(TAG, "Fetched " + entrantList.size() + " users from references.");

                    // Update the RecyclerView on the main thread
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> entrantsAdapter.setEntrants(entrantList));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user documents", e);
                });
    }

}
