package com.example.marill_many_events;


import com.example.marill_many_events.models.Image;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImageTest {

    private Image image;

    @Before
    public void setUp() {
        image = new Image("https://example.com/image.jpg", true);
    }

    @Test
    public void testGetImageURL() {
        assertEquals("https://example.com/image.jpg", image.getImageURL());
    }

    @Test
    public void testSetImageURL() {
        image.setImageURL("https://newexample.com/newimage.jpg");
        assertEquals("https://newexample.com/newimage.jpg", image.getImageURL());
    }

    @Test
    public void testIsGenerated() {
        assertTrue(image.isGenerated());
    }

    @Test
    public void testSetGenerated() {
        image.setGenerated(false);
        assertFalse(image.isGenerated());
    }
}
