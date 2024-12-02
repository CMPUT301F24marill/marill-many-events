package com.example.marill_many_events.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import com.example.marill_many_events.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Adapter for displaying a list of user profiles in a RecyclerView.
 * This adapter handles binding user profile data to the RecyclerView and managing click interactions
 * for viewing or deleting user profiles.
 */
public class ProfileyArrayAdapter extends RecyclerView.Adapter<ProfileyArrayAdapter.ProfileViewHolder> {
    private List<User> profileList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;
    private boolean set_Garbage;

    private String name;
    /**
     * ViewHolder class to hold references to the views for each user profile item in the RecyclerView.
     */
    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePicture;
        public TextView userName;
        public TextView status;
        /**
         * Constructor that initializes the view references for a profile item.
         *
         * @param itemView The view for the individual profile item.
         */
        public ProfileViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            userName = itemView.findViewById(R.id.participant_name);
            status = itemView.findViewById(R.id.status);
        }
    }
    /**
     * Constructor for the ProfileyArrayAdapter.
     *
     * @param userItemList List of user profiles to display.
     * @param listener OnItemClickListener to handle item clicks and delete actions.
     * @param set_Garbage_value Flag to determine if the leave button should be shown as a delete button.
     */
    public ProfileyArrayAdapter(List<User> userItemList, OnItemClickListener listener, boolean set_Garbage_value) {
        this.profileList = userItemList;
        this.listener = listener;

        set_Garbage = set_Garbage_value;
    }
    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent The parent ViewGroup that the item view will be added to.
     * @param viewType The view type of the new ViewHolder.
     * @return A new ProfileViewHolder instance.
     */
    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list, parent, false);
        return new ProfileViewHolder(itemView);
    }
    /**
     * Binds data from the user profile list to the views in the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        User currentItem = profileList.get(position);
        String imageURL = currentItem.getProfilePictureUrl();
        leaveButton = holder.itemView.findViewById(R.id.cancel_button);
        name = currentItem.getName();



        loadProfilewithGlide(null, imageURL, holder);
       /*// Load the image into the ImageView using Glide
        Glide.with(holder.itemView.getContext())  // 'getContext()' is used to specify the context
                .load(imageURL)       // URL of the image
                .into(holder.profilePicture);  // The ImageView to load the image into*/
        holder.userName.setText(currentItem.getName());

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
            holder.status.setVisibility(View.GONE);
        }
    }
    /**
     * Interface for handling item click and delete events.
     * Classes implementing this interface must define behavior for these actions.
     */
    // Define an interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(User user);  // Pass a single Event object on click
        void onDeleteClick(User user);
    }
    /**
     * Gets the total number of items in the profile list.
     *
     * @return The size of the profile list.
     */
    @Override
    public int getItemCount() {
        return profileList.size();
    }
    /**
     * Hides the leave button (delete functionality) for a profile item.
     * This can be used if the delete action is not needed for certain profiles.
     */
    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }

    /**
     * Loads the profile picture into the ImageView using Glide, prioritizing the URI if available,
     * otherwise falling back to a URL or a generated bitmap.
     *
     * @param profilePictureUri The URI of the profile picture.
     * @param profilePictureUrl The URL of the profile picture.
     * @param holder The holder with reference to profile picture and context
     */
    private void loadProfilewithGlide(Uri profilePictureUri, String profilePictureUrl, ProfileViewHolder holder) {
        if (profilePictureUri != null) {
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUri)
                    .transform(new CircleCrop())
                    .into(holder.profilePicture);
        } else if (profilePictureUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .transform(new CircleCrop())
                    .into(holder.profilePicture);
        } else {
            Glide.with(holder.itemView.getContext())
                    .asBitmap()
                    .load(generateprofile())
                    .transform(new CircleCrop())
                    .into(holder.profilePicture);
        }
    }

    /**
     * Generates a profile picture bitmap based on the user's name.
     *
     * @return A Bitmap representation of the profile picture.
     */
    private Bitmap generateprofile() {
        if (name != null) {
            return ProfilePictureGenerator.generateProfilePicture(name, 200);
        }
        return null;
    }
}