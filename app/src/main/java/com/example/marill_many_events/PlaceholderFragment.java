package com.example.marill_many_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlaceholderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate a simple layout with a solid color background
        View view = new View(getActivity());
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // Change this color as needed
        return view;
    }
}
