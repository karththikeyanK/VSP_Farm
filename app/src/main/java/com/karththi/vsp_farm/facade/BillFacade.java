package com.karththi.vsp_farm.facade;

import android.content.Context;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;

import java.util.List;

public class BillFacade {
    private BillService billService;
    private BillItemService billItemService;

    private Context context;

    private AppConstant appConstant;

    public BillFacade(Context context) {
        this.context = context;
        billService = new BillService(context);
        billItemService = new BillItemService(context);
        appConstant = new AppConstant(context);
    }

    public void addBill(List<BillItem> billItems,boolean isLoan,int customerId) {
        if (billItems == null && billItems.size() == 0) {
            appConstant.ErrorAlert("Error", "Please add items to the bill");
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
            bill.setPaymentMethod("Loan");
        } else {
            bill.setPaymentMethod("Cash");
        }
        bill.setCreatedDate(DateTimeUtils.getCurrentDate());
        bill.setCreateTime(DateTimeUtils.getCurrentTime());
        bill.setStatus(AppConstant.NEW);


    }
}
