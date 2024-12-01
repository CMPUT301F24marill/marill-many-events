package com.example.marill_many_events.models;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marill_many_events.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GeolocationActivity extends AppCompatActivity {
    private MapView mapView;
    private GeoPoint selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        setContentView(R.layout.geolocation);

        mapView = findViewById(R.id.mapView);
        Button selectLocationButton = findViewById(R.id.btnSelectLocation);

        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(53.5461, -113.4938)); // Default location

        // Handle map clicks to select a location
        mapView.getOverlays().clear();
        mapView.setOnLongClickListener(v -> {
            GeoPoint geoPoint = new GeoPoint(mapView.getMapCenter().getLatitude(), mapView.getMapCenter().getLongitude());
            mapView.getOverlays().clear();

            Marker marker = new Marker(mapView);
            marker.setPosition(geoPoint);
            marker.setTitle("Selected Location");
            mapView.getOverlays().add(marker);
            selectedLocation = geoPoint;
            return true;

        });

        // Handle the "Select Location" button
        selectLocationButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLocation.getLatitude());
                resultIntent.putExtra("longitude", selectedLocation.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
