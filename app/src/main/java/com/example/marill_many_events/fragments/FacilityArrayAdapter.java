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

//EventyArrayAdapter contains Events in list and retrieves a events information for the view
public class FacilityArrayAdapter extends RecyclerView.Adapter<FacilityArrayAdapter.FacilityViewHolder> {
    private List<Facility> facilityList;
    private OnItemClickListener listener; // Listener for item clicks
    private FloatingActionButton leaveButton;

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        public TextView facilityLocation;
        public TextView facilityName;

        public FacilityViewHolder(View itemView) {
            super(itemView);
            facilityLocation = itemView.findViewById(R.id.facility_location);
            facilityName = itemView.findViewById(R.id.facility_name);
        }
    }

    public FacilityArrayAdapter(List<Facility> facilityList, OnItemClickListener listener) {
        this.facilityList = facilityList;
        this.listener = listener;
    }

    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facility_list, parent, false);
        return new FacilityViewHolder(itemView);
    }

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

    // Define an interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(Facility facility);  // Pass a single Event object on click
        void onDeleteClick(Facility facility);
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public void hideLeaveButton(){
        leaveButton.setVisibility(View.GONE);
    }
}