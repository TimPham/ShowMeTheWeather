package com.smt.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

// Our About activity which shows application information
public class AboutWeatherApplication extends Activity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the About Application layout
        setContentView(R.layout.about);
    }

    public void returnToApplication(View view) {
        // Return to the Weather application
        this.finish();
    }
}
