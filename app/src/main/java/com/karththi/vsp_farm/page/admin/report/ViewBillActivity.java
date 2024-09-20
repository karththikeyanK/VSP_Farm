package com.karththi.vsp_farm.page.admin.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.BillAdapter;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.page.admin.AdminDashboardActivity;
import com.karththi.vsp_farm.service.BillService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ViewBillActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillService billService;

    private Button backButton;

    private Button date1Button, date2Button;
    private Calendar date1 = null;
    private Calendar date2 = null;

    private Button allButton,cashButton,loanButton,deletedButton;
    private List<Bill> allBills;
    private List<Bill> cashBills;
    private List<Bill> loanBills;
    private List<Bill> deletedBills;

    private TableLayout tableLayout;
    private double total, totalCash, totalLoan, totalDeleted;

    private TextView totalT,cashT,loanT,deleteT;

    private AppConstant appConstant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill_list_admin);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        billService = new BillService(this);
        appConstant = new AppConstant(this);
        backButton = findViewById(R.id.backButton);
        deletedBills = new ArrayList<>();
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
        });


        date1Button = findViewById(R.id.date1Button);
        date2Button = findViewById(R.id.date2Button);
        allButton = findViewById(R.id.allButton);
        cashButton = findViewById(R.id.cashButton);
        loanButton = findViewById(R.id.loanButton);
        deletedButton = findViewById(R.id.deletedButton);
        tableLayout = findViewById(R.id.tableLayout);
        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);
        deleteT = findViewById(R.id.delete);

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
        initialFilterClicks();
    }


    private void loadBills() {
        allBills = new ArrayList<>();
        cashBills = new ArrayList<>();
        loanBills = new ArrayList<>();
        deletedBills = new ArrayList<>();
        
        List<Bill> bills = billService.getBillsBetweenDates(
                date1Button.getText().toString(),
                date2Button.getText().toString()
        );

        if (bills.size() > 0) {
            // Reverse the list
            Collections.reverse(bills);

            double total = 0;
            double totalDeleted = 0;
            double totalCash = 0;
            double totalLoan = 0;

            // Iterate over the list once
            for (Bill bill : bills) {
                if (bill.getStatus().equals(AppConstant.MODIFIED_ORIGINAL) || bill.getStatus().equals(AppConstant.DELETED)) {
                    deletedBills.add(bill);
                    totalDeleted += bill.getTotalAmount();
                } else {
                    allBills.add(bill);
                    total += bill.getTotalAmount();

                    if (bill.getPaymentMethod().equals(AppConstant.CASH)) {
                        cashBills.add(bill);
                        totalCash += bill.getTotalAmount();
                    } else if (bill.getPaymentMethod().equals(AppConstant.LOAN)) {
                        loanBills.add(bill);
                        totalLoan += bill.getTotalAmount();
                    }
                }
            }


            addNewRow(total,totalCash,totalLoan,totalDeleted);

            // Check if the list is now empty after removing bills
            if (bills.isEmpty()) {
                Toast.makeText(this, "No bills found ", Toast.LENGTH_SHORT).show();
            } else {
                // Set the adapter with the remaining bills
                BillAdapter adapter = new BillAdapter(this, allBills);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "No bills found", Toast.LENGTH_SHORT).show();
        }
    }


    private void filterBills(List<Bill> filterBills){
        if (filterBills.isEmpty()) {
            Toast.makeText(this, "No bills found ", Toast.LENGTH_SHORT).show();
        } else {
            BillAdapter adapter = new BillAdapter(this, filterBills);
            recyclerView.setAdapter(adapter);
        }
    }

    private void checkIfBothDatesSelected() {
        if (date1 != null && date2 != null) {
           loadBills();
        }
    }

    public void initialFilterClicks(){
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBills(allBills);
            }
        });

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBills(cashBills);
            }
        });

        loanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBills(loanBills);
            }
        });

        deletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBills(deletedBills);
            }
        });


    }


    private void addNewRow(double total, double cash, double loan, double deleted) {
        totalT.setText(appConstant.formatAmount(total));
        cashT.setText(appConstant.formatAmount(cash));
        loanT.setText(appConstant.formatAmount(loan));
        deleteT.setText(appConstant.formatAmount(deleted));
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }
}
