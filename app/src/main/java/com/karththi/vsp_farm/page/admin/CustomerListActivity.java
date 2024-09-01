package com.karththi.vsp_farm.page.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.CustomerAdapter;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.service.CustomerService;

import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    private ListView customerListView;
    private List<Customer> customerList;
    private Button addNewCustomerButton,backButton;

    private TextView userNameTextView;

    private CustomerService customerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        customerListView = findViewById(R.id.customerListView);
        addNewCustomerButton = findViewById(R.id.addNewCustomerButton);

        customerService = new CustomerService(this);
        customerList = customerService.getAllCustomers();
        if (customerList != null && !customerList.isEmpty()) {
            CustomerAdapter adapter = new CustomerAdapter(this, R.layout.customer_list_row, customerList);
            customerListView.setAdapter(adapter);
        }

        backButton = findViewById(R.id.backButton);

        // Add new customer button logic
        addNewCustomerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateCustomerActivity.class);
            startActivity(intent);
        });

        userNameTextView = findViewById(R.id.userNameTextView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userNameTextView.setText(AppConstant.USER_NAME);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }
}
