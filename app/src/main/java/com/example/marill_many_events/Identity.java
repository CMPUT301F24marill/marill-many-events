package com.example.marill_many_events;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface Identity {
    String getdeviceID();
    FirebaseStorage getStorage();
    FirebaseFirestore getFirestore();
}
