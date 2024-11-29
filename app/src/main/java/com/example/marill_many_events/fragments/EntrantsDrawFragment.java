package com.example.marill_many_events.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.EntrantsAdapter;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EntrantsDrawFragment extends Fragment {

    private static final String TAG = "EntrantsDrawFragment";

    private String eventDocumentId;
    private FirebaseFirestore db;
    private EntrantsAdapter entrantsAdapter;

    private RecyclerView recyclerView;

    public EntrantsDrawFragment() {
        // Required empty public constructor
    }

    // Use this factory method to create a new instance of this fragment using the provided parameters
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
        // Inflate the fragment_participantlist.xml
        View view = inflater.inflate(R.layout.fragment_participantlist, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.participant_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter
        entrantsAdapter = new EntrantsAdapter();
        recyclerView.setAdapter(entrantsAdapter);

        // Get the eventDocumentId from arguments
        if (getArguments() != null) {
            eventDocumentId = getArguments().getString("eventDocumentId");
            // Fetch and display entrants when the fragment is created
            fetchAndDisplayEntrantsDemo();
        }



        return view;
    }

    private void fetchAndDisplayEntrantsDemo() {
        // If the passed eventDocumentId is null or empty, try getting it from fragment arguments
        db = FirebaseFirestore.getInstance();
        if (eventDocumentId == null || eventDocumentId.isEmpty()) {
            Bundle args = getArguments();
            if (args != null) {
                eventDocumentId = args.getString("eventDocumentId");
            }
        }

        // Reference to the event document
        assert eventDocumentId != null;
        DocumentReference eventDocRef = db.collection("events").document("XorSM4hvc6dtYe2rF1gN");
        Log.d(TAG, "DocumentReference path: " + eventDocRef.getPath());


        // Fetch the event document from Firestore
        eventDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                Log.d(TAG, "Successfully retrieved event document.");

                // Use event details here if needed
                DocumentSnapshot eventSnapshot = task.getResult();
                String eventName = eventSnapshot.getString("name");
                Log.d(TAG, "Event Name: " + eventName);

                // Get the waitList field from the event document
                Object waitListRefsObj = eventSnapshot.get("waitList");
                List<?> waitListRefs = null;

                if (waitListRefsObj != null && waitListRefsObj instanceof List) {
                    waitListRefs = (List<?>) waitListRefsObj;
                }

                if (waitListRefs != null && !waitListRefs.isEmpty()) {
                    Log.d("WaitListDebug", "Number of references in waitList: " + waitListRefs.size());

                    // Pass the list of references to a method that will retrieve the user details
                    fetchUserDetails(waitListRefs);
                } else {
                    Log.d("WaitListDebug", "WaitList is empty or null.");
                }

            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }

    private void fetchUserDetails(List<?> waitListRefs) {
        List<Task<DocumentSnapshot>> userFetchTasks = new ArrayList<>();

        for (Object userRefObj : waitListRefs) {
            if (userRefObj instanceof String) {
                String userRefPath = (String) userRefObj;
                DocumentReference userRef = db.document(userRefPath);
                userFetchTasks.add(userRef.get());
            } else if (userRefObj instanceof DocumentReference) {
                DocumentReference userRef = (DocumentReference) userRefObj;
                userFetchTasks.add(userRef.get());
            }
        }

        // Wait for all user data fetch tasks to complete
        Tasks.whenAllSuccess(userFetchTasks)
                .addOnSuccessListener(results -> {
                    List<Entrant> entrantList = new ArrayList<>();

                    for (Object obj : results) {
                        DocumentSnapshot userDoc = (DocumentSnapshot) obj;
                        if (userDoc.exists()) {
                            User user = userDoc.toObject(User.class);

                            // Create an Entrant object and set its user and other details
                            Entrant entrant = new Entrant();
                            entrant.setUser(user);
                            entrant.setStatus("waitlisted"); // Default status
                            entrantList.add(entrant);
                        }
                    }

                    Log.d(TAG, "Fetched " + entrantList.size() + " users from references.");

                    // Proceed with processing the fetched entrants
                    getEventCapacityAndSelectEntrants(entrantList);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error fetching user documents", e);
                });
    }



    private void fetchAndDisplayEntrants(String eventDocumentId) {
        db = FirebaseFirestore.getInstance();

        if (eventDocumentId == null || eventDocumentId.isEmpty()) {
            Log.e(TAG, "Event Document ID is null or empty");
            return;
        }


        // Reference to the waitList collection
        CollectionReference userInWaitList = db.collection("events")
                .document(eventDocumentId)
                .collection("waitList");

        // Fetch document references from waitList
        userInWaitList.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("WaitListDebug", "Successfully retrieved waitList collection.");
                List<Task<DocumentSnapshot>> userFetchTasks = new ArrayList<>();

                // Check if there are any documents in the waitList
                int documentCount = task.getResult().size();
                Log.d("WaitListDebug", "Number of documents in waitList: " + documentCount);

                // Loop through waitList documents
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID (user reference path)
                    String userRefPath = document.getString("userRef"); // Assuming "userRef" contains the path
                    Log.d("WaitListDebug", "User reference path found: " + userRefPath);

                    if (userRefPath != null) {
                        DocumentReference userRef = db.document(userRefPath);
                        userFetchTasks.add(userRef.get());
                    }
                }

                // Wait for all user data fetch tasks to complete
                Tasks.whenAllSuccess(userFetchTasks)
                        .addOnSuccessListener(results -> {
                            List<Entrant> entrantList = new ArrayList<>();

                            for (Object obj : results) {
                                DocumentSnapshot userDoc = (DocumentSnapshot) obj;
                                if (userDoc.exists()) {
                                    User user = userDoc.toObject(User.class);

                                    // Create an Entrant object and set its user and other details
                                    Entrant entrant = new Entrant();
                                    entrant.setUser(user);
                                    entrant.setStatus("waitlisted"); // Default status
                                    entrantList.add(entrant);
                                }
                            }

                            Log.d(TAG, "Fetched " + entrantList.size() + " users from references.");

                            // Proceed with processing the fetched entrants
                            getEventCapacityAndSelectEntrants(entrantList);
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error fetching user documents", e);
                        });
            } else {
                Log.w(TAG, "Error getting waitList documents.", task.getException());
            }
        });
    }


    private void getEventCapacityAndSelectEntrants(List<Entrant> entrantList) {
        // Fetch the event's capacity from Firestore
        db.collection("events").document(eventDocumentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long capacityLong = documentSnapshot.getLong("capacity");
                        int capacity = capacityLong != null ? capacityLong.intValue() : 10; // Default capacity if null

                        // Select random entrants based on capacity
                        List<Entrant> selectedEntrants = selectRandomEntrants(entrantList, capacity);

                        // Store selected entrants in Firestore
                        storeSelectedEntrants(selectedEntrants);

                        // Update the RecyclerView
                        entrantsAdapter.setEntrants(selectedEntrants);
                    } else {
                        Log.d(TAG, "No such event document");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting event document", e);
                });
    }

    private List<Entrant> selectRandomEntrants(List<Entrant> entrantList, int numberOfEntrantsToSelect) {
        if (entrantList.isEmpty()) {
            Log.w(TAG, "The entrant list is empty.");
            return new ArrayList<>();
        }

        if (numberOfEntrantsToSelect >= entrantList.size()) {
            Log.w(TAG, "Requested number of entrants exceeds or equals the available entrants. Returning all entrants.");
            return new ArrayList<>(entrantList);
        }

        Collections.shuffle(entrantList);
        return new ArrayList<>(entrantList.subList(0, numberOfEntrantsToSelect));
    }

    private void storeSelectedEntrants(List<Entrant> selectedEntrants) {
        if (selectedEntrants.isEmpty()) {
            Log.w(TAG, "No entrants to store.");
            return;
        }

        WriteBatch batch = db.batch();
        CollectionReference selectedEntrantsRef = db.collection("events")
                .document(eventDocumentId)
                .collection("selectedEntrants");

        for (Entrant entrant : selectedEntrants) {
            String entrantId = entrant.getUser().getEmail(); // Assuming email is unique
            batch.set(selectedEntrantsRef.document(entrantId), entrant);
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Selected entrants successfully stored in Firestore.");
            } else {
                Log.w(TAG, "Error storing selected entrants.", task.getException());
            }
        });
    }
}
