package com.karththi.vsp_farm.page.admin.report;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.facade.TodayReportFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.pdf.CreateReport;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.service.BillService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.PrimitiveIterator;

public class TodayReportActivity extends AppCompatActivity {

    private TodayReportFacade todayReportFacade;

    private TableLayout generalReportTableCash;
    private TableLayout generalReportTableLoan;

    private TableLayout getGeneralReportTable;

    private TextView reportDate;

    private Button downloadPdfButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);

        todayReportFacade = new TodayReportFacade(this);

        reportDate = findViewById(R.id.reportDate);

        reportDate.setText("Date - "+ DateTimeUtils.getCurrentDate()+" "+DateTimeUtils.getCurrentTime());


        generalReportTableCash = findViewById(R.id.generalReportTableCash);
        generalReportTableLoan = findViewById(R.id.generalReportTableLoan);
        getGeneralReportTable = findViewById(R.id.generalReportTable);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);

        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf();
            }
        });

        loadGeneralReport();
    }

    public void loadGeneralReport() {
        List<BillSummary> cashList = todayReportFacade.getTodaySummary(AppConstant.CASH);
        populateTable(generalReportTableCash, cashList,false);

        List<BillSummary> loanList = todayReportFacade.getTodaySummary(AppConstant.LOAN);
        populateTable(generalReportTableLoan, loanList,false);

        List<BillSummary> summaryList = todayReportFacade.getTodayTotalSummary();
        populateTable(getGeneralReportTable, summaryList,true);
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
        downloadPdfButton.setEnabled(false);
        todayReportFacade.downloadTodayPdf();
    }

    private void addTodayLoanDetails(){

    }

}
