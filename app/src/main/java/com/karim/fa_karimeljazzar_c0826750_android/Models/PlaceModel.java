package com.karim.fa_karimeljazzar_c0826750_android.Models;

public class PlaceModel {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private int Visited;
    private String dateAdded;

    public PlaceModel(int id, String name, double latitude, double longitude, int isVisited, String dateAdded) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.Visited = isVisited;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getVisited() {
        return Visited;
    }

    public void setVisited(int visited) {
        Visited = visited;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
