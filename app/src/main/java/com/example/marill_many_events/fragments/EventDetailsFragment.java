package com.example.marill_many_events.fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.GenerateQRcode;
import com.example.marill_many_events.models.PhotoPicker;
import com.example.marill_many_events.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.appcheck.internal.util.Logger;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Shows the details of any selected event object, invoked from either user's waitlist or organizers event list
 */
public class EventDetailsFragment extends Fragment implements PhotoPicker.OnPhotoSelectedListener, EventsCallback {

    private TextView nameField, locationField ,capacityField, datePickerStart, datePickerEnd;
    private ImageView QRview, posterView, pencil1, pencil2;
    private GenerateQRcode generateQRcode;
    private EventViewModel eventViewModel;
    private Button createButton, deleteButton;
    private Event event;
    private PhotoPicker photoPicker;
    private User user;
    private Uri posterUri;
    private Button drawEntrantsButton;
    private Button viewParticipantsButton;
    private Button viewMapButton;
    private String eventDocumentId;
    private FirebaseEvents firebaseEvents;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    private SwitchCompat switchCompat;
    boolean geo;
    boolean button_pressed = false;
    List<Object> current_geoPoints;
    ArrayList<GeoPoint> geoPointList;
    HomePageActivity parentActivity;
    private Identity identity;

    private Handler handler = new Handler();
    private Runnable runnable;
  
    private MaterialAlertDialogBuilder builder;
    private boolean isCheckGeo;
    private boolean dialogAccepted;

    public EventDetailsFragment() {
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
        firestore = identity.getFirestore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        generateQRcode = new GenerateQRcode();
        parentActivity = (HomePageActivity) getActivity();

        nameField = view.findViewById(R.id.NameField);
        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);
        locationField = view.findViewById(R.id.LocationField);
        pencil1 = view.findViewById(R.id.pencil1);
        pencil2 = view.findViewById(R.id.pencil2);
        pencil1.setAlpha((float) 0.0);
        pencil2.setAlpha((float) 0.0);
        QRview = view.findViewById(R.id.QRcode);
        posterView = view.findViewById(R.id.poster);
        user = eventViewModel.getCurrentUser();
        drawEntrantsButton = view.findViewById(R.id.draw_entrants_button);
        viewParticipantsButton = view.findViewById(R.id.view_participants_button);
        viewMapButton = view.findViewById(R.id.map_button);
        photoPicker = new PhotoPicker(this, this);
        firebaseStorage = eventViewModel.getFirebaseStorage();
        firebaseEvents = new FirebaseEvents(eventViewModel.getFirebaseReference(), firebaseStorage.getReference("event_posters"), eventViewModel.getUserReference().getId(), this);
        switchCompat = view.findViewById(R.id.GeoSwitch);

        createButton = view.findViewById(R.id.create);
        deleteButton = view.findViewById(R.id.delete);

        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        eventViewModel.getSelectedEvent().observe(getViewLifecycleOwner(), event -> {
            this.event = event;
            if (event != null) {
                loadPoster(event.getImageURL());
                nameField.setText(event.getName());
                locationField.setText(event.getLocation());
                datePickerStart.setText(formatter.format(event.getStartDate()));
                datePickerEnd.setText(formatter.format(event.getDrawDate()));
                capacityField.setText(String.valueOf(event.getCapacity()));
                switchCompat.setChecked(event.isCheckGeo());
                geo = event.isCheckGeo();

                setUI(); // Change UI elements based on context

                if (event.getFirebaseID() != null) {
                    eventDocumentId = event.getFirebaseID();
                    QRview.setVisibility(View.VISIBLE);
                    QRview.setImageBitmap(generateQRcode.generateQR(event.getQRcode()));
                }
            }
        });


        // Set up the OnClickListener for the drawEntrantsButton
        drawEntrantsButton.setOnClickListener(v -> {
            if (event.getFirebaseID() != null) {
//                // Create a new instance of EntrantsDrawFragment, passing the eventDocumentId
//                EntrantsDrawFragment entrantsDrawFragment = EntrantsDrawFragment.newInstance(event.getFirebaseID());
//
//                // Replace the current fragment with EntrantsDrawFragment
//                FragmentManager fragmentManager = getParentFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, entrantsDrawFragment)
//                        .addToBackStack(null)
//                        .commit();

                eventViewModel.performDraw();
            } else {
                Log.e(TAG, "Event Document ID is null");
                // Show an error message to the user if needed
            }
        });

