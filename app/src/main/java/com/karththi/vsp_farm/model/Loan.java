package com.karththi.vsp_farm.model;

public class Loan {
    private int id;
    private int customerId;
    private Double remainingAmount;
    private String updatedDate;

    private String customerName;

    public Loan() {
    }

    public Loan(int id, int customerId, Double remainingAmount, String updatedDate) {
        this.id = id;
        this.customerId = customerId;
        this.remainingAmount = remainingAmount;
        this.updatedDate = updatedDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
