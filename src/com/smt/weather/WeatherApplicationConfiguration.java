package com.smt.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;

import java.util.HashMap;

public class WeatherApplicationConfiguration extends WeatherApplicationDB {
    public static final int UNIT_KELVIN = 0;
    public static final int UNIT_CELSIUS = 1;
    public static final int UNIT_FAHRENHEIT = 2;

    public static final int GEO_DISABLED = 0;
    public static final int GEO_ENABLED = 1;

    protected static final String TABLE_NAME = "wa_configuration";
    protected static final String TABLE_PK = "configuration_id";
    protected static final String COLUMN_UNIT = "temperature_unit_setting";
    protected static final String COLUMN_GEOLOCATION = "geo_setting";
    protected static final String TABLE_CREATE_QUERY = "CREATE TABLE `" + WeatherApplicationConfiguration.TABLE_NAME + "` (" +
                                                            WeatherApplicationConfiguration.TABLE_PK + " INTEGER PRIMARY KEY NOT NULL, " +
                                                            WeatherApplicationConfiguration.COLUMN_UNIT + " INTEGER NOT NULL DEFAULT 1, " +
                                                            WeatherApplicationConfiguration.COLUMN_GEOLOCATION + " INTEGER NOT NULL DEFAULT 1" +
                                                        ");";
    protected static final String[] COLUMNS = { WeatherApplicationConfiguration.COLUMN_UNIT, WeatherApplicationConfiguration.COLUMN_GEOLOCATION };
    protected static final String WHERE_FILTER = WeatherApplicationConfiguration.TABLE_PK + " = ?";
    protected static final String[] WHERE_FILTER_VALUE = { "1" };

    // Constructor for WeatherApplicationConfiguration
    public WeatherApplicationConfiguration(Context context) {
        // Call WeatherApplicationDB constructor!
        super(context);
    }

    // Create our table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create our table!
        db.execSQL(WeatherApplicationConfiguration.TABLE_CREATE_QUERY);
    }

    // Not used, but required
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to do
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to do
    }

    // Retrieve our configuration parameters
    public HashMap<String, Integer> getConfigurationData() {
        // Initialize our HashMap which will hold our parameters
        HashMap<String, Integer> mData = new HashMap<String, Integer>();

        // Add default parameters
        mData.put("unit", WeatherApplicationConfiguration.UNIT_CELSIUS);
        mData.put("geo", WeatherApplicationConfiguration.GEO_ENABLED);

        // Try to load our configuration file
        try {
            // Get SQLite Database file
            SQLiteDatabase db = this.getReadableDatabase();

            // Run query to retrieve configuration
            Cursor cursor = db.query(
                WeatherApplicationConfiguration.TABLE_NAME,
                WeatherApplicationConfiguration.COLUMNS,
                WeatherApplicationConfiguration.WHERE_FILTER,
                WeatherApplicationConfiguration.WHERE_FILTER_VALUE,
                null,
                null,
                null
            );

            // Check if there is a result we can process
            if (cursor.getCount() > 0) {
                // Switch to the first result
                cursor.moveToFirst();

                // Read the data
                String degrees = cursor.getString(cursor.getColumnIndex(WeatherApplicationConfiguration.COLUMN_UNIT));
                String geolocation = cursor.getString(cursor.getColumnIndex(WeatherApplicationConfiguration.COLUMN_GEOLOCATION));

                // Try to convert from a String to an Integer to determine the temperature unit to use
                try {
                    // Convert
                    Integer degrees_setting = Integer.parseInt(degrees);
                    // Conversion ok, now determine if a valid option was selected
                    if (degrees_setting == WeatherApplicationConfiguration.UNIT_KELVIN ||
                        degrees_setting == WeatherApplicationConfiguration.UNIT_CELSIUS ||
                        degrees_setting == WeatherApplicationConfiguration.UNIT_FAHRENHEIT) {
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
                    if (geo_setting == WeatherApplicationConfiguration.GEO_DISABLED ||
                        geo_setting == WeatherApplicationConfiguration.GEO_ENABLED) {
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
            // Get SQLite Database file
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
                columns_to_update.put(WeatherApplicationConfiguration.COLUMN_UNIT, degree_option.toString());
                column_to_update = true;
            }

            // Determine if geolocation parameter was passed in
            if (geo_option != null) {
                columns_to_update.put(WeatherApplicationConfiguration.COLUMN_GEOLOCATION, geo_option.toString());
                column_to_update = true;
            }

            // Added configuration_id parameter
            columns_to_update.put(WeatherApplicationConfiguration.TABLE_PK, "1");

            // Anything to update?
            if (column_to_update) {
                // Logic here is since SQLite doesn't have INSERT ... ON DUPLICATE KEY UPDATE ...
                // First try an insert and if that doesn't work, update!

                // Run query to save configuration
                long success = db.insertWithOnConflict(
                    WeatherApplicationConfiguration.TABLE_NAME,
                    null,
                    columns_to_update,
                    SQLiteDatabase.CONFLICT_IGNORE
                );

                // Error, try as an update
                if (success == -1) {
                    // Run query to retrieve configuration
                    success = db.update(
                        WeatherApplicationConfiguration.TABLE_NAME,
                        columns_to_update,
                        WeatherApplicationConfiguration.WHERE_FILTER,
                        WeatherApplicationConfiguration.WHERE_FILTER_VALUE
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
        // Try to load our configuration file
        try {
            // Get SQLite Database file
            SQLiteDatabase db = this.getWritableDatabase();

            // Truncate table
            db.delete(WeatherApplicationConfiguration.TABLE_NAME, null, null);
        }
        catch (Exception e) { }
    }
}
