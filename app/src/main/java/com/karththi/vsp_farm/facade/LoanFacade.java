package com.karththi.vsp_farm.facade;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.karththi.vsp_farm.dto.LoanDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.page.cashier.PayLoanActivity;
import com.karththi.vsp_farm.service.LoanPaymentService;
import com.karththi.vsp_farm.service.LoanService;

import java.util.ArrayList;
import java.util.List;

public class LoanFacade {
    private LoanService loanService;
    private LoanPaymentService loanPaymentService;

    private Context context;

    private AppConstant appConstant;

    public LoanFacade(Context context) {
        this.context = context;
        loanService = new LoanService(context);
        loanPaymentService = new LoanPaymentService(context);
        appConstant = new AppConstant(context);
    }


    public void handleLoan(int customerId, double totalAmount) {
        Loan loan = new Loan();
        if (loanService.isLoanExistsByCustomerID(customerId)){
            loan = loanService.getLoanByCustomerId(customerId);
            loan.setRemainingAmount(loan.getRemainingAmount() + totalAmount);
            loanService.updateLoan(loan);
        }else {
            loan.setCustomerId(customerId);
            loan.setRemainingAmount(totalAmount);
            loanService.addLoan(loan);
        }
    }


    public void handleLoanPayment(int customerId, double amount) {
        Log.i("LoanFacade", "LoanFacade::handleLoanPayment()::is called");
        Loan loan = loanService.getLoanByCustomerId(customerId);
        loan.setRemainingAmount(Math.round((loan.getRemainingAmount() - amount) * 100.0) / 100.0);
        loanService.updateLoan(loan);
        Log.i("LoanFacade", "LoanFacade::handleLoanPayment():: Loan updated with id: "+loan.getId());
        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setPaymentAmount(amount);
        loanPayment.setLoanId(loan.getId());
        loanPaymentService.addLoanPayment(loanPayment);
        Intent intent = new Intent(context, PayLoanActivity.class);
        appConstant.SuccessAlert(AppConstant.SUCCESS,"Payment Successful", intent);
    }


    public LoanDto getLoanDtoByCustomerId(int customerId) {
        LoanDto loanDto = new LoanDto();
        Loan loan = loanService.getLoanByCustomerId(customerId);

        if (loan != null) {
            loanDto.setLoan(loan);
            LoanPayment loanPayment = loanPaymentService.getLastPaymentByLoanId(loan.getId());
            loanDto.setLastPayment(loanPayment);
            return loanDto;
        }
        return loanDto;
    }


    public List<LoanDto> getAllLoanDto(){
        Log.i("LoanFacade","LoanFacade::getAllLoanDto()::is called");
        List<LoanDto> loanDtoList = new ArrayList<>();
        List<Loan> loanList = loanService.getAllLoan();

        for (Loan loan : loanList){
            LoanDto loanDto = new LoanDto();
            LoanPayment loanPayment = loanPaymentService.getLastPaymentByLoanId(loan.getId());
            loanDto.setLastPayment(loanPayment);
            loanDto.setLoan(loan);
            loanDtoList.add(loanDto);
        }
        Log.i("LoanFacade","LoanFacade::getAllLoanDto():: is completed with size "+ loanDtoList.size());
        return loanDtoList;
    }


}
