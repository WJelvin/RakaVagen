package se.aniam.rakavagen.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import se.aniam.rakavagen.R;
import se.aniam.rakavagen.viewmodels.MainViewModel;
import se.aniam.rakavagen.viewmodels.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private int PERMISSIONS_REQUEST_LOCATION_CODE = 123;
    private ImageView arrowImage;
    private MainViewModel viewModel;
    private float degreeStart = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set standard theme when creating activity. Otherwise splashscreen is shown.
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Get viewmodel from the viewmodel factory.
        viewModel = new ViewModelProvider(this, MainViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);

        setContentView(R.layout.activity_main);
        TextView distanceText = findViewById(R.id.distanceText);
        TextView stationName = findViewById(R.id.stationName);

        checkForLocationPermission();
        checkIfGpsEnabled();

        // Start observing device location
        viewModel.getLastKnownLocation().observe(this, loc -> {
            if(viewModel.getClosestStation().getValue() == null) {
                viewModel.fetchClosestStation(loc.getLatitude(), loc.getLongitude());
            }
        });

        // Start observing closest station
        viewModel.getClosestStation().observe(this, station -> {
            if (station != null) {
                stationName.setText(station.getName());

                // Observe bearing to station when one is received
                viewModel.getBearingLiveData().observe(this, direction -> {
                    System.out.println("bearing from view: " + direction);
                });

                // Observe heading of device (For logging and forcing livedata to update)
                viewModel.getHeadingLiveData().observe(this, heading -> {

                });

                // Observe the direction that the station is in, and animate the arrow
                // to point in that direction
                viewModel.getDirectionLiveData().observe(this, direction -> {

                    arrowImage = findViewById(R.id.compass_arrow);
                    // Rotation animation
                    RotateAnimation ra = new RotateAnimation(
                            degreeStart,
                            direction,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setFillAfter(true);
                    // Set how long the animation for the arrow image will take
                    ra.setDuration(400);
                    // Start animation of arrow
                    arrowImage.startAnimation(ra);
                    // Set starting degree for arrow to current direction,
                    // so it starts where it ended
                    degreeStart = direction;
                });

                // Observed DistanceLiveData to update the UI with the distance to the closest station
                viewModel.getDistanceLiveData().observe(this, distance -> {
                    distanceText.setText("Distance to station: " + String.format("%.0f", distance) + " m");
                });
            }
        });


    }

    /**
     * Checks if user has granted permission to the app to access device location.
     * If not, asks the user for permission.
     */
    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION_CODE);
        }
    }

    /**
     * Checks if user has enabled GPS location.
     * If not, prompts the user to go to settings and enable it.
     */
    private void checkIfGpsEnabled() {
        LocationManager lm = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gpsEnabled) {
            showSettingsDialog();
        }
    }

    /**
     * Shows a dialog that asks the user to go to the settings screen and turn on GPS
     */
    private void showSettingsDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage( "GPS must be enabled calculate closest metro station. Please activate GPS in Settings to use this application" )
                .setPositiveButton( "Settings" , (paramDialogInterface, paramInt) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton( "Cancel" , null )
                .show() ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getLastKnownLocation().stopLocationUpdates();
    }
}
