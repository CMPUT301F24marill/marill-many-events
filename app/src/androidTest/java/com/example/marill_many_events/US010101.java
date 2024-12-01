package com.example.marill_many_events;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.EventsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class US010101 {

    @Rule
    public ActivityScenarioRule<EventsActivity> activityRule =
            new ActivityScenarioRule<>(EventsActivity.class);

    @Test
    public void testJoinWaitingList() {
        // Step 1: Select an event from the list
        Espresso.onView(withId(R.id.waitlist_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        // Step 2: Verify navigation to event details
        Espresso.onView(withId(R.id.draw_entrants_button))
                .check(matches(ViewMatchers.isDisplayed()));

        // Step 3: Simulate user action (Join Waiting List logic assumed here)
        // If clicking an event adds the user to the waitlist, we validate with a success message or updated UI
        Espresso.onView(withId(R.id.waitlist_list))
                .check(matches(ViewMatchers.hasDescendant(withText("Entrant Name"))));
    }
}
