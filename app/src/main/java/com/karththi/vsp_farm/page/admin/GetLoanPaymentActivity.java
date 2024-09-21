package com.karththi.vsp_farm.page.admin;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.Factory.ReportFactory;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.LoanPaymentDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.LoanPaymentAdapter;
import com.karththi.vsp_farm.helper.utils.LoadingDialog;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.LoanPaymentService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetLoanPaymentActivity extends AppCompatActivity {

    private Button date1Button, date2Button, downloadPdfButton;
    private Calendar date1 = null;
    private Calendar date2 = null;

    private AppConstant appConstant;

    private CustomerService customerService;
    private Spinner customerSpinner;

    private SimpleDateFormat dateFormat;

    private BillItemService billItemService;
    private Customer selectedCus;

    private LoadingDialog loadingDialog;

    private RecyclerView loanPaymentReportRecyclerView;


    private ExecutorService executorService;

    private ReportFactory reportFactory;

    private LoanPaymentAdapter loanPaymentAdapter;

    private LoanPaymentService loanPaymentService;

    private List<LoanPaymentDto> loanPaymentDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_loan_payment);

        customerService = new CustomerService(this);
        appConstant = new AppConstant(this);
        billItemService = new BillItemService(this);
        loadingDialog = new LoadingDialog(this);
        reportFactory = new ReportFactory(this);
        loanPaymentService= new LoanPaymentService(this);

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
                if (selectedCustomer.getId() ==0 && !selectedCustomer.getName().equals("ALL")){
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

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date2 = Calendar.getInstance();
        date2Button.setText(dateFormat.format(date2.getTime()));

        setupDatePickers();

        loanPaymentReportRecyclerView = findViewById(R.id.loanPaymentReportRecyclerView);

        loanPaymentReportRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        executorService = Executors.newSingleThreadExecutor();
        setupDownloadButton();
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
                    fetchedLoanPayments();


                    runOnUiThread(() -> {
                        updateView();
                        loadingDialog.dismiss();
                    });
                });
            }
        }
    }

    private void showDatePicker(GetLoanPaymentActivity.DateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            listener.onDateSet(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateView() {
        loanPaymentAdapter = new LoanPaymentAdapter(loanPaymentDtoList);
        loanPaymentReportRecyclerView.setAdapter(loanPaymentAdapter);
    }

    private void fetchedLoanPayments() {
        loanPaymentDtoList = loanPaymentService.getLoanPaymentListByDateRange(selectedCus.getId(),
                date1Button.getText().toString(), date2Button.getText().toString());
    }

    private void setupCustomerSpinner() {
        Customer all = new Customer(999999,"ALL",null,null);
        customerSpinner = findViewById(R.id.customerSpinner);
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getName().equals("DEFAULT")) {
                customers.remove(customer);
                break;
            }
        }
        customers.add( all);

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
            if (customers.get(i).getName().equals("ALL")) {
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
                            reportFactory.downloadLoanPaymentPdf(selectedCus.getName(),loanPaymentDtoList, date1Button.getText().toString(), date2Button.getText().toString());
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
