package com.example.marill_many_events;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/**
 * Interface for providing user and application identity details, along with access to Firebase services.
 */
public interface Identity {
    /**
     * Retrieves the unique device ID associated with the current user or device.
     *
     * @return A {@link String} representing the device ID.
     */
    String getdeviceID();
    /**
     * Provides access to the Firebase Storage instance used for file storage operations.
     *
     * @return A {@link FirebaseStorage} instance.
     */
    FirebaseStorage getStorage();
    /**
     * Provides access to the Firebase Firestore instance used for database operations.
     *
     * @return A {@link FirebaseFirestore} instance.
     */
    FirebaseFirestore getFirestore();
}
