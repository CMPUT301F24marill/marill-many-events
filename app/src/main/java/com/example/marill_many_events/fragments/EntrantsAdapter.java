package com.example.marill_many_events.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Entrant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * EntrantsAdapter is a RecyclerView adapter used for displaying a list of entrants.
 * Each entrant contains information about a user and their status, which is displayed
 * in individual list items (view holders) in a RecyclerView.
 */
public class EntrantsAdapter extends RecyclerView.Adapter<EntrantsAdapter.EntrantViewHolder> {
    // List of entrants to be displayed in the RecyclerView
    private List<Entrant> entrants = new ArrayList<>();
    /**
     * onCreateViewHolder is called when a new view holder is needed to display an entrant item.
     * It inflates the layout for the item and returns a new EntrantViewHolder.
     *
     * @param parent   The parent ViewGroup that the new view will be attached to
     * @param viewType The type of the view to create
     * @return A new EntrantViewHolder with the inflated item layout
     */
    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrant, parent, false);
        return new EntrantViewHolder(v);
    }
    /**
     * onBindViewHolder binds the data from an Entrant object to the corresponding views in the view holder.
     * This is called for each item in the RecyclerView as it is displayed.
     *
     * @param holder   The EntrantViewHolder that should be updated
     * @param position The position of the entrant in the list
     */
    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        Entrant entrant = entrants.get(position);
        holder.bind(entrant);
    }
    /**
     * getItemCount returns the total number of entrants in the list.
     * This is used by the RecyclerView to determine how many items need to be displayed.
     *
     * @return The size of the entrants list
     */
    @Override
    public int getItemCount() {
        return entrants.size();
    }

    /**
     * Sets the list of entrants to be displayed in the RecyclerView and notifies the adapter
     * that the data has changed, prompting a UI update.
     *
     * @param entrants The list of entrants to display in the RecyclerView
     */
    public void setEntrants(List<Entrant> entrants) {
        this.entrants = entrants;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for representing an individual entrant item in the RecyclerView.
     * This class binds the data of each Entrant object to the corresponding views.
     */
    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView statusTextView;

        /**
         * Constructor that initializes the views for the entrant item.
         *
         * @param itemView The view representing an individual item in the RecyclerView
         */
        EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.entrant_name);
            //statusTextView = itemView.findViewById(R.id.entrant_status);
        }
        /**
         * Binds the data from an Entrant object to the corresponding UI elements.
         *
         * @param entrant The Entrant object whose data is to be bound
         */
        void bind(Entrant entrant) {
            if (entrant.getUser() != null) {
                nameTextView.setText(entrant.getUser().getName());
                //statusTextView.setText(entrant.getStatus());
            } else {
                nameTextView.setText("Unknown");
                //statusTextView.setText("No Status");
            }
        }
    }
}
