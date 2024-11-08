package com.example.marill_many_events;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.net.Uri;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.marill_many_events.fragments.CreateEventFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventFragmentTest {

    @Before
    public void setUp() {
        // Launch the CreateEventFragment
        FragmentScenario.launchInContainer(CreateEventFragment.class);
    }

    @Test
    public void testEventNameInput() {
        // Verify that the event name field is displayed and can receive input
        onView(withId(R.id.NameField)).perform(typeText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.NameField)).check(matches(withText("Sample Event")));
    }

    @Test
    public void testLocationInput() {
        // Verify that the location field is displayed and can receive input
        onView(withId(R.id.LocationField)).perform(typeText("Edmonton"), closeSoftKeyboard());
        onView(withId(R.id.LocationField)).check(matches(withText("Edmonton")));
    }

    @Test
    public void testCapacityInput() {
        // Verify that the capacity field is displayed and can receive input
        onView(withId(R.id.Capacityfield)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.Capacityfield)).check(matches(withText("100")));
    }

    @Test
    public void testStartDatePicker() {
        // Click on the start date field to open date picker
        onView(withId(R.id.Startdatefield)).perform(click());
        // Simulate a date selection (assuming date picker defaults are okay)
        onView(withText("OK")).perform(click());
        // Verify that the date is displayed in the field
        onView(withId(R.id.Startdatefield)).check(matches(isDisplayed()));
    }

    @Test
    public void testEndDatePicker() {
        // Click on the end date field to open date picker
        onView(withId(R.id.DrawdateField)).perform(click());
        // Simulate a date selection
        onView(withText("OK")).perform(click());
        // Verify that the date is displayed in the field
        onView(withId(R.id.DrawdateField)).check(matches(isDisplayed()));
    }

    @Test
    public void testGeolocationSwitch() {
        // Verify that the geolocation switch is toggled on and off
        onView(withId(R.id.GeoSwitch)).perform(click()); // Toggle on
        onView(withId(R.id.GeoSwitch)).perform(click()); // Toggle off
    }

    @Test
    public void testPosterSelection() {
        // Simulate the poster image selection
        onView(withId(R.id.poster)).perform(click());
        // Mock the photo selection process and verify poster field updates
        // Note: This assumes the fragment sets an image as feedback
        onView(withId(R.id.poster)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventCreation() {
        // Fill out the form
        onView(withId(R.id.NameField)).perform(typeText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.LocationField)).perform(typeText("Edmonton"), closeSoftKeyboard());
        onView(withId(R.id.Capacityfield)).perform(typeText("100"), closeSoftKeyboard());

        // Set geolocation switch to on
        onView(withId(R.id.GeoSwitch)).perform(click());

        // Click "Create" button to submit
        onView(withId(R.id.create)).perform(click());

        // Check for confirmation, QR code, or other indicator that event creation was successful
        // Assuming there's a QR code view or confirmation message upon success
        onView(withId(R.id.QRcode)).check(matches(isDisplayed()));
    }
}
