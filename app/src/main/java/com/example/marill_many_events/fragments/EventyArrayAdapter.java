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

/**
 * EventyArrayAdapter is an {@link ArrayAdapter} for displaying a list of {@link Event} objects in a ListView or GridView.
 * It inflates the event list layout and binds data from an Event object to the view components, such as event name and poster image.
 */
public class EventyArrayAdapter extends ArrayAdapter<Event> {

    /**
     * Constructor for creating an EventyArrayAdapter.
     *
     * @param context The context in which the adapter is running.
     * @param events  The list of {@link Event} objects to be displayed.
     */
    public EventyArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    /**
     * Provides a view for an adapter view (e.g., ListView, GridView).
     * This method inflates the custom layout and populates it with data from the {@link Event} object at the specified position.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible. If null, a new view will be created.
     * @param parent      The parent view that this view will eventually be attached to.
     * @return A {@link View} corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        // Inflate a new view if no reusable convertView is available
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_list, parent, false);
        } else {
            view = convertView;
        }

        // Get the Event object for the current position
        Event event = getItem(position);
        TextView eventName = view.findViewById(R.id.EventName);
        ImageView eventPoster = view.findViewById(R.id.EventPoster);

        // Set the event name and load the poster image (if available)
        eventName.setText(event.getName());
        String posterURL = event.getImageURL();
        return view;
    }
}
