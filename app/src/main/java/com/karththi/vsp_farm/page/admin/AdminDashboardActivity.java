package com.karththi.vsp_farm.page.admin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.page.CreateUserActivity;
import com.karththi.vsp_farm.page.LoginActivity;
import com.karththi.vsp_farm.page.admin.item.ItemListActivity;
import com.karththi.vsp_farm.page.admin.report.ReportActivity;
import com.karththi.vsp_farm.page.admin.report.ViewBillActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private AppConstant appConstant;

    private TextView userName;

    private Button viewReportButton,viewBillButton,viewLoanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Example: Display a welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome to the Admin Dashboard!");
        appConstant = new AppConstant(this);
        userName = findViewById(R.id.userNameTextView);
        userName.setText(AppConstant.USER_NAME);
        viewReportButton = findViewById(R.id.viewReportButton);
        viewBillButton = findViewById(R.id.viewBillButton);
        viewReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });

        viewBillButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewBillActivity.class);
            startActivity(intent);
        });

        viewLoanButton = findViewById(R.id.viewLoanButton);
        viewLoanButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LoanActivity.class);
//            startActivity(intent);
        });

    }


    public void userAction(View view){
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::userActions():: is started");
        Intent intent = new Intent(this, UserActionActivity.class);
        startActivity(intent);
    }

    public void viewItems(View view) {
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::viewItems():: is started");
        Intent intent = new Intent(this, ItemListActivity.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        appConstant.ConfirmAlert("Logout", "Are you sure you want to logout?",() ->startActivity(intent) );
    }

}
