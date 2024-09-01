package com.karththi.vsp_farm.model;


public class Bill {
    private int id;
    private Double totalAmount;
    private int customerId;
    private int userId;

    private String status;  // DELETED, MODIFIED,MODIFIED_ORIGINAL, NEW

    public Bill() {
    }

    public Bill(int id, Double totalAmount, int customerId, int userId, String status) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
