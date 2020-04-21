package se.aniam.rakavagen.services;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class LocationService extends LiveData<Location> {

    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * LocationService that extends LiveData. Observable by an activity or fragment.
     * This class is used to access the devices geolocation.
     * @param context is necessary to get FusedLocationProviderClient
     */
    public LocationService(final Context context) {
        // LiveData that fetches current location and sets it as a LiveData to be observed by a view
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null) {
                setValue(location);
            }
        });
    }
}
