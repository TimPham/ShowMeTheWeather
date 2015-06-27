package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// Our Application activity which handles client input
public class WeatherApplication extends Activity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the main application layout
        setContentView(R.layout.main);
    }

    // Launches the Settings activity
    public void showApplicationSettings(View view) {
        // Create our intent to launch the About activity
        Intent showApplicationSettingsIntent = new Intent(this, WeatherApplicationSettings.class);

        // Start the Settings activity
        startActivity(showApplicationSettingsIntent);
    }

    // Launches the About activity
    public void showApplicationInformation(View view) {
        // Create our intent to launch the About activity
        Intent showApplicationInformationIntent = new Intent(this, AboutWeatherApplication.class);

        // Start the About activity
        startActivity(showApplicationInformationIntent);
    }
}
