package com.example.marill_many_events.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.AdminPageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all events as a list, events can either be user's waitlist or organizer's created events
 */
public class ImagesAdminFragment extends Fragment implements ImageyArrayAdapter.OnItemClickListener{

    private RecyclerView avatarlist;
    private RecyclerView posterlist;
    private ImageyArrayAdapter avatarAdapter;
    private ImageyArrayAdapter posterAdapter;
    private List<String> avatarItemList;
    private List<String> posterItemList;

    ScanOptions options = new ScanOptions();

    private FirebaseEvents firebaseEvents;
    private FirebaseStorage firestore;
    private StorageReference folder;
    private Identity identity;
    StorageReference user;
    StorageReference user_posters;

    /**
     * Default constructor for WaitlistFragment.
     * Required to ensure proper fragment instantiation.
     */
    public ImagesAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Make sure the activity implements the required interface
        if (context instanceof Identity) {
            identity = (Identity) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement Identity Interface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getImages();
        Log.d("FragmentLifecycle", "Fragment ImagesAdminFragment is now visible.");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment

        firestore = identity.getStorage();
        user = firestore.getReference().child("/profile_pictures");
        user_posters = firestore.getReference().child("/event_posters/eventposters");

        View view = inflater.inflate(R.layout.fragment_imageslist, container, false);

        ImageView gearButton = view.findViewById(R.id.admin_gear);

        gearButton.setOnClickListener(v -> {
            AdminPageActivity parentActivity = (AdminPageActivity) getActivity();
            if (parentActivity != null) {
                // navigate to AdminPageActivity
                parentActivity.openAdmin();
            }
        });

        // Initialize RecyclerView and CardAdapter for avatars
        avatarlist = view.findViewById(R.id.avatar_list);
        avatarlist.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        // Initialize the data list for avatars
        avatarItemList = new ArrayList<String>();

        // Initialize the adapter and set it to RecyclerView for avatars
        avatarAdapter = new ImageyArrayAdapter(avatarItemList, this);
        avatarlist.setAdapter(avatarAdapter);

        // Initialize RecyclerView and CardAdapter for posters
        posterlist = view.findViewById(R.id.posters_list);
        posterlist.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize the data list for posters
        posterItemList = new ArrayList<String>();

        // Initialize the adapter and set it to RecyclerView for posters
        posterAdapter = new ImageyArrayAdapter(posterItemList, this);
        posterlist.setAdapter(posterAdapter);

        return view;
    }

    /**
     * Get all of the Images that a user is registered in and populate the adapter
     */
    public void getImages(){
        avatarItemList.clear();
        posterItemList.clear();
        user.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>(){
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item: listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            addToAvatarItemList(url);
                            return ;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ImageURL", "Fail to get avatar image url");
                        }
                    });
                }
                return ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ImageURL", "Fail to list avatar files: " + e.getMessage());
                e.printStackTrace();
            }
        });

        user_posters.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>(){
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item: listResult.getItems()){
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            addToPosterItemList(url);
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ImageURL", "Fail to get poster image url");
                        }
                    });
                }
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ImageURL", "Fail to list poster files: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Add an avatar to the list
     */
    public void addToAvatarItemList(String url){
        if (!avatarItemList.contains(url)) {
            avatarItemList.add(url);
        }
        avatarAdapter.notifyDataSetChanged();
    }

    /**
     * Add a poster to the list
     */
    public void addToPosterItemList(String url){
        if (!posterItemList.contains(url)) {
            posterItemList.add(url);
        }
        posterAdapter.notifyDataSetChanged();
    }

    /**
     * Remove an item from the list
     */
    public void removeItemfromList(String url){
        if (avatarItemList.contains(url)) {
            avatarItemList.remove(url);
        }
        if (posterItemList.contains(url)) {
            posterItemList.remove(url);
        }
    }

    @Override
    public void onItemClick(String url) {
    }

    /**
     * Delete a file from Firebase Storage by url
     * @param url: url of image
     */
    @Override
    public void onDeleteClick(String url) {

        String fullPath = getPathfromURL(url);
        if(fullPath == null){
            return;
        }

        folder = firestore.getReference().child(fullPath);

        Log.d("S", "goal: "+fullPath);

        folder.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Photo deleted successfully");

                    //remove from local list
                    avatarItemList.remove(url);
                    posterItemList.remove(url);
                    posterAdapter.notifyDataSetChanged();
                    avatarAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to delete photo: " + e.getMessage());
                });
    }

    /**
     * get a file path from url
     * @param url: url of image
     * @return string path to file, null if url doesn't match base
     */
    private String getPathfromURL(String url){
        String urlBase = "https://firebasestorage.googleapis.com/v0/b/marill-many-events.appspot.com/o";
        if(url.startsWith(urlBase)) {
            String path = url.substring(urlBase.length());
            return path.substring(0, path.indexOf("?")).replace("%2F", "/");
        }
        else {
            Log.d("URL path", "URL does not start with URL base");
            return null;
        }
    }
}