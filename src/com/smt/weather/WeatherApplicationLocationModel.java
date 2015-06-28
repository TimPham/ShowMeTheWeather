package com.smt.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public class WeatherApplicationLocationModel extends WeatherApplicationDB {
    protected static final String TABLE_NAME = "wa_location";
    protected static final String TABLE_PK = "location_id";
    protected static final String COLUMN_DATA = "location_data";
    protected static final String TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS `" + WeatherApplicationLocationModel.TABLE_NAME + "` (" +
                                                            WeatherApplicationLocationModel.TABLE_PK + " INTEGER PRIMARY KEY NOT NULL, " +
                                                            WeatherApplicationLocationModel.COLUMN_DATA + " TEXT NOT NULL" +
                                                        ");";
    protected static final String[] COLUMNS = { WeatherApplicationLocationModel.TABLE_PK, WeatherApplicationLocationModel.COLUMN_DATA };
    protected static final String DELETE_WHERE_CLAUSE = WeatherApplicationLocationModel.TABLE_PK + " =  ?";

    // Constructor for WeatherApplicationLocationModel
    public WeatherApplicationLocationModel(Context context) {
        // Call WeatherApplicationDB constructor!
        super(context);

        // The onCreate method doesn't get invoked unless the SQLite database is missing/not created.
        // I have added this here to ensure that the table exists!
        // Try to create the table if it doesn't exist
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Try to create table -- if it already exists nothing will happen!
            db.execSQL(WeatherApplicationLocationModel.TABLE_CREATE_QUERY);
        }
        catch (Exception e) { }
    }

    // Retrieve our list of stored locations
    public HashMap<Integer, String> getLocations() {
        // Initialize our HashSet which will hold our location data
        HashMap<Integer, String> sData = new HashMap<>();

        // Get our location data
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getReadableDatabase();

            // Run query to retrieve locations
            Cursor cursor = db.query(
                WeatherApplicationLocationModel.TABLE_NAME,
                WeatherApplicationLocationModel.COLUMNS,
                null,
                null,
                null,
                null,
                null
            );

            // Check if there is a result we can process
            if (cursor.getCount() > 0) {
                // Go through the entire list
                while(cursor.moveToNext()) {
                    // Add to the list
                    // Determine if the string is empty
                    String value = cursor.getString(cursor.getColumnIndex(WeatherApplicationLocationModel.COLUMN_DATA));
                    // We will not copy the data if the string is empty!
                    if (value.length() > 0) {
                        // Copy the data since the string is not empty!
                        sData.put(cursor.getInt(cursor.getColumnIndex(WeatherApplicationLocationModel.TABLE_PK)), value);
                    }
                }
            }

            // Close cursor since we don't need it anymore
            cursor.close();
        }
        catch (Exception e) { }

        // Return data
        return sData;
    }

    public boolean saveLocations(HashMap<Integer, String> hs) {
        // Save our location data
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Container to hold the parameters to update
            ContentValues columns_data = new ContentValues();

            // Loop through the HashMap to convert to Key => Value
            // Use the new enhanced for statement!
            for (Map.Entry<Integer, String> entry : hs.entrySet()) {
                // Get the Key and Value
                Integer key = entry.getKey();
                String value = entry.getValue();

                // Add the value
                columns_data.put(String.valueOf(key), value);
            }

            // Run query to update locations
            long success = db.insertWithOnConflict(
                WeatherApplicationConfigurationModel.TABLE_NAME,
                null,
                columns_data,
                SQLiteDatabase.CONFLICT_REPLACE
            );

            // Did we insert/replace everything?
            if (success == hs.size()) {
                // Yes
                return true;
            }
        }
        catch (Exception e) { }

        // Couldn't save, or only a partial save!
        return false;
    }

    // Function to purge location data
    public void clearLocationData() {
        // Try to delete everything in the table
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Truncate table
            db.delete(WeatherApplicationLocationModel.TABLE_NAME, null, null);
        }
        catch (Exception e) { }
    }

    // Function to purge specific location data
    public void clearLocationData(HashMap<Integer, String> hs) {
        // Try to delete only a certain set of values
        try {
            // Get SQLite Database object
            SQLiteDatabase db = this.getWritableDatabase();

            // Get the number of ids to delete
            Integer number_of_entries = hs.size();

            // Ensure there is at least one entry to delete
            if (number_of_entries > 0) {
                // Initialize the String array
                String[] entries_to_delete = new String[number_of_entries];

                // Counter to keep track of where we are in the string array
                int entry_index = 0;

                // Loop through the HashMap to save the id
                // Use the new enhanced for statement!
                for (Map.Entry<Integer, String> entry : hs.entrySet()) {
                    // Add the value
                    if (entry_index < number_of_entries) {
                        // Copy the value
                        entries_to_delete[entry_index] = String.valueOf(entry.getKey());
                    }
                }

                // Truncate table
                db.delete(WeatherApplicationLocationModel.TABLE_NAME, WeatherApplicationLocationModel.DELETE_WHERE_CLAUSE, entries_to_delete);
            }
        }
        catch (Exception e) { }
    }
}
