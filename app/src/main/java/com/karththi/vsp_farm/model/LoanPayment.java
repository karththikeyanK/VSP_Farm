package com.karththi.vsp_farm.model;

public class LoanPayment {
    private int id;
    private int loanId;
    private Double paymentAmount;
    private String paymentDate;

    public LoanPayment() {
    }

    public LoanPayment(int id, int loanId, Double paymentAmount, String paymentDate) {
        this.id = id;
        this.loanId = loanId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
