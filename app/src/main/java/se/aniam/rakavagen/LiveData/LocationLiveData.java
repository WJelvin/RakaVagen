package se.aniam.rakavagen.LiveData;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<Location> {

    private static final long INTERVAL = 500 * 10;
    private static final long FASTEST_INTERVAL = 500 * 5;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    /**
     * LocationService that extends LiveData. Observable by an activity or fragment.
     * This class is used to access the devices geolocation.
     *
     * @param context is necessary to get FusedLocationProviderClient
     */
    public LocationLiveData(final Context context) {
        // LiveData that fetches current location and sets it as a LiveData to be observed by a view
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                setValue(location);
            }
        });
        createLocationRequest();
        startLocationUpdates();
    }

    /**
     * Creates a LocationRequest with parameters for the FusedLocationProviderClient
     */
    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    return;
                }
                setValue(locationResult.getLastLocation());
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        Log.d("LocationService", "Location update started ..............: ");
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
