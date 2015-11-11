package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoPosition {
    private double latitude;
    private double longitude;

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

    public GeoPosition() {
    }

    public GeoPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .toString();
    }
}
