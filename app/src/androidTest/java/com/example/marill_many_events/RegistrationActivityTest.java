package com.example.marill_many_events;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.marill_many_events.activities.RegistrationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationActivityTest {

    @Rule
    public ActivityScenarioRule<RegistrationActivity> scenario = new ActivityScenarioRule<>(RegistrationActivity.class);

    @Test
    public void testRegistrationFragmentIsDisplayed() {
        // Verify RegistrationFragment is displayed by checking the presence of profile_picture
        onView(withId(R.id.profile_picture)).check(matches(isDisplayed()));
    }

    @Test
    public void testEnterUserDetails() {
        // Simulate entering name in the name field
        onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
        // Check if the text "John Doe" is displayed in the name field
        onView(withId(R.id.editTextName)).check(matches(withText("John Doe")));

        // Simulate entering email in the email field
        onView(withId(R.id.editTextEmail)).perform(typeText("johndoe@example.com"), closeSoftKeyboard());
        // Check if the text "johndoe@example.com" is displayed in the email field
        onView(withId(R.id.editTextEmail)).check(matches(withText("johndoe@example.com")));

        // Simulate entering phone number in the mobile field
        onView(withId(R.id.editTextMobile)).perform(typeText("1234567890"), closeSoftKeyboard());
        // Check if the text "1234567890" is displayed in the mobile field
        onView(withId(R.id.editTextMobile)).check(matches(withText("1234567890")));
    }

    @Test
    public void testRegistrationFormSubmission() {
        // Enter user details
        onView(withId(R.id.editTextName)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText("johndoe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextMobile)).perform(typeText("1234567890"), closeSoftKeyboard());

        // Click on the Register button
        onView(withId(R.id.buttonRegister)).perform(click());

        // Verify that some post-submission view or confirmation message is displayed
        // Example: Assume there's a TextView with id "confirmation_message" showing "Registration Successful"
        onView(withId(R.id.confirmationText)).check(matches(withText("Registration Successful")));
    }
}
