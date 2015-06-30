package com.smt.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

// Our Settings activity which contains application settings
public class WeatherApplicationSettingsActivity extends WeatherApplicationSubActivity {
    // The spinner used for the Temperature Unit option
    private Spinner conf_spinner;

    // The switch used for the Geolocation option
    private Switch conf_switch;

    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the Application Settings layout
        setContentView(R.layout.settings);

        // Get data passed in from calling Activity
        Bundle bundle = this.getIntent().getExtras();

        // Make sure they passed something in
        if (bundle != null) {
            // Read the configuration passed in
            Integer temperature_config_option = bundle.getInt("unit");
            Integer geo_config_option = bundle.getInt("geo");

            // Update the spinner to reflect the current setting
            this.conf_spinner = (Spinner)this.findViewById(R.id.temperatureOption);
            if (temperature_config_option != null) {
                // Changed selection
                this.conf_spinner.setSelection(temperature_config_option);
            }

            // Update our switch to reflect the current setting
            this.conf_switch = (Switch)this.findViewById(R.id.geolocationOption);
            // Determine which option is chosen
            if (geo_config_option != null) {
                if (geo_config_option == WeatherApplicationConfigurationModel.GEO_DISABLED) {
                    // Disabled
                    this.conf_switch.setChecked(false);
                } else {
                    // Enabled (default), no option chosen or invalid option, we'll make the assumption it is enabled
                    this.conf_switch.setChecked(true);
                }
            }
            else {
                // Use default (Enabled)
                this.conf_switch.setChecked(true);
            }
        }
    }

    // Return the settings changed in the Settings activity
    public void saveConfiguration(View view) {
        // Create our Bundle we will pass back to the calling Activity that will contain our configuration options
        // Pass the configuration data
        // Create a bundle that will hold our data
        Bundle bundle = new Bundle();

        // Temperature Unit
        // Ensure that we have a handle on the element
        if (this.conf_spinner != null) {
            // Get the selected spinner item index
            Integer selected_index = this.conf_spinner.getSelectedItemPosition();

            // Ensure that the option is one of the following: Kelvin, Celsius or Fahrenheit
            if (selected_index == WeatherApplicationConfigurationModel.UNIT_KELVIN ||
                selected_index == WeatherApplicationConfigurationModel.UNIT_CELSIUS ||
                selected_index == WeatherApplicationConfigurationModel.UNIT_FAHRENHEIT) {
                // Save setting
                bundle.putInt("unit", selected_index);
            }
        }

        // Geolocation Setting
        // Ensure that we have a handle on the element
        if (this.conf_switch != null) {
            // Determine if the Geolocation option is enabled or disabled
            boolean checked_state = this.conf_switch.isChecked();

            // Check
            if (checked_state) {
                // Enabled
                bundle.putInt("geo", WeatherApplicationConfigurationModel.GEO_ENABLED);
            }
            else {
                // Disabled
                bundle.putInt("geo", WeatherApplicationConfigurationModel.GEO_DISABLED);
            }
        }

        // Add bundle
        Intent returnIntent = this.getIntent();
        returnIntent.putExtras(bundle);

        // Set status
        this.setResult(Activity.RESULT_OK, returnIntent);

        // Return
        this.finish();
    }
}
