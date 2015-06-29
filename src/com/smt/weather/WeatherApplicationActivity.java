package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashSet;

// Our Application activity which handles client input
public class WeatherApplicationActivity extends Activity {
    public static enum WeatherApplicationActivities {
        APP_SETTINGS(0),
        APP_ADDCITY(1),
        APP_REMOVECITY(2);

        private int value;

        private WeatherApplicationActivities(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    };

    // Field for our WeatherApplicationClock object
    private WeatherApplicationClock waClock;

    // Field for our WeatherApplicationConfigurationModel object
    private WeatherApplicationConfigurationModel waConfiguration;

    // Configuration for our WeatherApplicationActivity
    private HashMap<String, Integer> hmConfiguration;

    // Field for our WeatherApplicationLocation object
    private WeatherApplicationLocationModel waLocation;

    // List of Cities/Lat-Long etc.
    private LinkedHashSet<String> hsListOfLocations;

    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Create a Handler object
        Handler handler = new Handler();

        // Use the main application layout
        setContentView(R.layout.main);

        // Create our WeatherApplicationClock object
        this.waClock = new WeatherApplicationClock(handler, (TextView) this.findViewById(R.id.currentTime));

        // Initialize our SQLite database configuration object
        this.waConfiguration = new WeatherApplicationConfigurationModel(this);

        // Read Configuration from our SQLite database
        this.hmConfiguration = this.waConfiguration.getConfigurationData();

        // Initialize our SQLite database location object
        this.waLocation = new WeatherApplicationLocationModel(this);

        // Read list of cities from our SQLite database
        this.hsListOfLocations = this.waLocation.getLocationsSet();
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
        Bundle bundle = null;
        switch (item.getItemId()) {
            // Determine which menu item was chosen
            case R.id.addCity:
                // Show Add City Activity
                // Create our intent to launch the Add City activity
                Intent showAddCityIntent = new Intent(this, WeatherApplicationAddActivity.class);

                // Pass the configuration data
                // Create a bundle that will hold our data
                bundle = new Bundle();

                // Geolocation Setting
                if (this.hmConfiguration.containsKey("geo")) {
                    // Copy setting
                    bundle.putInt("geo", this.hmConfiguration.get("geo"));
                }
                else {
                    // Use default
                    bundle.putInt("geo", WeatherApplicationConfigurationModel.GEO_ENABLED);
                }

                // Add bundle
                showAddCityIntent.putExtras(bundle);

                // Start the Settings activity
                startActivityForResult(showAddCityIntent, WeatherApplicationActivities.APP_ADDCITY.getValue());
                return true;
            case R.id.removeCity:
                // Show Remove City Dialog
                return true;
            case R.id.settingsMenu:
                // Show Weather Settings activity
                // Create our intent to launch the Weather Settings activity
                Intent showApplicationSettingsIntent = new Intent(this, WeatherApplicationSettingsActivity.class);

                // Pass the configuration data
                // Create a bundle that will hold our data
                bundle = new Bundle();

                // Temperature Unit
                if (this.hmConfiguration.containsKey("unit")) {
                    // Copy setting
                    bundle.putInt("unit", this.hmConfiguration.get("unit"));
                }
                else {
                    // Use default
                    bundle.putInt("unit", WeatherApplicationConfigurationModel.UNIT_CELSIUS);
                }

                // Geolocation Setting
                if (this.hmConfiguration.containsKey("geo")) {
                    // Copy setting
                    bundle.putInt("geo", this.hmConfiguration.get("geo"));
                }
                else {
                    // Use default
                    bundle.putInt("geo", WeatherApplicationConfigurationModel.GEO_ENABLED);
                }

                // Add bundle
                showApplicationSettingsIntent.putExtras(bundle);

                // Start the Settings activity
                startActivityForResult(showApplicationSettingsIntent, WeatherApplicationActivities.APP_SETTINGS.getValue());
                return true;
            case R.id.aboutMenu:
                // Show About activity
                // Create our intent to launch the About activity
                Intent showApplicationInformationIntent = new Intent(this, WeatherApplicationAboutActivity.class);

                // Start the About activity
                startActivity(showApplicationInformationIntent);
                return true;
            default:
                // Catch-all if it's not on of the ones above or has not been implemented!
                return super.onOptionsItemSelected(item);
        }
    }

    // Resume the timer when the activity has focus
    @Override
    public void onResume() {
        // Call our superclass onResume function
        super.onResume();

        // Start the clock again
        this.waClock.start();
    }

    // Executed when the Activity loses focus/paused
    @Override
    public void onPause() {
        // Call our superclass onPause function
        super.onPause();

        // Stop the clock
        this.waClock.stop();
    }

    // Executed when the Activity is stopped
    @Override
    public void onStop() {
        // Call our superclass onStop function
        super.onStop();

        // Stop the clock
        this.waClock.stop();
    }

    // Read the parameters set in the Settings menu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check that the user confirmed the changes
        if(resultCode == Activity.RESULT_OK) {
            // Determine which dialog or activity is returning a value
            if (requestCode == WeatherApplicationActivities.APP_SETTINGS.getValue()) {
                // Settings Menu
                // Ensure we got something in return!
                if (data != null) {
                    // Process options
                    Bundle bundle = data.getExtras();

                    // Temperature Unit
                    if (bundle.containsKey("unit")) {
                        // Get value
                        Integer value = bundle.getInt("unit");

                        // Check if a valid option was selected
                        if (value == WeatherApplicationConfigurationModel.UNIT_KELVIN ||
                                value == WeatherApplicationConfigurationModel.UNIT_CELSIUS ||
                                value == WeatherApplicationConfigurationModel.UNIT_FAHRENHEIT) {
                            // Save
                            this.hmConfiguration.put("unit", value);
                        }
                    }

                    // Geolocation Setting
                    if (bundle.containsKey("geo")) {
                        // Get value
                        Integer value = bundle.getInt("geo");

                        // Check if a valid option was selected
                        if (value == WeatherApplicationConfigurationModel.GEO_DISABLED ||
                                value == WeatherApplicationConfigurationModel.GEO_ENABLED) {
                            // Save
                            this.hmConfiguration.put("geo", value);
                        }
                    }

                    // Save configuration data
                    this.waConfiguration.saveConfigurationData(this.hmConfiguration);
                }
            }
            else if (requestCode == WeatherApplicationActivities.APP_ADDCITY.getValue()) {
                // Add City
                // Ensure we got something in return!
                if (data != null) {
                    // Process options
                    Bundle bundle = data.getExtras();

                    // Location
                    if (bundle.containsKey("location")) {
                        // Get value
                        String value = bundle.getString("location");

                        // Check if a valid option was selected
                        if (value.length() > 0) {
                            // Add to the list
                            this.hsListOfLocations.add(value);
                        }
                    }

                    // Save configuration data
                    this.waLocation.saveLocations(this.hsListOfLocations);
                }
            }
            else if (requestCode == WeatherApplicationActivities.APP_REMOVECITY.getValue()) {
                // Remove City
            }
            else {
                // Not Implemented
            }
        }
    }
}
