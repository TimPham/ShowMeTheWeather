package com.smt.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WeatherApplicationAddActivity extends WeatherApplicationSubActivity implements LocationListener {
    // Store our Geolocation configuration option
    private Integer geoConfigOption;

    // Field for our LocationManager object
    private LocationManager locManager;

    // Field for our Location input
    private EditText locInput;

    // Store our Latitude and Longitude
    private double latitude;
    private double longitude;

    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the Application Settings layout
        setContentView(R.layout.add);

        // Get data passed in from calling Activity
        Bundle bundle = this.getIntent().getExtras();

        // Make sure they passed something in
        if (bundle != null) {
            // Read the configuration passed in
            this.geoConfigOption = bundle.getInt("geo");

            // Determine if the user has allowed Geolocation
            if (this.geoConfigOption == WeatherApplicationConfigurationModel.GEO_ENABLED) {
                // They have allowed Geolocation
                this.locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            }
            else {
                // Geolocation is disabled
                this.locManager = null;

                // Hide Current Location button
                Button addLocationButton = (Button)this.findViewById(R.id.btnAddCurrentLocation);
                addLocationButton.setVisibility(View.GONE);
            }
        }

        // Get location input field
        this.locInput = (EditText)this.findViewById(R.id.locationField);
    }

    // Resume the timer when the activity has focus
    @Override
    public void onResume() {
        // Call our superclass onResume function
        super.onResume();

        // Request updates again if LocationManager is not null and
        if (this.geoConfigOption == WeatherApplicationConfigurationModel.GEO_ENABLED) {
            // In the case that Geolocation was disabled, try to get the LocationManager service
            if (this.locManager == null) {
                // Get the LocationManager service
                this.locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            }

            // Now request location updates
            if (this.locManager != null) {
                this.locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    // Executed when the Activity loses focus/paused
    @Override
    public void onPause() {
        // Call our superclass onPause function
        super.onPause();

        // Stop the location from being updated
        if (this.locManager != null) {
            this.locManager.removeUpdates(this);
        }
    }

    // Executed when the Activity is stopped
    @Override
    public void onStop() {
        // Call our superclass onStop function
        super.onStop();

        // Stop the location from being updated
        if (this.locManager != null) {
            this.locManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Store location data
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

        System.out.println("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    // Handler for Use Current Location button
    public void getLocation(View view) {
        // Determine if the user has allowed Geolocation
        if (this.geoConfigOption == WeatherApplicationConfigurationModel.GEO_ENABLED) {
            // Ok, copy the location to the input box
            if (this.locInput != null) {
                this.locInput.setText(String.valueOf(this.latitude) + ", " + String.valueOf(this.longitude));
            }
        }

        // Invoke saveLocation
        this.saveLocation(view);
    }

    // Handler for Add button
    public void saveLocation(View view) {
        // Create our Bundle we will pass back to the calling Activity that will contain the city information
        // Create a bundle that will hold our data
        Bundle bundle = new Bundle();

        // Get location input
        if (this.locInput != null) {
            // Add to bundle
            bundle.putString("location", this.locInput.getText().toString());
        }

        // Add bundle
        Intent returnIntent = this.getIntent();
        returnIntent.putExtras(bundle);

        // Set status
        this.setResult(Activity.RESULT_OK, returnIntent);

        // Return
        this.finish();
    }
}
