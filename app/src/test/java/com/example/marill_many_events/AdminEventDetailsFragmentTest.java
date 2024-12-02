package com.example.marill_many_events;

import static org.mockito.Mockito.*;

import android.widget.Button;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.fragments.AdminEventDetailsFragment;
import com.example.marill_many_events.models.Event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@Config(manifest = Config.NONE)
public class AdminEventDetailsFragmentTest {

    @Mock
    private AdminEventDetailsFragment.OnItemClickListener mockListener; // Mock the listener

    @Mock
    private Event mockEvent; // Mock the Event object

    private FragmentScenario<AdminEventDetailsFragment> scenario;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Launch the fragment
        scenario = FragmentScenario.launchInContainer(AdminEventDetailsFragment.class);
    }

    @Test
    public void testDeleteEvent() {
        scenario.onFragment(fragment -> {
            // Simulate that the event has been set in the fragment
            when(mockEvent.getFirebaseID()).thenReturn("mockEventID");

            // Get the delete button
            Button createButton = fragment.getView().findViewById(R.id.create);

            // Simulate the button click to trigger event deletion
            createButton.performClick(); // Simulate button click for deletion

            // Verify that the listener's onDeleteHashDataClick() method was called with the event
            verify(mockListener, times(1)).onDeleteHashDataClick(mockEvent);
        });
    }
}