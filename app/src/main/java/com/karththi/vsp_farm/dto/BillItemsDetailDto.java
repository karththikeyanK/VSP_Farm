package com.karththi.vsp_farm.dto;

public class BillItemsDetailDto {
    private int billId;
    private String referenceNumber;
    private String status;
    private String paymentMethod;
    private String createdAt;
    private String createTime;

    private String updateAt;

    private String updateTime;
    private String customerName;
    private String userName;

    private double quantity;
    private double billItemPrice;
    private double discount;
    private String subItemName;
    private String itemName;

    public BillItemsDetailDto() {
    }

    public BillItemsDetailDto(int billId, String referenceNumber, String status, String paymentMethod, String createdAt, String createTime, String updateAt, String updateTime, String customerName, String userName, double quantity, double billItemPrice, double discount, String subItemName, String itemName) {
        this.billId = billId;
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.createTime = createTime;
        this.updateAt = updateAt;
        this.updateTime = updateTime;
        this.customerName = customerName;
        this.userName = userName;
        this.quantity = quantity;
        this.billItemPrice = billItemPrice;
        this.discount = discount;
        this.subItemName = subItemName;
        this.itemName = itemName;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getBillItemPrice() {
        return billItemPrice;
    }

    public void setBillItemPrice(double billItemPrice) {
        this.billItemPrice = billItemPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
}
