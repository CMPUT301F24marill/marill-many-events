package com.example.marill_many_events.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Facility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Adapter for displaying a list of facilities in a RecyclerView.
 * This adapter handles binding facility data to the RecyclerView and managing click interactions
 * for viewing or deleting a facility.
 */
public class FacilityArrayAdapter extends RecyclerView.Adapter<FacilityArrayAdapter.FacilityViewHolder> {
    private List<Facility> facilityList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;
    /**
     * ViewHolder class to hold references to the views for each facility item in the RecyclerView.
     */
    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        public TextView facilityLocation;
        public TextView facilityName;
        /**
         * Constructor that initializes the view references for a facility item.
         *
         * @param itemView The view for the individual facility item.
         */
        public FacilityViewHolder(View itemView) {
            super(itemView);
            facilityLocation = itemView.findViewById(R.id.facility_location);
            facilityName = itemView.findViewById(R.id.facility_name);
        }
    }
    /**
     * Constructor for the FacilityArrayAdapter.
     *
     * @param facilityList List of facilities to display.
     * @param listener OnItemClickListener to handle item clicks.
     */
    public FacilityArrayAdapter(List<Facility> facilityList, OnItemClickListener listener) {
        this.facilityList = facilityList;
        this.listener = listener;
    }
    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent The parent ViewGroup that the item view will be added to.
     * @param viewType The view type of the new ViewHolder.
     * @return A new FacilityViewHolder instance.
     */
    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facility_list, parent, false);
        return new FacilityViewHolder(itemView);
    }
    /**
     * Binds data from the facility list to the views in the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(FacilityViewHolder holder, int position) {
        Facility currentItem = facilityList.get(position);
        leaveButton = holder.itemView.findViewById(R.id.cancel_button);
        holder.facilityName.setText(currentItem.getFacilityName());
        holder.facilityLocation.setText(currentItem.getLocation());

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
        /**
         * Called when a facility item is clicked.
         *
         * @param facility The clicked facility.
         */
        void onItemClick(Facility facility);
        /**
         * Called when the delete (leave) button is clicked for a facility.
         *
         * @param facility The facility to be deleted.
         */// Pass a single Event object on click
        void onDeleteClick(Facility facility);
    }
    /**
     * Gets the total number of items in the facility list.
     *
     * @return The size of the facility list.
     */
    @Override
    public int getItemCount() {
        return facilityList.size();
    }
    /**
     * Hides the leave button (delete functionality) for a facility item.
     * This can be used if the delete action is not needed for certain facilities.
     */
    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}