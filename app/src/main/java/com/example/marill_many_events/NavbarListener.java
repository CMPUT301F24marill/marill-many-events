package com.example.marill_many_events;

/**
 * NavbarListener is an interface for handling navigation events in the app's navigation bar.
 * It provides methods to respond to different navigation actions such as profile, home, and menu selection.
 */
public interface NavbarListener {
    /**
     * Called when the profile option is selected in the navigation bar.
     */
    void onProfileSelected();
    /**
     * Called when the home option is selected in the navigation bar.
     */// Method to handle profile selection
    void onHomeSelected();
    /**
     * Called when the menu option is selected in the navigation bar.
     */
    void onMenuSelected();
}
