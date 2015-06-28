package com.smt.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class WeatherApplicationConfigurationModel extends WeatherApplicationDB {
    public static final int UNIT_KELVIN = 0;
    public static final int UNIT_CELSIUS = 1;
    public static final int UNIT_FAHRENHEIT = 2;

    public static final int GEO_DISABLED = 0;
    public static final int GEO_ENABLED = 1;

    protected static final String TABLE_NAME = "wa_configuration";
    protected static final String TABLE_PK = "configuration_id";
    protected static final String COLUMN_UNIT = "temperature_unit_setting";
    protected static final String COLUMN_GEOLOCATION = "geo_setting";
    protected static final String TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS `" + WeatherApplicationConfigurationModel.TABLE_NAME + "` (" +
                                                            WeatherApplicationConfigurationModel.TABLE_PK + " INTEGER PRIMARY KEY NOT NULL, " +
                                                            WeatherApplicationConfigurationModel.COLUMN_UNIT + " INTEGER NOT NULL DEFAULT 1, " +
                                                            WeatherApplicationConfigurationModel.COLUMN_GEOLOCATION + " INTEGER NOT NULL DEFAULT 1" +
                                                        ");";
    protected static final String[] COLUMNS = { WeatherApplicationConfigurationModel.COLUMN_UNIT, WeatherApplicationConfigurationModel.COLUMN_GEOLOCATION };
    protected static final String WHERE_FILTER = WeatherApplicationConfigurationModel.TABLE_PK + " = ?";
    protected static final String[] WHERE_FILTER_VALUE = { "1" };

    // Constructor for WeatherApplicationConfigurationModel
    public WeatherApplicationConfigurationModel(Context context) {
        // Call WeatherApplicationDB constructor!
        super(context);

        // The onCreate method doesn't get invoked unless the SQLite database is missing/not created.
        // I have added this here to ensure that the table exists!
        // Try to create the table if it doesn't exist
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Try to create table -- if it already exists nothing will happen!
            db.execSQL(WeatherApplicationConfigurationModel.TABLE_CREATE_QUERY);
        }
        catch (Exception e) { }
    }

    // Retrieve our configuration parameters
    public HashMap<String, Integer> getConfigurationData() {
        // Initialize our HashMap which will hold our parameters
        HashMap<String, Integer> mData = new HashMap<>();

        // Add default parameters
        mData.put("unit", WeatherApplicationConfigurationModel.UNIT_CELSIUS);
        mData.put("geo", WeatherApplicationConfigurationModel.GEO_ENABLED);

        // Try to load our configuration file
        try {
            // Get SQLite database object
            SQLiteDatabase db = this.getReadableDatabase();

            // Run query to retrieve configuration
            Cursor cursor = db.query(
                WeatherApplicationConfigurationModel.TABLE_NAME,
                WeatherApplicationConfigurationModel.COLUMNS,
                WeatherApplicationConfigurationModel.WHERE_FILTER,
                WeatherApplicationConfigurationModel.WHERE_FILTER_VALUE,
                null,
                null,
                null
            );

            // Check if there is a result we can process
            if (cursor.getCount() > 0) {
                // Switch to the first result
                cursor.moveToFirst();

                // Read the data
                String degrees = cursor.getString(cursor.getColumnIndex(WeatherApplicationConfigurationModel.COLUMN_UNIT));
                String geolocation = cursor.getString(cursor.getColumnIndex(WeatherApplicationConfigurationModel.COLUMN_GEOLOCATION));

                // Try to convert from a String to an Integer to determine the temperature unit to use
                try {
                    // Convert
                    Integer degrees_setting = Integer.parseInt(degrees);
                    // Conversion ok, now determine if a valid option was selected
                    if (degrees_setting == WeatherApplicationConfigurationModel.UNIT_KELVIN ||
                        degrees_setting == WeatherApplicationConfigurationModel.UNIT_CELSIUS ||
                        degrees_setting == WeatherApplicationConfigurationModel.UNIT_FAHRENHEIT) {
                        // Update the unit to use in the application
                        mData.put("unit", degrees_setting);
                    }
                }
                catch (Exception e) { }

                // Try to convert from a String to an Integer to determine whether we should use Geolocation
                try {
                    // Convert
                    Integer geo_setting = Integer.parseInt(geolocation);
                    // Conversion ok, now determine if a valid option was selected
                    if (geo_setting == WeatherApplicationConfigurationModel.GEO_DISABLED ||
                        geo_setting == WeatherApplicationConfigurationModel.GEO_ENABLED) {
                        // Update the unit to use in the application
                        mData.put("geo", geo_setting);
                    }
                }
                catch (Exception e) { }
            }

            // Close cursor since we don't need it anymore
            cursor.close();
        }
        catch (Exception e) { }

        // Return the configuration values
        return mData;
    }

    // Save our configuration parameters
    public boolean saveConfigurationData(HashMap<String, Integer> hm) {
        // Try to save our configuration parameters
        try {
            // Get SQLite database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Container to hold the parameters to update
            ContentValues columns_to_update = new ContentValues();

            // Get the parameters
            Integer degree_option = hm.get("unit");
            Integer geo_option = hm.get("geo");

            // Should we update? No point in updating if there is no data!
            boolean column_to_update = false;

            // Determine if degree parameter was passed in
            if (degree_option != null) {
                columns_to_update.put(WeatherApplicationConfigurationModel.COLUMN_UNIT, degree_option.toString());
                column_to_update = true;
            }

            // Determine if geolocation parameter was passed in
            if (geo_option != null) {
                columns_to_update.put(WeatherApplicationConfigurationModel.COLUMN_GEOLOCATION, geo_option.toString());
                column_to_update = true;
            }

            // Added configuration_id parameter
            columns_to_update.put(WeatherApplicationConfigurationModel.TABLE_PK, "1");

            // Anything to update?
            if (column_to_update) {
                // Logic here is since SQLite doesn't have INSERT ... ON DUPLICATE KEY UPDATE ...
                // First try an insert and if that doesn't work, update!

                // Run query to save configuration
                long success = db.insertWithOnConflict(
                    WeatherApplicationConfigurationModel.TABLE_NAME,
                    null,
                    columns_to_update,
                    SQLiteDatabase.CONFLICT_IGNORE
                );

                // Error, try as an update
                if (success == -1) {
                    // Run query to retrieve configuration
                    success = db.update(
                        WeatherApplicationConfigurationModel.TABLE_NAME,
                        columns_to_update,
                        WeatherApplicationConfigurationModel.WHERE_FILTER,
                        WeatherApplicationConfigurationModel.WHERE_FILTER_VALUE
                    );
                }

                // Did we create/update the row?
                if (success == 1) {
                    // Updated/created
                    return true;
                }
            }
        }
        catch (Exception e) { }

        // Couldn't save configuration
        return false;
    }

    // Function to purge configuration data
    public void clearConfigurationData() {
        try {
            // Get SQLite database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Truncate table
            db.delete(WeatherApplicationConfigurationModel.TABLE_NAME, null, null);
        }
        catch (Exception e) { }
    }
}
