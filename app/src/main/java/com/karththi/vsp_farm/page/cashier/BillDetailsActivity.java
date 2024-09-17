package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.facade.BillFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.BillItemAdapter;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;

import java.util.ArrayList;
import java.util.List;

public class BillDetailsActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private List<BillItem> billItems;
    private BillItemAdapter billItemAdapter;

    private BillItemService billItemService;

    private ImageView deleteButton;

    private Button backButton;

    private AppConstant appConstant;

    private BillService billService;

    private BillFacade billFacade;

    TextView referenceNumber, totalAmount, customerName, dateTime, paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        tableLayout = findViewById(R.id.bill_items_table);

        billItemService = new BillItemService(this);
        appConstant = new AppConstant(this);
        billService = new BillService(this);
        billFacade = new BillFacade(this);

        int billId = getIntent().getIntExtra("bill_id", -1);

        // Dummy data
        billItems = new ArrayList<>();
        billItems =  billItemService.getBillItemByBillId(billId);

        // Use BillItemAdapter to populate the table
        billItemAdapter = new BillItemAdapter(this, tableLayout, billItems);
        billItemAdapter.populateTable();

        // Set the bill details
        referenceNumber = findViewById(R.id.reference_number);
        totalAmount = findViewById(R.id.total_amount);
        customerName = findViewById(R.id.customer_name);
        dateTime = findViewById(R.id.date_time);
        paymentMethod = findViewById(R.id.paymentMethod);

        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteIcon);
        backButton.setOnClickListener(v -> {
            finish();
        });
        deleteButton.setOnClickListener(v -> {
            appConstant.ConfirmAlert(
                    "Are you sure you want to delete this bill?",
                    "This action cannot be undone",
                    () -> billFacade.deleteBill(billId)
            );
        });

        String r_referenceNumber, r_totalAmount, r_customerName, r_dateTime, r_paymentMethod;
        r_referenceNumber = getIntent().getStringExtra("reference_number");
        r_totalAmount = getIntent().getStringExtra("total_amount");
        r_customerName = getIntent().getStringExtra("customer_name");
        r_dateTime = getIntent().getStringExtra("created_date");
        r_paymentMethod = getIntent().getStringExtra("payment_method");

        referenceNumber.setText(r_referenceNumber);
        totalAmount.setText(r_totalAmount +" LKR");
        customerName.setText(r_customerName);
        dateTime.setText(r_dateTime);
        paymentMethod.setText(r_paymentMethod);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


