package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

// Our Application activity which handles client input
public class WeatherApplicationActivity extends Activity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the main application layout
        setContentView(R.layout.main);
    }

    // Initialize our application's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Call our superclass onCreateOptionsMenu function
        super.onCreateOptionsMenu(menu);

        // Get an instance of the MenuInflater class
        MenuInflater inflater = this.getMenuInflater();

        // Now create our menu
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    // Handle select events on our application's menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Process menu item selection
        switch (item.getItemId()) {
            // Determine which menu item was chosen
            case R.id.addCity:
                // Show Add City Dialog
                return true;
            case R.id.removeCity:
                // Show Remove City Dialog
                return true;
            case R.id.settingsMenu:
                // Show Weather Settings activity
                // Create our intent to launch the Weather Settings activity
                Intent showApplicationSettingsIntent = new Intent(this, WeatherApplicationApplicationSettingsActivity.class);

                // Start the Settings activity
                startActivity(showApplicationSettingsIntent);
                return true;
            case R.id.aboutMenu:
                // Show About activity
                // Create our intent to launch the About activity
                Intent showApplicationInformationIntent = new Intent(this, WeatherApplicationApplicationAboutActivity.class);

                // Start the About activity
                startActivity(showApplicationInformationIntent);
                return true;
            default:
                // Catch-all if it's not on of the ones above or has not been implemented!
                return super.onOptionsItemSelected(item);
        }
    }
}
