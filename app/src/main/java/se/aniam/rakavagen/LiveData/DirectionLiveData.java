package se.aniam.rakavagen.LiveData;


import androidx.lifecycle.MediatorLiveData;

public class DirectionLiveData extends MediatorLiveData<Float> {

    /**
     * DirectionLiveData that extends MediatorLiveData. Used to combine two different LiveDatas
     * into one, meaning that if one of the below parameters are updated,
     * DirectionliveData will calculate a new direction and emit it.
     * @param headingLiveData
     * @param bearingLiveData
     */
    public DirectionLiveData(HeadingLiveData headingLiveData, BearingLiveData bearingLiveData) {
        addSource(headingLiveData, currHeading -> {
            if (bearingLiveData.getValue() != null && currHeading != null) {
                setValue((float) (bearingLiveData.getValue() - currHeading));
            }
        });

        addSource(bearingLiveData, currBearing -> {
            if (currBearing != null && headingLiveData.getValue() != null) {
                setValue((float) (currBearing - headingLiveData.getValue()));
            }
        });
    }
}
