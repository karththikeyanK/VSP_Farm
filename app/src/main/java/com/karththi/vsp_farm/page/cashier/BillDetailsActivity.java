package com.karththi.vsp_farm.page.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.CustomerPrintDto;
import com.karththi.vsp_farm.dto.PrintItemDto;
import com.karththi.vsp_farm.facade.BillFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.BillItemAdapter;
import com.karththi.vsp_farm.helper.utils.DateTimeUtils;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.model.Measurement;
import com.karththi.vsp_farm.printer.EpsonPrinterHelper;
import com.karththi.vsp_farm.service.BillItemService;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.LoanPaymentService;
import com.karththi.vsp_farm.service.LoanService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.ArrayList;
import java.util.List;

public class BillDetailsActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private List<BillItem> billItems;
    private BillItemAdapter billItemAdapter;

    private BillItemService billItemService;

    private ImageView deleteButton;

    private Button backButton;

    private AppConstant appConstant;

    private BillService billService;

    private BillFacade billFacade;

    TextView referenceNumber, totalAmount, customerName, dateTime, paymentMethod;

    private EpsonPrinterHelper epsonPrinterHelper;

    private LoanService loanService;

    private LoanPaymentService loanPaymentService;

    private SubItemService subItemService;

    private Customer selectedCus;

    private CustomerService customerService;

    private Button rePrintButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        tableLayout = findViewById(R.id.bill_items_table);

        billItemService = new BillItemService(this);
        appConstant = new AppConstant(this);
        billService = new BillService(this);
        billFacade = new BillFacade(this);
        loanService = new LoanService(this);
        epsonPrinterHelper = new EpsonPrinterHelper(this);
        loanPaymentService = new LoanPaymentService(this);
        subItemService = new SubItemService(this);
        customerService = new CustomerService(this);

        int billId = getIntent().getIntExtra("bill_id", -1);

        // Dummy data
        billItems = new ArrayList<>();
        billItems =  billItemService.getBillItemByBillId(billId);

        // Use BillItemAdapter to populate the table
        billItemAdapter = new BillItemAdapter(this, tableLayout, billItems);
        billItemAdapter.populateTable();

        // Set the bill details
        referenceNumber = findViewById(R.id.reference_number);
        totalAmount = findViewById(R.id.total_amount);
        customerName = findViewById(R.id.customer_name);
        dateTime = findViewById(R.id.date_time);
        paymentMethod = findViewById(R.id.paymentMethod);
        rePrintButton = findViewById(R.id.rePrintButton);

        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteIcon);
        backButton.setOnClickListener(v -> {
            finish();
        });
        deleteButton.setOnClickListener(v -> {
            appConstant.ConfirmAlert(
                    "Are you sure you want to delete this bill?",
                    "This action cannot be undone",
                    () -> billFacade.deleteBill(billId)
            );
        });


        String r_referenceNumber, r_totalAmount, r_customerName, r_dateTime, r_paymentMethod;
        r_referenceNumber = getIntent().getStringExtra("reference_number");
        r_totalAmount = getIntent().getStringExtra("total_amount");
        r_customerName = getIntent().getStringExtra("customer_name");
        r_dateTime = getIntent().getStringExtra("created_date");
        r_paymentMethod = getIntent().getStringExtra("payment_method");



        referenceNumber.setText(r_referenceNumber);
        totalAmount.setText(r_totalAmount +" LKR");
        customerName.setText(r_customerName);
        dateTime.setText(r_dateTime);
        paymentMethod.setText(r_paymentMethod);

        String billDate = r_dateTime.substring(0, 10);

        if (DateTimeUtils.getCurrentDate().equals(billDate)) {
            rePrintButton.setVisibility(View.VISIBLE);
        }
        rePrintButton.setOnClickListener(v -> {
            printBill(billItems, r_paymentMethod.equals(AppConstant.LOAN), Double.parseDouble(r_totalAmount), Double.parseDouble(r_totalAmount), 0,r_referenceNumber);
        });

    }


    private boolean printBill(List<BillItem> billItems, boolean isLoan, double totalAmount, double inputAmount, double balance,String referenceNumber) {
        int customer_id = getIntent().getIntExtra("customer_id", -1);
        selectedCus = customerService.getCustomerById(customer_id);

        List<PrintItemDto> printItems = new ArrayList<>();
        boolean  isReprint = false;
        boolean isDefault = true;
        for (BillItem billItem : billItems) {
            PrintItemDto printItemDto = subItemService.getPrintItem(billItem.getSubItemId());
            printItemDto.setQuantity(billItem.getQuantity());
            printItemDto.setTotal(billItem.getPrice());
            printItemDto.setDiscount(billItem.getDiscount());
            printItems.add(printItemDto);

            if (billItem.getPrice() == 0) {
                appConstant.ShowAlert("Error", "Please enter the total for " + printItemDto.getItemName());
                return false;
            }

            if (printItemDto.getMeasurement().equals(Measurement.LITRE.name())){
                printItemDto.setMeasurement("L");
            }else if (printItemDto.getMeasurement().equals(Measurement.PIECE.name())) {
                printItemDto.setMeasurement("PC");
            }

            if (printItemDto.getName().toLowerCase().equals("chicken") || printItemDto.getName().toLowerCase().equals("mutton")) {
                isReprint = true;
            }
        }
        CustomerPrintDto customerPrintDto = new CustomerPrintDto();
        customerPrintDto.setCustomerName(selectedCus.getName());
        customerPrintDto.setReferenceNumber(referenceNumber);
        customerPrintDto.setBalance(balance);
        if (!selectedCus.getName().equals(AppConstant.DEFAULT)) {
            Loan loan = loanService.getLoanByCustomerId(customer_id);
            if (loan != null) {
                LoanPayment loanPayment = loanPaymentService.getLastPaymentByLoanId(loan.getId());
                customerPrintDto.setTotalRemainingAmount(loan.getRemainingAmount());
                if (loanPayment != null) {
                    customerPrintDto.setLastPaymentAmount(loanPayment.getPaymentAmount());
                    customerPrintDto.setLastPaymentDate(loanPayment.getPaymentDate());
                }
            }
            isDefault = false;
        }
        customerPrintDto.setTotalAmount(totalAmount);
        if (!isLoan) {
            customerPrintDto.setTotalPaidAmount(inputAmount);
            customerPrintDto.setTotalRemainingAmount(balance);

        }
        return epsonPrinterHelper.printBill(printItems, customerPrintDto, isLoan, isDefault,isReprint,true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


