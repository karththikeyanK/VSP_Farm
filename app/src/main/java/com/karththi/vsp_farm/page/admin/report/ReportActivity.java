package com.karththi.vsp_farm.page.admin.report;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.page.admin.AdminDashboardActivity;

public class ReportActivity extends AppCompatActivity {

    private Button backButton,todayReportButton,todayDetailReportButton,getSummaryReportButton,getDetailReportButton;

    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        loadingDialog = new LoadingDialog(this);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
        });

        todayReportButton = findViewById(R.id.todayReportButton);
        todayReportButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, TodayReportActivity.class);
            startActivity(intent);
        });

        todayDetailReportButton = findViewById(R.id.todayDetailReportButton);
        todayDetailReportButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, TodayDetailReportActivity.class);
            startActivity(intent);
        });

        getSummaryReportButton = findViewById(R.id.getSummaryReportButton);
        getSummaryReportButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, GetSummaryReportActivity.class);
            startActivity(intent);
        });

        getDetailReportButton = findViewById(R.id.getDetailReportButton);
        getDetailReportButton.setOnClickListener(v -> {
            loadingDialog.show("Loading...");
            Intent intent = new Intent(this, GetDetailReportActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
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
