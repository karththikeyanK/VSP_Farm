package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.repo.LoanPaymentRepository;

public class LoanPaymentService {
    private LoanPaymentRepository loanPaymentRepository;

    public LoanPaymentService(Context context) {
        loanPaymentRepository = new LoanPaymentRepository(context);
    }

    public int addLoanPayment(LoanPayment loanPayment) {
        loanPayment.setPaymentDate(DateTimeUtils.getCurrentDate());
        return loanPaymentRepository.addLoanPayment(loanPayment);
    }

    public LoanPayment getLastPaymentByLoanId(int loanId) {
        Log.i("LoanPaymentService", "LoanPaymentService::getLastPaymentByLoanId()::is called");
        LoanPayment loanPayment = new LoanPayment();
        loanPayment = loanPaymentRepository.getLastPaymentByLoanId(loanId);
        Log.i("LoanPaymentService", "LoanPaymentService::getLastPaymentByLoanId():: LoanPayment fetched successfully");
        return loanPayment;
    }


}
