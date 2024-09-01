package com.karththi.vsp_farm.model;

public class BillItem {
    private int id;
    private int billId;
    private int subItemId;
    private Double quantity;
    private double price;


    public BillItem() {
    }

    public BillItem(int id, int billId, int subItemId, Double quantity, double price) {
        this.id = id;
        this.billId = billId;
        this.subItemId = subItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getSubItemId() {
        return subItemId;
    }

    public void setSubItemId(int subItemId) {
        this.subItemId = subItemId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
