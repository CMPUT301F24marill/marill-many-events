// File: app/src/androidTest/java/com/example/marill_many_events/EntrantsDrawActivityTest.java
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import com.example.marill_many_events.activities.EntrantsDrawActivity;

@RunWith(AndroidJUnit4.class)
public class EntrantsDrawActivityTest {

    @Rule
    public ActivityTestRule<EntrantsDrawActivity> activityRule = new ActivityTestRule<>(EntrantsDrawActivity.class);

    @Test
    public void testDrawEntrant() {
        // Navigate to the entrant draw screen
        onView(withId(R.id.button_view_entrant_draw)).perform(click());

        // Click on the draw button
        onView(withId(R.id.button_draw_entrant)).perform(click());

        // Verify the draw result
        onView(withId(R.id.text_draw_result)).check(matches(withText("Expected Result")));
    }
}