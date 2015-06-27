package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// Our Splash Screen activity which is displayed when we first launch the application
public class WeatherApplicationStartActivity extends Activity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the splash screen layout
        setContentView(R.layout.splash);
    }

    // Launches the Weather application activity
    public void startApplication(View view) {
        // Create our intent to launch the Weather application
        Intent applicationLaunchIntent = new Intent("android.intent.action.WEATHERAPPLICATION");

        // Start the Weather application activity
        startActivity(applicationLaunchIntent);
    }
}
