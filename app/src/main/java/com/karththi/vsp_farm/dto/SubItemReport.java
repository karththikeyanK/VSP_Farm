package com.karththi.vsp_farm.dto;

public class SubItemReport {
    private String subItemName;
    private double quantity;
    private double discount;
    private double total;

    public SubItemReport() {
    }

    public SubItemReport(String subItemName,  double quantity, double discount, double total) {
        this.subItemName = subItemName;
        this.quantity = quantity;
        this.discount = discount;
        this.total = total;
    }

    public String getSubItemName() {
        return subItemName;
    }

    public void setSubItemName(String subItemName) {
        this.subItemName = subItemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
