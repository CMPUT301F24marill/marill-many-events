package com.example.marill_many_events.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.marill_many_events.R;
import com.example.marill_many_events.UserCallback;
import com.example.marill_many_events.models.PhotoPicker;
import com.example.marill_many_events.models.ProfilePictureGenerator;
import com.example.marill_many_events.models.User;
import com.example.marill_many_events.models.FirebaseRegistration;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * RegistrationFragment is responsible for user registration and profile update functionalities.
 * It provides a form for users to input their details, upload a profile picture,
 * and store the information in Firebase Firestore and Firebase Storage.
 */

public class RegistrationFragment extends Fragment implements PhotoPicker.OnPhotoSelectedListener, UserCallback {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private FirebaseFirestore firestore;
    private String deviceId;
    private ImageView profilePicture;

    private String profilePictureUrl;
    private StorageReference storageReference;
    private boolean isEditMode = false;

    private Button buttonRegister;
    private Uri profilePictureUri;
    private PhotoPicker.OnPhotoSelectedListener listener;
    private PhotoPicker photoPicker;
    private User user;
    String name;

    FirebaseRegistration firebaseRegistration;

    public RegistrationFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = getArguments() != null ? getArguments().getString("deviceId") : null;
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        firebaseRegistration = new FirebaseRegistration(firestore, storageReference, deviceId, this);
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     * Initializes UI elements and sets up click listeners.
     *
     * @param view              The view returned by onCreateView.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        //Initialization for error prompts within the textinput boxes
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = view.findViewById(R.id.textInputLayoutMobile);


        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile);


        profilePicture = view.findViewById(R.id.profile_picture);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        firebaseRegistration.loadUserDetails(); // Try getting an existing user

        profilePicture.setOnClickListener(v -> photoPicker.showPhotoOptions(profilePictureUrl));



        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                if (isEditMode) {
                    firebaseRegistration.updateUser(
                            editTextName.getText().toString().trim(),
                            editTextEmail.getText().toString().trim(),
                            editTextMobile.getText().toString().trim(),
                            profilePictureUri);
                } else {
                    firebaseRegistration.registerUser(
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
        firebaseRegistration.loadUserDetails();
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
        firebaseRegistration.deleteProfilePicture();
    }

}
