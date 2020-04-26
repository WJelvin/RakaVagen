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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get viewmodel from the viewmodel factory.
        viewModel = new ViewModelProvider(this, MainViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);

        setContentView(R.layout.activity_main);
        TextView mainText = findViewById(R.id.mainText);
        TextView stationName = findViewById(R.id.stationName);

        checkForLocationPermission();
        checkIfGpsEnabled();

        // Start observing device location
        viewModel.getLastKnownLocation().observe(this, loc -> {
            mainText.setText(loc.getLatitude() + " " + loc.getLongitude());
            if(viewModel.getClosestStation().getValue() == null) {
                viewModel.fetchClosestStation(loc.getLatitude(), loc.getLongitude());
            }

            // -------------------REWORK THIS WHEN ADDING BEARING-----------------------------------------------

            arrowImage = findViewById(R.id.compass_arrow);
            // rotation animation - reverse turn degree degrees
            RotateAnimation ra = new RotateAnimation(
                    0,
                    180,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            // set the compass animation after the end of the reservation status
            ra.setFillAfter(true);
            // set how long the animation for the compass image will take place
            ra.setDuration(820);
            // Start animation of compass image
            arrowImage.startAnimation(ra);

            // -------------------REWORK THIS WHEN ADDING BEARING-------------------------------------------------


        });

        // Start observing closest station
        viewModel.getClosestStation().observe(this, station -> {
            if (station != null) {
                stationName.setText(station.getName());

                // Observe bearing to station
                viewModel.getBearingLiveData().observe(this, direction -> {
                    System.out.println("direction from view: " + direction);
                });

                // Observe heading of device
                viewModel.getHeadingLiveData().observe(this, heading -> {
                    System.out.println("HEADING: " + heading + " +++++++++++++++");
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
