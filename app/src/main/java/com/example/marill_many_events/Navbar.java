package com.example.marill_many_events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends Fragment {

    private BottomNavigationView bottomNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navbar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigation = view.findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            clearfocus(bottomNavigation, itemId);

            if (itemId == R.id.nav_home) {
                // Handle home navigation
                bottomNavigation.getMenu().findItem(R.id.nav_home).setIcon(R.drawable.home_focused);
                clearfocus(bottomNavigation, itemId);
                return true;
            } else if (itemId == R.id.nav_menu) {
                // Handle dashboard navigation
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Handle notifications navigation
                bottomNavigation.getMenu().findItem(R.id.nav_profile).setIcon(R.drawable.default_profile_focus);
                clearfocus(bottomNavigation, itemId);
                return true;
            } else {
                return false;
            }
        });
    }



    private void clearfocus(BottomNavigationView bottomNavigation, int selectedItemId) {
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
            // Add more if statements for additional items as needed
        }
    }



}