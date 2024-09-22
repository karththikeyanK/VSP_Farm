package com.karththi.vsp_farm.dto;

public class CustomerPrintDto {
    private String customerName;

    private String lastPaymentDate;

    private double totalAmount;

    private double totalPaidAmount;
    private double balance;

    private double totalRemainingAmount;

    private double lastPaymentAmount;

    private String referenceNumber;


    public CustomerPrintDto() {
    }

    public double getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(double lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(double totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public double getTotalRemainingAmount() {
        return totalRemainingAmount;
    }

    public void setTotalRemainingAmount(double totalRemainingAmount) {
        this.totalRemainingAmount = totalRemainingAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }
}
