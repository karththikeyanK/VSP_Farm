package com.karththi.vsp_farm.page.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.service.CustomerService;

public class EditCustomerActivity extends AppCompatActivity {

    private CustomerService customerService ;

    EditText nameEditText, mobileEditText, descriptionEditText;

    Button saveButton,CancelButton,backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        // Get customer id from intent
        int customerId = getIntent().getIntExtra("customerId", 0);
        customerService = new CustomerService(this);

        // Find views
        nameEditText = findViewById(R.id.etCustomerName);
        mobileEditText = findViewById(R.id.etCustomerMobile);
        descriptionEditText = findViewById(R.id.etCustomerDescription);
        saveButton = findViewById(R.id.btnSave);

        // Set data to views
        Customer customer = customerService.getCustomer(customerId);
        nameEditText.setText(customer.getName());
        mobileEditText.setText(customer.getMobile());
        descriptionEditText.setText(customer.getDescription());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Set click listener
        saveButton.setOnClickListener(v -> {
            // Handle save
            String name = nameEditText.getText().toString();
            String mobile = mobileEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            customerService.updateCustomer(customerId, name, description, mobile);
        });

        CancelButton = findViewById(R.id.btnCancel);
        CancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }
}
