package com.seventh.transiro.model;

public class Halte{
    private String halteName;
    private double longitude;
    private double latitude;
    private double distanceFromUser;

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public String getHalteName() {
        return halteName;
    }

    public void setHalteName(String halteName) {
        this.halteName = halteName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
