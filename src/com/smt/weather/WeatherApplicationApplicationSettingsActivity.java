package com.smt.weather;

import android.os.Bundle;

// Our Settings activity which contains application settings
public class WeatherApplicationApplicationSettingsActivity extends WeatherApplicationSubActivity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the Application Settings layout
        setContentView(R.layout.settings);
    }
}
