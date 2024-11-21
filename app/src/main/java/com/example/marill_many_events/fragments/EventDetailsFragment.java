package com.example.marill_many_events.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.GenerateQRcode;
import com.example.marill_many_events.models.User;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Shows the details of any selected event object, invoked from either user's waitlist or organizers event list
 */
public class EventDetailsFragment extends Fragment {

    private TextView nameField, locationField ,capacityField, datePickerStart, datePickerEnd;
    private ImageView QRview, posterView;
    private GenerateQRcode generateQRcode;
    private EventViewModel eventViewModel;
    private Button createButton, deleteButton;
    private Event event;
    private User user;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        generateQRcode = new GenerateQRcode();

        nameField = view.findViewById(R.id.NameField);
        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);
        locationField = view.findViewById(R.id.LocationField);
        QRview = view.findViewById(R.id.QRcode);
        posterView = view.findViewById(R.id.poster);
        user = eventViewModel.getCurrentUser();


        createButton = view.findViewById(R.id.create);
        deleteButton = view.findViewById(R.id.delete);


        setUI(); // Change UI elements based on context

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

                if (event.getFirebaseID() != null) {
                    QRview.setVisibility(View.VISIBLE);
                    QRview.setImageBitmap(generateQRcode.generateQR(event.getFirebaseID()));
                }
            }
        });




        return view;
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



    public void setUI() {
        if(user != null){
            ArrayList<DocumentReference> waitList = user.getwaitList();
            if (waitList != null){
                if (waitList.contains(eventViewModel.getEventDocumentReference())) {
                    createButton.setText("Leave Event");
                }
                else
                    eventNotFound();
            }
            else
                eventNotFound();
        }


        if (this.user.isOrganizer()) {
            deleteButton.setVisibility(View.VISIBLE);
        }


    }

    private void eventNotFound(){
            createButton.setText("Join Event");
            createButton.setOnClickListener(v-> {
                eventViewModel.registerUser();
            });
    }

    public void leaveEvent(){

    }
}
