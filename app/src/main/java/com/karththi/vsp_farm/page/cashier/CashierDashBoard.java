package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.model.Permission;
import com.karththi.vsp_farm.page.LoginActivity;
import com.karththi.vsp_farm.page.admin.CreateCustomerActivity;
import com.karththi.vsp_farm.page.admin.CustomerListActivity;
import com.karththi.vsp_farm.page.admin.GetLoanPaymentActivity;
import com.karththi.vsp_farm.page.admin.item.ItemListActivity;
import com.karththi.vsp_farm.page.admin.report.GetCustomerDetailReportActivity;
import com.karththi.vsp_farm.page.admin.report.ReportActivity;
import com.karththi.vsp_farm.repo.PermissionRepository;

import java.util.List;

public class CashierDashBoard extends AppCompatActivity {
    
    private AppConstant appConstant;
    
    private TextView userName;

    private LoadingDialog loadingDialog;

    private Button getDetailReportByCustomerButton;

    private Button viewReportButton,addCustomerButton,viewCustomerButton,viewItemsButton,viewLoanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_dashboard);
        appConstant = new AppConstant(this);
        userName = findViewById(R.id.userNameTextView);
        userName.setText(AppConstant.USER_NAME);
        loadingDialog = new LoadingDialog(this);
        viewReportButton = findViewById(R.id.viewReportButton);

        viewReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });

        addCustomerButton = findViewById(R.id.addCustomerButton);
        addCustomerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateCustomerActivity.class);
            startActivity(intent);
        });

        viewCustomerButton = findViewById(R.id.viewCustomerButton);
        viewCustomerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerListActivity.class);
            startActivity(intent);
        });

        viewItemsButton = findViewById(R.id.viewItemsButton);
        viewItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ItemListActivity.class);
            startActivity(intent);
        });

        if (AppConstant.ADD_CUSTOMER_PERMISSION){
            addCustomerButton.setEnabled(true);
        }else {
            addCustomerButton.setEnabled(false);
        }

        getDetailReportByCustomerButton = findViewById(R.id.getDetailReportByCustomerButton);
        getDetailReportByCustomerButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, GetCustomerDetailReportActivity.class);
            startActivity(intent);
        });

        if (AppConstant.GET_CUSTOMER_REPORT_PERMISSION) {
            getDetailReportByCustomerButton.setEnabled(true);
        } else {
            getDetailReportByCustomerButton.setEnabled(false);
        }

        viewLoanButton = findViewById(R.id.viewLoanButton);
        viewLoanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetLoanPaymentActivity.class);
            startActivity(intent);

        });

        if (AppConstant.VIEW_LOAN_PAYMENT_PERMISSION) {
            viewLoanButton.setEnabled(true);
        } else {
            viewLoanButton.setEnabled(false);
        }

    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        appConstant.ConfirmAlert("Logout", "Are you sure you want to logout?",() ->
        {
            startActivity(intent);
            finishAffinity();
            System.exit(0);
        });
    }

    public void openBillingPage(View view) {
        loadingDialog.show("Loading...");
        Intent intent = new Intent(this, BillingPageActivity.class);
        // Ensure it's opened as a new task, which will invoke onCreate
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void openBillHistory(View view){
        loadingDialog.show("Loading...");
        Intent intent = new Intent(this, BillListActivity.class);
        startActivity(intent);
    }

    public void payLoan(View view){
        loadingDialog.show("Loading...");
        Intent intent = new Intent(this, PayLoanActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.dismiss();
    }
}
