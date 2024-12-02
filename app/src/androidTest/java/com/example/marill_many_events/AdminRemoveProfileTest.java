package com.example.marill_many_events;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminRemoveProfileTest {

    @Rule
    public ActivityTestRule<AdminActivity> activityRule = new ActivityTestRule<>(AdminActivity.class);

    @Test
    public void testRemoveProfile() {
        // Navigate to the profile list
        onView(withId(R.id.button_view_profiles)).perform(click());

        // Click on the profile to remove
        onView(withText("Profile Name")).perform(click());

        // Click on the remove button
        onView(withId(R.id.button_remove_profile)).perform(click());

        // Confirm the removal
        onView(withText("Yes")).perform(click());
    }
}