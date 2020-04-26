package se.aniam.rakavagen.LiveData;


import androidx.lifecycle.MediatorLiveData;

public class DirectionLiveData extends MediatorLiveData<Float> {

    public DirectionLiveData(HeadingLiveData headingLiveData, BearingLiveData bearingLiveData) {
        addSource(headingLiveData, currHeading -> {
            System.out.println(bearingLiveData.getValue() + " ett " + currHeading);
            if (bearingLiveData.getValue() != null && currHeading != null) {
                setValue((float) (bearingLiveData.getValue() - currHeading));
            }
        });

        addSource(bearingLiveData, currBearing -> {
            System.out.println(headingLiveData.getValue() + " två " + currBearing);
            if (currBearing != null && headingLiveData.getValue() != null) {
                setValue((float) (currBearing - headingLiveData.getValue()));
            }
        });
    }
}
