package com.example.marill_many_events;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import android.graphics.Bitmap;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ProfilePictureGeneratorTest {

    @Before
    public void setUp() {
        // Any setup needed for instrumented tests can be done here.
    }

    @Test
    public void testGenerateAvatar() {
        String input = "John";
        int size = 100;
        Bitmap avatar = ProfilePictureGenerator.generateProfilePicture(input, size);

        // Check that the avatar is not null
        assertNotNull(avatar);

        // Check the size of the generated avatar
        assertEquals(size, avatar.getWidth());
        assertEquals(size, avatar.getHeight());
    }

    @Test
    public void testGenerateAvatarColor() {
        String input = "TestColor";
        int size = 100;
        Bitmap avatar = ProfilePictureGenerator.generateProfilePicture(input, size);

        // Verify that the generated avatar has a pixel color set
        int color = avatar.getPixel(size / 2, size / 2);
        assertNotEquals(0, color); // Ensure the color is not transparent or default
    }

    @Test
    public void testGenerateAvatarInitialLetter() {
        String input = "Alice";
        int size = 100;
        Bitmap avatar = ProfilePictureGenerator.generateProfilePicture(input, size);

        // Ensure the avatar is created correctly without any issues
        assertNotNull(avatar);
    }
}
