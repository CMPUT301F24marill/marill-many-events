package com.example.marill_many_events.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;

import com.example.marill_many_events.UserCallback;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.PhotoPicker;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import com.example.marill_many_events.models.User;
import com.example.marill_many_events.models.FirebaseUserRegistration;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * RegistrationFragment is responsible for user registration and profile update functionalities.
 * It provides a form for users to input their details, upload a profile picture,
 * and store the information in Firebase Firestore and Firebase Storage.
 */

public class RegistrationFragment extends Fragment implements PhotoPicker.OnPhotoSelectedListener, UserCallback {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private Button buttonRegister;

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
    FirebaseUserRegistration firebaseUserRegistration;

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

        firebaseUserRegistration = new FirebaseUserRegistration(firestore, storageReference, deviceId, this);

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
        //Initialization for error prompts within the textinput boxes
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = view.findViewById(R.id.textInputLayoutMobile);

        buttonRegister = view.findViewById(R.id.buttonRegister);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        profilePicture = view.findViewById(R.id.profile_picture);

        firebaseUserRegistration.loadUserDetails(); // Try getting an existing user

        profilePicture.setOnClickListener(v -> photoPicker.showPhotoOptions(profilePictureUrl));

        firebaseUserRegistration.loadUserDetails();

        // Set click listener for the register button
        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                if (isEditMode) {
                    firebaseUserRegistration.updateUser(
                            editTextName.getText().toString().trim(),
                            editTextEmail.getText().toString().trim(),
                            editTextMobile.getText().toString().trim(),
                            profilePictureUri);
                } else {
                    firebaseUserRegistration.registerUser(
                            editTextName.getText().toString().trim(),
                            editTextEmail.getText().toString().trim(),
                            editTextMobile.getText().toString().trim(),
                            profilePictureUri);
                }
            }
        });
    }

    private Bitmap generateprofile() {
        if (name != null) {
            return ProfilePictureGenerator.generateAvatar(name, 200);
        }
        return null;
    }

    private void loadProfilewithGlide(Uri profilePictureUri, String profilePictureUrl) {
        if (profilePictureUri != null) {
            // Load from URI if it exists
            Glide.with(this)
                    .load(profilePictureUri) // Load from Uri
                    .transform(new CircleCrop()) // Apply transformations if needed
                    .into(profilePicture); // Set the ImageView
        } else if (profilePictureUrl != null) {
            // Load from URL if it exists
            Glide.with(this)
                    .load(profilePictureUrl) // Load from URL
                    .transform(new CircleCrop()) // Apply transformations if needed
                    .into(profilePicture); // Set the ImageView
        } else {
            // Load the generated Bitmap if both URL and URI are null
            Glide.with(this)
                    .asBitmap() // Specify that you are loading a Bitmap
                    .load(generateprofile()) // Load the Bitmap
                    .transform(new CircleCrop()) // Apply transformations if needed
                    .into(profilePicture); // Set the ImageView
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
            textInputLayoutName.setError(null); // Clear error
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Valid email is required");
            return false;
        } else {
            textInputLayoutEmail.setError(null); // Clear error
        }

        if (!mobile.isEmpty() && mobile.length() < 10) {
            textInputLayoutMobile.setError("Valid mobile number is required");
            return false;
        } else {
            textInputLayoutMobile.setError(null); // Clear error
        }

        return true;
    }

    public void onUserloaded(User returnedUser){
        if (returnedUser != null) {
            user = returnedUser;
            isEditMode = true;

            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextMobile.setText(user.getPhone());
            buttonRegister.setText("Save");

            name = user.getName();
            profilePictureUrl = user.getProfilePictureUrl();

            loadProfilewithGlide(null, profilePictureUrl);
        }

        else{
            Toast.makeText(getActivity(), "User not found. You can register.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUserUpdated() {
        firebaseUserRegistration.loadUserDetails();
    }

    public void onRegistered(){
        assert getActivity() != null;
        getActivity().finish(); // new user activity only starts when user isn't found on login
    }

    public void onPhotoSelected(Uri uri){ // when the upload photo button is pressed and a photo is uploaded
        profilePictureUri = uri;
        loadProfilewithGlide(profilePictureUri, null);
    }

    public void onPhotoDeleted(){ // when the delete photo button in the bottom sheet is pressed
        profilePictureUri = null;
        firebaseUserRegistration.deleteProfilePicture();
    }

}
