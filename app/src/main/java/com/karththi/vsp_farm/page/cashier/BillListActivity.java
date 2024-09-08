package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BillListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillService billService;

    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        billService = new BillService(this);
        backButton = findViewById(R.id.backButton);
        loadBills();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CashierDashBoard.class);
            startActivity(intent);
        });

    }

    private void loadBills() {
        String currentDate = DateTimeUtils.getCurrentDate();
        List<Bill> bills = billService.getAllBillByDate(currentDate);

        if (bills.size() > 0) {
            // Reverse the list
            Collections.reverse(bills);

            // Use an iterator to safely remove bills with unwanted statuses
            Iterator<Bill> iterator = bills.iterator();
            while (iterator.hasNext()) {
                Bill bill = iterator.next();
                if (bill.getStatus().equals(AppConstant.MODIFIED_ORIGINAL) || bill.getStatus().equals(AppConstant.DELETED)) {
                    iterator.remove(); // Safe removal
                }
            }

            // Check if the list is now empty after removing bills
            if (bills.isEmpty()) {
                Toast.makeText(this, "No bills found for today", Toast.LENGTH_SHORT).show();
            } else {
                // Set the adapter with the remaining bills
                BillAdapter adapter = new BillAdapter(this, bills);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "No bills found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CashierDashBoard.class);
        startActivity(intent);
    }
}