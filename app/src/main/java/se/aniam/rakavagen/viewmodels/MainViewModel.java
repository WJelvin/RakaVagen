package se.aniam.rakavagen.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import se.aniam.rakavagen.LiveData.BearingLiveData;
import se.aniam.rakavagen.LiveData.DirectionLiveData;
import se.aniam.rakavagen.LiveData.DistanceLiveData;
import se.aniam.rakavagen.LiveData.HeadingLiveData;
import se.aniam.rakavagen.models.RetrievedStations;
import se.aniam.rakavagen.models.Station;
import se.aniam.rakavagen.LiveData.LocationLiveData;
import se.aniam.rakavagen.views.MainActivity;
import se.aniam.rakavagen.webapi.ApiService;
import se.aniam.rakavagen.webapi.HttpClient;


public class MainViewModel extends AndroidViewModel {

    private LocationLiveData lastKnownLocation;
    private ApiService apiService;
    private MutableLiveData<Station> closestStation = new MutableLiveData<>();
    private BearingLiveData bearingLiveData;
    private HeadingLiveData headingLiveData;
    private DirectionLiveData directionLiveData;
    private DistanceLiveData distanceLiveData;

    /**
     * MainViewModel - Responsible for providing the View {@link MainActivity} with observable data
     * @param application is needed to access the application context
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        lastKnownLocation = new LocationLiveData(getApplication());
        init();
    }

    /**
     * Initial method calls when the viewmodel is created
     * Fetches device location using the {@link LocationLiveData}
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
        headingLiveData = new HeadingLiveData(getApplication().getApplicationContext(), lastKnownLocation);
        apiService = HttpClient.getSLRetrofitInstance().create(ApiService.class);
        Call<RetrievedStations> call = apiService.getClosestStation(String.valueOf(latitude), String.valueOf(longitude));

        call.enqueue(new Callback<RetrievedStations>() {
            @Override
            public void onResponse(Call<RetrievedStations> call, Response<RetrievedStations> response) {
                // When closest station is received we can start initiating our livedatas
                // because we then have both our last known location, and our target station
                bearingLiveData = new BearingLiveData(lastKnownLocation, closestStation);
                directionLiveData = new DirectionLiveData(headingLiveData, bearingLiveData);
                distanceLiveData = new DistanceLiveData(lastKnownLocation, closestStation);
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

    // Most of the getters for our livedata classes return LiveData to make them immutable from the view
    public LiveData<Station> getClosestStation() {
        return closestStation;
    }

    public LocationLiveData getLastKnownLocation() {
        return this.lastKnownLocation;
    }

    public LiveData<Double> getBearingLiveData() {
        return bearingLiveData;
    }

    public LiveData<Float> getHeadingLiveData() {
        return headingLiveData;
    }

    public LiveData<Float> getDirectionLiveData() {
        return directionLiveData;
    }

    public LiveData<Float> getDistanceLiveData() {
        return distanceLiveData;
    }
}
