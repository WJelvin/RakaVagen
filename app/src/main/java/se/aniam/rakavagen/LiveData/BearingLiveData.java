package se.aniam.rakavagen.LiveData;

import android.location.Location;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import se.aniam.rakavagen.models.Station;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;


public class BearingLiveData extends MediatorLiveData<Double> {

    /**
     * BearingLiveData that extends MediatorLiveData. Used to combine two different LiveDatas
     * into one, meaning that if one of the below parameters are updated,
     * BearingLiveData will calculate a new direction and emit it.
     *
     * @param currentLocation
     * @param targetLocation
     */
    public BearingLiveData(LocationLiveData currentLocation, MutableLiveData<Station> targetLocation) {
        addSource(currentLocation, currLoc -> {
            setValue(computeBearing(currLoc, targetLocation.getValue()));
        });

        addSource(targetLocation, targetLoc -> {
            setValue(computeBearing(currentLocation.getValue(), targetLoc));
        });
    }

    /**
     * Calculates the bearing in degrees to the closest station.
     *
     * @param currentLoc
     * @param targetLoc
     * @return
     */
    public double computeBearing(Location currentLoc, Station targetLoc) {
        double currentLat = toRadians(currentLoc.getLatitude());
        double currentLng = toRadians(currentLoc.getLongitude());
        double targetLat = toRadians(targetLoc.getLatitude());
        double targetLng = toRadians(targetLoc.getLongitude());
        double dLng = targetLng - currentLng;
        double bearing = atan2(
                sin(dLng) * cos(targetLat),
                cos(currentLat) * sin(targetLat) - sin(currentLat) * cos(targetLat) * cos(dLng));
        bearing = toDegrees(bearing);
        if (bearing < 0) {
            bearing = bearing + 360;
        }
        return bearing;
    }
}
