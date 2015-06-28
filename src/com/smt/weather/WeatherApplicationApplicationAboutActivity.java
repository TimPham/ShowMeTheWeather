package com.smt.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

// Our About activity which shows application information
public class WeatherApplicationApplicationAboutActivity extends WeatherApplicationSubActivity {
    // Initialize the activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Call our superclass onCreate function
        super.onCreate(savedInstanceState);

        // Use the About Application layout
        setContentView(R.layout.about);
    }

    // Handle GitHub button press
    public void goToGitHubProject(View view) {
        // Create our intent
        Intent launchGitHubIntent = new Intent(Intent.ACTION_VIEW);

        // Set the data for our intent to the GitHub URL
        launchGitHubIntent.setData(Uri.parse(this.getString(R.string.app_github_link)));

        // Launches the web browser
        startActivity(launchGitHubIntent);
    }
}
