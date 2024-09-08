package com.karththi.vsp_farm.page.admin.report;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.facade.TodayReportFacade;
import com.karththi.vsp_farm.service.BillService;

public class TodayReportActivity extends AppCompatActivity {

    private BillService billService;

    private TodayReportFacade todayReportFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report);
        todayReportFacade = new TodayReportFacade(this);

    }

    public void loadGeneralReport() {
        // Load General Report
    }
}
