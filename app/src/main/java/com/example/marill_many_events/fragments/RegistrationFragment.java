package com.example.marill_many_events.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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
import com.example.marill_many_events.models.User;
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

public class RegistrationFragment extends Fragment {

    private TextInputLayout textInputLayoutName, textInputLayoutEmail, textInputLayoutMobile;
    private TextInputEditText editTextName, editTextEmail, editTextMobile;
    private Button buttonRegister, buttonUploadProfilePicture;
    private FirebaseFirestore firestore;
    private String deviceId;
    private ImageView profilePicture;
    private Uri profilePictureUri;
    private String profilePictureUrl;
    private StorageReference storageReference;
    private boolean isEditMode = false;

    public RegistrationFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = getArguments() != null ? getArguments().getString("deviceId") : null;
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
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
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutMobile = view.findViewById(R.id.textInputLayoutMobile);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        profilePicture = view.findViewById(R.id.profile_picture);
        buttonRegister = view.findViewById(R.id.buttonRegister);


        profilePicture.setOnClickListener(v -> showPhotoOptions());
        loadUserDetails();

        buttonRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                if (isEditMode) {
                    updateUser();
                } else {
                    registerUser();
                }
            }
        });

    }

    /**
     * Loads user details from Firestore if in edit mode.
     */

    private void loadUserDetails() {
        firestore.collection("users").document(deviceId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            isEditMode = true;
                            buttonRegister.setText("Save");
                            User user = document.toObject(User.class);
                            if (user != null) {
                                editTextName.setText(user.getName());
                                editTextEmail.setText(user.getEmail());
                                editTextMobile.setText(user.getPhone());

                                profilePictureUrl = user.getProfilePictureUrl();
                                if(profilePictureUrl != null) {
                                    Glide.with(this)
                                            .load(profilePictureUrl)
                                            .transform(new CircleCrop())
                                            .into(profilePicture);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "User not found. You can register.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to load user details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Opens the photo picker to select a profile picture.
     */

    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        profilePictureUri = data.getData();
                        if (profilePictureUri != null) {
                            Glide.with(this)
                                    .load(profilePictureUri)
                                    .transform(new CircleCrop())
                                    .into(profilePicture);
                        }
                    }
                }
            });

    /**
     * Validates user inputs for registration.
     *
     * @return True if all inputs are valid, otherwise false.
     */
    // Validate user inputs

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
     * Registers a new user and uploads the profile picture to Firebase Storage if provided.
     */

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        User user = new User(name, email, mobile, null); // Register without profile picture
        firestore.collection("users").document(deviceId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    uploadProfilePicture();
                    Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to register user.", Toast.LENGTH_SHORT).show());
    }

    /**
     * Upload profile picture to firebase storage and get the download url.
     */

    private void uploadProfilePicture() {
        if (profilePictureUri != null) {
            StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");
            fileReference.putFile(profilePictureUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        updateProfilePictureUrl(uri.toString());
                        profilePictureUrl = uri.toString();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Upload profile picture URL to firestore under a user's profilepictureurl field.
     */
    private void updateProfilePictureUrl(String profilePictureUrl) {
        firestore.collection("users").document(deviceId)
                .update("profilePictureUrl", profilePictureUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Profile picture updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update profile picture.", Toast.LENGTH_SHORT).show());
    }

    /**
     * Open a menu to select between deleting and uploading a profile picture.
     */
    private void showPhotoOptions() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_profilepicture, null);
        bottomSheetDialog.setContentView(sheetView);

        if (profilePictureUrl == null)
            sheetView.findViewById(R.id.option_delete_photo).setVisibility(View.GONE);

        // Set up click listeners for the replace and delete options
        sheetView.findViewById(R.id.option_replace_photo).setOnClickListener(v -> {
            openPhotoPicker();
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.option_delete_photo).setOnClickListener(v -> {
            deleteProfilePicture();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    /**
     * Deletes the current profile picture from Firebase Storage and removes the download link reference from Firestore.
     */
    private void deleteProfilePicture() {
            // Delete profile picture from Firebase Storage
            StorageReference fileReference = storageReference.child("profile_pictures/" + deviceId + ".jpg");
            fileReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Profile picture deleted!", Toast.LENGTH_SHORT).show();

                        // Remove profile picture URL from Firestore
                        firestore.collection("users").document(deviceId)
                                .update("profilePictureUrl", null)  // Set URL to null
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(getActivity(), "Profile picture reference removed from Firestore.", Toast.LENGTH_SHORT).show();
                                    profilePicture.setImageResource(R.drawable.default_profile); // Reset to default
                                    profilePictureUri = null;
                                    profilePictureUrl = null;
                                })
                                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to remove picture reference from Firestore.", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete profile picture.", Toast.LENGTH_SHORT).show());
    }

    /**
     * Update an existing user's details.
     */
    private void updateUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("email", email);
        userUpdates.put("phone", mobile);

        firestore.collection("users").document(deviceId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "User details updated successfully!", Toast.LENGTH_SHORT).show();
                        uploadProfilePicture();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update user details.", Toast.LENGTH_SHORT).show());
    }
}
