package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.repo.BillRepository;

public class BillService {

    private static final String NEW = AppConstant.NEW;
    private static final String MODIFIED = AppConstant.MODIFIED;

    private static final String MODIFIED_ORIGINAL = AppConstant.MODIFIED_ORIGINAL;
    private static final String DELETED = AppConstant.DELETED;

    private BillRepository billRepository;
    private AppConstant appConstant;
    public BillService(Context context) {
        billRepository = new BillRepository(context);
        appConstant = new AppConstant(context);

    }

    public void createBill(int customerId, int userId, double totalAmount) {
        Log.i("BillService", "BillService::createBill()::is called");
        Bill bill = new Bill(0, totalAmount, customerId, userId, NEW);
        billRepository.createBill(bill);
        Log.i("BillService", "BillService::createBill()::is completed");
    }

    public void updateBill(int id, int customerId, int userId, double totalAmount , String status) {
        Log.i("BillService", "BillService::updateBill()::is called");
        Bill bill = new Bill(id, totalAmount, customerId, userId, status);
        billRepository.updateBill(bill);
        Log.i("BillService", "BillService::updateBill()::is completed");
    }

    public void deleteBill(int id) {
        Log.i("BillService", "BillService::deleteBill()::is called");

        Log.i("BillService", "BillService::deleteBill()::is completed");
    }

    public Bill getBillByCustomerId(int customerId) {
        Log.i("BillService", "BillService::getBillByCustomerId()::is called");
        Bill bill = billRepository.getBillByCustomerId(customerId);
        Log.i("BillService", "BillService::getBillByCustomerId()::is completed");
        return bill;
    }
}
