//package com.example.marill_many_events.fragments;
//
//import static android.content.ContentValues.TAG;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.CustomTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.example.marill_many_events.R;
//import com.example.marill_many_events.activities.HomePageActivity;
//import com.example.marill_many_events.models.Event;
//import com.example.marill_many_events.models.GenerateQRcode;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Shows the details of any selected event object, invoked from either user's waitlist or organizers event list
// */
//public class EventDetailsFragment extends Fragment {
//
//    private TextView NameField, locationField ,capacityField, datePickerStart, datePickerEnd;
//    private ImageView QRview, posterView;
//    private GenerateQRcode generateQRcode;
//
//    public EventDetailsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
//        HomePageActivity parentActivity = (HomePageActivity) getActivity();
//        generateQRcode = new GenerateQRcode();
//
//        NameField = view.findViewById(R.id.NameField);
//        datePickerStart = view.findViewById(R.id.Startdatefield);
//        datePickerEnd = view.findViewById(R.id.DrawdateField);
//        capacityField = view.findViewById(R.id.Capacityfield);
//        locationField = view.findViewById(R.id.LocationField);
//        QRview = view.findViewById(R.id.QRcode);
//        posterView = view.findViewById(R.id.poster);
//
//
//
//        Button createButton = view.findViewById(R.id.create);
//        createButton.setText("Leave Event");
//        createButton.setVisibility(View.INVISIBLE);
//        Event event = parentActivity.getCurrentEvent();
//
//
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
//
//
//        // Fill in all views as long as a valid event was passed
//
//        if(event != null) {
//            loadPoster(event.getImageURL());
//            NameField.setText(event.getName());
//            locationField.setText(event.getLocation());
//
//            datePickerStart.setText(formatter.format(event.getStartDate()));
//            datePickerEnd.setText(formatter.format(event.getDrawDate()));
//
//            if(event.getQRcode() != null){ // If a qr code string is available generate and display it
//                QRview.setVisibility(View.VISIBLE);
//                QRview.setImageBitmap(generateQRcode.generateQR(event.getQRcode()));
//            }
//
//            capacityField.setText(Integer.toString(event.getCapacity()));
//        }
//
//        return view;
//    }
//
//    /**
//     * Retrieve poster from firebase storage and load with aspect ratio in mind
//     */
//
//    public void loadPoster(String url) {
//        posterView.post(() -> {
//            Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    float imageAspectRatio = (float) resource.getWidth() / resource.getHeight();
//                    int viewWidth = posterView.getWidth();
//                    int height = imageAspectRatio >= 1
//                            ? (int) (viewWidth / imageAspectRatio)
//                            : getView().getHeight() / 4;
//
//                    ViewGroup.LayoutParams params = posterView.getLayoutParams();
//                    params.height = height;
//                    posterView.setLayoutParams(params);
//                    posterView.setImageBitmap(resource);
//                }
//
//                @Override
//                public void onLoadCleared(@Nullable Drawable placeholder) {}
//            });
//        });
//    }
//
//
//    public void leaveEvent(){
//
//    }
//}


package com.example.marill_many_events.fragments;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Import Button
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager; // Import FragmentManager

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.GenerateQRcode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Shows the details of any selected event object, invoked from either user's waitlist or organizers event list
 */
public class EventDetailsFragment extends Fragment {

    private TextView NameField, locationField, capacityField, datePickerStart, datePickerEnd;
    private ImageView QRview, posterView;
    private GenerateQRcode generateQRcode;
    private Button viewParticipantsButton; // Declare the button
    private String eventDocumentId; // Document ID of the event

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        HomePageActivity parentActivity = (HomePageActivity) getActivity();
        generateQRcode = new GenerateQRcode();

        NameField = view.findViewById(R.id.NameField);
        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);
        locationField = view.findViewById(R.id.LocationField);
        QRview = view.findViewById(R.id.QRcode);
        posterView = view.findViewById(R.id.poster);
        viewParticipantsButton = view.findViewById(R.id.view_participants_button); // Initialize the button

        Event event = parentActivity.getCurrentEvent();

        // Explicitly set the eventDocumentId to the desired value
        eventDocumentId = "XorSM4hvc6dtYe2rF1gN";

        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        // Fill in all views as long as a valid event was passed
        if (event != null) {
            loadPoster(event.getImageURL());
            NameField.setText(event.getName());
            locationField.setText(event.getLocation());

            datePickerStart.setText(formatter.format(event.getStartDate()));
            datePickerEnd.setText(formatter.format(event.getDrawDate()));

            if (event.getQRcode() != null) { // If a QR code string is available, generate and display it
                QRview.setVisibility(View.VISIBLE);
                QRview.setImageBitmap(generateQRcode.generateQR(event.getQRcode()));
            }

            capacityField.setText(Integer.toString(event.getCapacity()));
        }

        // Set up the OnClickListener for the viewParticipantsButton
        viewParticipantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eventDocumentId != null) {

                    // Create a new instance of EntrantsDrawFragment, passing the eventDocumentId
                    EntrantsDrawFragment entrantsDrawFragment = EntrantsDrawFragment.newInstance(eventDocumentId);

                    // Replace the current fragment with EntrantsDrawFragment
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, entrantsDrawFragment)
                            .addToBackStack(null)
                            .commit();

                } else {
                    // Handle the case where eventDocumentId is null
                    Log.e(TAG, "Event Document ID is null");
                    // You might want to show an error message to the user
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

    public void leaveEvent() {
        // Implement if necessary
    }
}

