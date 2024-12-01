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

public class EntrantsAdapter extends RecyclerView.Adapter<EntrantsAdapter.EntrantViewHolder> {

    private List<Entrant> entrants = new ArrayList<>();

    @NonNull
    @Override
    public EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrant, parent, false);
        return new EntrantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        Entrant entrant = entrants.get(position);
        holder.bind(entrant);
    }

    @Override
    public int getItemCount() {
        return entrants.size();
    }

    /**
     * Sets the list of entrants and notifies the adapter.
     *
     * @param entrants The list of entrants to display.
     */
    public void setEntrants(List<Entrant> entrants) {
        this.entrants = entrants;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for Entrant items.
     */
    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView statusTextView;

        EntrantViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.entrant_name);
            //statusTextView = itemView.findViewById(R.id.entrant_status);
        }

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
