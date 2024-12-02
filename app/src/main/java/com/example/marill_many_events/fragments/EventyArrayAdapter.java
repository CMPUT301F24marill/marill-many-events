package com.example.marill_many_events.fragments;

import static androidx.test.InstrumentationRegistry.getContext;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
/**
 * Adapter for displaying a list of events in a RecyclerView.
 * This adapter handles the binding of event data to the RecyclerView and manages item click interactions.
 * It also provides functionality for deleting an event, if needed, based on the passed flag.
 */
public class EventyArrayAdapter extends RecyclerView.Adapter<EventyArrayAdapter.EventViewHolder> {
    private List<Event> eventList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;
    private boolean set_Garbage;
    /**
     * ViewHolder class to hold references to the views for each item in the RecyclerView.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView eventName;
        /**
         * Constructor that initializes the view references.
         *
         * @param itemView The view for the individual item.
         */
        public EventViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.event_poster);
            eventName = itemView.findViewById(R.id.event_name);
        }
    }
    /**
     * Constructor for the EventyArrayAdapter.
     *
     * @param eventItemList List of events to display.
     * @param listener OnItemClickListener to handle item clicks.
     * @param set_Garbage_value Flag to determine if delete functionality is enabled.
     */
    public EventyArrayAdapter(List<Event> eventItemList, OnItemClickListener listener, boolean set_Garbage_value) {
        this.eventList = eventItemList;
        this.listener = listener;

        set_Garbage = set_Garbage_value;
    }
    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent The parent ViewGroup that the item view will be added to.
     * @param viewType The view type of the new ViewHolder.
     * @return A new EventViewHolder instance.
     */
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);
        return new EventViewHolder(itemView);
    }
    /**
     * Binds data from the event list to the views in the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event currentItem = eventList.get(position);
        String imageURL = currentItem.getImageURL();
        leaveButton = holder.itemView.findViewById(R.id.leave);

        // Load the image into the ImageView using Glide
        Glide.with(holder.itemView.getContext())  // 'getContext()' is used to specify the context
                .load(imageURL)       // URL of the image
                .into(holder.poster);  // The ImageView to load the image into
        holder.eventName.setText(currentItem.getName());

        // Set click listener on the CardView
        holder.itemView.setOnClickListener(v -> {
            // Pass the clicked item to the listener
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });

        leaveButton.setOnClickListener(v-> {
            if (listener != null) {
                listener.onDeleteClick(currentItem);
            }

        });

        if(set_Garbage){
            leaveButton.setImageResource(R.drawable.delete);
        }
    }

    /**
     * Interface for handling item click and delete events.
     * The implementing class should define the behavior when an item is clicked or deleted.
     */
    public interface OnItemClickListener {
        /**
         * Called when an event item is clicked.
         *
         * @param event The clicked event.
         */
        void onItemClick(Event event);  // Pass a single Event object on click
        /**
         * Called when the delete (leave) button is clicked for an event.
         *
         * @param event The event to be deleted.
         */
        void onDeleteClick(Event event);
    }
    /**
     * Gets the total number of items in the event list.
     *
     * @return The size of the event list.
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }
    /**
     * Hides the leave button (delete functionality) in the event item.
     * This is useful if we don't want to show the delete button for certain events.
     */
    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}