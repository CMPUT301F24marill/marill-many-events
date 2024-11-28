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

/**
 * NavbarFragment represents a fragment that contains a {@link BottomNavigationView}
 * for navigating between different sections of the application. This fragment handles
 * user interactions with the navigation bar and communicates with the parent activity
 * using the {@link NavbarListener} interface.
 */
public class AdminNavbarFragment extends Fragment {

    private BottomNavigationView bottomNavigation;
    public NavbarListener navbarListener; // Changed to NavbarListener

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater           The LayoutInflater used to inflate the view.
     * @param container          The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     * @return The view for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navbar_admin, container, false);
    }

    /**
     * Called after the view has been created. Initializes the BottomNavigationView and sets
     * up listeners for navigation item selection.
     *
     * @param view              The view returned by {@link #onCreateView}.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigation = view.findViewById(R.id.bottom_navigation);


        // Set the listener here
        if (getActivity() instanceof NavbarListener) {
            navbarListener = (NavbarListener) getActivity();
        }

        // Handle item selection for navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            clearFocus(bottomNavigation, itemId);

            if (itemId == R.id.nav_facilities) {
                // Handle home navigation
                if (navbarListener != null) {
                    // Pass the deviceId when opening the registration fragment
                    bottomNavigation.getMenu().findItem(R.id.nav_facilities).setIcon(R.drawable.home_focused);
                    navbarListener.onFacilitiesSelected();
                }

                return true;
            } else if (itemId == R.id.nav_images) {
                if (navbarListener != null) {
                    // Pass the deviceId when opening the registration fragment
                    bottomNavigation.getMenu().findItem(R.id.nav_images).setIcon(R.drawable.images);
                    navbarListener.onImagesSelected();
                }
                return true;
            } else if (itemId == R.id.nav_events) {
                // Open registration fragment when profile is clicked
                if (navbarListener != null) {
                    // Pass the deviceId when opening the registration fragment
                    bottomNavigation.getMenu().findItem(R.id.nav_events).setIcon(R.drawable.menu);
                    navbarListener.onEventsSelected();
                }
                return true;
            } else if (itemId == R.id.nav_profiles) {
                // Open registration fragment when profile is clicked
                if (navbarListener != null) {
                    // Pass the deviceId when opening the registration fragment
                    bottomNavigation.getMenu().findItem(R.id.nav_profiles).setIcon(R.drawable.default_profile_focus);
                    navbarListener.onProfilesSelected();
                }
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * Clears the focus from unselected menu icons in the {@link BottomNavigationView}.
     * This method ensures that only the selected item is highlighted, and other items revert
     * to their default icons.
     *
     * @param bottomNavigation The {@link BottomNavigationView} instance.
     * @param selectedItemId   The ID of the currently selected menu item.
     */
    private void clearFocus(BottomNavigationView bottomNavigation, int selectedItemId) {
        // Iterate through all menu items
        for (int i = 0; i < bottomNavigation.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigation.getMenu().getItem(i);
            if (menuItem.getItemId() == selectedItemId) {
                // Skip the selected item
                continue;
            }

            // Reset the icon for unselected items
            if (menuItem.getItemId() == R.id.nav_facilities) {
                menuItem.setIcon(R.drawable.home); // Reset to unselected icon
            } else if (menuItem.getItemId() == R.id.nav_images) {
                menuItem.setIcon(R.drawable.images); // Reset to unselected icon
            } else if (menuItem.getItemId() == R.id.nav_events) {
                menuItem.setIcon(R.drawable.menu); // Reset to unselected icon
            } else if (menuItem.getItemId() == R.id.nav_profiles) {
                menuItem.setIcon(R.drawable.default_profile); // Reset to unselected icon
            }
        }
    }
}
