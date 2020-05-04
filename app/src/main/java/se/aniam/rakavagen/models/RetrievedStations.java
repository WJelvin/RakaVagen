package se.aniam.rakavagen.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class RetrievedStations {

    @SerializedName("stopLocationOrCoordLocation")
    @Expose
    private List<StopLocationOrCoordLocation> stopLocationOrCoordLocation = null;
    @SerializedName("serverVersion")
    @Expose
    private String serverVersion;
    @SerializedName("dialectVersion")
    @Expose
    private String dialectVersion;
    @SerializedName("requestId")
    @Expose
    private String requestId;

    /**
     * No args constructor for use in serialization
     *
     */
    public RetrievedStations() {
    }

    /**
     *
     * @param dialectVersion
     * @param serverVersion
     * @param requestId
     * @param stopLocationOrCoordLocation
     */
    public RetrievedStations(List<StopLocationOrCoordLocation> stopLocationOrCoordLocation, String serverVersion, String dialectVersion, String requestId) {
        super();
        this.stopLocationOrCoordLocation = stopLocationOrCoordLocation;
        this.serverVersion = serverVersion;
        this.dialectVersion = dialectVersion;
        this.requestId = requestId;
    }

    public List<StopLocationOrCoordLocation> getStopLocationOrCoordLocation() {
        return stopLocationOrCoordLocation;
    }

    public void setStopLocationOrCoordLocation(List<StopLocationOrCoordLocation> stopLocationOrCoordLocation) {
        this.stopLocationOrCoordLocation = stopLocationOrCoordLocation;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getDialectVersion() {
        return dialectVersion;
    }

    public void setDialectVersion(String dialectVersion) {
        this.dialectVersion = dialectVersion;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("stopLocationOrCoordLocation", stopLocationOrCoordLocation).append("serverVersion", serverVersion).append("dialectVersion", dialectVersion).append("requestId", requestId).toString();
    }
}
