package com.example.marill_many_events.activities;

import android.util.Log;

import com.example.marill_many_events.models.Entrant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntrantsDrawActivity {

    private static final String TAG = "Firestore";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String eventID;

    public EntrantsDrawActivity(String eventID) {
        this.eventID = eventID;
    }

    public void fetchAndSelectEntrants(int numberOfEntrantsToSelect) {
        if (eventID == null || eventID.isEmpty()) {
            Log.e(TAG, "Event ID is not initialized.");
            return;
        }

        CollectionReference userInWaitList = db.collection("events")
                .document(eventID)
                .collection("waitList");

        userInWaitList.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Entrant> entrantList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Entrant entrant = document.toObject(Entrant.class);
                    entrantList.add(entrant);
                }
                Log.d(TAG, "Fetched " + entrantList.size() + " entrants.");

                // Randomly select entrants
                List<Entrant> selectedEntrants = selectRandomEntrants(entrantList, numberOfEntrantsToSelect);
                Log.d(TAG, "Selected " + selectedEntrants.size() + " entrants.");

                // Store selected entrants in Firestore
                storeSelectedEntrants(selectedEntrants);

            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private List<Entrant> selectRandomEntrants(List<Entrant> entrantList, int numberOfEntrantsToSelect) {
        if (entrantList.isEmpty()) {
            Log.w(TAG, "The entrant list is empty.");
            return new ArrayList<>();
        }

        if (numberOfEntrantsToSelect >= entrantList.size()) {
            Log.w(TAG, "Requested number of entrants exceeds or equals the available entrants. Returning all entrants.");
            return new ArrayList<>(entrantList);
        }

        Collections.shuffle(entrantList);
        return new ArrayList<>(entrantList.subList(0, numberOfEntrantsToSelect));
    }

    private void storeSelectedEntrants(List<Entrant> selectedEntrants) {
        if (selectedEntrants.isEmpty()) {
            Log.w(TAG, "No entrants to store.");
            return;
        }

        WriteBatch batch = db.batch();
        CollectionReference selectedEntrantsRef = db.collection("events")
                .document(eventID)
                .collection("selectedEntrants");

        for (Entrant entrant : selectedEntrants) {
            String entrantId = entrant.getUser().getEmail(); // Assuming email is unique
            batch.set(selectedEntrantsRef.document(entrantId), entrant);
        }

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Selected entrants successfully stored in Firestore.");
            } else {
                Log.w(TAG, "Error storing selected entrants.", task.getException());
            }
        });
    }
}



//package com.example.marill_many_events.activities;
//
//import android.util.Log;
//
//import com.example.marill_many_events.models.Entrant;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class EntrantsDrawActivity {
//
//    private static final String TAG = "EntrantsDrawActivity";
//
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private final String eventID;
//
//    public EntrantsDrawActivity(String eventID) {
//        this.eventID = eventID;
//    }
//
//    public interface EntrantsCallback {
//        void onEntrantsSelected(List<Entrant> selectedEntrants);
//    }
//
//    public void fetchAndSelectEntrants(int numberOfEntrantsToSelect, EntrantsCallback callback) {
//        if (eventID == null || eventID.isEmpty()) {
//            Log.e(TAG, "Event ID is not initialized.");
//            callback.onEntrantsSelected(new ArrayList<>());
//            return;
//        }
//
//        CollectionReference userInWaitList = db.collection("events")
//                .document(eventID)
//                .collection("waitList");
//
//        userInWaitList.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<Entrant> entrantList = new ArrayList<>();
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Entrant entrant = document.toObject(Entrant.class);
//                    entrantList.add(entrant);
//                }
//                Log.d(TAG, "Fetched " + entrantList.size() + " entrants.");
//
//                // Randomly select entrants
//                List<Entrant> selectedEntrants = selectRandomEntrants(entrantList, numberOfEntrantsToSelect);
//                Log.d(TAG, "Selected " + selectedEntrants.size() + " entrants.");
//
//                callback.onEntrantsSelected(selectedEntrants);
//            } else {
//                Log.w(TAG, "Error getting documents.", task.getException());
//                callback.onEntrantsSelected(new ArrayList<>());
//            }
//        });
//    }
//
//    private List<Entrant> selectRandomEntrants(List<Entrant> entrantList, int numberOfEntrantsToSelect) {
//        if (entrantList.isEmpty()) {
//            Log.w(TAG, "The entrant list is empty.");
//            return new ArrayList<>();
//        }
//
//        if (numberOfEntrantsToSelect >= entrantList.size()) {
//            Log.w(TAG, "Requested number of entrants exceeds or equals the available entrants. Returning all entrants.");
//            return new ArrayList<>(entrantList);
//        }
//
//        Collections.shuffle(entrantList);
//        return new ArrayList<>(entrantList.subList(0, numberOfEntrantsToSelect));
//    }
//}
