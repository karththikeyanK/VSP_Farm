package com.karththi.vsp_farm.facade;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.ItemReport;
import com.karththi.vsp_farm.dto.SubItemReport;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodayReportFacade {

    private Context context;
    private BillItemService billItemService;

    private  List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> loanBillItemList;

    private List<BillItemsDetailDto> cashBillItemList;

    private List<BillItemsDetailDto> deletedBillItemList;





    public TodayReportFacade(Context context) {
        this.context = context;
        billItemService = new BillItemService(context);
        billItemsDetailDtoList = new ArrayList<>();
        billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());
        deletedBillItemList = new ArrayList<>();
        loanBillItemList = new ArrayList<>();
        cashBillItemList = new ArrayList<>();

        for (BillItemsDetailDto dto : billItemsDetailDtoList){
            if (dto.getStatus().equals(AppConstant.DELETED)){
                deletedBillItemList.add(dto);
            }else if (dto.getPaymentMethod().equals(AppConstant.LOAN)){
                loanBillItemList.add(dto);
            }else if (dto.getPaymentMethod().equals(AppConstant.CASH)){
                cashBillItemList.add(dto);
            }
        }

    }


   public List<BillSummary> getTodaySummary(String methode){
        return billItemService.getSummaryByDateAndPaymentMethode(DateTimeUtils.getCurrentDate(),methode);
   }

    public List<BillSummary> getTodayTotalSummary(){
        return billItemService.getSummaryByDate(DateTimeUtils.getCurrentDate());
    }

}
