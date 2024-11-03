package com.example.marill_many_events.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.marill_many_events.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PhotoPicker {


    public interface OnPhotoSelectedListener {
        void onPhotoSelected(Uri uri);
        void onPhotoDeleted();
    }

    private final OnPhotoSelectedListener listener;
    private final Context context;
    private final ActivityResultLauncher<Intent> photoPickerLauncher;
    private Uri profilePictureUri;

    public PhotoPicker(Fragment fragment, OnPhotoSelectedListener listener) {
        this.context = fragment.requireContext();
        this.listener = listener;

        // Register photo picker launcher in the fragment
        this.photoPickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        profilePictureUri = uri;
                        listener.onPhotoSelected(uri);
                    }
                }
        );
    }

    /**
     * Opens the photo picker to select a profile picture.
     */
    public void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    /**
     * Opens a menu to select between deleting and uploading a profile picture.
     */
    public void showPhotoOptions(String profilePictureUrl) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_profilepicture, null);
        bottomSheetDialog.setContentView(sheetView);

        // Hide delete option if there’s no profile picture to delete
        if (profilePictureUrl == null) {
            sheetView.findViewById(R.id.option_delete_photo).setVisibility(View.GONE);
        }

        // Set up click listeners for the replace and delete options
        sheetView.findViewById(R.id.option_replace_photo).setOnClickListener(v -> {
            openPhotoPicker();
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.option_delete_photo).setOnClickListener(v -> {
            listener.onPhotoDeleted();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
