package com.example.marill_many_events.fragments;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.Facility;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that displays all facilities in a list. It can show the user's waitlist
 * or the facilities created by the organizer. This fragment allows for the deletion
 * of facilities and their associated events.
 */
public class FacilitiesAdminFragment extends Fragment implements FacilityArrayAdapter.OnItemClickListener{

    private RecyclerView facilityList;
    private FacilityArrayAdapter facilityAdapter;
    private List<Facility> facilityItemList;

    private FirebaseEvents firebaseEvents;
    private FirebaseFirestore firestore;
    private Identity identity;
    private CollectionReference user;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public FacilitiesAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Attaches the fragment to the activity and checks if the activity implements the Identity interface.
     *
     * @param context The context to attach the fragment to.
     */

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
    /**
     * Called when the fragment is resumed. It retrieves the facilities from the Firestore database.
     */
    @Override
    public void onResume() {
        super.onResume();
        getFacilities();
        Log.d("FragmentLifecycle", "Facility Fragment is now visible.");
    }
    /**
     * Inflates the layout for this fragment and sets up the RecyclerView for displaying facilities.
     * Initializes the FirebaseFirestore instance and sets up the adapter for the RecyclerView.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        firestore = identity.getFirestore();
        user = firestore.collection("facilities");

        View view = inflater.inflate(R.layout.fragment_eventlist_admin, container, false);

        ImageView gearButton = view.findViewById(R.id.admin_gear);

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText(getString(R.string.lbl_all_facilities));

        gearButton.setOnClickListener(v -> {
            AdminPageActivity parentActivity = (AdminPageActivity) getActivity();
            if (parentActivity != null) {
                // navigate to AdminPageActivity
                parentActivity.openAdmin();
            }
        });

        // Initialize RecyclerView and CardAdapter
        facilityList = view.findViewById(R.id.waitlist_list);
        facilityList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        facilityItemList = new ArrayList<Facility>();

        // Initialize the adapter and set it to RecyclerView
        facilityAdapter = new FacilityArrayAdapter(facilityItemList, this);
        facilityList.setAdapter(facilityAdapter);

        return view;
    }

    /**
     * Get all of the facilities and populate the adapter
     */
    public void getFacilities(){
        facilityItemList.clear();
        user.get()
                .addOnCompleteListener(documentSnapshot -> {
                    if (documentSnapshot.isSuccessful()) {
                        QuerySnapshot docRefs = documentSnapshot.getResult();
                        for (DocumentSnapshot reference : docRefs) {
                            // Fetch each document using the DocumentReference
                            Facility facilityIter = reference.toObject(Facility.class);
                            if(facilityIter != null){
                                facilityIter.setId(reference.getId());
                                addToItemList(facilityIter);
                            }
                        }
                    } else {
                        // Document doesn't exist
                        Toast.makeText(getContext(), "Document not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error in retrieving the document
                    Toast.makeText(getContext(), "Error getting document", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Adds a facility to the item list and notifies the adapter.
     *
     * @param facility The facility to add to the list.
     */
    public void addToItemList(Facility facility){
        if (!facilityItemList.contains(facility)) {
            facilityItemList.add(facility);
        }
        facilityAdapter.notifyDataSetChanged();
    }

    /**
     * Removes a facility from the item list and notifies the adapter.
     *
     * @param facility The facility to remove from the list.
     */
    public void removeItemfromList(Facility facility){
        if (facilityItemList.contains(facility)) {
            facilityItemList.remove(facility);
        }
        facilityAdapter.notifyDataSetChanged();
    }
    /**
     * Displays the details of an event by navigating to the EventDetailsFragment.
     */
    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
    /**
     * Handles item clicks on the facilities list.
     *
     * @param facility The clicked facility.
     */
    @Override
    public void onItemClick(Facility facility) {

    }

    /**
     * Delete a facility
     * @param facility: facility to be deleted
     */
    @Override
    public void onDeleteClick(Facility facility) {
        firestore.collection("facilities").document(facility.getId())
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
                        firestore.collection("facilities").document(facility.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Facility deleted successfully", Toast.LENGTH_SHORT).show();

                                    //remove from local list
                                    removeItemfromList( facility);
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error deleting facility", e));
                    } else {
                        Toast.makeText(getActivity(), "Facility does not exist.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching facility details", e));
    }
}