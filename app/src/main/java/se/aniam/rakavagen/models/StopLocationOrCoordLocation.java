package se.aniam.rakavagen.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StopLocationOrCoordLocation {

    @SerializedName("StopLocation")
    @Expose
    private StopLocation stopLocation;

    /**
     * No args constructor for use in serialization
     *
     */
    public StopLocationOrCoordLocation() {
    }

    /**
     *
     * @param stopLocation
     */
    public StopLocationOrCoordLocation(StopLocation stopLocation) {
        super();
        this.stopLocation = stopLocation;
    }

    public StopLocation getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(StopLocation stopLocation) {
        this.stopLocation = stopLocation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("stopLocation", stopLocation).toString();
    }

}
