package com.example.marill_many_events.fragments;

import android.util.Log;
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
/**
 * Adapter for displaying a list of images in a RecyclerView.
 * This adapter handles binding image URLs to the RecyclerView and managing click interactions
 * for viewing or deleting images.
 */
public class ImageyArrayAdapter extends RecyclerView.Adapter<ImageyArrayAdapter.ImageViewHolder> {
    private List<String> imageList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;
    /**
     * ViewHolder class to hold references to the views for each image item in the RecyclerView.
     */
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        /**
         * Constructor that initializes the view references for an image item.
         *
         * @param itemView The view for the individual image item.
         */
        public ImageViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
        }
    }
    /**
     * Constructor for the ImageyArrayAdapter.
     *
     * @param imageItemList List of image URLs to display.
     * @param listener OnItemClickListener to handle item clicks and delete actions.
     */
    public ImageyArrayAdapter(List<String> imageItemList, OnItemClickListener listener) {
        this.imageList = imageItemList;
        this.listener = listener;
    }
    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent The parent ViewGroup that the item view will be added to.
     * @param viewType The view type of the new ViewHolder.
     * @return A new ImageViewHolder instance.
     */
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        return new ImageViewHolder(itemView);
    }
    /**
     * Binds data from the image list to the views in the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String currentItem = imageList.get(position);
        leaveButton = holder.itemView.findViewById(R.id.leave);

        Log.d("s", "current image URL:"+currentItem);

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
    /**
     * Interface for handling item click and delete events.
     * Classes implementing this interface must define behavior for these actions.
     */
    // Define an interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(String event);  // Pass a single Event object on click
        void onDeleteClick(String event);
    }
    /**
     * Gets the total number of items in the image list.
     *
     * @return The size of the image list.
     */
    @Override
    public int getItemCount() {
        return imageList.size();
    }
    /**
     * Hides the leave button (delete functionality) for an image item.
     * This can be used if the delete action is not needed for certain images.
     */
    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}