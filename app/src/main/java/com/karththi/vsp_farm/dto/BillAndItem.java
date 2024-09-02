package com.karththi.vsp_farm.dto;

import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.BillItem;

import java.util.List;

public class BillAndItem {

    private Bill bill;
    private List<BillItem> billItemList;

    public BillAndItem() {
    }

    public BillAndItem(Bill bill, List<BillItem> billItemList) {
        this.bill = bill;
        this.billItemList = billItemList;
    }


    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<BillItem> getBillItemList() {
        return billItemList;
    }

    public void setBillItemList(List<BillItem> billItemList) {
        this.billItemList = billItemList;
    }
}
