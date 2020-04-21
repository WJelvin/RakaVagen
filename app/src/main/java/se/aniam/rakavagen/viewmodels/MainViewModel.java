package se.aniam.rakavagen.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

    private String text =  "Hello world!!";
    private LocationService locationService;
    private ApiService apiService;
    private Station closestStation;

    /**
     * MainViewModel - Responsible for providing the View {@link MainActivity} with observable data
     * @param application is needed to access the application context
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        locationService = new LocationService(getApplication());
        init();
    }

    /**
     * Initial api calls when the viewmodel is created
     * Fetches closest metro station using the {@link ApiService}
     */
    public void init() {
        apiService = HttpClient.getSLRetrofitInstance().create(ApiService.class);
        Call<RetrievedStations> call = apiService.getClosestStation(String.valueOf(59.261269), String.valueOf(18.047944));

        call.enqueue(new Callback<RetrievedStations>() {

            @Override
            public void onResponse(Call<RetrievedStations> call, Response<RetrievedStations> response) {
                RetrievedStations stations = response.body();
                closestStation = new Station(stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getName(), stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getLat(), stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getLon());
                System.out.println("NAME: " + closestStation.getName());
                System.out.println("HELA: " + closestStation.toString());
            }

            @Override
            public void onFailure(Call<RetrievedStations> call, Throwable t) {
                System.out.println("FAILED  FETCH");
            }
        });
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocationService getLocationService() {
        return this.locationService;
    }

}
