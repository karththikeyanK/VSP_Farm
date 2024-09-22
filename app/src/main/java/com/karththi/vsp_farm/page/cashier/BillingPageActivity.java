package com.karththi.vsp_farm.page.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.callBack.SaveBill;
import com.karththi.vsp_farm.dto.CustomerPrintDto;
import com.karththi.vsp_farm.dto.PrintItemDto;
import com.karththi.vsp_farm.facade.BillFacade;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.adapter.ItemRecycleAdapter;
import com.karththi.vsp_farm.helper.adapter.SubItemGridAdapter;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.model.LoanPayment;
import com.karththi.vsp_farm.model.Measurement;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.printer.EpsonPrinterHelper;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.LoanPaymentService;
import com.karththi.vsp_farm.service.LoanService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BillingPageActivity extends AppCompatActivity implements SaveBill {
    private AppConstant appConstant;

    private ItemService itemService;
    private CustomerService customerService;

    private SubItemService subItemService;

    private LoanService loanService;

    private LoanPaymentService loanPaymentService;
    private TableLayout billingTableLayout;
    private TextView totalPriceTextView;
    private EditText inputAmountEditText;
    private TextView balanceTextView;
    private double totalAmount = 0.0;

    private Spinner customerSpinner;

    private List<BillItem> billItems;

    private Button backButton, loanButton, printBillButton;

    private int customerId;

    private Customer selectedCus;

    private BillFacade billFacade;

    private int billId = 0;

    private double balance = 0;

    private boolean isLoan = false;

    private EpsonPrinterHelper epsonPrinterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_page);


        itemService = new ItemService(this);
        subItemService = new SubItemService(this);
        customerService = new CustomerService(this);
        appConstant = new AppConstant(this);
        billFacade = new BillFacade(this);
        epsonPrinterHelper = new EpsonPrinterHelper(this);
        loanService = new LoanService(this);
        loanPaymentService = new LoanPaymentService(this);

        loanButton = findViewById(R.id.loanButton);
        printBillButton = findViewById(R.id.printBillButton);
        printBillButton.setOnClickListener(v -> printBill());
        loanButton.setOnClickListener(v -> LoanButtonClick());

        billItems = new ArrayList<>();


        initializeUIComponents();
        setupItemsGridView();
        setupInputAmountEditText();
        setupCustomerSpinner();
        setupBackButton();

        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                selectedCus = selectedCustomer;
                if (!selectedCustomer.getName().equals("DEFAULT")) {
                    loanButton.setVisibility(View.VISIBLE);
                }

                if (selectedCustomer.equals(null) || selectedCustomer == null) {
                    appConstant.ShowAlert("Error", "Please select a customer");
                    return;
                }
                customerId = selectedCustomer.getId();
                if (customerId == 0) {
                    appConstant.ShowAlert("Error", "Please select a customer");
                    return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    // Method to display a dialog with sub-items for a selected item
    private void showSubItemDialog(Item item) {
        // Get sub-items for the selected item
        List<SubItem> subItems = subItemService.getSubItemsByItemId(item.getId());

        // Create a dialog to show the sub-items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Sub-item");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sub_items, null);
        GridView subItemsGridView = dialogView.findViewById(R.id.subItemsGridView);

        // Set up adapter for the sub-items GridView
        SubItemGridAdapter adapter = new SubItemGridAdapter(this, subItems);
        subItemsGridView.setAdapter(adapter);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        subItemsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubItem selectedSubItem = subItems.get(position);
                addSubItemToBill(selectedSubItem);
                dialog.dismiss();
            }
        });
    }

    private void addSubItemToBill(SubItem subItem) {
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow row = (TableRow) inflater.inflate(R.layout.billing_row, billingTableLayout, false);

        TextView itemName = row.findViewById(R.id.itemName);
        TextView itemPrice = row.findViewById(R.id.itemPrice);
        EditText itemDiscount = row.findViewById(R.id.itemDiscount);
        EditText itemQuantity = row.findViewById(R.id.itemQuantity);
        EditText itemTotal = row.findViewById(R.id.itemTotal);
        TextView deleteItem = row.findViewById(R.id.deleteItem);

        itemDiscount.setText("0.0");

        // Create and add BillItem to the list
        billId++;
        BillItem billItem = new BillItem();
        billItem.setId(billId);
        billItem.setSubItemId(subItem.getId());
        billItem.setPrice(subItem.getPrice());
        billItem.setQuantity(1.0);
        billItem.setDiscount(0.0);

        billItems.add(billItem);
        row.setTag(billId); // Store BillItem object in row tag

        itemName.setText(subItem.getSubItemName());
        itemPrice.setText(String.valueOf(subItem.getPrice()));
        itemQuantity.setText("1");
        itemTotal.setText(String.valueOf(subItem.getPrice()));

        Customer selectedCustomer = (Customer) customerSpinner.getSelectedItem();
        if (selectedCustomer.getName().equals("DEFAULT")) {
            itemDiscount.setEnabled(false);
        }

        updateRowOnFocusChange(itemPrice, itemDiscount, itemQuantity, itemTotal, billId);

        // Handle row deletion
        deleteItem.setOnClickListener(v -> {
            int id = (int) row.getTag();
            Iterator<BillItem> iterator = billItems.iterator();
            while (iterator.hasNext()) {
                BillItem billItem1 = iterator.next();
                if (billItem1.getId() == id) {
                    iterator.remove();
                    billingTableLayout.removeView(row);
                    updateTotalAmount();
                    break;
                }
            }

        });

        billingTableLayout.addView(row);
        updateTotalAmount();
    }


    private void updateRowOnFocusChange(TextView itemPrice, EditText itemDiscount, EditText itemQuantity, EditText itemTotal, int billId) {
        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                boolean isDefaultCustomer = selectedCustomer.getName().equals("DEFAULT");
                selectedCus = selectedCustomer;
                customerId = selectedCustomer.getId();
                if (isDefaultCustomer) {
                    loanButton.setVisibility(View.GONE);
                } else {
                    loanButton.setVisibility(View.VISIBLE);
                }

                for (int i = 1; i < billingTableLayout.getChildCount(); i++) {
                    TableRow row = (TableRow) billingTableLayout.getChildAt(i);
                    EditText itemDiscount = row.findViewById(R.id.itemDiscount);
                    EditText itemQuantity = row.findViewById(R.id.itemQuantity);
                    EditText itemTotal = row.findViewById(R.id.itemTotal);
                    TextView itemPrice = row.findViewById(R.id.itemPrice);

                    if (isDefaultCustomer) {
                        itemDiscount.setText("0.0");
                        itemDiscount.setEnabled(false);  // Disable editing
                        double price = parseDoubleOrDefault(itemPrice.getText().toString());
                        double qty = parseDoubleOrDefault(itemQuantity.getText().toString());
                        double total = qty * price;
                        itemTotal.setText(String.format("%.2f", total));
                        updateTotalAmount();
                    } else {
                        itemDiscount.setEnabled(true); // Enable editing
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        BillItem billItem = new BillItem();

        itemTotal.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                double total = parseDoubleOrDefault(itemTotal.getText().toString());
                double discount = parseDoubleOrDefault(itemDiscount.getText().toString());
                double price = parseDoubleOrDefault(itemPrice.getText().toString());
                double qty = 0.0;
                if (total == 0) {
                    itemQuantity.setText("0.0");
                    itemTotal.setText("0.0");
                    billItem.setPrice(0.0);
                    billItem.setQuantity(0.0);
                    billItem.setDiscount(0.0);
                    updateBillItem(billItem, billId);
                    updateTotalAmount();
                    appConstant.ShowAlert("Error", "Please enter the price");
                    return;
                }else {
                    qty = total / (price - discount);
                };
                itemQuantity.setText(String.format("%.3f", qty));
                billItem.setPrice(total);
                billItem.setQuantity(Double.parseDouble(String.format("%.3f", qty)));
                billItem.setDiscount(Double.parseDouble(String.format("%.3f", discount)));
                updateBillItem(billItem, billId);
                updateTotalAmount();
            }
        });

        itemQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                double qty = parseDoubleOrDefault(itemQuantity.getText().toString());
                double discount = parseDoubleOrDefault(itemDiscount.getText().toString());
                double price = parseDoubleOrDefault(itemPrice.getText().toString());
                double total = qty * (price - discount);
                itemTotal.setText(String.format("%.2f", total));
                billItem.setPrice(total);
                billItem.setQuantity(Double.parseDouble(String.format("%.3f", qty)));
                billItem.setDiscount(Double.parseDouble(String.format("%.3f", discount)));
                updateBillItem(billItem, billId);
                updateTotalAmount();
            }
        });

        itemDiscount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                double price = parseDoubleOrDefault(itemPrice.getText().toString());
                double discount = parseDoubleOrDefault(itemDiscount.getText().toString());
                double qty = parseDoubleOrDefault(itemQuantity.getText().toString());
                double total = qty * (price - discount);
                itemTotal.setText(String.format("%.2f", total));
                billItem.setPrice(total);
                billItem.setQuantity(Double.parseDouble(String.format("%.3f", qty)));
                billItem.setDiscount(Double.parseDouble(String.format("%.3f", discount)));
                updateBillItem(billItem, billId);
                updateTotalAmount();
            }
        });
    }

    private void updateBillItem(BillItem billItem, int id) {
        for (BillItem item : billItems) {
            if (item.getId() == id) {
                item.setPrice(billItem.getPrice());
                item.setQuantity(billItem.getQuantity());
                item.setDiscount(billItem.getDiscount());
            }
        }
    }

    private double parseDoubleOrDefault(String value) {
        if (value.isEmpty()) {
            return 0.0;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 0.0; // Handle any other potential NumberFormatException
            }
        }
    }


    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (int i = 1; i < billingTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) billingTableLayout.getChildAt(i);
            EditText itemTotal = row.findViewById(R.id.itemTotal);
            totalAmount += Double.parseDouble(itemTotal.getText().toString());
        }
        totalPriceTextView.setText(String.format("Total: %.2f", totalAmount));
        calculateBalance();
    }


    private void calculateBalance() {
        String inputAmountStr = inputAmountEditText.getText().toString();
        double inputAmount = inputAmountStr.isEmpty() ? 0.0 : Double.parseDouble(inputAmountStr);
        balance = inputAmount - totalAmount;
        balanceTextView.setText(String.format("Balance: %.2f", balance));
    }

    private void printBill() {

        if (inputAmountEditText.getText().toString().isEmpty()) {
            appConstant.ShowAlert("Error", "Please enter the amount paid");
            return;
        }

        if (balance < 0) {
            appConstant.ShowAlert("Error", "Amount paid is less than the total amount");
            return;
        }
        if (customerId == 0) {
            appConstant.ShowAlert("Error", "Please select a customer");
            return;
        }
        if (billItems == null || billItems.isEmpty()) {
            appConstant.ShowAlert("Error", "Please add items to the bill");
            return;
        }
        printBillButton.setEnabled(false);

        String referenceNumber = appConstant.generateReferenceNumber();
        double inputAmount = inputAmountEditText.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(inputAmountEditText.getText().toString());
        if (printBill(billItems, false, totalAmount, inputAmount, balance,referenceNumber)) {
            billFacade.addBill(billItems, false, customerId, this, referenceNumber);
            epsonPrinterHelper.closePrinter();
        } else {
            failedToPrintBill();
        }

    }

    private void LoanButtonClick() {
        if (selectedCus.getName().equals(AppConstant.DEFAULT)) {
            Intent intent = new Intent(BillingPageActivity.this, BillingPageActivity.class);
            appConstant.SuccessAlert(AppConstant.ERROR, "Please Add items Again", intent);
            return;
        }
        if (customerId == 0) {
            appConstant.ShowAlert("Error", "Please select a customer");
            return;
        }
        if (billItems == null || billItems.isEmpty()) {
            appConstant.ShowAlert("Error", "Please add items to the bill");
            return;
        }
        String referenceNumber = appConstant.generateReferenceNumber();
        loanButton.setEnabled(false);
        isLoan = true;

        boolean isPrinted = printBill(billItems, true, totalAmount, 0, 0,referenceNumber);
        if (isPrinted) {
            billFacade.addBill(billItems, true, customerId, this, referenceNumber);
            epsonPrinterHelper.closePrinter();
        } else {
            failedToPrintBill();
        }


    }

    private void failedToPrintBill() {
        runOnUiThread(() -> {
            printBillButton.setEnabled(true);
            loanButton.setEnabled(true);
            appConstant.ShowAlert("Error", "Failed to print bill");
        });
    }


    private boolean printBill(List<BillItem> billItems, boolean isLoan, double totalAmount, double inputAmount, double balance,String referenceNumber) {
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
            Loan loan = loanService.getLoanByCustomerId(customerId);
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
        return epsonPrinterHelper.printBill(printItems, customerPrintDto, isLoan, isDefault,isReprint);
    }


    private void initializeUIComponents() {
        billingTableLayout = findViewById(R.id.billingTableLayout);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        inputAmountEditText = findViewById(R.id.inputAmountEditText);
        balanceTextView = findViewById(R.id.balanceTextView);
    }

    private void setupItemsGridView() {
        List<Item> items = itemService.getAllItems();
        RecyclerView itemsRecycleView = findViewById(R.id.itemsGridView);
        ItemRecycleAdapter adapter = new ItemRecycleAdapter(this, items, item -> showSubItemDialog(item));
        itemsRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        itemsRecycleView.setAdapter(adapter);
    }

    private void setupInputAmountEditText() {
        inputAmountEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                calculateBalance();
            }
        });
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

    private void setupBackButton() {
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BillingPageActivity.this, CashierDashBoard.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent the back button from working
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Get the view that has focus
        View view = getCurrentFocus();

        // Check if the touch event is outside the focused view (keyboard is open)
        if (view != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (view instanceof EditText) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                float x = ev.getRawX() + view.getLeft() - location[0];
                float y = ev.getRawY() + view.getTop() - location[1];

                // If the touch is outside the EditText, hide the keyboard
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                    hideKeyboard(view);
                    view.clearFocus();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onSaveBillSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BillingPageActivity.this, BillingPageActivity.class);
                if (isLoan) {
                    startActivity(intent);
                } else {
                    appConstant.showBalancePopup("Balance", String.format("Balance: %.2f", balance), () -> startActivity(intent));
                }
            }
        });
    }

    @Override
    public void onSaveBillError(String message) {
//        appConstant.ShowAlert("Error", "Failed to save bill. Please add this your Note");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        epsonPrinterHelper.closePrinter();
    }

}