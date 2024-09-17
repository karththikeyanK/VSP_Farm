package com.karththi.vsp_farm.service;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.repo.BillRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BillService {

    private static final String NEW = AppConstant.NEW;
    private static final String MODIFIED = AppConstant.MODIFIED;

    private static final String MODIFIED_ORIGINAL = AppConstant.MODIFIED_ORIGINAL;
    private static final String DELETED = AppConstant.DELETED;

    private BillRepository billRepository;
    private AppConstant appConstant;

    private Context context;
    public BillService(Context context) {
        this.context = context;
        billRepository = new BillRepository(context);
        appConstant = new AppConstant(context);

    }

    public int createBill(Bill bill) {
        Log.i("BillService", "BillService::createBill()::is called");
        int id = billRepository.createBill(bill);
        Log.i("BillService", "BillService::createBill()::is completed with id: " + id);
        return id;
    }

    public void updateBill(int id, int customerId, int userId, double totalAmount , String paymentMethode,String status) {
        Log.i("BillService", "BillService::updateBill()::is called");
        Bill bill = new Bill();
        bill.setId(id);
        bill.setCustomerId(customerId);
        bill.setUserId(userId);
        bill.setTotalAmount(totalAmount);
        bill.setStatus(status);
        bill.setPaymentMethod(paymentMethode);
        bill.setUpdatedDate(DateTimeUtils.getCurrentDate());
        bill.setUpdateTime(DateTimeUtils.getCurrentTime());
        bill.setModifiedBy(AppConstant.USER_NAME);
        billRepository.updateBill(bill);
        Log.i("BillService", "BillService::updateBill()::is completed");
    }

    public void deleteBill(int id) {
        Log.i("BillService", "BillService::deleteBill()::is called");
        billRepository.deleteBill(id);
        Log.i("BillService", "BillService::deleteBill()::is completed");
    }

    public Bill getBillByCustomerId(int customerId) {
        Log.i("BillService", "BillService::getBillByCustomerId()::is called");
        Bill bill = billRepository.getBillByCustomerId(customerId);
        Log.i("BillService", "BillService::getBillByCustomerId()::is completed");
        return bill;
    }



    public Bill getBillById(int id) {
        Log.i("BillService", "BillService::getBillById()::is called");
        Bill bill = billRepository.getBillById(id);
        Log.i("BillService", "BillService::getBillById()::is completed");
        return bill;
    }

    public List<Bill> getAllBillByDate(String date) {
        List<Bill> bills = new ArrayList<>();
        Log.i("BillService", "BillService::getAllBills()::is called");
        bills = billRepository.getBillsByDate(date);
        Log.i("BillService", "BillService::getAllBills()::is completed");
        return bills;
    }

    public void updateBillStatus(Bill bill) {
        Log.i("BillService", "BillService::updateBillStatus()::is called");
        billRepository.updateBill(bill);
        Log.i("BillService", "BillService::updateBillStatus()::is completed");
    }

    public List<Bill> getAllByStatusAndDateAndPaymentMethod(String status, String date, String paymentMethod) {
        List<Bill> bills = new ArrayList<>();
        Log.i("BillService", "BillService::getAllByStatusAndDateAndPaymentMethod()::is called");
        bills = billRepository.getAllByStatusAndDateAndPaymentMethod(status, date, paymentMethod);
        Log.i("BillService", "BillService::getAllByStatusAndDateAndPaymentMethod()::is completed");
        return bills;
    }

    public List<Bill> getAllDeletedBIllsByDate(String date) {
        List<Bill> bills = new ArrayList<>();
        Log.i("BillService", "BillService::getAllDeletedBIllsByDate()::is called");
        bills = billRepository.getAllDeletedBIllsByDate(date);
        Log.i("BillService", "BillService::getAllDeletedBIllsByDate()::is completed");
        return bills;
    }

    public List<Bill> getBillsBetweenDates(String startDate, String endDate){
        Log.i("BillService","BillService::getBillsBetweenDates():: is called..");
        List<Bill> billList = new ArrayList<>();
        billList = billRepository.getBillsBetweenDates(startDate,endDate);
        Log.i("BillService","BillService::getBillsBetweenDates()::is completed with size: "+billList.size());
        return billList;
    }




}
