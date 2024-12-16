package com.karththi.vsp_farm.page.admin.report;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.Factory.ReportFactory;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.LoanPaymentDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.DetailReportAdapter;
import com.karththi.vsp_farm.helper.adapter.LoanPaymentAdapter;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.LoanPaymentService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetCustomerDetailReportActivity extends AppCompatActivity {

    private Button date1Button, date2Button, downloadPdfButton;
    private TextView totalT, cashT, loanT, loanPayment;
    private Calendar date1 = null;
    private Calendar date2 = null;

    private AppConstant appConstant;

    private CustomerService customerService;
    private Spinner customerSpinner;

    private SimpleDateFormat dateFormat;

    private BillItemService billItemService;
    private Customer selectedCus;

    private LoadingDialog loadingDialog;

    private List<BillItemsDetailDto> billItemsDetailDtoList, deletedBills,loanBills,cashBills;

    private double total, cash, loan, deleteTotal;

    private RecyclerView detailReportRecyclerView;
    private RecyclerView detailReportDeleteRecyclerView;

    private DetailReportAdapter detailReportAdapter;

    private ExecutorService executorService;

    private ReportFactory reportFactory;

    private List<LoanPaymentDto> loanPaymentDtoList;

    private LoanPaymentService loanPaymentService;

    private LoanPaymentAdapter loanPaymentAdapter;

    private RecyclerView loanPaymentReportRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_customer_detail_report);

        customerService = new CustomerService(this);
        appConstant = new AppConstant(this);
        billItemService = new BillItemService(this);
        loadingDialog = new LoadingDialog(this);
        reportFactory = new ReportFactory(this);
        loanPaymentService = new LoanPaymentService(this);

        selectedCus = new Customer();

        setupCustomerSpinner();

        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                selectedCus = selectedCustomer;

                if (selectedCustomer.equals(null) || selectedCustomer == null){
                    appConstant.ShowAlert("Error", "Please select a customer");
                    return;
                };
                if (selectedCustomer.getId() ==0 ){
                    appConstant.ShowAlert("Error", "Please select a customer");
                    return;
                }

                checkIfBothDatesSelected();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadingDialog = new LoadingDialog(this);

        date1Button = findViewById(R.id.date1Button);
        date2Button = findViewById(R.id.date2Button);
        downloadPdfButton = findViewById(R.id.downloadPdfButton);
        totalT = findViewById(R.id.total);
        cashT = findViewById(R.id.cash);
        loanT = findViewById(R.id.loan);
        loanPayment = findViewById(R.id.loanPayment);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date2 = Calendar.getInstance();
        date2Button.setText(dateFormat.format(date2.getTime()));

        setupDatePickers();

        detailReportRecyclerView = findViewById(R.id.detailReportRecyclerView);
        detailReportDeleteRecyclerView = findViewById(R.id.deleteDetailReportRecyclerView);

        loanPaymentReportRecyclerView = findViewById(R.id.loanPaymentReportRecyclerView);

        loanPaymentReportRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        detailReportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailReportDeleteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Use ExecutorService for background task
        executorService = Executors.newSingleThreadExecutor();
        setupDownloadButton();

    }

    private void updateView() {
        downloadPdfButton.setEnabled(true);
        if (billItemsDetailDtoList.isEmpty()) {
            Toast.makeText(this, "No Bills found", Toast.LENGTH_SHORT).show();
            return;
        }

        detailReportAdapter = new DetailReportAdapter(billItemsDetailDtoList);
        detailReportRecyclerView.setAdapter(detailReportAdapter);

        detailReportAdapter = new DetailReportAdapter(deletedBills);
        detailReportDeleteRecyclerView.setAdapter(detailReportAdapter);

        loanPaymentAdapter = new LoanPaymentAdapter(loanPaymentDtoList);
        loanPaymentReportRecyclerView.setAdapter(loanPaymentAdapter);

        updateSummaryTextViews();

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

    private void checkIfBothDatesSelected() {
        if (date1 != null && date2 != null) {
            if (selectedCus.getId() != 0) {
                loadingDialog.show("Loading....");

                executorService.execute(() -> {
                    fetchedBills();  // Run this in background

                    // After fetchedBills(), update the UI on the main thread
                    runOnUiThread(() -> {
                        updateView();    // This runs on UI thread
                        loadingDialog.dismiss();  // Hide loading dialog once done
                    });
                });
            }
        }
    }

    private void updateSummaryTextViews() {
        double total_loan_payment = 0;
        for (LoanPaymentDto loanPaymentDto : loanPaymentDtoList) {
            total_loan_payment += loanPaymentDto.getPaymentAmount();
        }
        totalT.setText(appConstant.formatAmount(total));
        cashT.setText(appConstant.formatAmount(cash));
        loanT.setText(appConstant.formatAmount(loan));
        loanPayment.setText(appConstant.formatAmount(total_loan_payment));
    }



    private void fetchedBills(){
        billItemsDetailDtoList = new ArrayList<>();
        deletedBills = new ArrayList<>();
        loanBills = new ArrayList<>();
        cashBills = new ArrayList<>();
        loanPaymentDtoList = new ArrayList<>();

        loanPaymentDtoList = loanPaymentService.getLoanPaymentListByDateRange(selectedCus.getId(),date1Button.getText().toString(), date2Button.getText().toString());
        billItemsDetailDtoList = billItemService.getAllBillDtoByDateRangeAndCustomerId(  date1Button.getText().toString(), date2Button.getText().toString(),selectedCus.getId());
        total = cash = loan = deleteTotal = 0;
        Iterator<BillItemsDetailDto> iterator = billItemsDetailDtoList.iterator();
        while (iterator.hasNext()) {
            BillItemsDetailDto billItemsDetailDto = iterator.next();
            if (billItemsDetailDto.getStatus().equals(AppConstant.DELETED)) {
                deletedBills.add(billItemsDetailDto);
                deleteTotal += billItemsDetailDto.getBillItemPrice();
                iterator.remove();
            }else if (billItemsDetailDto.getPaymentMethod().equals(AppConstant.LOAN)) {
                loanBills.add(billItemsDetailDto);
                loan += billItemsDetailDto.getBillItemPrice();
            }else if (billItemsDetailDto.getPaymentMethod().equals(AppConstant.CASH)) {
                cashBills.add(billItemsDetailDto);
                cash += billItemsDetailDto.getBillItemPrice();
            }
            total += billItemsDetailDto.getBillItemPrice();
        }
    }

    private void showDatePicker(GetCustomerDetailReportActivity.DateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            listener.onDateSet(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setupCustomerSpinner() {
        customerSpinner = findViewById(R.id.customerSpinner);
        List<Customer> customers = customerService.getAllCustomers();

        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, customers) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(20);
                tv.setText(getItem(position).getName());  // Display the customer's name
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setText(getItem(position).getName());  // Display the customer's name
                return view;
            }
        };

        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setAdapter(customerAdapter);

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getName().equals("DEFAULT")) {
                customerSpinner.setSelection(i);
                break;
            }
        }
    }

    private void setupDownloadButton() {
        downloadPdfButton.setOnClickListener(v ->
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadPdf();
                    }

                    private void downloadPdf() {
                        AsyncTask.execute(() -> {
                           reportFactory.downloadDetailReportPdfByCustomer(
                                   date1Button.getText().toString(), date2Button.getText().toString(),
                                   total,cash,loan,deleteTotal,selectedCus.getName(),
                                   cashBills,loanBills, deletedBills,loanPaymentDtoList);
                        });
                    }
                })
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