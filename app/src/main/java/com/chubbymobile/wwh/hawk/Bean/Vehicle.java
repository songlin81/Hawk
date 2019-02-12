package com.chubbymobile.wwh.hawk.Bean;

public class Vehicle {

    private int id;
    private String model;

    public Vehicle(int id, String model) {
        this.id = id;
        this.model = model;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}