package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.BillAdapter;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.service.BillService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BillListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillService billService;
    private Button backButton;
    private EditText searchBar;

    private double total, totalCash, totalLoan;
    private TextView totalT, cashT, loanT;

    private AppConstant appConstant;
    private List<Bill> bills; // Keep the original list of bills

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar = findViewById(R.id.search_bar); // Initialize the search bar
        billService = new BillService(this);
        appConstant = new AppConstant(this);
        backButton = findViewById(R.id.backButton);
        loadBills();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CashierDashBoard.class);
            startActivity(intent);
        });

        // Set up search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBills(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadBills() {
        String currentDate = DateTimeUtils.getCurrentDate();
        bills = billService.getAllBillByDate(currentDate); // Keep the full list of bills
        total = 0;
        totalCash = 0;
        totalLoan = 0;

        if (bills.size() > 0) {
            Collections.reverse(bills);

            Iterator<Bill> iterator = bills.iterator();
            while (iterator.hasNext()) {
                Bill bill = iterator.next();
                if (bill.getStatus().equals(AppConstant.MODIFIED_ORIGINAL) || bill.getStatus().equals(AppConstant.DELETED)) {
                    iterator.remove(); // Safe removal
                } else {
                    total += bill.getTotalAmount();
                    if (bill.getPaymentMethod().equals(AppConstant.CASH)) {
                        totalCash += bill.getTotalAmount();
                    } else if (bill.getPaymentMethod().equals(AppConstant.LOAN)) {
                        totalLoan += bill.getTotalAmount();
                    }
                }
            }

            addNewRow(total, totalCash, totalLoan);

            if (bills.isEmpty()) {
                Toast.makeText(this, "No bills found for today", Toast.LENGTH_SHORT).show();
            } else {
                BillAdapter adapter = new BillAdapter(this, bills);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "No bills found", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterBills(String query) {
        List<Bill> filteredBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getReferenceNumber().toLowerCase().contains(query.toLowerCase())) {
                filteredBills.add(bill);
            }
        }

        if (!filteredBills.isEmpty()) {
            BillAdapter adapter = new BillAdapter(this, filteredBills);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(null); // Clear the adapter if no bills match
            Toast.makeText(this, "No matching bills found", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewRow(double total, double cash, double loan) {
        totalT.setText(appConstant.formatAmount(total));
        cashT.setText(appConstant.formatAmount(cash));
        loanT.setText(appConstant.formatAmount(loan));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CashierDashBoard.class);
        startActivity(intent);
    }
}
