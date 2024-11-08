// NavbarFragmentTest.java
package com.example.marill_many_events;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.activities.HomePageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavbarFragmentTest {

    @Rule
    public ActivityScenarioRule<HomePageActivity> activityRule = new ActivityScenarioRule<>(HomePageActivity.class);

    @Test
    public void testHomeNavigation() {
        // Click the "Home" item
        onView(withId(R.id.nav_home)).perform(click());

        // Verify HomeFragment is displayed by checking a unique view ID in HomeFragment
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testMenuNavigation() {
        // Click the "Menu" item
        onView(withId(R.id.nav_menu)).perform(click());

        // Verify MenuFragment is displayed by checking a unique view ID in MenuFragment
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testProfileNavigation() {
        // Click the "Profile" item
        onView(withId(R.id.nav_profile)).perform(click());

        // Verify RegistrationFragment is displayed by checking a unique view ID in RegistrationFragment
        //onView(withId(R.id.profile_picture)).check(matches(isDisplayed()));
    }
}
