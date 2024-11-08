package com.example.marill_many_events;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.example.marill_many_events.R.id.nav_profile;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.marill_many_events.activities.HomePageActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomePageActivityTest {

    @Rule
    public ActivityScenarioRule<HomePageActivity> activityRule = new ActivityScenarioRule<>(HomePageActivity.class);

    @Before
    public void setUp() {
        // Any initial setup before running tests can be added here
    }

    @Test
    public void testProfileNavigationAndDisplay() {
        // Click the "Profile" item in the bottom navigation to open the RegistrationFragment
        onView(withId(nav_profile)).perform(click());

        // Verify that profile picture and register button in RegistrationFragment are displayed
        onView(withId(R.id.profile_picture)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventCreation() {
        // Navigate to the "Create Event" section by selecting the appropriate item
        onView(withId(R.id.create)).perform(click());

        // Verify CreateEventFragment is displayed by checking specific views
        onView(withId(R.id.NameField)).check(matches(isDisplayed()));
        onView(withId(R.id.LocationField)).check(matches(isDisplayed()));

        // Fill in event details
        onView(withId(R.id.NameField)).perform(typeText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.LocationField)).perform(typeText("Edmonton"), closeSoftKeyboard());

        // Click the "Create" button
        onView(withId(R.id.create)).perform(click());

        // Verify that the event confirmation (or created event) appears on screen
        onView(withText("Sample Event")).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeFragmentListViewData() {
        // Navigate to the "Home" section
        onView(withId(R.id.nav_home)).perform(click());

        // Verify that both WaitlistList and RegistratedList ListViews are displayed in HomeFragment
        onView(withId(R.id.WaitlistList)).check(matches(isDisplayed()));
        onView(withId(R.id.RegistratedList)).check(matches(isDisplayed()));

        // Optionally, verify specific content within the list if data is expected to be pre-populated
        // This example assumes at least one item is in the list and can be tested further if needed
    }
}
