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
import android.widget.ImageView;
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
import com.karththi.vsp_farm.helper.adapter.ItemRecycleAdapter;
import com.karththi.vsp_farm.helper.adapter.SubItemGridAdapter;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.ItemService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.List;


// ToDO: put the bill items in the ArrayList
//

public class BillingPageActivity extends AppCompatActivity {

    private ItemService itemService;

    private CustomerService customerService;

    private SubItemService subItemService;
    private TableLayout billingTableLayout;
    private TextView totalPriceTextView;
    private EditText inputAmountEditText;
    private TextView balanceTextView;
    private double totalAmount = 0.0;

    private Spinner customerSpinner;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_page);


        itemService = new ItemService(this);
        subItemService = new SubItemService(this);
        customerService = new CustomerService(this);

        initializeUIComponents();
        setupItemsGridView();
        setupPrintBillButton();
        setupInputAmountEditText();
        initializeHeader();
        setupCustomerSpinner();
        setupBackButton();
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

        // Handle sub-item click
        subItemsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubItem selectedSubItem = subItems.get(position);
                addSubItemToBill(selectedSubItem);
                dialog.dismiss(); // Close the dialog after selecting a sub-item
            }
        });
    }

    private void addSubItemToBill(SubItem subItem) {
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow row = (TableRow) inflater.inflate(R.layout.billing_row, billingTableLayout, false);

        TextView itemName = row.findViewById(R.id.itemName);
        EditText itemPrice = row.findViewById(R.id.itemPrice);
        EditText itemQuantity = row.findViewById(R.id.itemQuantity);
        EditText itemTotal = row.findViewById(R.id.itemTotal);
        ImageView deleteIcon = row.findViewById(R.id.deleteIcon);
        float density = getResources().getDisplayMetrics().density;
        int newWidthDp = (int)(40 * density);
        int newHeightDp = (int)(40 * density);
        deleteIcon.getLayoutParams().width = newWidthDp;
        deleteIcon.getLayoutParams().height = newHeightDp;
        deleteIcon.requestLayout();

        Customer selectedCustomer = (Customer) customerSpinner.getSelectedItem();
        if (selectedCustomer.getName().equals("DEFAULT")) {
            itemPrice.setClickable(false);
            itemPrice.setFocusable(false);
        }

        itemName.setText(subItem.getSubItemName());
        itemPrice.setText(String.valueOf(subItem.getPrice()));
        itemQuantity.setText("1");  // Default quantity is 1
        itemTotal.setText(String.valueOf(subItem.getPrice()));

        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = (Customer) parent.getItemAtPosition(position);
                for (int i = 1; i < billingTableLayout.getChildCount(); i++) {
                    TableRow row = (TableRow) billingTableLayout.getChildAt(i);
                    EditText itemPrice = row.findViewById(R.id.itemPrice);
                    if (selectedCustomer.getName().equals("DEFAULT")) {
                        itemPrice.setClickable(false);
                        itemPrice.setFocusable(false);
                    } else {
                        itemPrice.setClickable(true);
                        itemPrice.setFocusable(true);
                        itemPrice.setFocusableInTouchMode(true); // To gain focus on touch
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        itemTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    double total = Double.parseDouble(itemTotal.getText().toString());
                    double price = subItem.getPrice();
                    double qty = total / price;
                    itemQuantity.setText(String.format("%.2f", qty));
                    updateTotalAmount();
                }
            }
        });

        itemQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String quantityStr = itemQuantity.getText().toString();
                    if (!quantityStr.isEmpty()) {
                        double qty = Double.parseDouble(quantityStr);
                        double price = subItem.getPrice();
                        double total = qty * price;
                        itemTotal.setText(String.format("%.2f", total));
                        updateTotalAmount();
                    }
                }
            }
        });

        itemPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    double price = Double.parseDouble(itemPrice.getText().toString());
                    double qty = Double.parseDouble(itemQuantity.getText().toString());
                    double total = qty * price;
                    itemTotal.setText(String.format("%.2f", total));
                    updateTotalAmount();
                }
            }
        });

        itemQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    double qty = Double.parseDouble(itemQuantity.getText().toString());
                    double price = subItem.getPrice();
                    double total = qty * price;
                    itemTotal.setText(String.format("%.2f", total));
                    updateTotalAmount();
                }
            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingTableLayout.removeView(row);
                updateTotalAmount();
            }
        });

        billingTableLayout.addView(row);
        updateTotalAmount();
    }


    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (int i = 1; i < billingTableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) billingTableLayout.getChildAt(i);
            TextView itemTotal = (TextView) row.getChildAt(3);
            totalAmount += Double.parseDouble(itemTotal.getText().toString());
        }
        totalPriceTextView.setText(String.format("Total: %.2f", totalAmount));
        calculateBalance();  // Recalculate balance after updating total amount
    }
    private void calculateBalance() {
        String inputAmountStr = inputAmountEditText.getText().toString();
        double inputAmount = inputAmountStr.isEmpty() ? 0.0 : Double.parseDouble(inputAmountStr);
        double balance = inputAmount - totalAmount;
        balanceTextView.setText(String.format("Balance: %.2f", balance));
    }

    private void printBill() {
        // Implement printing logic here, like sending data to a printer or showing a print preview
        Toast.makeText(this, "Bill printed successfully!", Toast.LENGTH_SHORT).show();
    }


    private void initializeHeader(){
        TableRow headerRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.billing_row, billingTableLayout, false);
        TextView headerItemName = headerRow.findViewById(R.id.itemName);
        EditText headerItemPrice = headerRow.findViewById(R.id.itemPrice);
        EditText headerItemQuantity = headerRow.findViewById(R.id.itemQuantity);
        EditText headerItemTotal = headerRow.findViewById(R.id.itemTotal);
        headerItemName.setText("Item Name");
        headerItemPrice.setText("Price");
        headerItemQuantity.setText("Quantity");
        headerItemTotal.setText("Total");
        headerItemName.setFocusable(false);
        headerItemName.setClickable(false);

        headerItemPrice.setFocusable(false);
        headerItemPrice.setClickable(false);

        headerItemQuantity.setFocusable(false);
        headerItemQuantity.setClickable(false);

        headerItemTotal.setFocusable(false);
        headerItemTotal.setClickable(false);
        billingTableLayout.addView(headerRow);
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

    private void setupPrintBillButton() {
        Button printBillButton = findViewById(R.id.printBillButton);
        printBillButton.setOnClickListener(v -> printBill());
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
}
