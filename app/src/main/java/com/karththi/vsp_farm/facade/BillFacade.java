package com.karththi.vsp_farm.facade;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.callBack.SaveBill;
import com.karththi.vsp_farm.dto.ItemReport;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.page.cashier.BillingPageActivity;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.LoanService;

import java.util.List;
import java.util.Locale;

public class BillFacade {
    private BillService billService;
    private BillItemService billItemService;

    private Context context;

    private AppConstant appConstant;

    private LoanFacade loanFacade;

    private LoanService loanService;


    public BillFacade(Context context) {
        this.context = context;
        billService = new BillService(context);
        billItemService = new BillItemService(context);
        appConstant = new AppConstant(context);
        loanFacade = new LoanFacade(context);
        loanService = new LoanService(context);
    }

    public void addBill(List<BillItem> billItems,boolean isLoan,int customerId, SaveBill saveBill) {
        Log.i("BillFacade", "BillFacade::addBill()::is called");
        if (billItems == null && billItems.size() == 0) {
            appConstant.ErrorAlert("Error", "Please add items to the bill");
            return;
        }

        if (customerId == 0) {
            appConstant.ErrorAlert("Error", "Please select a customer");
            return;
        }

        double totalAmount = 0;

        for (BillItem billItem : billItems) {
            totalAmount += billItem.getPrice();
        }

        Bill bill = new Bill();
        bill.setReferenceNumber(appConstant.generateReferenceNumber());
        bill.setTotalAmount(totalAmount);
        bill.setCustomerId(customerId);
        bill.setUserId(Integer.parseInt(AppConstant.USER_ID));
        if (isLoan) {
            bill.setPaymentMethod(AppConstant.LOAN);
            loanFacade.handleLoan(customerId, totalAmount);
        } else {
            bill.setPaymentMethod(AppConstant.CASH);
        }
        bill.setCreatedDate(DateTimeUtils.getCurrentDate());
        bill.setCreateTime(DateTimeUtils.getCurrentTime());
        bill.setStatus(AppConstant.NEW);

       int billId = billService.createBill(bill);

        for (BillItem billItem : billItems) {
            billItem.setBillId(billId);
            billItemService.addBillItem(billItem);
        }
        Toast.makeText(context, "Bill added successfully", Toast.LENGTH_SHORT).show();
        Log.i("BillFacade", "BillFacade::addBill()::is completed");
        saveBill.onSaveBillSuccess();
    }

    public void deleteBill(int billId){
        Bill bill = billService.getBillById(billId);
        if(bill.getPaymentMethod().equals(AppConstant.LOAN)){
            Loan loan = loanService.getLoanByCustomerId(bill.getCustomerId());
            loan.setRemainingAmount(loan.getRemainingAmount() - bill.getTotalAmount());
            loanService.updateLoan(loan);
        }
        bill.setStatus(AppConstant.DELETED);
        billService.updateBillStatus(bill);
    }


}
