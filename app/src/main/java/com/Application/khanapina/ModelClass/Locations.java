package com.Application.khanapina.ModelClass;

public class Locations {

    private String latitude;
    private String longitude;
    private String Address;
    private String landmark;

    public Locations() {
    }

    public Locations(String latitude, String longitude, String address, String landmark) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.landmark = landmark;
        Address = address;

    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
