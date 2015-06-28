package com.smt.weather;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

// Our class that serves as the basis for the About and Settings activities
public class WeatherApplicationSubActivity extends Activity {
    // Initialize our menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Call our superclass onCreateOptionsMenu function
        super.onCreateOptionsMenu(menu);

        // Get an instance of the MenuInflater class
        MenuInflater inflater = this.getMenuInflater();

        // Now create our menu
        inflater.inflate(R.menu.backmenu, menu);

        return true;
    }

    // Handle select events on our application's menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Process menu item selection
        switch (item.getItemId()) {
            // Determine which menu item was chosen
            case R.id.backButton:
                // Go back to the WeatherApplicationActivity activity
                this.finish();
                return true;
            default:
                // Catch-all if it's not on of the ones above or has not been implemented!
                return super.onOptionsItemSelected(item);
        }
    }

    // Call to return to the WeatherApplicationActivity activity
    public void returnToApplication(View view) {
        // Return to the WeatherApplicationActivity activity
        this.finish();
    }
}
