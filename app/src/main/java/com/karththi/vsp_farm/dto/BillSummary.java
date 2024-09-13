package com.karththi.vsp_farm.dto;

public class BillSummary {
    private String subItemName;
    private String itemName;
    private double totalQuantity;
    private double totalPrice;
    private double totalDiscount;

    public BillSummary() {
    }

    public BillSummary(String subItemName, String itemName, double totalQuantity, double totalPrice, double totalDiscount) {
        this.subItemName = subItemName;
        this.itemName = itemName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
    }

    public String getSubItemName() {
        return subItemName;
    }

    public void setSubItemName(String subItemName) {
        this.subItemName = subItemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
