package com.karththi.vsp_farm.facade;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.List;

public class TodayReportFacade {

    private Context context;
    private BillService billService;
    private BillItemService billItemService;
    private ItemService itemService;
    private SubItemService subItemService;

    private CustomerService customerService;




    public TodayReportFacade(Context context) {
        this.context = context;
        billService = new BillService(context);
        billItemService = new BillItemService(context);
        itemService = new ItemService(context);
        subItemService = new SubItemService(context);

        List<BillItemsDetailDto> billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());
        for (BillItemsDetailDto billItemsDetailDto : billItemsDetailDtoList) {
            Log.i("Item Name", "Item"+billItemsDetailDto.getItemName());
            Log.i("Sub Item Name", "Sub item : "+billItemsDetailDto.getSubItemName());
            Log.i("Quantity", "Quantity: "+billItemsDetailDto.getQuantity());
            Log.i("Price", "Price: "+billItemsDetailDto.getTotalAmount());
            Log.i("Customer Name", "Customer: "+billItemsDetailDto.getCustomerName());
            Log.i("User Name", "User: "+billItemsDetailDto.getUserName());
            Log.i("Payment Method", "Payment Method: "+billItemsDetailDto.getPaymentMethod());
            Log.i("Status", "Status: "+billItemsDetailDto.getStatus());

        }

    }

//    private List<ItemReport> getGeneratedReport() {
//
//        List<ItemReport> newReports = new ArrayList<>();
//        List<Bill> newBillsWithCASH = billService.getAllByStatusAndDateAndPaymentMethod(AppConstant.NEW, DateTimeUtils.getCurrentDate(), AppConstant.CASH);
//        List<Bill> newBillsWithLOAN = billService.getAllByStatusAndDateAndPaymentMethod(AppConstant.NEW, DateTimeUtils.getCurrentDate(), AppConstant.LOAN);
//        List<Bill> deletedBills = billService.getAllDeletedBIllsByDate(DateTimeUtils.getCurrentDate());
//
//
//    }




}
