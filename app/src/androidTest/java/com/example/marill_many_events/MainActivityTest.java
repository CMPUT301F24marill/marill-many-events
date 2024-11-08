package com.example.marill_many_events;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.activities.MainActivity;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.activities.RegistrationActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoginButton_withExistingDeviceId() throws InterruptedException {
        Intents.init();

        // Simulate click on login button
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Thread.sleep(2000);

        // Verify that HomePageActivity is launched if the user is registered
        Intents.intended(IntentMatchers.hasComponent(HomePageActivity.class.getName()));


        Intents.release();
    }

    @Test
    public void testLoginButton_withNonExistingDeviceId() throws InterruptedException {

        Random rand = new Random();
        String deviceID = String.valueOf(rand.nextInt());

        activityRule.getScenario().onActivity(activity -> {
            Random Random;
            activity.deviceId = deviceID; // Inject mock deviceID
        });


        Intents.init();

        // Simulate click on login button
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Thread.sleep(2000);


        // Verify that RegistrationActivity is launched if the user is not registered
        Intents.intended(IntentMatchers.hasComponent(RegistrationActivity.class.getName()));

        // Simulate user entering name, email, and phone number in registration form
        Espresso.onView(ViewMatchers.withId(R.id.editTextName))
                .perform(ViewActions.typeText("Tester test"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextEmail))
                .perform(ViewActions.typeText("test@test.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextMobile))
                .perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());

        // Submit the registration form
        Espresso.onView(ViewMatchers.withId(R.id.buttonRegister)).perform(ViewActions.click());

        Thread.sleep(2000);


        // Re-test the login button and check for HomePageActivity and navbar fragment
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Thread.sleep(2000);

        Intents.intended(IntentMatchers.hasComponent(HomePageActivity.class.getName()));


        Intents.release();
    }
}



