package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewParticipantsFragment extends Fragment implements EntrantyArrayAdapter.OnItemClickListener {

    private RecyclerView entrantList;
    private EntrantyArrayAdapter entrantAdapter;
    private List<Entrant> EntrantItemList;

    private static final String TAG = "ViewParticipantsFrag";

    private FirebaseFirestore firestore;
    private Identity identity;
    private DocumentReference user;

    private String eventDocumentId;
    private FirebaseFirestore db;

    private EntrantsAdapter entrantsAdapter;
    private RecyclerView recyclerView;

    public ViewParticipantsFragment() {
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

    public static ViewParticipantsFragment newInstance(String eventDocumentId) {
        ViewParticipantsFragment fragment = new ViewParticipantsFragment();
        Bundle args = new Bundle();
        args.putString("eventDocumentId", eventDocumentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getEntrants();
        Log.d("FragmentLifecycle", "Profiles Fragment is now visible.");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        firestore = identity.getFirestore();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.waitlist_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText(getString(R.string.lbl_entrants));

        // Initialize the adapter
        entrantsAdapter = new EntrantsAdapter();
        recyclerView.setAdapter(entrantsAdapter);

        if (getArguments() != null) {
            eventDocumentId = getArguments().getString("eventDocumentId");
            Log.d(TAG, "Event Document ID from arguments: " + eventDocumentId);
        }
        user = firestore.collection("events").document(eventDocumentId);

        // Initialize RecyclerView and CardAdapter
        entrantList = view.findViewById(R.id.waitlist_list);
        entrantList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        EntrantItemList = new ArrayList<Entrant>();

        // Initialize the adapter and set it to RecyclerView
        entrantAdapter = new EntrantyArrayAdapter(EntrantItemList, this);
        entrantList.setAdapter(entrantAdapter);

        return view;
    }


    /**
     * Get all of the entrants under and event complete with their status
     */
    public void getEntrants() {
        EntrantItemList.clear();

        // Fetch the selectedEntrants from Firestore
        user.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                DocumentSnapshot eventSnapshot = task.getResult();
                Log.d(TAG, "Event document fetched successfully: " + eventSnapshot.getId());

                // Get the accepted Entrants field from the event document
                Object selectedEntrantRefsObj = eventSnapshot.get("acceptedEntrants");
                Log.d(TAG, "acceptedEntrantRefsObj: " + selectedEntrantRefsObj);

                List<?> selectedEntrantRefs = null;
                if (selectedEntrantRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) selectedEntrantRefsObj;
                    Log.d(TAG, "acceptedEntrants: " + selectedEntrantRefs);
                }

                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d(TAG, "Number of references in acceptedEntrants: " + selectedEntrantRefs.size());

                    // Fetch and display selected entrants
                    fetchUserDetails((List<DocumentReference>) selectedEntrantRefs, getString(R.string.lbl_accepted));
                } else {
                    Log.d(TAG, "acceptedEntrants is empty or null.");
                }

                // Get the selectedEntrants field from the event document
                selectedEntrantRefsObj = eventSnapshot.get("selectedEntrants");
                Log.d(TAG, "selectedEntrantRefsObj: " + selectedEntrantRefsObj);

                selectedEntrantRefs = null;
                if (selectedEntrantRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) selectedEntrantRefsObj;
                    Log.d(TAG, "selectedEntrantRefs: " + selectedEntrantRefs);
                }

                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d(TAG, "Number of references in selectedEntrants: " + selectedEntrantRefs.size());

                    // Fetch and display selected entrants
                    fetchUserDetails((List<DocumentReference>) selectedEntrantRefsObj, getString(R.string.lbl_invited));
                } else {
                    Log.d(TAG, "SelectedEntrants is empty or null.");
                }

                // Get the waitlistedEntrants field from the event document
                selectedEntrantRefs = null;
                selectedEntrantRefsObj = eventSnapshot.get("waitList");
                Log.d(TAG, "waitListRefsObj: " + selectedEntrantRefsObj);

                if (selectedEntrantRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) selectedEntrantRefsObj;
                    Log.d(TAG, "waitList: " + selectedEntrantRefs);
                }

                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d(TAG, "Number of references in waitList: " + selectedEntrantRefs.size());

                    // Fetch and display selected entrants
                    fetchUserDetails((List<DocumentReference>) selectedEntrantRefs, getString(R.string.lbl_waitlisted));
                } else {
                    Log.d(TAG, "waitList is empty or null.");
                }

                // Get the cancelled Entrants field from the event document
                selectedEntrantRefs = null;
                selectedEntrantRefsObj = eventSnapshot.get("cancelled");
                Log.d(TAG, "cancelledRefsObj: " + selectedEntrantRefsObj);

                if (selectedEntrantRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) selectedEntrantRefsObj;
                    Log.d(TAG, "cancelled: " + selectedEntrantRefs);
                }

                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d(TAG, "Number of references in cancelled: " + selectedEntrantRefs.size());

                    // Fetch and display selected entrants
                    fetchUserDetails((List<DocumentReference>) selectedEntrantRefs, getString(R.string.lbl_cancelled));
                } else {
                    Log.d(TAG, "cancelled is empty or null.");
                }

            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }

    /**
     * get a list of users details based on their reference and add them to the entrant list with set status
     *
     * @param userRefs list of users referenced
     * @param status   status to set them when adding to the list
     */
    private void fetchUserDetails(List<DocumentReference> userRefs, String status) {
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
                    int i = 0;
                    for (Object obj : results) {
                        if (obj instanceof DocumentSnapshot) {
                            DocumentSnapshot userDoc = (DocumentSnapshot) obj;
                            if (userDoc.exists()) {
                                User user = userDoc.toObject(User.class);
                                if (user != null && user.getName() != null) {
                                    Entrant entrant = new Entrant();
                                    entrant.setUser(user);
                                    entrant.setReference(userRefs.get(i));
                                    entrant.setStatus(status); // Adjust status as needed
                                    addToItemList(entrant);
                                } else {
                                    Log.e(TAG, "User data is null or missing name for document: " + userDoc.getId());
                                }
                            } else {
                                Log.e(TAG, "User document does not exist: " + userDoc.getId());
                            }
                        } else {
                            Log.e(TAG, "Unexpected result type: " + obj.getClass().getName());
                        }
                        i++;
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user documents", e);
                });
    }

    /**
     * Add a entrant to the list
     *
     * @param entrant: entrant to add
     */
    public void addToItemList(Entrant entrant) {
        if (!EntrantItemList.contains(entrant)) {
            EntrantItemList.add(entrant);
        }
        entrantAdapter.notifyDataSetChanged();
    }

    /**
     * Remove an entrant from the list
     *
     * @param entrant: entrant to remove
     */
    public void removeItemfromList(Entrant entrant) {
        if (EntrantItemList.contains(entrant)) {
            EntrantItemList.remove(entrant);
        }
        entrantAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Entrant entrant) {

    }

    /**cancel an entrant
     *
     * @param entrant entrant to be cancelled
     */
    @Override
    public void onDeleteClick(Entrant entrant) {
        RemoveFirebaseEventList( entrant);
        entrant.setStatus(getString(R.string.lbl_cancelled));
        addToFirebaseEventList( entrant);
        getEntrants();
    }

    /** removes entrant from a list based on their status
     *
     * @param entrant to be removed from list
     */
    private void RemoveFirebaseEventList(Entrant entrant) {
        String status = entrant.getStatus();
        String database_list_name;

        if (status.equals(getString(R.string.lbl_invited))) {
            database_list_name = "selectedEntrants";
        } else if (status.equals(getString(R.string.lbl_accepted))) {
            database_list_name = "acceptedEntrants";
        } else if (status.equals(getString(R.string.lbl_waitlisted))) {
            database_list_name = "waitList";
        } else{
            database_list_name = "cancelled";
        }

        // Fetch the event document from Firestore
        user.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                Log.d(TAG, "Successfully retrieved event document.");

                // Get the waitList field from the event document
                DocumentSnapshot eventSnapshot = task.getResult();
                Object waitListRefsObj = eventSnapshot.get(database_list_name);
                List<?> selectedEntrantRefs = null;

                if (waitListRefsObj instanceof List) {
                    selectedEntrantRefs = (List<?>) waitListRefsObj;
                }
                if (selectedEntrantRefs != null && !selectedEntrantRefs.isEmpty()) {
                    Log.d("WaitListDebug", "Number of references in waitList: " + selectedEntrantRefs.size());

                    if(selectedEntrantRefs.contains(entrant.getReference())) {
                        selectedEntrantRefs.remove(entrant.getReference());

                        // Update the selectedEntrants field in the event document
                        user.update(database_list_name, selectedEntrantRefs)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Selected entrants successfully stored in Firestore.");
                                    getEntrants();

                                    //remove on entrants side too
                                    if(status.equals(getString(R.string.lbl_waitlisted)) || status.equals(getString(R.string.lbl_accepted))){
                                        entrant.getReference().get().addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                DocumentSnapshot document = task2.getResult();
                                                if(document.exists()){
                                                    User current_user = document.toObject(User.class);
                                                    ArrayList<DocumentReference> userEvents = current_user.getEvents();
                                                    if(userEvents != null) userEvents.remove(user);
                                                    current_user.setEvents(userEvents);
                                                    ArrayList<DocumentReference> userWaitlist = current_user.getwaitList();
                                                    userWaitlist.remove(user);
                                                    current_user.setWaitList(userWaitlist);

                                                    DocumentReference documentref = document.getReference();
                                                    documentref.set(current_user);

                                                }
                                            }
                                        });


                                        /*User current_user = entrant.getUser();
                                        ArrayList<DocumentReference> userEvents = current_user.getEvents();
                                        userEvents.remove(user);
                                        current_user.setEvents(userEvents);
                                        ArrayList<DocumentReference> userWaitlist = current_user.getwaitList();
                                        userWaitlist.remove(user);
                                        current_user.setWaitList(userWaitlist);*/

                                        /*user.get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if(document.exists()){
                                                    Event event = document.toObject(Event.class);
                                                }
                                            }
                                        });*/
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error storing selected entrants.", e);
                                });
                    }
                    else{
                        Log.d("WaitListDebug", "List doesn't have user.");
                    }

                } else {
                    Log.d("WaitListDebug", "List is empty or null.");
                }
            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }

    /** adds entrant from a list based on their status
     *
     * @param entrant to be removed from list
     */
    private void addToFirebaseEventList(Entrant entrant){
        String status = entrant.getStatus();
        String database_list_name;

        if (status.equals(getString(R.string.lbl_invited))) {
            database_list_name = "selectedEntrants";
        } else if (status.equals(getString(R.string.lbl_accepted))) {
            database_list_name = "acceptedEntrants";
        } else if (status.equals(getString(R.string.lbl_waitlisted))) {
            database_list_name = "waitList";
        } else{
            database_list_name = "cancelled";
        }

        // Fetch the event document from Firestore
        user.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                Log.d(TAG, "Successfully retrieved event document.");

                // Get the waitList field from the event document
                DocumentSnapshot eventSnapshot = task.getResult();
                List<DocumentReference> selectedEntrantRefs = new ArrayList<DocumentReference>();

                //make field if it doesn't already exist
                if(!eventSnapshot.contains(database_list_name)){
                    selectedEntrantRefs.add(entrant.getReference());
                    user.update(database_list_name, selectedEntrantRefs)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "Field created successfully.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Error updating document", e);
                            });
                }
                else {
                    Object waitListRefsObj = eventSnapshot.get(database_list_name);

                    if (waitListRefsObj instanceof List) {
                        selectedEntrantRefs = (List<DocumentReference>) waitListRefsObj;
                    }
                    if (selectedEntrantRefs != null) {
                        Log.d("WaitListDebug", "Number of references in waitList: " + selectedEntrantRefs.size());

                        selectedEntrantRefs.add(entrant.getReference());
                        getEntrants();
                        Log.d("S", "sadas");

                        // Update the selectedEntrants field in the event document
                        user.update(database_list_name, selectedEntrantRefs)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Selected entrants successfully stored in Firestore.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error storing selected entrants.", e);
                                });

                    } else {
                        Log.d("WaitListDebug", "null.");
                    }
                }
            } else {
                Log.w(TAG, "Error retrieving event document or document does not exist.", task.getException());
            }
        });
    }
}
