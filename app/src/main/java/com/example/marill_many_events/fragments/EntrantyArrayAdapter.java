package com.example.marill_many_events.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Entrant;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import com.example.marill_many_events.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
/**
 * The adapter for displaying a list of participants (Entrants) in a RecyclerView.
 * This adapter binds each participant's information to a view (e.g., name, status, and profile picture).
 */
public class EntrantyArrayAdapter extends RecyclerView.Adapter<EntrantyArrayAdapter.ProfileViewHolder> {
    private List<Entrant> profileList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;

    private String name;
    private String entrantStatus;
    /**
     * ViewHolder class for holding the views of a single item (participant).
     */
    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePicture;
        public TextView userName;
        public TextView status;
        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The root view of the layout for each item (entrant).
         */
        public ProfileViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            userName = itemView.findViewById(R.id.participant_name);
            status = itemView.findViewById(R.id.status);
        }
    }
    /**
     * Constructor for EntrantyArrayAdapter.
     *
     * @param userItemList List of entrants to display.
     * @param listener     OnItemClickListener for handling clicks.
     */
    public EntrantyArrayAdapter(List<Entrant> userItemList, OnItemClickListener listener) {
        this.profileList = userItemList;
        this.listener = listener;
    }
    /**
     * Called when a new ViewHolder is created. This method inflates the layout for the individual
     * list item (participant) and returns a new instance of the ViewHolder.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new ProfileViewHolder instance containing the inflated view.
     */
    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list, parent, false);
        return new ProfileViewHolder(itemView);
    }
    /**
     * Binds the data to the ViewHolder for the given position. This method sets the participant's
     * details such as name, profile picture, status, and handles button visibility and click events.
     *
     * @param holder The ProfileViewHolder to bind data to.
     * @param position The position of the item in the profileList.
     */
    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Entrant currentItem = profileList.get(position);
        String imageURL = currentItem.getUser().getProfilePictureUrl();
        leaveButton = holder.itemView.findViewById(R.id.cancel_button);
        name = currentItem.getUser().getName();
        entrantStatus = currentItem.getStatus();

        Log.d("S", "entrant status: "+entrantStatus);

        loadProfilewithGlide(null, imageURL, holder);
        holder.userName.setText(name);
        holder.status.setText(entrantStatus);

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

        //leave button is gone if user already cancelled
        if(currentItem.getStatus().equals("Cancelled")){
            leaveButton.setEnabled(false);
            leaveButton.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Interface for handling item clicks and delete button clicks.
     * This interface is used to define actions for when an entrant is clicked or when the "leave" button is clicked.
     */
    // Define an interface for item click listener
    public interface OnItemClickListener {
        /**
         * Called when an item (entrant) is clicked.
         *
         * @param entrant The clicked entrant object.
         */
        void onItemClick(Entrant entrant);  // Pass a single Event object on click
        /**
         * Called when the delete (leave) button is clicked for an entrant.
         *
         * @param entrant The entrant object for which the leave button was clicked.
         */
        void onDeleteClick(Entrant entrant);
    }
    /**
     * Returns the total number of items (Entrants) in the list.
     * This method is called by the RecyclerView to determine how many items
     * are in the dataset, so it knows how many views to create and bind.
     *
     * @return The total number of items in the profile list.
     */
    @Override
    public int getItemCount() {
        return profileList.size();
    }
    /**
     * Hides the leave (cancel) button in the adapter. This can be called if the button should not be shown.
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