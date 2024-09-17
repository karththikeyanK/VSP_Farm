package com.karththi.vsp_farm.page.admin.report;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.facade.TodayReportFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;

import java.util.ArrayList;
import java.util.List;

public class TodayDetailReportActivity extends AppCompatActivity {

    private TodayReportFacade todayReportFacade;

    private TableLayout generalReportTableCash;
    private TableLayout generalReportTableLoan;

    private TableLayout getGeneralReportTable;

    private TextView reportDate;

    private Button downloadPdfButton;

    private BillItemService billItemService;

    private List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> deletedBills;

    private double total, cash,loan,deleteTotal;

    private TableLayout tableLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_detail_report);

        todayReportFacade = new TodayReportFacade(this);
        billItemService = new BillItemService(this);

        reportDate = findViewById(R.id.reportDate);
        tableLayout = findViewById(R.id.detailReportTable);


        reportDate.setText("Date - " + DateTimeUtils.getCurrentDate() + " " + DateTimeUtils.getCurrentTime());

        billItemsDetailDtoList =  new ArrayList<>();
        deletedBills = new ArrayList<>();

        loadBills();

    }


    private void loadBills(){
        billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());

        for (BillItemsDetailDto dto : billItemsDetailDtoList){
            if (dto.getStatus().equals(AppConstant.DELETED)){
                deletedBills.add(dto);
                billItemsDetailDtoList.remove(dto);
            }else{
                if (dto.getPaymentMethod().equals(AppConstant.CASH)){
                    cash += dto.getBillItemPrice();
                }else if (dto.getPaymentMethod().equals(AppConstant.LOAN)){
                    loan += dto.getBillItemPrice();
                }
            }
        }

        TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_header, tableLayout, false);
        tableLayout.addView(header);


    }


}
