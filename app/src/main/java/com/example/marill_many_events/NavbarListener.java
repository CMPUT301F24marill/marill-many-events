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
    /**
     * Called when the facilities option is selected in the navigation bar FOR ADMINS.
     */
    void onFacilitiesSelected();
    /**
     * Called when the images option is selected in the navigation bar FOR ADMINS.
     */// Method to handle profile selection
    void onImagesSelected();
    /**
     * Called when the events option is selected in the navigation bar FOR ADMINS.
     */// Method to handle profile selection
    void onEventsSelected();
    /**
     * Called when the profiles option is selected in the navigation bar FOR ADMINS.
     */
    void onProfilesSelected();
}
