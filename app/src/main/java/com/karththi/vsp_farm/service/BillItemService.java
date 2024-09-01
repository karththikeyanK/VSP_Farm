package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.repo.BillItemRepository;

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


}
