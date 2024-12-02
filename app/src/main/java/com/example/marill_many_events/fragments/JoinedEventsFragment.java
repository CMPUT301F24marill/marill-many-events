package com.example.marill_many_events.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all events as a list, events can either be user's waitlist or organizer's created events
 */
public class JoinedEventsFragment extends Fragment implements EventyArrayAdapter.OnItemClickListener{

    private Event currentEvent;
    private RecyclerView waitlistList;
    private EventyArrayAdapter eventAdapter;
    private List<Event> eventItemList;
    private HomePageActivity parentActivity;
    private EventViewModel eventViewModel;
    private User user;
    private FloatingActionButton notificationbutton;
    ScanOptions options = new ScanOptions();
    private boolean allownotifications;
    private FirebaseEvents firebaseEvents;
    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    DocumentReference userReference;
    //private onLeaveListener listener;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public JoinedEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (HomePageActivity) getActivity();
        // Make sure the activity implements the required interface
        if (context instanceof Identity) {
            identity = (Identity) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement Identity Interface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentLifecycle", "Fragment is now visible.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        userReference = firestore.collection("users").document(deviceId);

        eventViewModel.setUserReference(userReference);
        eventViewModel.setFirebaseStorage(identity.getStorage());
        eventViewModel.setFirebaseReference(firestore);

        allownotifications = true;

        getUser();

        TextView title = view.findViewById(R.id.waitlist_label);
        title.setText("My Joined Events");
        FloatingActionButton scanButton = view.findViewById(R.id.scan);
        notificationbutton = view.findViewById(R.id.notification);
        scanButton.setVisibility(View.GONE);
        notificationbutton.setVisibility((View.VISIBLE));



        notificationbutton.setOnClickListener(v-> {
            allownotifications = !allownotifications;

            if(!allownotifications)
                notificationbutton.setImageResource(R.drawable.notification);
            else
                notificationbutton.setImageResource(R.drawable.notification_fill);


            user.setAllownotifications(allownotifications);
            userReference.update("allownotifications", allownotifications);
        });


        // Initialize RecyclerView and CardAdapter
        waitlistList = view.findViewById(R.id.waitlist_list);
        waitlistList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the data list
        eventItemList = new ArrayList<Event>();

        // Initialize the adapter and set it to RecyclerView
        eventAdapter = new EventyArrayAdapter(eventItemList, this, true);
        waitlistList.setAdapter(eventAdapter);

        eventViewModel.getUserEventlist();
        checkNotifications();


        eventViewModel.getUserEventList().observe(getViewLifecycleOwner(), updatedList -> {
            eventItemList.clear(); // Clear the old list
            eventItemList.addAll(updatedList); // Add the updated list
            eventAdapter.notifyDataSetChanged(); // Notify the adapter of the changes
        });

        return view;
    }


    public void onDeleteClick(Event event){
        Log.d("FragmentLifecycle", "Deleting Event.");
        eventViewModel.setSelectedEvent(event);
        eventViewModel.leaveEvent();
    }


    public void showEventDetails(){
        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

        // Replace the current fragment with the child fragment
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_container, eventDetailsFragment)
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void onItemClick(Event event) {
        eventViewModel.setSelectedEvent(event);
        Log.d("FragmentLifecycle", "Opening details.");
        showEventDetails();
    }


    public void getUser(){
        userReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                eventViewModel.setCurrentUser(user);
                allownotifications = user.allownotifications;
                if(!allownotifications)
                    notificationbutton.setImageResource(R.drawable.notification);
                else
                    notificationbutton.setImageResource(R.drawable.notification_fill);
            } else {
                Log.d("Firestore", "No such user");
            }
        }).addOnFailureListener(e -> {
            Log.d("Firestore", "Error getting user: ", e);
        });
    }


    private void checkNotifications(){
        userReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve the field
                ArrayList<String> notificationStack = (ArrayList<String>) documentSnapshot.get("notifications"); // Replace 'fieldName' with your field key
                StringBuilder notificationString = new StringBuilder();
                if (notificationStack != null && !notificationStack.isEmpty()) {
                    for(String notification : notificationStack){
                        notificationString.append(notification);
                        notificationString.append('\n');
                    }

                    if(allownotifications) {
                        sendNotification("Update", notificationString.toString());
                    }
                    notificationStack.clear();

                    userReference.update("notifications", notificationStack)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Notifications cleared successfully"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Failed to clear notifications", e));
                } else {
                    System.out.println("Field does not exist in the document.");
                }
            } else {
                System.out.println("Document does not exist.");
            }
        }).addOnFailureListener(e -> {
            System.err.println("Error fetching document: " + e.getMessage());
        });
    }


    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "entrant_updates";

        // Create a notification channel (for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Entrant Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Build and send the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_marill) // Replace with your icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }

}