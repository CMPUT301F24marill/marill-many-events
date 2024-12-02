package com.example.marill_many_events;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.R;
import com.example.marill_many_events.fragments.AdminNavbarFragment;
import com.example.marill_many_events.NavbarListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminNavbarFragmentTest {

    private FragmentScenario<AdminNavbarFragment> scenario;
    private NavbarListener mockNavbarListener;

    @Before
    public void setUp() {
        mockNavbarListener = mock(NavbarListener.class);

        scenario = FragmentScenario.launchInContainer(
                AdminNavbarFragment.class,
                null,
                R.style.Theme_marill, // Ensure you use your actual app theme here
                Lifecycle.State.RESUMED
        );
        scenario.onFragment(fragment -> fragment.navbarListener = mockNavbarListener);
    }

    @Test
    public void testFragmentInitialization() {
        // Verify that the view is initialized correctly.
        scenario.onFragment(fragment -> {
            BottomNavigationView bottomNavigation = fragment.getView().findViewById(R.id.bottom_navigation);
            assertNotNull("BottomNavigationView should not be null", bottomNavigation);
        });
    }

    @Test
    public void testNavItemSelectionTriggersListener() {
        scenario.onFragment(fragment -> {
            BottomNavigationView bottomNavigation = fragment.getView().findViewById(R.id.bottom_navigation);
            bottomNavigation.setSelectedItemId(R.id.nav_facilities);

            // Verify the listener method is called
            verify(mockNavbarListener).onFacilitiesSelected();
        });
    }
}