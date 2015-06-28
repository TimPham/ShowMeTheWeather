package com.smt.weather;

import android.os.Handler;
import android.widget.TextView;

import java.io.Console;
import java.util.Calendar;

public class WeatherApplicationClock implements Runnable {
    // The format we will use to get the time
    private final String timeFormat = "%02d:%02d:%02d %s";

    // How frequently we should update the time?
    public static final int updateInterval = 1000;

    // The TextView that we will use to display the time
    TextView textView;

    // The Handler that controls the update of time
    Handler handler;

    // The flag that controls the run state of the time update
    boolean runUpdate;

    // Our constructor
    public WeatherApplicationClock(Handler h, TextView tv) {
        // Copy the Handler which we will use to control updates to the timer
        this.handler = h;

        // Copy the TextView which we will update
        this.textView = tv;

        // Initialize to false until we invoke start()
        this.runUpdate = false;
    }

    // Gets the current time formatted as a string
    public String GetCurrentTime() {
        // Get a Calendar object instance
        Calendar calendarInstance = Calendar.getInstance();

        // Get the hour
        int hour = calendarInstance.get(Calendar.HOUR_OF_DAY);

        // Get the minute
        int minute = calendarInstance.get(Calendar.MINUTE);

        // Get the second
        int second = calendarInstance.get(Calendar.SECOND);

        // Get whether it is AM or PM
        String am_or_pm = "AM";
        // Check if it matches PM
        if (calendarInstance.get(Calendar.AM_PM) == Calendar.PM) {
            // Yes, change it to PM
            am_or_pm = "PM";
        }
        // Otherwise we can leave it at AM!

        // Return the string
        return String.format(this.timeFormat, hour, minute, second, am_or_pm);
    }

    // Starts the time view updates
    public void start() {
        // Set flag to run state
        this.runUpdate = true;

        // Queue up an update!
        this.handler.postDelayed(this, WeatherApplicationClock.updateInterval);
    }

    // Stops the time view updates
    public void stop() {
        // Set flag to terminate state
        this.runUpdate = false;
    }

    // Updates the time
    @Override
    public void run() {
        String current_time = this.GetCurrentTime();

        // Get the current time and update the view
        this.textView.setText(current_time);

        // Invalidate the view to get Android to refresh the view
        this.textView.invalidate();

        // Should we continue updating?
        if (this.runUpdate) {
            // Yes, Queue up another update!
            this.handler.postDelayed(this, WeatherApplicationClock.updateInterval);
        }
    }
}
