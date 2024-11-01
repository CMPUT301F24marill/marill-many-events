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

public class HomeFragment extends Fragment{

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