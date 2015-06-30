package com.smt.weather;

import android.os.Bundle;

public class WeatherApplicationForecastActivity extends WeatherApplicationSubActivity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the About Application layout
        setContentView(R.layout.forecast);
    }
}
