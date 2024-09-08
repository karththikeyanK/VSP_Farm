package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.repo.LoanRepository;

import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private LoanRepository loanRepository;

    public LoanService(Context context){
        loanRepository = new LoanRepository(context);
    }

    public int addLoan(Loan loan){
        Log.i("LoanService", "LoanService::addLoan():: is called");
        loan.setUpdatedDate(DateTimeUtils.getCurrentDate());
        int id = loanRepository.crateLoan(loan);
        Log.i("LoanService", "LoanService::addLoan():: Loan created with id: "+id);
        return id;
    }

    public void updateLoan(Loan loan){
        Log.i("LoanService", "LoanService::updateLoan():: is called");
        loan.setUpdatedDate(DateTimeUtils.getCurrentDate());
        loanRepository.updateLoan(loan);
        Log.i("LoanService", "LoanService::updateLoan():: Loan updated with id: "+loan.getId());
    }

    public void deleteLoan(Loan loan){
        Log.i("LoanService", "LoanService::deleteLoan():: is called");
        loanRepository.deleteLoan(loan);
        Log.i("LoanService", "LoanService::deleteLoan():: Loan deleted with id: "+loan.getId());
    }

    public Loan getLoanById(int id){
        Log.i("LoanService", "LoanService::getLoanById():: is called");
        Loan loan = loanRepository.getLoanById(id);
        Log.i("LoanService", "LoanService::getLoanById():: Loan fetched with id: "+loan.getId());
        return loan;
    }

    public List<Loan> getAllLoan(){
        Log.i("LoanService", "LoanService::getAllLoan():: is called");
        List<Loan> loans = new ArrayList<>();
        loans = loanRepository.getAllLoans();
        Log.i("LoanService", "LoanService::getAllLoan():: Fetched "+loans.size()+" loans");
        return loans;
    }

    public Loan getLoanByCustomerId(int customerId) {
        Log.i("LoanService", "LoanService::getLoanByCustomerId():: is called");
        Loan loan = loanRepository.getLoansByCustomerId(customerId);

        if (loan == null) {
            Log.i("LoanService", "LoanService::getLoanByCustomerId():: No loan found for customerId: " + customerId);
        } else {
            Log.i("LoanService", "LoanService::getLoanByCustomerId():: Loan fetched with id: " + loan.getId());
        }

        return loan;
    }


    public boolean isLoanExistsByCustomerID(int customerId){
        Log.i("LoanService", "LoanService::isLoanExists():: is called");
        boolean isExists = loanRepository.isExistByCustomerID(customerId);
        Log.i("LoanService", "LoanService::isLoanExists():: isExists: "+isExists);
        return isExists;
    }


}
