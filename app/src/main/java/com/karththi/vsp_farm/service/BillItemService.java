package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.Sale;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.repo.BillItemRepository;

import java.util.ArrayList;
import java.util.List;

public class BillItemService {
    private BillItemRepository billItemRepository;

    private Context context;

    public BillItemService(Context context) {
        this.context = context;
        billItemRepository = new BillItemRepository(context);
    }

    public void addBillItem(BillItem billItem) {
        Log.i("BillItemService", "BillItemService::addBillItem()::is called");
        billItemRepository.addBillItem(billItem);
    }

    public void updateBillItem(BillItem billItem) {
        Log.i("BillItemService", "BillItemService::updateBillItem()::is called");
        billItemRepository.updateBillItem(billItem);
    }

    public void deleteBillItem(int id) {
        Log.i("BillItemService", "BillItemService::deleteBillItem()::is called");
        billItemRepository.deleteBillItem(id);
    }

    public BillItem getBillItemById(int id) {
        Log.i("BillItemService", "BillItemService::getBillItemById()::is called");
        return billItemRepository.getBillItemById(id);
    }

    public List<BillItem> getBillItemByBillId(int billId) {
        Log.i("BillItemService", "BillItemService::getBillItemByBillId()::is called");
        List<BillItem> billItems = new ArrayList<>();
        billItems = billItemRepository.getBillItemByBillId(billId);
        Log.i("BillItemService", "BillItemService::getBillItemByBillId():: fetched " + billItems.size() + " items");
        return billItems;
    }

    public List<BillItemsDetailDto> getAllBillDtoByDate(String date) {
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDate()::is called");
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        billItemsDetailDtoList = billItemRepository.getAllBillDtoByDate(date);
        if (billItemsDetailDtoList.isEmpty()){
            Log.w("BillService", "BillService::getAllBillDtoByDate():: returned null list");
        }
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDate():: fetched " + billItemsDetailDtoList.size() + " items");
        return billItemsDetailDtoList;
    }

    public List<BillItemsDetailDto> getAllBillDtoByDateRange(String startDate, String endDate) {
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDateRange()::is called");
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        billItemsDetailDtoList = billItemRepository.getAllBillDtoByDateRange(startDate, endDate);
        if (billItemsDetailDtoList.isEmpty()){
            Log.w("BillService", "BillService::getAllBillDtoByDateRange():: returned null list");
        }
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDateRange():: fetched " + billItemsDetailDtoList.size() + " items");
        return billItemsDetailDtoList;
    }

    public List<BillItemsDetailDto> getAllBillDtoByDateRangeAndCustomerId(String startDate, String endDate, int customerId) {
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDateRangeAndCustomerId()::is called");
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        billItemsDetailDtoList = billItemRepository.getAllBillDtoByDateRangeAndCustomerId(startDate, endDate, customerId);
        if (billItemsDetailDtoList.isEmpty()){
            Log.w("BillService", "BillService::getAllBillDtoByDateRangeAndCustomerId():: returned null list");
        }
        Log.i("BillItemService", "BillItemService::getAllBillDtoByDateRangeAndCustomerId():: fetched " + billItemsDetailDtoList.size() + " items");
        return billItemsDetailDtoList;
    }



}
