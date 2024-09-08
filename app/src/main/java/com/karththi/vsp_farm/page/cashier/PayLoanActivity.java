package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.LoanDto;
import com.karththi.vsp_farm.facade.LoanFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.service.CustomerService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PayLoanActivity extends AppCompatActivity {

    private CustomerService customerService;
    private LoanFacade loanFacade;
    private Spinner customerDropdown;
    private TextView remainingLoanAmount;
    private TextView lastPaymentAmount;
    private TextView lastPaymentDate;
    private EditText paymentAmountInput;
    private Button payLoanButton;
    private View loanStatusSection;

    private LoanDto loanDto;

    private List<String> customerNames = new ArrayList<>();

    private List<Customer> customerList = new ArrayList<>();

    private int CUSTOMER_ID = 0;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_loan);

        customerService = new CustomerService(this);
        loanFacade = new LoanFacade(this);

        customerDropdown = findViewById(R.id.customerDropdown);
        remainingLoanAmount = findViewById(R.id.remainingLoanAmount);
        lastPaymentAmount = findViewById(R.id.lastPaymentAmount);
        lastPaymentDate = findViewById(R.id.lastPaymentDate);
        paymentAmountInput = findViewById(R.id.paymentAmountInput);
        payLoanButton = findViewById(R.id.payLoanButton);
        loanStatusSection = findViewById(R.id.loanStatusSection);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CashierDashBoard.class);
            startActivity(intent);
        });


        loadCustomers();
        setupListeners();
    }

    private void loadCustomers() {
        customerList = customerService.getAllCustomers();
        if (customerList.isEmpty()) {
            Toast.makeText(this, "No customers found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add default "Select Customer" option
        customerNames.add("Select Customer");

        Iterator<Customer> iterator = customerList.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getName().equals(AppConstant.DEFAULT)) {
                iterator.remove();
            } else {
                customerNames.add(customer.getName());
            }
        }
        ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerNames);
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerDropdown.setAdapter(customerAdapter);
    }

    private void setupListeners() {
        customerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCustomer = (String) adapterView.getItemAtPosition(position);
                if (selectedCustomer != null && !selectedCustomer.equals("Select Customer")) {
                    int customerId = findByName(selectedCustomer).getId();
                    if (customerId != 0) {
                        CUSTOMER_ID = customerId;
                        loanDto = loanFacade.getLoanDtoByCustomerId(customerId);

                        if (loanDto != null && loanDto.getLoan() != null) {
                            remainingLoanAmount.setText(String.valueOf(loanDto.getLoan().getRemainingAmount()));

                            LoanPayment lastPayment = loanDto.getLastPayment();
                            if (lastPayment != null) {
                                lastPaymentAmount.setText(String.valueOf(lastPayment.getPaymentAmount()));
                                lastPaymentDate.setText(lastPayment.getPaymentDate());
                            } else {
                                lastPaymentAmount.setText("N/A");
                                lastPaymentDate.setText("N/A");
                            }

                            loanStatusSection.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(PayLoanActivity.this, "No loan found for the selected customer.", Toast.LENGTH_SHORT).show();
                            loanStatusSection.setVisibility(View.GONE);
                        }
                    }
                } else {
                    loanStatusSection.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                loanStatusSection.setVisibility(View.GONE);
            }
        });

        payLoanButton.setOnClickListener(v -> processLoanPayment());
    }

    private void loadLoanStatus(int customerId) {
        // Fetch loan details for the selected customer
        loanDto = loanFacade.getLoanDtoByCustomerId(customerId);

        if (loanDto != null && loanDto.getLoan() != null) {
            remainingLoanAmount.setText(String.valueOf(loanDto.getLoan().getRemainingAmount()));

            LoanPayment lastPayment = loanDto.getLastPayment();
            if (lastPayment != null) {
                lastPaymentAmount.setText(String.valueOf(lastPayment.getPaymentAmount()));
                lastPaymentDate.setText(lastPayment.getPaymentDate());
            } else {
                lastPaymentAmount.setText("N/A");
                lastPaymentDate.setText("N/A");
            }

            loanStatusSection.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "No loan found for the selected customer.", Toast.LENGTH_SHORT).show();
            loanStatusSection.setVisibility(View.GONE);
        }
    }

    private void processLoanPayment() {
        String paymentAmountStr = paymentAmountInput.getText().toString().trim();
        if (paymentAmountStr.isEmpty()) {
            Toast.makeText(this, "Please enter a payment amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        double paymentAmount = Double.parseDouble(paymentAmountStr);
        if (CUSTOMER_ID != 0) {
            loanFacade.handleLoanPayment(CUSTOMER_ID, paymentAmount);
            Toast.makeText(this, "Payment processed successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a customer.", Toast.LENGTH_SHORT).show();
        }
    }

    private Customer findByName(String name){
        for (Customer customer : customerList) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }

}
