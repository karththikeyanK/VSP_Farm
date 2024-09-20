package com.karththi.vsp_farm.page.admin.report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.Factory.ReportFactory;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.Sale;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetSummaryReportActivity extends AppCompatActivity {


    private Button date1Button, date2Button, downloadPdfButton;
    private Calendar date1 = null;
    private Calendar date2 = null;

    private AppConstant appConstant;

    private TableLayout generalReportTable,detailByDateTable,byDateDetailTable,subItemSummaryTable,payment_summary_table,payment_detail_table;

    private BillService billService;

    private ReportFactory reportFactory;
    private List<BillSummary> summaryList;
    private List<BillSummary> subItemSummary;
    private List<BillSummary> summaryListByDate;
    private List<BillSummary> subItemSummaryByDate;
    private List<Sale> saleList;

    private LoadingDialog loadingDialog;

    private ExecutorService executorService;
    private Handler mainHandler;




    private double sum_total = 0, sum_cash = 0, sum_loan = 0, sum_delete = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_report);

        appConstant = new AppConstant(this);
        billService = new BillService(this);
        reportFactory = new ReportFactory(this);
        loadingDialog = new LoadingDialog(this);

        date1Button = findViewById(R.id.date1Button);
        date2Button = findViewById(R.id.date2Button);

        generalReportTable = findViewById(R.id.generalReportTable);
        detailByDateTable = findViewById(R.id.detailByDateTable);
        byDateDetailTable = findViewById(R.id.byDateDetailTable);
        subItemSummaryTable = findViewById(R.id.subItemSummaryTable);
        payment_summary_table = findViewById(R.id.payment_summary_table);
        payment_detail_table = findViewById(R.id.payment_detail_table);

        saleList = new ArrayList<>();
        summaryList = new ArrayList<>();
        subItemSummary = new ArrayList<>();
        summaryListByDate = new ArrayList<>();
        subItemSummaryByDate = new ArrayList<>();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        date1Button.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        date1 = Calendar.getInstance();
                        date1.set(selectedYear, selectedMonth, selectedDay);

                        // Format date to "yyyy-MM-dd" and set the text on the button
                        String formattedDate = dateFormat.format(date1.getTime());
                        date1Button.setText(formattedDate);

                        checkIfBothDatesSelected();
                    }, year, month, day);
            datePickerDialog.show();
        });

        date2Button.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        date2 = Calendar.getInstance();
                        date2.set(selectedYear, selectedMonth, selectedDay);

                        // Format date to "yyyy-MM-dd" and set the text on the button
                        String formattedDate = dateFormat.format(date2.getTime());
                        date2Button.setText(formattedDate);

                        checkIfBothDatesSelected();
                    }, year, month, day);
            datePickerDialog.show();
        });

        date2 = Calendar.getInstance();
        String todayDateString = dateFormat.format(date2.getTime());
        date2Button.setText(todayDateString);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(getMainLooper());

        downloadPdfButton = findViewById(R.id.downloadPdfButton);
        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        reportFactory.downloadReportByDateRangePdf(
                            date1Button.getText().toString(),date2Button.getText().toString()
                            ,saleList,
                            summaryList,
                            subItemSummary,
                            summaryListByDate,
                            subItemSummaryByDate
                        );
                    }
                });
            }
        });

    }




    private void checkIfBothDatesSelected() {
        if (date1 != null && date2 != null) {
            // Show loading dialog
            loadingDialog.show("Loading...");

            // Run loadGeneralReport in a background thread
            executorService.execute(() -> {
                loadGeneralReportInBackground();

                // Once complete, update the UI on the main thread
                mainHandler.post(() -> {
                    loadGeneralReport();
                    loadingDialog.dismiss(); // Hide loading dialog
                });
            });
        }
    }

    private void loadGeneralReportInBackground() {
        summaryList = billService.getSummaryByDateRange(date1Button.getText().toString(), date2Button.getText().toString());
        subItemSummary = billService.getSubItemSummaryByDateRange(date1Button.getText().toString(), date2Button.getText().toString());
        summaryListByDate = billService.getDetailedSummaryByDateRange(date1Button.getText().toString(), date2Button.getText().toString());
        subItemSummaryByDate = billService.getSubItemDetailSummaryByDateRange(date1Button.getText().toString(), date2Button.getText().toString());
        saleList = billService.getSalesByDateRange(date1Button.getText().toString(), date2Button.getText().toString());
    }


    public void loadGeneralReport() {
        clearTableRows(generalReportTable);
        clearTableRows(detailByDateTable);
        clearTableRows(byDateDetailTable);
        clearTableRows(subItemSummaryTable);
        payment_summary_table.removeAllViews();
        payment_detail_table.removeAllViews();


        if (summaryList.isEmpty()){
            Toast.makeText(this, "No bills found ", Toast.LENGTH_SHORT).show();
            return;
        }
        populateTable(generalReportTable, summaryList,true,false);

        populateTable(subItemSummaryTable, subItemSummary,false,false);

        populateTable(detailByDateTable, summaryListByDate,true,true);

        populateTable(byDateDetailTable, subItemSummaryByDate,false,true);
        populatePaymentTables(payment_summary_table,payment_detail_table, saleList);

    }

    private void populatePaymentTables(TableLayout payment_summary_table, TableLayout payment_detail_table, List<Sale> list) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Add header for details table
        TableRow header = (TableRow) inflater.inflate(R.layout.payment_header, null);
        payment_detail_table.addView(header);

        // Reset sums to 0 before calculating
        sum_total = 0;
        sum_cash = 0;
        sum_loan = 0;
        sum_delete = 0;

        // Populate the detail table and calculate totals
        for (Sale sale : list) {
            TableRow row = (TableRow) inflater.inflate(R.layout.payment_row, null);
            payment_detail_table.addView(populatePaymentRow(row, sale, false));  // Not summary row
        }

        // Set up the summary table
        LayoutInflater sum_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow summary_header = (TableRow) sum_inflater.inflate(R.layout.payment_header, null);

        // Hide date column in the summary
        TextView payment_date = summary_header.findViewById(R.id.payment_date);
        payment_date.setVisibility(View.GONE);
        payment_summary_table.addView(summary_header);

        // Create Sales object for summary row with calculated totals
        Sale sumSale = new Sale();
        sumSale.setCash(sum_cash);
        sumSale.setLoan(sum_loan);
        sumSale.setDelete(sum_delete);

        // Add the summary row with totals
        TableRow sum_row = (TableRow) sum_inflater.inflate(R.layout.payment_row, null);
        payment_summary_table.addView(populatePaymentRow(sum_row, sumSale, true));
    }

    private TableRow populatePaymentRow(TableRow row, Sale sale, boolean isSummary) {
        TextView summary_date = row.findViewById(R.id.summary_date);
        TextView total = row.findViewById(R.id.total);
        TextView cash = row.findViewById(R.id.cash);
        TextView loan = row.findViewById(R.id.loan);
        TextView delete = row.findViewById(R.id.delete);

        if (isSummary) {
            // Hide the date for summary row
            summary_date.setVisibility(View.GONE);
        } else {
            // Accumulate totals for cash, loan, and delete
            sum_total += sale.getCash() + sale.getLoan();
            sum_cash += sale.getCash();
            sum_loan += sale.getLoan();
            sum_delete += sale.getDelete();
        }

        // Set row values
        summary_date.setText(sale.getDate());
        total.setText(appConstant.formatAmount(sale.getCash() + sale.getLoan()));
        cash.setText(appConstant.formatAmount(sale.getCash()));
        loan.setText(appConstant.formatAmount(sale.getLoan()));
        delete.setText(appConstant.formatAmount(sale.getDelete()));

        return row;
    }


    private void populateTable(TableLayout tableLayout, List<BillSummary> billSummaryList, boolean isSummary,boolean byDate) {

        double totalD=0;
        double totalP=0;


        for (BillSummary summary : billSummaryList) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TableRow row = (TableRow) inflater.inflate(R.layout.report_gentral_row, null);

            TextView date =  row.findViewById(R.id.date);
            TextView itemName = row.findViewById(R.id.itemName);
            TextView subItemName = row.findViewById(R.id.subItemName);
            TextView totalQuantity = row.findViewById(R.id.totalQuantity);
            TextView totalDiscount = row.findViewById(R.id.totalDiscount);
            TextView itemTotal = row.findViewById(R.id.itemTotal);

            if (isSummary){
                subItemName.setVisibility(View.GONE);
            }
            if(byDate){
                date.setVisibility(View.VISIBLE);
            }

            date.setText(summary.getDate());
            itemName.setText(summary.getItemName());
            subItemName.setText(summary.getSubItemName());
            totalQuantity.setText(String.valueOf(summary.getTotalQuantity()));
            totalDiscount.setText(String.valueOf(appConstant.formatAmount(summary.getTotalDiscount())));
            itemTotal.setText(appConstant.formatAmount(summary.getTotalPrice()));

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
        totalDiscount.setText(appConstant.formatAmount(totalD));
        itemTotal.setText(appConstant.formatAmount(totalP));
        tableLayout.addView(row);

    }

    private void clearTableRows(TableLayout tableLayout) {
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) { // Check if there are more than just the header
            for (int i = childCount - 1; i > 0; i--) { // Start from the end to avoid index issues
                tableLayout.removeViewAt(i);
            }
        }
    }


}
