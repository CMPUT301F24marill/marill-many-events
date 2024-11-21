package com.example.marill_many_events;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FirebaseTestUtils {

    private static final String MOCK_USER_ID = "testUser123";

    /**
     * Sets up a mock user in Firestore with sample data for testing.
     */
    public static void setupMockUser(FirebaseFirestore firestore) {
        // Define mock user data
        Map<String, Object> mockUserData = new HashMap<>();
        mockUserData.put("name", "Test User");
        mockUserData.put("email", "testuser@example.com");
        mockUserData.put("phone", "1234567890");
        mockUserData.put("profilePictureUrl", null); // or use a dummy URL if needed
        mockUserData.put("waitList", new ArrayList<>()); // Initialize empty waitlist

        // Set up the mock user in Firestore
        firestore.collection("users").document(MOCK_USER_ID)
                .set(mockUserData)
                .addOnSuccessListener(aVoid -> {
                    // Mock user created successfully; proceed with your test
                    System.out.println("Mock user created successfully.");
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur during the setup
                    System.err.println("Failed to create mock user: " + e.getMessage());
                });
    }

    /**
     * Gets the mock user ID for testing purposes.
     *
     * @return the mock user ID
     */
    public static String getMockUserId() {
        return MOCK_USER_ID;
    }
}
