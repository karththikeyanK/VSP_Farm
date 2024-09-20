package com.karththi.vsp_farm.page.admin.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.Factory.TodayReportFactory;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.DetailReportAdapter;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.service.BillItemService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDetailReportActivity extends AppCompatActivity {

    private Button downloadPdfButton, date1Button, date2Button;
    private BillItemService billItemService;
    private List<BillItemsDetailDto> billItemsDetailDtoList, deletedBills;
    private double total, cash, loan, deleteTotal;
    private TableLayout tableLayout;
    private TextView totalT, cashT, loanT, deleteT;
    private Calendar date1, date2;
    private LoadingDialog loadingDialog;
    private SimpleDateFormat dateFormat;

    private AppConstant appConstant;
    private TodayReportFactory todayReportFactory;

    private ExecutorService executorService;

    private RecyclerView detailReportRecyclerView;
    private RecyclerView detailReportDeleteRecyclerView;
    private DetailReportAdapter detailReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);

        appConstant = new AppConstant(this);
        todayReportFactory = new TodayReportFactory(this);

        initViews();
        setupDatePickers();
        setupDownloadButton();

        billItemsDetailDtoList = new ArrayList<>();
        deletedBills = new ArrayList<>();
        downloadPdfButton.setEnabled(false);

        executorService = Executors.newSingleThreadExecutor();

        detailReportRecyclerView = findViewById(R.id.detailReportRecyclerView);
        detailReportDeleteRecyclerView = findViewById(R.id.deleteDetailReportRecyclerView);

        detailReportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailReportDeleteRecyclerView.setLayoutManager(new LinearLayoutManager(this));





    }

    private void initViews() {
        billItemService = new BillItemService(this);
        loadingDialog = new LoadingDialog(this);

        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);
        deleteT = findViewById(R.id.delete);

        date1Button = findViewById(R.id.date1Button);
        date2Button = findViewById(R.id.date2Button);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date2 = Calendar.getInstance();
        date2Button.setText(dateFormat.format(date2.getTime()));
    }
    private void setupDatePickers() {
        date1Button.setOnClickListener(v -> showDatePicker((date) -> {
            date1 = date;
            date1Button.setText(dateFormat.format(date1.getTime()));
            checkIfBothDatesSelected();
        }));

        date2Button.setOnClickListener(v -> showDatePicker((date) -> {
            date2 = date;
            date2Button.setText(dateFormat.format(date2.getTime()));
            checkIfBothDatesSelected();
        }));
    }

    private void showDatePicker(DateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            listener.onDateSet(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setupDownloadButton() {
        downloadPdfButton.setOnClickListener(v ->
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadPdf();
                    }
                })
        );
    }

    private void checkIfBothDatesSelected() {
        if (date1 != null && date2 != null) {
            loadingDialog.show("Loading....");
            new Handler().post(() -> {
                resetReport(); // Move the reset code here after showing dialog
                loadBills();
            });
        }
    }

    private void resetReport() {
        total = cash = loan = deleteTotal = 0;
        billItemsDetailDtoList.clear();
        deletedBills.clear();
    }

    private void loadBills() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            billItemsDetailDtoList = billItemService.getAllBillDtoByDateRange(
                    date1Button.getText().toString(), date2Button.getText().toString()
            );

            handler.post(() -> {
                updateView();
                loadingDialog.dismiss();
            });
        });
    }

    private void updateView() {
        downloadPdfButton.setEnabled(true);
        if (billItemsDetailDtoList.isEmpty()) {
            Toast.makeText(this, "No Bills found", Toast.LENGTH_SHORT).show();
            return;
        }

        processBillData();
        detailReportAdapter = new DetailReportAdapter(billItemsDetailDtoList);
        detailReportRecyclerView.setAdapter(detailReportAdapter);

        detailReportAdapter = new DetailReportAdapter(deletedBills);
        detailReportDeleteRecyclerView.setAdapter(detailReportAdapter);

    }



    private void processBillData() {
        Iterator<BillItemsDetailDto> iterator = billItemsDetailDtoList.iterator();
        while (iterator.hasNext()) {
            BillItemsDetailDto dto = iterator.next();
            if (AppConstant.DELETED.equals(dto.getStatus())) {
                deleteTotal += dto.getBillItemPrice();
                deletedBills.add(dto);
                iterator.remove();
            } else {
                total += dto.getBillItemPrice();
                if (AppConstant.CASH.equals(dto.getPaymentMethod())) cash += dto.getBillItemPrice();
                else if (AppConstant.LOAN.equals(dto.getPaymentMethod())) loan += dto.getBillItemPrice();
            }
        }

        updateSummaryTextViews();
    }

    private void updateSummaryTextViews() {
        totalT.setText(appConstant.formatAmount(total));
        cashT.setText(appConstant.formatAmount(cash));
        loanT.setText(appConstant.formatAmount(loan));
        deleteT.setText(appConstant.formatAmount(deleteTotal));
    }


    private void downloadPdf() {
        todayReportFactory.downloadDetailPdfByDateRange(
                date1Button.getText().toString(), date2Button.getText().toString(),
                billItemsDetailDtoList, deletedBills
        );
    }

    interface DateSetListener {
        void onDateSet(Calendar date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
