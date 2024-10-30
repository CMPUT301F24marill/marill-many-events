package com.example.marill_many_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

public class ProfileButton extends Fragment {
    private ImageView profileImageView;

    // Key for the argument
    private static final String ARG_PROFILE_URL = "profileImageUrl";

    public static ProfileButton newInstance(String profileImageUrl) {
        ProfileButton fragment = new ProfileButton();
        Bundle args = new Bundle();
        args.putString(ARG_PROFILE_URL, profileImageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        profileImageView = view.findViewById(R.id.profileImageView);

        // Retrieve the profile picture URL from arguments
        if (getArguments() != null) {
            String profileImageUrl = getArguments().getString(ARG_PROFILE_URL);

            // Use Glide to load the image
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.default_profile)
                    .into(profileImageView);
        }

        return view;
    }
}
