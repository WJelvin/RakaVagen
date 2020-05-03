package se.aniam.rakavagen.LiveData;

import android.location.Location;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import se.aniam.rakavagen.models.Station;

public class DistanceLiveData extends MediatorLiveData<Float> {

    /**
     * DistanceLiveData that extends MediatorLiveData. Used to combine two different LiveDatas
     * into one, meaning that if one of the below parameters are updated,
     * DistanceLiveData will calculate a new direction and emit it.
     * @param currentLocation
     * @param targetLocation
     */
    public DistanceLiveData(LocationLiveData currentLocation, MutableLiveData<Station> targetLocation) {
        addSource(currentLocation, currLoc -> {
            setValue(computeDistance(currLoc, targetLocation.getValue()));
        });

        addSource(targetLocation, targetLoc -> {
            setValue(computeDistance(currentLocation.getValue(), targetLoc));
        });
    }

    /**
     * Calculates the distance in meters to the closest station.
     * @param currentLoc
     * @param targetLoc
     * @return
     */
    public float computeDistance(Location currentLoc, Station targetLoc) {
        Location targetStation = new Location("");
        targetStation.setLatitude(targetLoc.getLatitude());
        targetStation.setLongitude(targetLoc.getLongitude());
        return currentLoc.distanceTo(targetStation);
    }
}