        // Set up the OnClickListener for the viewParticipantsButton
        viewParticipantsButton.setOnClickListener(v -> {
            if (event.getFirebaseID() != null) {
                // Create a new instance of ViewParticipantsFragment, passing the eventDocumentId
                ViewParticipantsFragment viewParticipantsFragment = ViewParticipantsFragment.newInstance(event.getFirebaseID());

                // Replace the current fragment with ViewParticipantsFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, viewParticipantsFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.e(TAG, "Event Document ID is null");
                // Show an error message to the user if needed
            }
        });

        // Set up the OnClickListener for the viewMapButton
        geoPointList = new ArrayList<>();
        viewMapButton.setOnClickListener(v -> {
                FirebaseFirestore db = identity.getFirestore();
                db.collection("events").document(eventDocumentId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Get the GeoPoints array from the document
                            current_geoPoints = (List<Object>) document.get("entrantGeoPoints");

                            // Convert the array into a list of GeoPoint objects
                            if(current_geoPoints != null) {
                                for (Object obj : current_geoPoints) {
                                    if (obj instanceof GeoPoint) {
                                        geoPointList.add((GeoPoint) obj);
                                    }
                                }
                            }

                            MapFragment mapFragment = new MapFragment(geoPointList);

                            // Replace the current fragment with ViewParticipantsFragment
                            FragmentManager fragmentManager = getParentFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, mapFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else{
                            Log.d("Firebase", "Failed to get document");
                        }
                    }
                    else{
                        Log.d("Firebase", "Failed to get document");
                    }
                }).addOnFailureListener(task -> {
                    Log.d("Firebase", "Failed to get document");}
                );

        });

        //code that runs every frame
        runnable = new Runnable() {
            @Override
            public void run() {
                update();
                handler.postDelayed(this, 20);
            }
        };
        handler.post(runnable);

        return view;
    }

    /**
     * code that runs every frame
     */
    private void update() {
        if(!button_pressed) {
            //get perms
            boolean permission_check = parentActivity.getLocationPerms();
            if(!permission_check){
                createButton.setVisibility(View.INVISIBLE);
                createButton.setEnabled(false);
            }
            else{
                createButton.setVisibility(View.VISIBLE);
                createButton.setEnabled(true);
            }
        } // Change UI elements based on context
    }

    /**
     * Retrieve poster from firebase storage and load with aspect ratio in mind
     */

    public void loadPoster(String url) {
        posterView.post(() -> {
            Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    float imageAspectRatio = (float) resource.getWidth() / resource.getHeight();
                    int viewWidth = posterView.getWidth();
                    int height = imageAspectRatio >= 1
                            ? (int) (viewWidth / imageAspectRatio)
                            : getView().getHeight() / 4;

                    ViewGroup.LayoutParams params = posterView.getLayoutParams();
                    params.height = height;
                    posterView.setLayoutParams(params);
                    posterView.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {}
            });
        });
    }

    public void setNoEdit(TextView textView){
        textView.setFocusable(false);
        textView.setCursorVisible(false);
        textView.setKeyListener(null);
        textView.setTextIsSelectable(true);
    }

    public void setUI() {
        if (eventViewModel.userOwnsEvent()) {
            isOrganizer();
        }
        else {
            setNoEdit(nameField);
            setNoEdit(locationField);
            setNoEdit(capacityField);
            setNoEdit(datePickerStart);
            setNoEdit(datePickerEnd);
            switchCompat.setEnabled(false);

            if (user != null) {
                ArrayList<DocumentReference> waitList = user.getwaitList();
                ArrayList<DocumentReference> pending = user.getPending();
                ArrayList<DocumentReference> events = user.getEvents();



                if (waitList != null) {
                    if (waitList.contains(eventViewModel.getEventDocumentReference())) { // if the event is in the waitlist
                        eventInWaitlist();
                    }

                    else if (pending.contains(eventViewModel.getEventDocumentReference())) // if the event is in the pending list
                        invitePending();

                    else if (events.contains(eventViewModel.getEventDocumentReference())) // if the event is in the events list
                        eventFound();

                    else
                        eventNotFound();
                } else
                    eventNotFound();
            }
        }
    }

    private void eventNotFound(){
        createButton.setText(getString(R.string.lbl_join_event));

        if (event.isCheckGeo()) {
            parentActivity.checkLocationPerms();
        }
        createButton.setOnClickListener(v -> {
            firestore.collection("events").document(eventViewModel.getSelectedEvent().getValue().getFirebaseID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            ArrayList<?> waitList = (ArrayList<?>) documentSnapshot.get("waitList");
                            int currentCapacity = waitList != null ? waitList.size() : 0;
                            int maxCapacity = documentSnapshot.getLong("capacity").intValue();

                            if (currentCapacity >= maxCapacity) {
                                // Show a popup message indicating that the event is full
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Capacity Reached")
                                        .setMessage("Can't join the waitlist as the event is full.")
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else {
                                // Register the user if the capacity is not reached
                                if (event.isCheckGeo()) {
                                    showGeoDialog();
                                } else {
                                    eventViewModel.registerUser();
                                }
                            }
                        } else {
                            Log.e(Logger.TAG, "Event document not found.");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(Logger.TAG, "Error fetching event details", e));
        });
    }


    private void eventFound(){
        createButton.setText(getString(R.string.lbl_leave_event));
        createButton.setOnClickListener(v-> {
            eventViewModel.leaveEvent();
        });
    }


    private void eventInWaitlist(){
        createButton.setText("Leave Waitlist");
        createButton.setOnClickListener(v-> {
            eventViewModel.leaveWaitlist();
        });
    }

    private void invitePending(){
        createButton.setText("Accept Invite");
        deleteButton.setText("Reject Invite");
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(v->{
            eventViewModel.rejectEvent();
        });


        createButton.setOnClickListener(v-> {
            eventViewModel.enterUser();
        });
    }

    private void isOrganizer(){
        deleteButton.setText(getString(R.string.lbl_delete_event));
        deleteButton.setVisibility(View.VISIBLE);
        createButton.setVisibility(View.GONE);
        drawEntrantsButton.setVisibility(View.VISIBLE);
        viewParticipantsButton.setVisibility(View.VISIBLE);
        pencil1.setAlpha((float) 1.0);
        pencil2.setAlpha((float) 1.0);

        if(geo){
            viewMapButton.setVisibility(View.VISIBLE);
        }

        posterView.setOnClickListener(v-> {
            photoPicker.showPhotoOptions(null);
        });

        deleteButton.setOnClickListener(v -> {
            eventViewModel.deleteEvent(eventViewModel.getSelectedEvent().getValue());
            requireActivity().getSupportFragmentManager().popBackStack();
            button_pressed = true;
        });
    }

    /**
     * Sets the poster imageview once an image is selected.
     *
     * @param uri The image resource id.
     */
    public void onPhotoSelected(Uri uri){ // when the upload photo button is pressed and a photo is uploaded
        posterUri = uri;
        Glide.with(this).asBitmap().load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                        float imageAspectRatio = (float) resource.getWidth() / (float) resource.getHeight(); // Get the aspect ratio of the image

                        int viewwidth = posterView.getWidth(); // Get the width of the ImageView


                        int height;
                        if (imageAspectRatio >= 1)  // Landscape or square image (16:9 or wider)
                            height = (int) (viewwidth / imageAspectRatio);
                        else // Portrait image (9:16 or taller), limit the height to 1/4 of the fragment height
                            height = getView().getHeight()/4;


                        // Set the height of the ImageView
                        ViewGroup.LayoutParams params = posterView.getLayoutParams();
                        params.height = height;
                        posterView.setLayoutParams(params);

                        posterView.setImageBitmap(resource); // Add the image in the view
                    }

                    public void onLoadCleared(Drawable draw){}
                });

        if(posterUri != null) firebaseEvents.uploadPoster(posterUri);
    }


    public void onPosterUpload(String posterUrl){
        event.setImageURL(posterUrl);
        eventViewModel.getFirebaseReference().collection("events").document(event.getFirebaseID()).update("imageURL", posterUrl);
    }

    public void getEvent (Event event){}
    public void onEventCreate(String string){}
    public void onPhotoDeleted(){}

    /**
     * Displays a dialog warning the user that their geolocation will be recorded and shared with the event organizer.
     * The user is given the option to either accept or cancel the action.
     * If the user accepts, the user's registration will be processed.
     * If the user cancels, no action will be taken.
     */
    private void showGeoDialog(){

        builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Geolocation Warning")
                .setMessage("This event will record your location and will make it available to the organizer")
                .setCancelable(false) // Prevent dismissal by tapping outside
                .setPositiveButton("Okay", (dialog, which) -> {
                    Log.d("Dialog", "Positive button clicked");
                    parentActivity.getLocation();
                    GeoPoint current_geo = parentActivity.getCurrent_geo();
                    Log.d("S", "currentgeo:" + current_geo);
                    eventViewModel.registerUserGeo(current_geo);
                    requireActivity().getSupportFragmentManager().popBackStack();
                    button_pressed = true;
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Log.d("Dialog", "Negative button clicked");

                });
        builder.show();
    }
}
