package com.example.marill_many_events.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailsFragment extends Fragment {

    private TextView NameField, locationField ,capacityField, datePickerStart, datePickerEnd;
    private ImageView QRview, posterView;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        HomePageActivity parentActivity = (HomePageActivity) getActivity();

        NameField = view.findViewById(R.id.NameField);
        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);
        locationField = view.findViewById(R.id.LocationField);
        QRview = view.findViewById(R.id.QRcode);
        posterView = view.findViewById(R.id.poster);

        Button createButton = view.findViewById(R.id.create);
        createButton.setVisibility(View.INVISIBLE);


        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        Event event = parentActivity.getCurrentEvent();
        // Initialize TextView

        if(event != null) {
            loadPoster(event.getImageURL());
            NameField.setText(event.getName());
            locationField.setText(event.getLocation());

            datePickerStart.setText(formatter.format(event.getStartDate()));
            datePickerEnd.setText(formatter.format(event.getDrawDate()));

            capacityField.setText(Integer.toString(event.getCapacity()));
        }

        return view;
    }

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
}
