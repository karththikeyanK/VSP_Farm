package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.LoanDto;
import com.karththi.vsp_farm.dto.LoanPaymentDto;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.repo.LoanPaymentRepository;

import java.util.List;

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

    public List<LoanPaymentDto> getAllCustomerLoanPaymentsByDate(String date) {
        Log.i("LoanPaymentService", "LoanPaymentService::getAllCustomerLoanPaymentsByDate()::is called");
        List<LoanPaymentDto> loanPaymentDtoList = loanPaymentRepository.getAllCustomerLoanPaymentsByDate(date);
        Log.i("LoanPaymentService", "LoanPaymentService::getAllCustomerLoanPaymentsByDate():: LoanPayment fetched successfully");
        return loanPaymentDtoList;
    }

    public List<LoanPaymentDto> getLoanPaymentListByDateRange(int customerId, String fromDate, String toDate) {
        Log.i("LoanPaymentService", "LoanPaymentService::getLoanPaymentListByDateRange()::is called with customer id "+customerId);
        List<LoanPaymentDto> loanPaymentDtoList = loanPaymentRepository.getLoanPaymentListByDateRange(customerId, fromDate, toDate);
        Log.i("LoanPaymentService", "LoanPaymentService::getLoanPaymentListByDateRange():: LoanPayment fetched successfully");
        return loanPaymentDtoList;
    }


}
