package com.smt.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// The base class that configuration and data storage classes will use
public class WeatherApplicationDB extends SQLiteOpenHelper {
    // Weather Application Database information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WeatherApplicationData.db";

    // Constructor
    public WeatherApplicationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Methods the children classes can implement
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Nothing to do
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
}
