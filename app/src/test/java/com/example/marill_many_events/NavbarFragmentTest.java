package com.example.marill_many_events;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.runner.RunWith;


import com.example.marill_many_events.fragments.NavbarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class NavbarFragmentTest {

    private NavbarListener mockNavbarListener;

    @Before
    public void setUp() {
        // Initialize the mock listener before each test
        mockNavbarListener = mock(NavbarListener.class);
    }

    @Test
    public void testFragmentInflation() {
        // Create the FragmentScenario to test the NavbarFragment
        FragmentScenario<NavbarFragment> scenario = FragmentScenario.launchInContainer(NavbarFragment.class);

        scenario.onFragment(fragment -> {
            // Verify that the BottomNavigationView is not null
            BottomNavigationView bottomNavigationView = fragment.getView().findViewById(R.id.bottom_navigation);
            assertNotNull(bottomNavigationView);
        });
    }

    @Test
    public void testOnViewCreated_SetupListener() {
        FragmentScenario<NavbarFragment> scenario = FragmentScenario.launchInContainer(NavbarFragment.class);

        scenario.onFragment(fragment -> {
            BottomNavigationView bottomNavigationView = fragment.getView().findViewById(R.id.bottom_navigation);
            // Simulate the NavbarListener setup
            fragment.onAttach(getApplicationContext());
            fragment.navbarListener = mockNavbarListener; // Inject mock listener
            fragment.onViewCreated(fragment.getView(), null);

            // Simulate the home navigation selection
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            // Verify that the home item icon is set correctly
            verify(mockNavbarListener, never()).onProfileSelected();
        });
    }

    @Test
    public void testOnProfileSelected() {
        FragmentScenario<NavbarFragment> scenario = FragmentScenario.launchInContainer(NavbarFragment.class);

        scenario.onFragment(fragment -> {
            // Set the mock listener
            fragment.navbarListener = mockNavbarListener;

            // Simulate the profile navigation selection
            BottomNavigationView bottomNavigationView = fragment.getView().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);

            // Verify that the onProfileSelected method is called
            verify(mockNavbarListener).onProfileSelected();
        });
    }
}
