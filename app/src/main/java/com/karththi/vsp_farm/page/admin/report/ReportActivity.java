package com.karththi.vsp_farm.page.admin.report;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.page.admin.AdminDashboardActivity;

public class ReportActivity extends AppCompatActivity {

    private Button backButton,todayReportButton,todayDetailReportButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
        });

        todayReportButton = findViewById(R.id.todayReportButton);
        todayReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TodayReportActivity.class);
            startActivity(intent);
        });

        todayDetailReportButton = findViewById(R.id.todayDetailReportButton);
        todayDetailReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TodayDetailReportActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}
