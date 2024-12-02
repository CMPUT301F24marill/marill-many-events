package com.example.marill_many_events;

import android.os.Bundle;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateFacilityFragmentTest {

    private FirebaseFirestore mockFirestore;

    @Before
    public void setUp() {
        // Mock Firestore instance
        mockFirestore = mock(FirebaseFirestore.class);

        // Launch the fragment in isolation
        FragmentScenario<CreateFacilityFragment> scenario = FragmentScenario.launchInContainer(CreateFacilityFragment.class, new Bundle(), R.style.Theme_marill, (FragmentFactory) null);
        scenario.onFragment(fragment -> {
            // Inject the mock Firestore instance into the fragment
            fragment.firestore = mockFirestore;
        });
    }

    @Test
    public void testCreateFacility() {
        // Simulate user input
        onView(withId(R.id.editTextFacilityName)).perform(replaceText("Test Facility"));
        onView(withId(R.id.editTextFacilityLocation)).perform(replaceText("Test Location"));

        // Simulate button click
        onView(withId(R.id.buttonCreateFacility)).perform(click());

        // Verify that the facility name and location are set correctly
        onView(withId(R.id.editTextFacilityName)).check(matches(withText("Test Facility")));
        onView(withId(R.id.editTextFacilityLocation)).check(matches(withText("Test Location")));
    }

    @Test
    public void testDeleteFacility() {
        // Simulate button click
        onView(withId(R.id.buttonDeleteFacility)).perform(click());

        // Verify that the deletion confirmation dialog is displayed
        onView(withText("Are you sure you want to delete this facility and all its events?")).check(matches(isDisplayed()));
    }
}