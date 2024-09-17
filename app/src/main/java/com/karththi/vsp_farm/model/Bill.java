package com.karththi.vsp_farm.model;

public class Bill {
    private int id;
    private String referenceNumber;
    private Double totalAmount;
    private int customerId;
    private int userId;
    private String paymentMethod;
    private String createdDate;
    private String createTime;
    private String updatedDate;
    private String updateTime;
    private String modifiedBy;
    private String status;  // DELETED, MODIFIED,MODIFIED_ORIGINAL, NEW

    public Bill() {
    }

    public Bill(int id, String referenceNumber, Double totalAmount, int customerId, int userId, String paymentMethod, String createdDate, String createTime, String updatedDate, String updateTime, String modifiedBy, String status) {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.createdDate = createdDate;
        this.createTime = createTime;
        this.updatedDate = updatedDate;
        this.updateTime = updateTime;
        this.modifiedBy = modifiedBy;
        this.status = status;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
