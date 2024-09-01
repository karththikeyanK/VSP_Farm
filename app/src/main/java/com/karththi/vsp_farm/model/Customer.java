package com.karththi.vsp_farm.model;

public class Customer {
    private int id;
    private String name;
    private String description;

    private String mobile;

    public Customer() {
    }

    public Customer(int id, String name, String description, String mobile) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mobile = mobile;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
