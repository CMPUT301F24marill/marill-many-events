package com.example.marill_many_events.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.marill_many_events.models.User;
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
 * A fragment that displays a list of user profiles for the admin.
 * Admins can view user details, delete users (except themselves), and manage profiles.
 */
public class ProfilesAdminFragment extends Fragment implements ProfileyArrayAdapter.OnItemClickListener{

    private RecyclerView userList;
    private ProfileyArrayAdapter userAdapter;
    private List<User> userItemList;

    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    private CollectionReference user;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public ProfilesAdminFragment() {
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
     * Called when the fragment is resumed. Fetches the list of user profiles.
     */
    @Override
    public void onResume() {
        super.onResume();
        getUsers();
        Log.d("FragmentLifecycle", "Profiles Fragment is now visible.");
    }
    /**
     * Inflates the layout for this fragment, initializes UI components, and sets up the user list.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        user = firestore.collection("users");

        View view = inflater.inflate(R.layout.fragment_eventlist_admin, container, false);

        ImageView gearButton = view.findViewById(R.id.admin_gear);

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText(getString(R.string.lbl_all_Profiles));

        gearButton.setOnClickListener(v -> {
            AdminPageActivity parentActivity = (AdminPageActivity) getActivity();
            if (parentActivity != null) {
                // navigate to AdminPageActivity
                parentActivity.openAdmin();
            }
        });

        // Initialize RecyclerView and CardAdapter
        userList = view.findViewById(R.id.waitlist_list);
        userList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        userItemList = new ArrayList<User>();

        // Initialize the adapter and set it to RecyclerView
        userAdapter = new ProfileyArrayAdapter(userItemList, this, true);
        userList.setAdapter(userAdapter);

        return view;
    }

    /**
     * Get all of the events that a user is registered in and populate the adapter
     */
    public void getUsers(){
        userItemList.clear();
        user.get()
                .addOnCompleteListener(documentSnapshot -> {
                    if (documentSnapshot.isSuccessful()) {
                        QuerySnapshot docRefs = documentSnapshot.getResult();
                        for (DocumentSnapshot reference : docRefs) {
                            // Fetch each document using the DocumentReference
                            User userIter = reference.toObject(User.class);
                            if(userIter != null){
                                userIter.setId(reference.getId());
                                addToItemList(userIter);
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
     * Adds a user profile to the list and updates the adapter.
     *
     * @param user The user profile to add.
     */
    public void addToItemList(User user){
        if (!userItemList.contains(user)) {
            userItemList.add(user);
        }
        userAdapter.notifyDataSetChanged();
    }

    /**
     * Remove an item from the list
     * @param user: user to remove
     */
    public void removeItemfromList(User user){
        if (userItemList.contains(user)) {
            userItemList.remove(user);
        }
        userAdapter.notifyDataSetChanged();
    }
    /**
     * Handles item click events. (Currently a placeholder.)
     *
     * @param user The clicked user profile.
     */
    @Override
    public void onItemClick(User user) {

    }

    /**
     * Deletes a user profile from Firestore and removes it from the local list.
     * The admin cannot delete their own profile.
     *
     * @param userObject The user profile to delete.
     */
    @Override
    public void onDeleteClick(User userObject) {
        //cant delete yourself
        if(deviceId.equals(userObject.getId())){
            Toast.makeText(this.getContext(), getString(R.string.lbl_delete_yourself), Toast.LENGTH_SHORT).show();
        }
        else {
            DocumentReference eventDoc = user.document(userObject.getId());
            Log.d("S", "user id: " + userObject.getId());
            eventDoc.delete()
                    .addOnSuccessListener(aVoid -> {
                        //remove from local list
                        removeItemfromList(userObject);
                        Log.d("Firebase", "Event deleted successfully");

                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "failed to delete event: " + e.getMessage());
                    });
        }
    }
}