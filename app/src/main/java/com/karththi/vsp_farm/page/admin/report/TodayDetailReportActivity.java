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
import com.karththi.vsp_farm.Factory.TodayReportFactory;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillItemService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private TodayReportFactory todayReportFactory;

    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_detail_report);

        billItemService = new BillItemService(this);
        appConstant = new AppConstant(this);
        todayReportFactory = new TodayReportFactory(this);

        reportDate = findViewById(R.id.reportDate);
        tableLayout = findViewById(R.id.detailReportTable);
        deleteDetailReportTable = findViewById(R.id.deleteDetailReportTable);
        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);
        deleteT = findViewById(R.id.delete);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);

        executorService = Executors.newSingleThreadExecutor();
        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadPdf();  // This runs on a background thread
                    }
                });
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

        downloadPdfButton.setEnabled(false);

    }


    private void loadBills() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                billItemsDetailDtoList = billItemService.getAllBillDtoByDate(DateTimeUtils.getCurrentDate());

                // Check if the list is empty
                if (billItemsDetailDtoList == null || billItemsDetailDtoList.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TodayDetailReportActivity.this, "No Bills found", Toast.LENGTH_SHORT).show();
                            downloadPdfButton.setEnabled(false);
                        }
                    });
                    return;
                }

                // Initialize totals
                total = 0;
                cash = 0;
                loan = 0;
                deleteTotal = 0;
                deletedBills.clear(); // Clear any existing deleted bills

                // Separate bills
                for (BillItemsDetailDto dto : billItemsDetailDtoList) {
                    if (dto.getStatus().equals(AppConstant.DELETED)) {
                        deleteTotal += dto.getBillItemPrice();
                        deletedBills.add(dto);
                    } else {
                        total += dto.getBillItemPrice();
                        if (dto.getPaymentMethod().equals(AppConstant.CASH)) {
                            cash += dto.getBillItemPrice();
                        } else if (dto.getPaymentMethod().equals(AppConstant.LOAN)) {
                            loan += dto.getBillItemPrice();
                        }
                    }
                }

                // Update UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Add table header
                        TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_header, tableLayout, false);
                        tableLayout.addView(header);
                        TableRow deletedTableHeader = (TableRow) getLayoutInflater().inflate(R.layout.detail_report_header, deleteDetailReportTable, false);
                        deleteDetailReportTable.addView(deletedTableHeader);

                        // Update total amounts
                        totalT.setText(appConstant.formatAmount(total));
                        cashT.setText(appConstant.formatAmount(cash));
                        loanT.setText(appConstant.formatAmount(loan));
                        deleteT.setText(appConstant.formatAmount(deleteTotal));

                        // Load the bills into the table
                        loadTable(tableLayout, billItemsDetailDtoList);
                        loadTable(deleteDetailReportTable, deletedBills);

                        downloadPdfButton.setEnabled(true);
                    }
                });
            }
        });
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
        todayReportFactory.downloadTodayDetailPdf(billItemsDetailDtoList,deletedBills);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }



}
