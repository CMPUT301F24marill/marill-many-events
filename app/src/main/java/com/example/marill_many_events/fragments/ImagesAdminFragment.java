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
 * A fragment that displays all images (avatars and posters) in separate lists.
 * The images can be managed by an admin, including viewing, deleting, or performing other operations.
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
    /**
     * Attaches the fragment to the activity and checks if the activity implements the Identity interface.
     *
     * @param context The context to attach the fragment to.
     * @throws ClassCastException if the context does not implement Identity.
     */
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
    /**
     * Called when the fragment is resumed. Fetches the images (avatars and posters) from Firebase Storage.
     */
    @Override
    public void onResume() {
        super.onResume();
        getImages();
        Log.d("FragmentLifecycle", "Fragment ImagesAdminFragment is now visible.");

    }
    /**
     * Inflates the fragment layout, sets up the RecyclerView for avatars and posters,
     * and initializes the Firebase Storage references.
     *
     * @param inflater The LayoutInflater used to inflate the fragment's view.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The view for the fragment.
     */
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
     * Adds an avatar image URL to the avatar list and notifies the adapter.
     *
     * @param url The URL of the avatar image to add.
     */
    public void addToAvatarItemList(String url){
        if (!avatarItemList.contains(url)) {
            avatarItemList.add(url);
        }
        avatarAdapter.notifyDataSetChanged();
    }

    /**
     * Adds a poster image URL to the poster list and notifies the adapter.
     *
     * @param url The URL of the poster image to add.
     */
    public void addToPosterItemList(String url){
        if (!posterItemList.contains(url)) {
            posterItemList.add(url);
        }
        posterAdapter.notifyDataSetChanged();
    }

    /**
     * Removes an image URL from both the avatar and poster lists and updates the adapters.
     *
     * @param url The URL of the image to remove.
     */
    public void removeItemfromList(String url){
        if (avatarItemList.contains(url)) {
            avatarItemList.remove(url);
        }
        if (posterItemList.contains(url)) {
            posterItemList.remove(url);
        }
    }
    /**
     * Handles item click events on an image.
     *
     * @param url The URL of the clicked image.
     */
    @Override
    public void onItemClick(String url) {
    }

    /**
     * Deletes an image from Firebase Storage and removes it from the local list.
     *
     * @param url The URL of the image to delete.
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