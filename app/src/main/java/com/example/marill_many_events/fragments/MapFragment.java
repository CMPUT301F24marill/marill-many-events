package com.example.marill_many_events.fragments;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.marill_many_events.EventsCallback;
import com.example.marill_many_events.R;
import com.example.marill_many_events.activities.HomePageActivity;
import com.example.marill_many_events.models.Event;
import com.example.marill_many_events.models.EventViewModel;
import com.example.marill_many_events.models.FirebaseEvents;
import com.example.marill_many_events.models.GenerateQRcode;
import com.example.marill_many_events.models.PhotoPicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the details of any selected event object, invoked from either user's waitlist or organizers event list
 */
public class MapFragment extends Fragment {

    private Button backButton;
    private ImageView map;
    ViewGroup frameLayout;
    ArrayList<ImageView> drawnViews;
    private ArrayList<GeoPoint> geoPointList;


    public MapFragment(ArrayList<GeoPoint> geoPointList) {
        this.geoPointList = geoPointList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        backButton = view.findViewById(R.id.back);
        map = view.findViewById(R.id.map);
        map.setAlpha((float)0.0);
        backButton.setAlpha((float)0.0);
        backButton.setEnabled(false);
        frameLayout = container;
        drawnViews = new ArrayList<>();

        int n = geoPointList.size();
        double[][] coordinates = new double[n][2];
        int i = 0;
        for(GeoPoint geoPoint : geoPointList){
            coordinates[i][0] = -1*geoPoint.getLatitude();
            Log.d("S", "latitude:" + coordinates[i][0]);
            coordinates[i][1] = -1*geoPoint.getLongitude();
            Log.d("S", "longitude:" + coordinates[i][1]);
            i++;
        }

        // Draw map
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageResource(R.drawable.worldmapsmall); // Your drawable resource
        // Set layout parameters to position the image
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        // Position the image using the x and y coordinates
        params.leftMargin = (int) 0;
        params.topMargin = (int) 200;
        // Apply the layout parameters to the image view
        imageView.setLayoutParams(params);
        // Add the ImageView to the container (FrameLayout)
        container.addView(imageView);
        drawnViews.add(imageView);
        // Loop through coordinates and place the drawables at those positions
        for (double[] coordinate : coordinates) {
            float x = (float) (412*((coordinate[0] + 180)/360)+20);
            float y = (float) (200+(215*((coordinate[1] + 90)/180))+70);

            // Create a new ImageView and set an image drawable
             imageView = new ImageView(this.getContext());
            imageView.setImageResource(R.drawable.pushpin); // Your drawable resource

            // Set layout parameters to position the image
            params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // Position the image using the x and y coordinates
            params.leftMargin = (int) x;
            params.topMargin = (int) y;

            params.height = 50;
            params.width = 50;

            // Apply the layout parameters to the image view
            imageView.setLayoutParams(params);

            // Add the ImageView to the container (FrameLayout)
            container.addView(imageView);
        }

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        //add to array for removal after
        drawnViews.add(imageView);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove all drawn views from the container
        for(ImageView drawn: drawnViews){
            frameLayout.removeView(drawn);
        }
    }
}
