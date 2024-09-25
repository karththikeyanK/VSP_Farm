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
import com.karththi.vsp_farm.page.LoginActivity;
import com.karththi.vsp_farm.page.cashier.CashierDashBoard;
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
            Intent intent = new Intent(this, UserActionActivity.class);
            startActivity(intent);
        });

        userNameTextView = findViewById(R.id.userNameTextView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.USER_ROLE.equals(AppConstant.ADMIN)){
                    Intent intent = new Intent(CustomerListActivity.this, UserActionActivity.class);
                    startActivity(intent);
                }else if (AppConstant.USER_ROLE.equals(AppConstant.CASHIER)) {
                    Intent intent = new Intent(CustomerListActivity.this, CashierDashBoard.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(CustomerListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        userNameTextView.setText(AppConstant.USER_NAME);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(AppConstant.USER_ROLE.equals(AppConstant.ADMIN)){
            Intent intent = new Intent(CustomerListActivity.this, UserActionActivity.class);
            startActivity(intent);
        }else if (AppConstant.USER_ROLE.equals(AppConstant.CASHIER)) {
            Intent intent = new Intent(CustomerListActivity.this, CashierDashBoard.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(CustomerListActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
