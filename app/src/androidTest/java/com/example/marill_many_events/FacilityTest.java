package com.example.marill_many_events;

import static org.mockito.Mockito.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.marill_many_events.fragments.CreateFacilityFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class FacilityTest {

    private FirebaseFirestore mockFirestore;
    private DocumentReference mockFacilityDocRef;
    private DocumentReference mockEventDocRef;
    private CreateFacilityFragment createFacilityFragment;
    private String facilityId = "testFacilityId";
    private ArrayList<String> eventIds;

    @Before
    public void setUp() {
        // Initialize mocks
        mockFirestore = mock(FirebaseFirestore.class);
        mockFacilityDocRef = mock(DocumentReference.class);
        mockEventDocRef = mock(DocumentReference.class);

        // Initialize the fragment under test
        createFacilityFragment = new CreateFacilityFragment();
        createFacilityFragment.firestore = mockFirestore;
        createFacilityFragment.facilityId = facilityId;

        // Mock event IDs associated with the facility
        eventIds = new ArrayList<>();
        eventIds.add("event1");
        eventIds.add("event2");
    }

    @Test
    public void testDeleteFacilityDeletesAllAssociatedEvents() {
        // Arrange
        when(mockFirestore.collection("facilities").document(facilityId)).thenReturn(mockFacilityDocRef);

        // Mock retrieving the facility document
        when(mockFacilityDocRef.get()).thenReturn(taskWithSuccessMock(eventIds));

        // Mock event deletion calls
        for (String eventId : eventIds) {
            when(mockFirestore.collection("events").document(eventId)).thenReturn(mockEventDocRef);
        }

        // Act
        createFacilityFragment.deleteFacility();

        // Assert
        // Verify that deletion was called for each event
        for (String eventId : eventIds) {
            verify(mockFirestore.collection("events").document(eventId)).delete();
        }

        // Verify that the facility document itself was deleted
        verify(mockFacilityDocRef).delete();
    }

    // Helper method to simulate a successful task returning an ArrayList of event IDs
    private Task<DocumentSnapshot> taskWithSuccessMock(ArrayList<String> events) {
        DocumentSnapshot mockDocumentSnapshot = mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("events")).thenReturn(events);

        Task<DocumentSnapshot> mockTask = mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);

        return mockTask;
    }
}

