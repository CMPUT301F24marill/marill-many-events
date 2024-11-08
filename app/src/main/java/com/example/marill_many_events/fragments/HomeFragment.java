package com.example.marill_many_events.fragments;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.platform.app.InstrumentationRegistry.getArguments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * HomeFragment is a simple {@link Fragment} subclass that creates a view with a red background.
 */
public class HomeFragment extends Fragment{

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate
     *                           the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, with a red background.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create a new View
        View view = new View(getContext());
        // Set the background color to red
        view.setBackgroundColor(Color.RED);
        return view;
    }
}