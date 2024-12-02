package com.example.marill_many_events;

import static org.mockito.Mockito.*;

import android.view.View;
import android.widget.Button;

import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.fragments.AdminEventDetailsFragment;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.GenerateQRcode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

import androidx.fragment.app.FragmentTransaction;

public class AdminEventDetailsFragmentTest {

    @Mock
    private AdminEventDetailsFragment.OnItemClickListener mockListener; // Mock the listener (AdminPageActivity)

    @Mock
    private Event mockEvent; // Mock the Event object

    private AdminEventDetailsFragment fragment;
    private Button createButton; // The delete button

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Create the fragment and attach the mock listener
        fragment = new AdminEventDetailsFragment(mockListener);

        // Simulate the fragment's view being created (using reflection to get the button)
        try {
            Field createButtonField = AdminEventDetailsFragment.class.getDeclaredField("createButton");
            createButtonField.setAccessible(true); // Make the private field accessible
            createButton = (Button) createButtonField.get(fragment); // Get the button reference from the fragment
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteEvent() {
        // Simulate that the event has been set in the fragment (via parent activity or ViewModel)
        when(mockEvent.getFirebaseID()).thenReturn("mockEventID");

        // Simulate the button click to trigger event deletion
        createButton.performClick(); // Simulate button click for deletion

        // Verify that the listener's onDeleteHashDataClick() method was called with the event
        verify(mockListener, times(1)).onDeleteHashDataClick(mockEvent);
    }
}


