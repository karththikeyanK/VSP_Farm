package com.karththi.vsp_farm.model;

public class Item {

    private int id;
    private String name;
    private Measurement measurement;

    private byte[] image;


    public Item() {
    }

    public Item(int id, String name, Measurement measurement, byte[] image) {
        this.id = id;
        this.name = name;
        this.measurement = measurement;
        this.image = image;
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

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
