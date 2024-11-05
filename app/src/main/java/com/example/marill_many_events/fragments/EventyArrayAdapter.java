package com.example.marill_many_events.fragments;

import android.content.Context;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;

import java.util.ArrayList;

//EventyArrayAdapter contains Events in list and retrieves a books information for the view
public class EventyArrayAdapter extends ArrayAdapter<Event> {
    public EventyArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_list, parent, false);
        } else {
            view = convertView;
        }
        Event event = getItem(position);
        TextView eventName = view.findViewById(R.id.EventName);
        ImageView eventPoster = view.findViewById(R.id.EventPoster);

        eventName.setText(event.getName());
        String posterURL = event.getImageURL();
        return view;
    }
}
