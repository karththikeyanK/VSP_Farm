package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.page.LoginActivity;

public class CashierDashBoard extends AppCompatActivity {
    
    private AppConstant appConstant;
    
    private TextView userName;

    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_dashboard);
        appConstant = new AppConstant(this);
        userName = findViewById(R.id.userNameTextView);
        userName.setText(AppConstant.USER_NAME);
        loadingDialog = new LoadingDialog(this);
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        appConstant.ConfirmAlert("Logout", "Are you sure you want to logout?",() ->startActivity(intent) );
    }

    public void openBillingPage(View view){
        loadingDialog.show("Loading...");
        Intent intent = new Intent(this, BillingPageActivity.class);
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
