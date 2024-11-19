package com.example.marill_many_events;

import com.example.marill_many_events.models.User;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testSetName() {
        user.setName("Jane Smith");
        assertEquals("Jane Smith", user.getName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", user.getEmail());
    }

    @Test
    public void testGetPhone() {
        assertEquals("1234567890", user.getPhone());
    }

    @Test
    public void testSetPhone() {
        user.setPhone("0987654321");
        assertEquals("0987654321", user.getPhone());
    }

    @Test
    public void testGetProfilePictureUrl() {
        assertEquals("https://example.com/profile.jpg", user.getProfilePictureUrl());
    }

    @Test
    public void testSetProfilePictureUrl() {
        user.setProfilePictureUrl("https://newexample.com/newprofile.jpg");
        assertEquals("https://newexample.com/newprofile.jpg", user.getProfilePictureUrl());
    }
}

