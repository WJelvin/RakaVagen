package se.aniam.rakavagen.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Station {

    private String name;
    private double latitude;
    private double longitude;
    private String line;

    public Station(String name, double latitude, double longitude, String line) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
    }

    public Station(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("lon", longitude).append("lat", latitude).toString();
    }
}
