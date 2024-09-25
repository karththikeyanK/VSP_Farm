package com.karththi.vsp_farm.page.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.karththi.vsp_farm.page.LoginActivity;
import com.karththi.vsp_farm.printer.EpsonPrinterHelper;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.LoanPaymentService;
import com.karththi.vsp_farm.service.LoanService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


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


    private Button viewBasketButton,closeBasketButton;

    private ScrollView billingScrollView;

    private LinearLayout customerLayout;

    private RecyclerView itemsGridView;



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

        viewBasketButton = findViewById(R.id.viewBasketButton);
        closeBasketButton = findViewById(R.id.closeBasketButton);
        billingScrollView = findViewById(R.id.billingScrollView);
        customerLayout = findViewById(R.id.customerLayout);

        billItems = new ArrayList<>();

        if (AppConstant.USER_NAME == null) {
            Intent intent = new Intent(BillingPageActivity.this, LoginActivity.class);
            appConstant.SuccessAlert(AppConstant.ERROR, "Please Login Again", intent);
        }


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

        viewBasketButton.setOnClickListener(v -> {
            billingScrollView.setVisibility(View.VISIBLE);
            closeBasketButton.setVisibility(View.VISIBLE);

            viewBasketButton.setVisibility(View.GONE);
            customerLayout.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            itemsGridView.setVisibility(View.GONE);

        });

        closeBasketButton.setOnClickListener(v -> {
            billingScrollView.setVisibility(View.GONE);
            closeBasketButton.setVisibility(View.GONE);

            viewBasketButton.setVisibility(View.VISIBLE);
            customerLayout.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            itemsGridView.setVisibility(View.VISIBLE);
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
                double total = parseDoubleOrDefault(itemTotal.getText().toString());
                double newTotal = (price - discount) * qty;
                if (qty !=0 && ((total - newTotal) > 1 || (total - newTotal) < -1 )){
                    total = (price - discount) * qty;
                }
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
                double discount = parseDoubleOrDefault(itemDiscount.getText().toString());;
                double total = parseDoubleOrDefault(itemTotal.getText().toString());
                double qty = total / (price - discount);
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
            String itemTotalStr = itemTotal.getText().toString();
            if (!itemTotalStr.isEmpty()) {
                totalAmount += Double.parseDouble(itemTotalStr);
            }
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
        validateBill();
        if (AppConstant.USER_NAME == null) {
            Intent intent = new Intent(BillingPageActivity.this, LoginActivity.class);
            appConstant.SuccessAlert(AppConstant.ERROR, "Please Login Again", intent);
        }
        if (inputAmountEditText.getText().toString().isEmpty()) {
            appConstant.ShowAlert("Error", "Please enter the amount paid");
            return;
        }

        if ( balance <= -20) {
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
        loanButton.setEnabled(false);
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
        validateBill();
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
        printBillButton.setEnabled(false);
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
           if (selectedCus.getName().equals(AppConstant.DEFAULT)) {
               loanButton.setEnabled(true);
           }
            appConstant.ShowAlert("Error", "Failed to print bill");
        });
    }

    private void validateBill() {
        for (BillItem billItem : billItems) {
            billItem.setQuantity(Double.parseDouble(String.format(Locale.US, "%.3f", billItem.getQuantity())));
            billItem.setPrice(Double.parseDouble(String.format(Locale.US, "%.2f", billItem.getPrice())));
            billItem.setDiscount(Double.parseDouble(String.format(Locale.US, "%.2f", billItem.getDiscount())));
        }
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
        return epsonPrinterHelper.printBill(printItems, customerPrintDto, isLoan, isDefault,isReprint,false);
    }


    private void initializeUIComponents() {
        billingTableLayout = findViewById(R.id.billingTableLayout);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        inputAmountEditText = findViewById(R.id.inputAmountEditText);
        balanceTextView = findViewById(R.id.balanceTextView);
    }

    private void setupItemsGridView() {
         itemsGridView = findViewById(R.id.itemsGridView);
        List<SubItem> subItemList = subItemService.getAllSubItems();
        SubItemAdapter adapter = new SubItemAdapter(subItemList, this);
        itemsGridView.setAdapter(adapter);
        itemsGridView.setLayoutManager(new GridLayoutManager(this, 5)); // 5 columns

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
                tv.setTextSize(24);
                tv.setText(getItem(position).getName());  // Display the customer's name
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_item, parent, false);
                }
                TextView tv = convertView.findViewById(R.id.dropdown_text);
                tv.setText(getItem(position).getName());  // Display the customer's name
                return convertView;
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

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ViewHolder> {
        private List<SubItem> subItemList;
        private Context context;

        public SubItemAdapter(List<SubItem> subItemList, Context context) {
            this.subItemList = subItemList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sub, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SubItem subItem = subItemList.get(position);
            holder.subItemName.setText(subItem.getSubItemName());
            holder.subItemPrice.setText( subItem.getPrice()+" LKR");

            holder.itemView.setOnClickListener(v -> showPopup(subItem));
        }

        @Override
        public int getItemCount() {
            return subItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subItemName, subItemPrice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                subItemName = itemView.findViewById(R.id.subItemName);
                subItemPrice = itemView.findViewById(R.id.subItemPrice);
            }
        }

        private void showPopup(SubItem subItem) {
            // Inflate the popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(popupView);

            TextView discountLabel = popupView.findViewById(R.id.discountLabel);
            TextView itemName = popupView.findViewById(R.id.itemName);
            EditText priceInput = popupView.findViewById(R.id.priceInput);
            EditText qtyInput = popupView.findViewById(R.id.qtyInput);
            EditText discountInput = popupView.findViewById(R.id.discountInput);
            Button addButton = popupView.findViewById(R.id.addButton);

            itemName.setText(subItem.getSubItemName() + " - " + subItem.getPrice() + " LKR");
            qtyInput.setText("1");

            if (selectedCus.getName().equals(AppConstant.DEFAULT)) {
                discountInput.setEnabled(false);
                discountInput.setVisibility(View.GONE);
                discountLabel.setVisibility(View.GONE);
            } else {
                discountInput.setEnabled(true);
                discountInput.setVisibility(View.VISIBLE);
                discountLabel.setVisibility(View.VISIBLE);
                discountInput.setText("0.0");
            }

            priceInput.setText(String.valueOf(subItem.getPrice()));

            // Clear the field when touched
            priceInput.setOnTouchListener((v, event) -> {
                priceInput.getText().clear();
                return false;
            });

            qtyInput.setOnTouchListener((v, event) -> {
                qtyInput.getText().clear();
                return false;
            });

            discountInput.setOnTouchListener((v, event) -> {
                discountInput.getText().clear();
                return false;
            });

            priceInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    double price = parseDoubleSafely(priceInput, subItem.getPrice());
                    double discount = parseDoubleSafely(discountInput, 0.0);

                    // Calculate quantity based on the new price
                    double qty = price / (subItem.getPrice() - discount);
                    qtyInput.setText(String.valueOf(Double.parseDouble(String.format("%.3f", qty))));
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            qtyInput.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    double qty = parseDoubleSafely(qtyInput, 1.0);
                    double discount = parseDoubleSafely(discountInput, 0.0);

                    double price = qty * (subItem.getPrice() - discount);
                    discountInput.setText(String.valueOf(discount));
                    qtyInput.setText(String.valueOf(qty));
                    priceInput.setText(String.valueOf(price));
                }
            });

            discountInput.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    double qty = parseDoubleSafely(qtyInput, 1.0);
                    double discount = parseDoubleSafely(discountInput, 0.0);

                    double price = qty * (subItem.getPrice() - discount);
                    discountInput.setText(String.valueOf(discount));
                    qtyInput.setText(String.valueOf(qty));
                    priceInput.setText(String.valueOf(price));
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            addButton.setOnClickListener(v -> {
                double price = parseDoubleSafely(priceInput, subItem.getPrice());
                double qty = parseDoubleSafely(qtyInput, 1.0);
                double discount = parseDoubleSafely(discountInput, 0.0);

                if ((price + 1)  < (subItem.getPrice() - discount) * qty)  {
                    price = (subItem.getPrice() - discount) * qty;
                } else if (price == 0 && qty == 0) {
                    appConstant.ShowAlert("Error", "Please enter the price or quantity");
                    return;
                } else if (price > (subItem.getPrice() - discount) * qty) {
                    qty = price / (subItem.getPrice() - discount);
                    qty = Double.parseDouble(String.format("%.3f", qty));
                }
                price = Double.parseDouble(String.format("%.2f", price));
                addItemToCart(subItem, price, qty, discount);
                dialog.dismiss();
            });
        }



        private double parseDoubleSafely(EditText editText, double defaultValue) {
            String text = editText.getText().toString().trim();
            if (text.isEmpty()) {
                return defaultValue; // Return default if the input is empty
            } else {
                return Double.parseDouble(text); // Parse the value if it's not empty
            }
        }

        private void addItemToCart(SubItem subItem, double price, double qty, double discount) {
            BillItem billItem = new BillItem();
            billItem.setSubItemId(subItem.getId());
            billItem.setPrice(price);
            billItem.setQuantity(qty);
            billItem.setDiscount(discount);

            LayoutInflater inflater = LayoutInflater.from(context);
            TableRow row = (TableRow) inflater.inflate(R.layout.billing_row, billingTableLayout, false);

            TextView itemName = row.findViewById(R.id.itemName);
            TextView itemPrice = row.findViewById(R.id.itemPrice);
            EditText itemDiscount = row.findViewById(R.id.itemDiscount);
            EditText itemQuantity = row.findViewById(R.id.itemQuantity);
            EditText itemTotal = row.findViewById(R.id.itemTotal);
            TextView deleteItem = row.findViewById(R.id.deleteItem);
            itemDiscount.setOnTouchListener((v, event) -> {
                itemDiscount.getText().clear();
                return false;
            });

            itemQuantity.setOnTouchListener((v, event) -> {
                itemQuantity.getText().clear();
                return false;
            });

            itemTotal.setOnTouchListener((v, event) -> {
                itemTotal.getText().clear();
                return false;
            });


            if (selectedCus.getName().equals(AppConstant.DEFAULT)) {
                itemDiscount.setEnabled(false);
            }
            // Create and add BillItem to the list
            billId++;
            billItem.setId(billId);
            billItems.add(billItem);
            row.setTag(billId);

            itemDiscount.setText(String.valueOf(discount));
            itemName.setText(subItem.getSubItemName());
            itemPrice.setText(String.valueOf(subItem.getPrice()));
            itemQuantity.setText(String.valueOf(qty));
            itemTotal.setText(String.valueOf(price ));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                itemTotal.getFocusable();
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


    }


}