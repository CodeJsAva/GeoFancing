package com.app.geofancing;

import android.location.Location;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by beyond on 31-Mar-17.
 */

public class GeoFenceObject {
    String id;
    double latitude;
    double longitude;
    float radius;
    Advertisment advertisment;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public Advertisment getAdvertisment() {
        return advertisment;
    }

    public void setAdvertisements(Advertisment advertisements) {
        this.advertisment = advertisements;
    }
}
