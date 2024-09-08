package com.karththi.vsp_farm.dto;

import java.util.List;

public class ItemReport {
    private String itemName;
    private double total_quantity;
    private double total_discount;
    private double total_price;

    private String status;

    private String paymentMethod;

    private String billDate;

    private List<SubItemReport> subItemReports;

    public ItemReport() {
    }

    public ItemReport(String itemName, double total_quantity, double total_discount, double total_price, String status, String paymentMethod, String billDate, List<SubItemReport> subItemReports) {
        this.itemName = itemName;
        this.total_quantity = total_quantity;
        this.total_discount = total_discount;
        this.total_price = total_price;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.billDate = billDate;
        this.subItemReports = subItemReports;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(double total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(double total_discount) {
        this.total_discount = total_discount;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public List<SubItemReport> getSubItemReports() {
        return subItemReports;
    }

    public void setSubItemReports(List<SubItemReport> subItemReports) {
        this.subItemReports = subItemReports;
    }
}
