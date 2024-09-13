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

//    private void downloadPdf(){
//        CreateReport report = new CreateReport();
//        report.startPage(595, 842); // A4 size
//
//// Add header
//        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
//
//// Add dynamic report title and date
//        report.addReportTitle("Daily Sales Report", "12-09-2024");
//
//// Add table headers
//        String[] headers = {"Item", "Total Quantity", "Total Discount", "Total Amount"};
//        int[] colWidths = {150, 100, 100, 150};
//        report.addTableHeader(headers, colWidths);
//
//// Add table rows dynamically
//        String[] row1 = {"Rice", "100", "10", "9000"};
//        report.addTableRow(row1, colWidths);
//
//// Finish and save the PDF
//        report.finishReport("VSP_Farm_Report.pdf");
//
//    }
//
    private void downloadPdf() {
        CreateReport report = new CreateReport();
        report.startPage(595, 842); // A4 size

        report.addCenteredHeader("VSP FARM", "Theniyambai, Valvettithurai, Sri Lanka", "077 023 8493");
        report.addReportTitle("Daily Sales Report", "12-09-2024");
        report.addTableHeading("Summary");
        String[] summary_headers = {"Item", "Quantity", "Discount", "Total"};
        int[] summary_col_width = {150, 100, 100, 150};
        report.addTableHeader(summary_headers,summary_col_width);

        for (BillSummary summary : todayReportFacade.getTodayTotalSummary()) {
            String[] row = {
                    summary.getItemName(),
                    String.valueOf(summary.getTotalQuantity()),
                    String.valueOf(summary.getTotalDiscount()),
                    String.format("%.2f", summary.getTotalPrice())
            };
            report.addTableRow(row,summary_col_width);
        }

        // Add TOTAL row
        double totalPrice = 0;
        double totalDiscount = 0;

        List<BillSummary> summaryList = todayReportFacade.getTodayTotalSummary();
        for (BillSummary summary : summaryList) {
            totalPrice += summary.getTotalPrice();
            totalDiscount += summary.getTotalDiscount();
        }

        String[] summary_total = {"TOTAL", "==", String.valueOf(totalDiscount), String.format("%.2f", totalPrice)};
        report.addTableRow(summary_total, summary_col_width);

        report.addTableHeading("Cash Sales");
        String[] detail_header = {"Item", "Sub Item", "Quantity", "Discount", "Total"};
        int[] detail_col_width = {100, 150, 80, 80, 120};
        report.addTableHeader(detail_header, detail_col_width);

        for (BillSummary cashSummary : todayReportFacade.getTodaySummary(AppConstant.CASH)) {
            String[] row = {
                    cashSummary.getItemName(),
                    cashSummary.getSubItemName(),
                    String.valueOf(cashSummary.getTotalQuantity()),
                    String.valueOf(cashSummary.getTotalDiscount()),
                    String.valueOf(cashSummary.getTotalPrice())
            };
            report.addTableRow(row, detail_col_width);

        }

        report.addTableHeading("Loan Sales");


        report.addTableHeader( detail_header, detail_col_width);


        for (BillSummary loanSummary : todayReportFacade.getTodaySummary(AppConstant.LOAN)) {
            String[] row = {
                    loanSummary.getItemName(),
                    loanSummary.getSubItemName(),
                    String.valueOf(loanSummary.getTotalQuantity()),
                    String.valueOf(loanSummary.getTotalDiscount()),
                    String.valueOf(loanSummary.getTotalPrice())
            };
            report.addTableRow(row, detail_col_width);
        }

        String fileName = "VSP_Farm_Report_" + System.currentTimeMillis() + ".pdf";
        report.finishReport(fileName);
    }

}
