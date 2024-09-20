package com.karththi.vsp_farm.dto;

public class Sale {
    private double loan;
    private double cash;

    private double delete;
    private String date;

    public Sale() {
    }

    public Sale(double loan, double cash, double delete, String date) {
        this.loan = loan;
        this.cash = cash;
        this.delete = delete;
        this.date = date;
    }

    public double getDelete() {
        return delete;
    }

    public void setDelete(double delete) {
        this.delete = delete;
    }

    public double getLoan() {
        return loan;
    }

    public void setLoan(double loan) {
        this.loan = loan;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
