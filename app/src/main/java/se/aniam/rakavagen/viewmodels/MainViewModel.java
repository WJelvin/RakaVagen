package se.aniam.rakavagen.viewmodels;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.aniam.rakavagen.models.RetrievedStations;
import se.aniam.rakavagen.models.Station;
import se.aniam.rakavagen.services.LocationService;
import se.aniam.rakavagen.views.MainActivity;
import se.aniam.rakavagen.webapi.ApiService;
import se.aniam.rakavagen.webapi.HttpClient;


public class MainViewModel extends AndroidViewModel {

    private LocationService lastKnownLocation;
    private ApiService apiService;
    private MutableLiveData<Station> closestStation = new MutableLiveData<>();

    /**
     * MainViewModel - Responsible for providing the View {@link MainActivity} with observable data
     * @param application is needed to access the application context
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        lastKnownLocation = new LocationService(getApplication());
        init();
    }

    private void checkIfGpsAndNetworkEnabled() {
        LocationManager lm = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gpsEnabled && !networkEnabled) {
            System.out.println("DU MÅSTE ENABLA GPS ----------------");
        }
    }


    /**
     * Initial method calls when the viewmodel is created
     * Fetches device location using the {@link LocationService}
     */
    public void init() {
        lastKnownLocation.startLocationUpdates();
    }

    /**
     * Calls Trafiklabs API to fetch the closest metro station based on:
     * @param latitude
     * @param longitude
     */
    public void fetchClosestStation(double latitude, double longitude)  {
        apiService = HttpClient.getSLRetrofitInstance().create(ApiService.class);
        Call<RetrievedStations> call = apiService.getClosestStation(String.valueOf(latitude), String.valueOf(longitude));

        call.enqueue(new Callback<RetrievedStations>() {
            @Override
            public void onResponse(Call<RetrievedStations> call, Response<RetrievedStations> response) {
                RetrievedStations stations = response.body();
                closestStation.setValue(new Station(stations.getStopLocationOrCoordLocation().get(0).
                        getStopLocation().getName(),
                        stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getLat(),
                        stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getLon()
                ));
            }

            @Override
            public void onFailure(Call<RetrievedStations> call, Throwable t) {
                System.out.println("FAILED FETCH STATION");
            }
        });
    }

    public LiveData<Station> getClosestStation() {
        return closestStation;
    }

    public LocationService getLastKnownLocation() {
        return this.lastKnownLocation;
    }

}
