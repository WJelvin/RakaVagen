package se.aniam.rakavagen.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StopLocation {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("extId")
    @Expose
    private String extId;
    @SerializedName("hasMainMast")
    @Expose
    private boolean hasMainMast;
    @SerializedName("mainMastId")
    @Expose
    private String mainMastId;
    @SerializedName("mainMastExtId")
    @Expose
    private String mainMastExtId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("dist")
    @Expose
    private int dist;
    @SerializedName("products")
    @Expose
    private int products;

    /**
     * No args constructor for use in serialization
     *
     */
    public StopLocation() {
    }

    /**
     *
     * @param mainMastId
     * @param hasMainMast
     * @param name
     * @param weight
     * @param dist
     * @param lon
     * @param id
     * @param extId
     * @param mainMastExtId
     * @param lat
     * @param products
     */
    public StopLocation(String id, String extId, boolean hasMainMast, String mainMastId, String mainMastExtId, String name, double lon, double lat, int weight, int dist, int products) {
        super();
        this.id = id;
        this.extId = extId;
        this.hasMainMast = hasMainMast;
        this.mainMastId = mainMastId;
        this.mainMastExtId = mainMastExtId;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.weight = weight;
        this.dist = dist;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public boolean isHasMainMast() {
        return hasMainMast;
    }

    public void setHasMainMast(boolean hasMainMast) {
        this.hasMainMast = hasMainMast;
    }

    public String getMainMastId() {
        return mainMastId;
    }

    public void setMainMastId(String mainMastId) {
        this.mainMastId = mainMastId;
    }

    public String getMainMastExtId() {
        return mainMastExtId;
    }

    public void setMainMastExtId(String mainMastExtId) {
        this.mainMastExtId = mainMastExtId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("extId", extId).append("hasMainMast", hasMainMast).append("mainMastId", mainMastId).append("mainMastExtId", mainMastExtId).append("name", name).append("lon", lon).append("lat", lat).append("weight", weight).append("dist", dist).append("products", products).toString();
    }
}
