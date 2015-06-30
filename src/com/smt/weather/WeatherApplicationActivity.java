package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.HashMap;
import java.util.LinkedHashSet;

// Our Application activity which handles client input
public class WeatherApplicationActivity extends Activity implements AdapterView.OnItemSelectedListener {
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

    // Location Spinner object
    private Spinner spinnerElement;

    // Updates the UI
    public void refreshUI() {
        TextView noLocLabel = (TextView)this.findViewById(R.id.noLocationLabel);
        if (this.hsListOfLocations != null) {
            // Check if noLocLabel is not null
            if (noLocLabel != null) {
                // Hide No Locations Label
                noLocLabel.setVisibility(View.GONE);
            }

            // Show Elements
            this.showLocationUI();
        }
        else {
            // Check if noLocLabel is not null
            if (noLocLabel != null) {
                // Show No Locations Label
                noLocLabel.setVisibility(View.VISIBLE);
            }

            // Hide Elements
            this.hideLocationUI();
        }
    }

    // Shows Weather UI controls
    private void showLocationUI() {
        // Shows the Location Elements
        // Spinner (ie. Dropdown) Title
        TextView spinnerTitle = (TextView)this.findViewById(R.id.spinnerTitle);
        if (spinnerTitle != null) {
            // Show Spinner (ie. Dropdown) Title
            spinnerTitle.setVisibility(View.VISIBLE);
        }

        // Spinner (ie. Dropdown)
        if (this.spinnerElement != null) {
            // Convert from LinkedHashSet to String[]
            int number_locations = this.hsListOfLocations.size();

            // Only create if the number of locations is > 0
            if (number_locations > 0) {
                // Initialize our array of strings to hold the locations
                String[] locations = new String[number_locations];

                // Copy the strings
                int location_counter = 0;
                for (String location : this.hsListOfLocations) {
                    if (location_counter < number_locations) {
                        locations[location_counter] = location;
                    }
                    location_counter++;
                }

                // Create our data adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Update Spinner content
                this.spinnerElement.setAdapter(adapter);
            }
            else {
                // Purge data
                this.spinnerElement.setAdapter(null);
            }

            // Show Spinner (ie. Dropdown)
            this.spinnerElement.setVisibility(View.VISIBLE);
        }

        // Map

        // Forecast Button
        Button showForecastButton = (Button)this.findViewById(R.id.btnShowForecast);
        if (showForecastButton != null) {
            // Show Forecast button
            showForecastButton.setVisibility(View.VISIBLE);
        }

        // History Button
        Button showHistoryButton = (Button)this.findViewById(R.id.btnShowHistory);
        if (showHistoryButton != null) {
            // Show History button
            showHistoryButton.setVisibility(View.VISIBLE);
        }
    }

    // Hides Weather UI controls
    private void hideLocationUI() {
        // Hides the Location Elements
        // Spinner (ie. Dropdown) Title
        TextView spinnerTitle = (TextView)this.findViewById(R.id.spinnerTitle);
        if (spinnerTitle != null) {
            // Show Spinner (ie. Dropdown) Title
            spinnerTitle.setVisibility(View.GONE);
        }

        // Spinner (ie. Dropdown)
        this.spinnerElement = (Spinner)this.findViewById(R.id.locationOption);
        if (this.spinnerElement != null) {
            // Purge data
            this.spinnerElement.setAdapter(null);

            // Show Spinner (ie. Dropdown)
            this.spinnerElement.setVisibility(View.GONE);
        }

        // Map

        // Forecast Button
        Button showForecastButton = (Button)this.findViewById(R.id.btnShowForecast);
        if (showForecastButton != null) {
            // Show Forecast button
            showForecastButton.setVisibility(View.GONE);
        }

        // History Button
        Button showHistoryButton = (Button)this.findViewById(R.id.btnShowHistory);
        if (showHistoryButton != null) {
            // Show History button
            showHistoryButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Change Location
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

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
        this.waClock = new WeatherApplicationClock(handler, (TextView)this.findViewById(R.id.currentTime));

        // Initialize our SQLite database configuration object
        this.waConfiguration = new WeatherApplicationConfigurationModel(this);

        // Read Configuration from our SQLite database
        this.hmConfiguration = this.waConfiguration.getConfigurationData();

        // Initialize our SQLite database location object
        this.waLocation = new WeatherApplicationLocationModel(this);

        // Read list of cities from our SQLite database
        this.hsListOfLocations = this.waLocation.getLocationsSet();

        // Bind Spinner (ie. Dropdown) item listener
        this.spinnerElement = (Spinner)this.findViewById(R.id.locationOption);
        if (this.spinnerElement != null) {
            // Bind item listener
            this.spinnerElement.setOnItemSelectedListener(this);
        }

        // Update the Main Activity View UI
        this.refreshUI();
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
            case R.id.btnAddCity:
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
            case R.id.btnRemoveCity:
                // Show Remove City Dialog
                return true;
            case R.id.btnSettingsMenu:
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
            case R.id.btnAboutMenu:
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
                    // Process input
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

                    // Update the Main Activity View UI
                    this.refreshUI();
                }
            }
            else if (requestCode == WeatherApplicationActivities.APP_REMOVECITY.getValue()) {
                // Remove City
                // Ensure we got something in return!
                if (data != null) {
                    // Process data
                    Bundle bundle = data.getExtras();

                    // Location
                    if (bundle.containsKey("locations")) {
                        // Get value
                        String[] values = bundle.getStringArray("locations");

                        // Purge the list of locations
                        this.hsListOfLocations.clear();

                        // Ensure that the array is not null
                        if (values != null) {
                            // Iterate through the array of values
                            for (String value : values) {
                                // Add to the list
                                this.hsListOfLocations.add(value);
                            }
                        }
                    }

                    // Save configuration data
                    this.waLocation.saveLocations(this.hsListOfLocations);
                }
            }
            else {
                // Not Implemented
            }
        }
    }

    // Show Forecast
    public void showForecast(View view) {
        // Show Weather Settings activity
        // Create our intent to launch the Weather Settings activity
        Intent showForecastIntent = new Intent(this, WeatherApplicationForecastActivity.class);

        // Create a bundle that will hold our data
        Bundle bundle = new Bundle();

        // Get selected location from the Spinner
        if (this.spinnerElement != null) {
            // Get selected location
            String selectedLocation = this.spinnerElement.getSelectedItem().toString();

            // Make sure that something was selected
            if (selectedLocation != null && selectedLocation.length() > 0) {
                // Pass location to the Forecast Activity
                bundle.putString("location", selectedLocation);

                // Add bundle
                showForecastIntent.putExtras(bundle);

                // Start the Forecast activity
                startActivity(showForecastIntent);
            }
        }
    }

    // Show History
    public void showHistory(View view) {
        // Show Weather Settings activity
        // Create our intent to launch the Weather Settings activity
        Intent showHistoryIntent = new Intent(this, WeatherApplicationHistoryActivity.class);

        // Create a bundle that will hold our data
        Bundle bundle = new Bundle();

        // Get selected location from the Spinner
        if (this.spinnerElement != null) {
            // Get selected location
            String selectedLocation = this.spinnerElement.getSelectedItem().toString();

            // Make sure that something was selected
            if (selectedLocation != null && selectedLocation.length() > 0) {
                // Pass location to the History Activity
                bundle.putString("location", selectedLocation);

                // Add bundle
                showHistoryIntent.putExtras(bundle);

                // Start the Forecast activity
                startActivity(showHistoryIntent);
            }
        }
    }
}
