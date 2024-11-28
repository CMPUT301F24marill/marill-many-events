package com.example.marill_many_events.fragments;

import static androidx.test.InstrumentationRegistry.getContext;

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

//EventyArrayAdapter contains Events in list and retrieves a events information for the view
public class EventyArrayAdapter extends RecyclerView.Adapter<EventyArrayAdapter.EventViewHolder> {
    private List<Event> eventList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;
    private boolean set_Garbage;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView eventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.event_poster);
            eventName = itemView.findViewById(R.id.event_name);
        }
    }

    public EventyArrayAdapter(List<Event> eventItemList, OnItemClickListener listener, boolean set_Garbage_value) {
        this.eventList = eventItemList;
        this.listener = listener;

        set_Garbage = set_Garbage_value;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);
        return new EventViewHolder(itemView);
    }

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
            leaveButton.setImageResource(R.id.delete);
        }
    }

    // Define an interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Event event);  // Pass a single Event object on click
        void onDeleteClick(Event event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}