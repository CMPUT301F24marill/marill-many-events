package com.example.marill_many_events.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.NavbarListener;
import com.example.marill_many_events.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavbarFragment extends Fragment {

    private BottomNavigationView bottomNavigation;
    private NavbarListener navbarListener; // Changed to NavbarListener

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navbar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = view.findViewById(R.id.bottom_navigation);


        // Set the listener here
        if (getActivity() instanceof NavbarListener) {
            navbarListener = (NavbarListener) getActivity();
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            clearFocus(bottomNavigation, itemId);

            if (itemId == R.id.nav_home) {
                // Handle home navigation
                bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_focused);
                return true;
            } else if (itemId == R.id.nav_menu) {
                // Handle dashboard navigation
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Open registration fragment when profile is clicked
                if (navbarListener != null) {
                    // Pass the deviceId when opening the registration fragment
                    bottomNavigation.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.default_profile_focus);
                    navbarListener.onProfileSelected();
                }
                return true;
            } else {
                return false;
            }
        });
    }

    private void clearFocus(BottomNavigationView bottomNavigation, int selectedItemId) {
        // Iterate through all menu items
        for (int i = 0; i < bottomNavigation.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigation.getMenu().getItem(i);
            if (menuItem.getItemId() == selectedItemId) {
                // Skip the selected item
                continue;
            }

            // Reset the icon for unselected items
            if (menuItem.getItemId() == R.id.nav_home) {
                menuItem.setIcon(R.drawable.home); // Reset to unselected icon
            } else if (menuItem.getItemId() == R.id.nav_menu) {
                menuItem.setIcon(R.drawable.menu); // Reset to unselected icon
            } else if (menuItem.getItemId() == R.id.nav_profile) {
                menuItem.setIcon(R.drawable.default_profile); // Reset to unselected icon
            }
        }
    }
}
