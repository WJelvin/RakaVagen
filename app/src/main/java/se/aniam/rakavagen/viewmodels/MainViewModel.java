package se.aniam.rakavagen.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import se.aniam.rakavagen.services.LocationService;


public class MainViewModel extends AndroidViewModel {

    private String text =  "Hello world!!";
    private LocationService locationService;


    public MainViewModel(@NonNull Application application) {
        super(application);
        locationService = new LocationService(getApplication());
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
