package com.example.marill_many_events.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;

public class EventDetailsFragment extends Fragment {

    private TextView NameField, locationField ,capacityField, datePickerStart, datePickerEnd;
    private ImageView QRview;

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

        Button createButton = view.findViewById(R.id.create);
        createButton.setVisibility(View.INVISIBLE);

        Event event = parentActivity.getCurrentEvent();
        // Initialize TextView

        if(event != null) {
            NameField.setText(event.getName());
            locationField.setText(event.getLocation());
            //datePickerStart.setText(event.getStartDate().toString());
            //datePickerEnd.setText(event.getDrawDate().toString());
            //capacityField.setText(event.getCapacity());
        }

        return view;
    }
}
