package com.example.marill_many_events.fragments;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.Identity;
import com.example.marill_many_events.R;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.GenerateQRcode;
import com.example.marill_many_events.models.PhotoPicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class CreateEventFragment extends Fragment implements EventsCallback, PhotoPicker.OnPhotoSelectedListener {

    //Views
    private TextView NameField, datePickerStart, datePickerEnd, capacityField, locationField;
    private ListView waitlistList, registeredList;
    private ArrayList<Event> waitlistdataList;
    private ArrayList<Event> registereddataList;
    private EventyArrayAdapter waitlistAdapter, registeredAdapter;
    private SwitchCompat switchCompat;
    private Button createButton;
    private ImageView posterview, QRview;


    //Variables
    private String eventName;
    private Date startDate;
    private Date endDate;
    private int capacity;
    private boolean geolocation;
    private PhotoPicker photoPicker;
    private Uri posterUri;
    private String posterUrl, location;
    private GenerateQRcode generateQRcode;

    //Data Storage
    private FirebaseFirestore firestore;
    private String deviceId;
    private StorageReference storageReference;
    private Identity identity;
    private FirebaseEvents firebaseEvents;


    public CreateEventFragment() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = identity.getdeviceID();
        firestore = identity.getFirestore();
        storageReference = identity.getStorage().getReference("event_posters");
        firebaseEvents = new FirebaseEvents(firestore, storageReference, deviceId, this);
        photoPicker = new PhotoPicker(this, this);
        generateQRcode = new GenerateQRcode();
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
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    /**
     * Initializes UI elements and sets up click listeners.
     *
     * @param view              The view returned by onCreateView.
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NameField = view.findViewById(R.id.NameField);
        createButton = view.findViewById(R.id.create);
        posterview = view.findViewById(R.id.poster);
        datePickerStart = view.findViewById(R.id.Startdatefield);
        datePickerEnd = view.findViewById(R.id.DrawdateField);
        capacityField = view.findViewById(R.id.Capacityfield);
        locationField = view.findViewById(R.id.LocationField);
        switchCompat = view.findViewById(R.id.GeoSwitch);
        QRview = view.findViewById(R.id.QRcode);

        datePicker(datePickerStart, true);
        datePicker(datePickerEnd,false);

        posterview.setOnClickListener(v-> {
            photoPicker.showPhotoOptions(null);
        });

        createButton.setOnClickListener(v-> {
                if(posterUri != null) firebaseEvents.uploadPoster(posterUri);
                else firebaseEvents.createEvent(createEvent());
        });

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            geolocation = isChecked;
        });

    }

    /**
     * Callback for when an event document is created. The qr code is drawn using the url locally
     *
     * @param documentID          The firestore document ID for the newly created event
     */
    public void onEventCreate(String documentID){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.qr_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        Bitmap code = generateQRcode.generateQR(documentID);
        QRview.setImageBitmap(code);
        QRview.setVisibility(View.VISIBLE);


        //bottomSheetDialog.show();
        ImageView qrview = sheetView.findViewById(R.id.QRcode);
        qrview.setImageBitmap(code);
    }

    public void onEventDelete(){}

    public void joinEvent(){}

    public void getEvent(Event event){


    }

    /**
     * Initializes the date picker on a given view.
     *
     * @param view              The view on which datepicker is initialized.
     * @param isStartDate boolean indicating if this view is for the start date.
     */
    public void datePicker(TextView view, boolean isStartDate) {

        view.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

            // Show the date picker dialog
            datePicker.show(getParentFragmentManager(), "DATE_PICKER");

            // Handle the date selection
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Date selectedDate = new Date(selection);
                view.setText(datePicker.getHeaderText()); // Update the TextView with the selected date
                if(isStartDate) startDate = selectedDate; // Crude way to avoid more handlers, if boolean flag is true then this is a start date
                else endDate = selectedDate;
            });
        });
    }

    /**
     * Sets the poster imageview once an image is selected.
     *
     * @param uri The image resource id.
     */
    public void onPhotoSelected(Uri uri){ // when the upload photo button is pressed and a photo is uploaded
        posterUri = uri;
        Glide.with(this).asBitmap().load(uri)
                .into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                    float imageAspectRatio = (float) resource.getWidth() / (float) resource.getHeight(); // Get the aspect ratio of the image

                    int viewwidth = posterview.getWidth(); // Get the width of the ImageView


                    int height;
                    if (imageAspectRatio >= 1)  // Landscape or square image (16:9 or wider)
                        height = (int) (viewwidth / imageAspectRatio);
                    else // Portrait image (9:16 or taller), limit the height to 1/4 of the fragment height
                        height = getView().getHeight()/4;


                    // Set the height of the ImageView
                    ViewGroup.LayoutParams params = posterview.getLayoutParams();
                    params.height = height;
                    posterview.setLayoutParams(params);

                    posterview.setImageBitmap(resource); // Add the image in the view
                }

                public void onLoadCleared(Drawable draw){}
        });
    }

    public void onPhotoDeleted(){ // when the upload photo button is pressed and a photo is uploaded
            // reset the view height to the height of a default "add poster here" image
    }

    /**
     * Creates an event object with a poster link embedded once the create button is pressed.
     *
     * @param url The url of the image uploaded to firebase storage.
     */
    public void onPosterUpload(String url){
        posterUrl = url;
        Event event = createEvent();
        firebaseEvents.createEvent(event);
    }

    /**
     * Create an event object.
     */
    public Event createEvent(){
        capacity = Integer.parseInt(capacityField.getText().toString().trim());
        eventName = NameField.getText().toString().trim();
        location = locationField.getText().toString().trim();
        Event event = new Event(posterUrl, eventName, location, startDate, endDate, capacity, geolocation, null);
        return event;
    }

    /**
     * Locally generate QR code from a documentID.
     */


}
