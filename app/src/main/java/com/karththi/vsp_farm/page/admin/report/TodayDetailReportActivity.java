package com.karththi.vsp_farm.page.admin.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.facade.TodayReportFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodayDetailReportActivity extends AppCompatActivity {


    private TextView reportDate;

    private Button downloadPdfButton;

    private BillItemService billItemService;

    private List<BillItemsDetailDto> billItemsDetailDtoList;

    private List<BillItemsDetailDto> deletedBills;

    private double total, cash,loan,deleteTotal;

    private TableLayout tableLayout,deleteDetailReportTable;

    private int previousBillID = 0;

    private TextView totalT,cashT,loanT,deleteT;

    private AppConstant appConstant;

    private TodayReportFacade todayReportFacade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_detail_report);

        billItemService = new BillItemService(this);
        appConstant = new AppConstant(this);
        todayReportFacade = new TodayReportFacade(this);

        reportDate = findViewById(R.id.reportDate);
        tableLayout = findViewById(R.id.detailReportTable);
        deleteDetailReportTable = findViewById(R.id.deleteDetailReportTable);
        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);
        deleteT = findViewById(R.id.delete);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);
        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf();
            }
        });

        reportDate.setText("Date - " + DateTimeUtils.getCurrentDate() + " " + DateTimeUtils.getCurrentTime());

        billItemsDetailDtoList =  new ArrayList<>();
        deletedBills = new ArrayList<>();

        total = 0;
        cash = 0;
        loan = 0;
        deleteTotal = 0;

        loadBills();

    }


    private void loadBills() {
        billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());

        // Add table header
        TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_header, tableLayout, false);
        tableLayout.addView(header);
        TableRow deleted_table_header = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_header, deleteDetailReportTable, false);
        deleteDetailReportTable.addView(deleted_table_header);

        if (billItemsDetailDtoList.isEmpty()) {
            Toast.makeText(this, "No Bills found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use iterator to avoid ConcurrentModificationException
        Iterator<BillItemsDetailDto> iterator = billItemsDetailDtoList.iterator();
        while (iterator.hasNext()) {
            BillItemsDetailDto dto = iterator.next();

            if (dto.getStatus().equals(AppConstant.DELETED)) {
                deleteTotal += dto.getBillItemPrice();
                deletedBills.add(dto);
                iterator.remove(); // Remove safely during iteration
            } else {
                total += dto.getBillItemPrice();
                if (dto.getPaymentMethod().equals(AppConstant.CASH)) {
                    cash += dto.getBillItemPrice();
                } else if (dto.getPaymentMethod().equals(AppConstant.LOAN)) {
                    loan += dto.getBillItemPrice();
                }
            }
        }

        // Update total amounts
        totalT.setText(appConstant.formatAmount(total));
        cashT.setText(appConstant.formatAmount(cash));
        loanT.setText(appConstant.formatAmount(loan));
        deleteT.setText(appConstant.formatAmount(deleteTotal));

        // Load the bills into the table
        loadTable(tableLayout, billItemsDetailDtoList);
        loadTable(deleteDetailReportTable, deletedBills);
    }

    private void loadTable(TableLayout tLayout, List<BillItemsDetailDto> list){
        for (BillItemsDetailDto dto : list){

            if (previousBillID != dto.getBillId() && previousBillID != 0){
                TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_row, tLayout, false);
                row.setBackground(null);
                tLayout.addView(row);
            }
            previousBillID = dto.getBillId();

            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_row, tLayout, false);

            // Assign TextViews to variables timeColumn
            TextView dateColumn = row.findViewById(R.id.dateColumn);
            TextView timeColumn = row.findViewById(R.id.timeColumn);
            TextView userColumn = row.findViewById(R.id.userColumn);
            TextView itemColumn = row.findViewById(R.id.itemColumn);
            TextView subItemColumn = row.findViewById(R.id.subItemColumn);
            TextView referenceNumberColumn = row.findViewById(R.id.referenceNumberColumn);
            TextView customerColumn = row.findViewById(R.id.customerColumn);
            TextView paymentColumn = row.findViewById(R.id.paymentColumn);
            TextView statusColumn = row.findViewById(R.id.statusColumn);
            TextView discountColumn = row.findViewById(R.id.discountColumn);
            TextView quantityColumn = row.findViewById(R.id.quantityColumn);
            TextView priceColumn = row.findViewById(R.id.priceColumn);

            if (dto.getPaymentMethod().equals(AppConstant.LOAN)){
                paymentColumn.setTextColor(Color.parseColor("#b2102b"));
            }

            if (dto.getStatus().equals(AppConstant.DELETED)){
                statusColumn.setTextColor(Color.parseColor("#b2102b"));
            }

            // Set the text for each TextView
            dateColumn.setText(dto.getCreatedAt());
            timeColumn.setText(dto.getCreateTime());
            userColumn.setText(dto.getUserName());
            itemColumn.setText(dto.getItemName());
            subItemColumn.setText(dto.getSubItemName());
            referenceNumberColumn.setText(dto.getReferenceNumber());
            customerColumn.setText(dto.getCustomerName());
            paymentColumn.setText(dto.getPaymentMethod());
            statusColumn.setText(dto.getStatus());
            discountColumn.setText(String.valueOf(dto.getDiscount()));
            quantityColumn.setText(String.valueOf(dto.getQuantity()));
            priceColumn.setText(String.valueOf(dto.getBillItemPrice()));

            // Add the populated row to the table layout
            tLayout.addView(row);
        }

    }

    private void downloadPdf(){
        todayReportFacade.downloadTodayDetailPdf(total,cash,loan,deleteTotal,billItemsDetailDtoList,deletedBills);
    }


}
