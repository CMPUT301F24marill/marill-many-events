package com.example.marill_many_events.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;

import com.example.marill_many_events.UserCallback;
import com.example.marill_many_events.models.FirebaseUsers;
import com.example.marill_many_events.models.PhotoPicker;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import com.example.marill_many_events.models.User;

import com.example.marill_many_events.models.RegistrationViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * RegistrationFragment is responsible for user registration and profile update functionalities.
 * It provides a form for users to input their details, upload a profile picture,
 * and store the information in Firebase Firestore and Firebase Storage.
 */
public class RegistrationFragment extends Fragment implements PhotoPicker.OnPhotoSelectedListener {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private Button buttonRegister;
    private RegistrationViewModel viewModel;


    private ImageView profilePicture;
    private String profilePictureUrl;
    private Uri profilePictureUri;
    private PhotoPicker photoPicker;
    private User user;
    String name;

    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;

    private boolean isEditMode = false;
    FirebaseUsers firebaseUsers;

    /**
     * Default constructor for RegistrationFragment.
     */
    public RegistrationFragment() {}

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        deviceId = getArguments() != null ? getArguments().getString("deviceId") : null; // Get device ID from arguments
//        firestore = FirebaseFirestore.getInstance();
//        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");

        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        storageReference = identity.getStorage().getReference("profile_pictures");

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new RegistrationViewModel(identity.getFirestore(),
                        identity.getStorage(),
                        identity.getdeviceID());
            }
        }).get(RegistrationViewModel.class);

        photoPicker = new PhotoPicker(this, this);


    }

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater           The LayoutInflater used to inflate the view.
     * @param container          The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     * @return The view for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false); // Ensure this matches your layout XML file name
    }

    /**
     * Initializes UI elements and sets up click listeners.
     *
     * @param view              The view returned by onCreateView.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initialize UI elements
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = view.findViewById(R.id.textInputLayoutMobile);

        buttonRegister = view.findViewById(R.id.buttonRegister);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        profilePicture = view.findViewById(R.id.profile_picture);

        //firebaseUsers.loadUserDetails(); // Try getting an existing user


        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                name = user.getName();
                editTextName.setText(name);
                editTextEmail.setText(user.getEmail());
                editTextMobile.setText(user.getPhone());
            }
        });

        viewModel.isEditMode().observe(getViewLifecycleOwner(), isEditMode -> {
            buttonRegister.setText(isEditMode ? "Save" : "Register");
        });

        viewModel.getProfilePictureUri().observe(getViewLifecycleOwner(), uri -> {
            Log.d("Profile", "Profile URI: " + uri);
            loadProfilewithGlide(uri, null);
        });

        viewModel.getProfilePictureUrl().observe(getViewLifecycleOwner(), url -> {
            Log.d("Profile", "Profile URL: " + url);
            loadProfilewithGlide(null, url);
        });

        viewModel.getProfilePictureUrl().observe(getViewLifecycleOwner(), profilePictureUrl -> {
            profilePicture.setOnClickListener(v -> photoPicker.showPhotoOptions(profilePictureUrl));
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });


        profilePicture.setOnClickListener(v -> photoPicker.showPhotoOptions(null));

        // Set click listener for the register button
        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextMobile.getText().toString().trim();
                Uri profileUri = viewModel.getProfilePictureUri().getValue();

                if (viewModel.isEditMode().getValue()) {
                    viewModel.updateUser(name, email, phone, profileUri);
                } else {
                    viewModel.registerUser(name, email, phone, profileUri);
                }
            }
        });
    }

    /**
     * Generates a profile picture bitmap based on the user's name.
     *
     * @return A Bitmap representation of the profile picture.
     */
    private Bitmap generateprofile() {
        if (name != null) {
            return ProfilePictureGenerator.generateProfilePicture(name, 200);
        }
        return null;
    }

    /**
     * Loads the profile picture into the ImageView using Glide, prioritizing the URI if available,
     * otherwise falling back to a URL or a generated bitmap.
     *
     * @param profilePictureUri The URI of the profile picture.
     * @param profilePictureUrl The URL of the profile picture.
     */
    private void loadProfilewithGlide(Uri profilePictureUri, String profilePictureUrl) {
        Log.d("Profile", "Profile URL and URI: " + profilePictureUrl + profilePictureUri);
        if (profilePictureUri != null) {
            Glide.with(this)
                    .load(profilePictureUri)
                    .transform(new CircleCrop())
                    .into(profilePicture);
        } else if (profilePictureUrl != null) {
            Glide.with(this)
                    .load(profilePictureUrl)
                    .transform(new CircleCrop())
                    .into(profilePicture);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(generateprofile())
                    .transform(new CircleCrop())
                    .into(profilePicture);
        }
    }

    /**
     * Validates user inputs for registration.
     *
     * @return True if all inputs are valid, otherwise false.
     */
    private boolean validateInputs() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required");
            return false;
        } else {
            textInputLayoutName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Valid email is required");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
        }

        if (!mobile.isEmpty() && mobile.length() < 10) {
            textInputLayoutMobile.setError("Valid mobile number is required");
            return false;
        } else {
            textInputLayoutMobile.setError(null);
        }

        return true;
    }



    /**
     * Callback method called when a photo is selected by the user.
     *
     * @param uri The URI of the selected photo.
     */
    public void onPhotoSelected(Uri uri) {
        viewModel.onPhotoSelected(uri);
    }

    /**
     * Callback method called when the user deletes their photo.
     */
    public void onPhotoDeleted() {
        viewModel.onPhotoDeleted();
    }
}

