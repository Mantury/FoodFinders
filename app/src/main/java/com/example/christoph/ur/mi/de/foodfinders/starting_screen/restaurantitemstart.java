package com.example.christoph.ur.mi.de.foodfinders.starting_screen;

//This class saves the basic information of a restaurant from the Google API.
//The markers for the map are created with this information.
public class restaurantitemstart {

    private String name;
    private double latitude;
    private double longitude;
    private String place_id;
    private int openednow;
    private String address;

    public restaurantitemstart(String name, double latitude, double longitude, String place_id, int openednow, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_id = place_id;
        this.openednow = openednow;
        this.address = address;
    }

    public int isOpenednow() {
        return openednow;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {

        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}










