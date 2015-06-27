package com.smt.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

// Our Settings activity which contains application settings
public class WeatherApplicationSettings extends Activity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the Application Settings layout
        setContentView(R.layout.settings);
    }

    public void returnToApplication(View view) {
        // Return to the Weather application
        this.finish();
    }
}
