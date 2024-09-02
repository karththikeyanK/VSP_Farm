package com.karththi.vsp_farm.model;

public class BillItem {
    private int id;
    private int billId;
    private int subItemId;
    private Double quantity;
    private double price;

    private double discount;


    public BillItem() {
    }

    public BillItem(int id, int billId, int subItemId, Double quantity, double price, double discount) {
        this.id = id;
        this.billId = billId;
        this.subItemId = subItemId;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
