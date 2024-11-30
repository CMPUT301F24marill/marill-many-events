package com.example.marill_many_events.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

//ImageyArrayAdapter contains images in list and retrieves a images information for the view
public class ImageyArrayAdapter extends RecyclerView.Adapter<ImageyArrayAdapter.ImageViewHolder> {
    private List<String> imageList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;

        public ImageViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
        }
    }

    public ImageyArrayAdapter(List<String> imageItemList, OnItemClickListener listener) {
        this.imageList = imageItemList;
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String currentItem = imageList.get(position);
        leaveButton = holder.itemView.findViewById(R.id.leave);

        // Load the image into the ImageView using Glide
        Glide.with(holder.itemView.getContext())  // 'getContext()' is used to specify the context
                .load(currentItem)       // URL of the image
                .into(holder.poster);  // The ImageView to load the image into

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
    }

    // Define an interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(String event);  // Pass a single Event object on click
        void onDeleteClick(String event);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}