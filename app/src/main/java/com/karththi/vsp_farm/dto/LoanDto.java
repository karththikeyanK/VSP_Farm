package com.karththi.vsp_farm.dto;

import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.model.LoanPayment;

public class LoanDto {
    private Loan loan;
    private LoanPayment lastPayment;

    private String customerName;

    public LoanDto() {
    }

    public LoanDto(Loan loan, LoanPayment lastPayment, String customerName) {
        this.loan = loan;
        this.lastPayment = lastPayment;
        this.customerName = customerName;
    }


    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LoanPayment getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(LoanPayment lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
