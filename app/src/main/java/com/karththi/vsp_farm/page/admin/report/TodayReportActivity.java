package com.karththi.vsp_farm.page.admin.report;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.Factory.TodayReportFactory;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodayReportActivity extends AppCompatActivity {

    private TodayReportFactory todayReportFactory;

    private TableLayout generalReportTableCash;
    private TableLayout generalReportTableLoan;

    private TableLayout getGeneralReportTable;

    private TextView reportDate;

    private Button downloadPdfButton;

    List<BillSummary> cashList;
    List<BillSummary> loanList;
    List<BillSummary> summaryList;

    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);

        todayReportFactory = new TodayReportFactory(this);

        reportDate = findViewById(R.id.reportDate);

        reportDate.setText("Date - "+ DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime());


        generalReportTableCash = findViewById(R.id.generalReportTableCash);
        generalReportTableLoan = findViewById(R.id.generalReportTableLoan);
        getGeneralReportTable = findViewById(R.id.generalReportTable);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);

        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadPdf();
                    }
                });
            }
        });

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                fetchList();

                loadGeneralReport();
            }
        });

        downloadPdfButton.setEnabled(false);
    }

    private void fetchList() {
        cashList = todayReportFactory.getTodaySummary(AppConstant.CASH);
        loanList = todayReportFactory.getTodaySummary(AppConstant.LOAN);
        summaryList = todayReportFactory.getTodayTotalSummary();
    }
    public void loadGeneralReport() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                populateTable(generalReportTableCash, cashList, false);
                populateTable(generalReportTableLoan, loanList, false);
                populateTable(getGeneralReportTable, summaryList, true);

                // Enable the button on the UI thread
                downloadPdfButton.setEnabled(!cashList.isEmpty() || !loanList.isEmpty() || !summaryList.isEmpty());
            }
        });
    }


    private void populateTable(TableLayout tableLayout, List<BillSummary> billSummaryList, boolean isSummary) {

        double totalD=0;
        double totalP=0;


        for (BillSummary summary : billSummaryList) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TableRow row = (TableRow) inflater.inflate(R.layout.report_gentral_row, null);

            TextView itemName = row.findViewById(R.id.itemName);
            TextView subItemName = row.findViewById(R.id.subItemName);
            TextView totalQuantity = row.findViewById(R.id.totalQuantity);
            TextView totalDiscount = row.findViewById(R.id.totalDiscount);
            TextView itemTotal = row.findViewById(R.id.itemTotal);

            if (isSummary){
                subItemName.setVisibility(View.GONE);
            }

            itemName.setText(summary.getItemName());
            subItemName.setText(summary.getSubItemName());
            totalQuantity.setText(String.valueOf(summary.getTotalQuantity()));
            totalDiscount.setText(String.valueOf(summary.getTotalDiscount()));
            itemTotal.setText(String.format("%.2f", summary.getTotalPrice()));

            tableLayout.addView(row);

            totalP += summary.getTotalPrice();
            totalD += summary.getTotalDiscount();
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow row = (TableRow) inflater.inflate(R.layout.report_gentral_row, null);

        TextView itemName = row.findViewById(R.id.itemName);
        TextView subItemName = row.findViewById(R.id.subItemName);
        TextView totalQuantity = row.findViewById(R.id.totalQuantity);
        TextView totalDiscount = row.findViewById(R.id.totalDiscount);
        TextView itemTotal = row.findViewById(R.id.itemTotal);
        itemTotal.setTextSize(20);
        itemTotal.setTypeface(null, Typeface.BOLD);

        if (isSummary){
            subItemName.setVisibility(View.GONE);
        }

        itemName.setText("TOTAL");
        subItemName.setText("==");
        totalQuantity.setText("==");
        totalDiscount.setText(String.valueOf(totalD));
        itemTotal.setText(String.format("%.2f", totalP));
        tableLayout.addView(row);

    }

    private void downloadPdf() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadPdfButton.setEnabled(false); // Disable the button on the UI thread
            }
        });
        todayReportFactory.downloadTodayPdf();
    }

    private void addTodayLoanDetails(){

    }

}
