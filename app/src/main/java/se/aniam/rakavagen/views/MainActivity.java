package se.aniam.rakavagen.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import se.aniam.rakavagen.R;
import se.aniam.rakavagen.viewmodels.MainViewModel;
import se.aniam.rakavagen.viewmodels.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private int PERMISSIONS_REQUEST_LOCATION_CODE = 123;

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

        // Start observing device location
        viewModel.getLastKnownLocation().observe(this, loc -> {
            mainText.setText(loc.getLatitude() + " " + loc.getLongitude());
            if(viewModel.getClosestStation().getValue() == null) {
                viewModel.fetchClosestStation(loc.getLatitude(), loc.getLongitude());
            }
        });

        // Start observing closest station
        viewModel.getClosestStation().observe(this, station -> {
            if (station != null) {
                stationName.setText(station.getName());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getLastKnownLocation().stopLocationUpdates();
    }
}
