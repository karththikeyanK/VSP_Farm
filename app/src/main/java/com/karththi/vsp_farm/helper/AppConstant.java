package com.karththi.vsp_farm.helper;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.TextView;

import com.karththi.vsp_farm.helper.utils.DateTimeUtils;

import java.util.Random;
import java.util.function.Function;

public class AppConstant {
    public static final String DATABASE_NAME = "vsp_farm";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "users";

    public static final String ITEM_TABLE = "items";

    public static final String SUB_ITEM_TABLE = "sub_items";

    public static final String BILL_TABLE = "Bill";

    public static final String CUSTOMER_TABLE = "Customer";

    public static final String BILL_ITEM_TABLE = "BillItem";

    public static final String LOAN_TABLE = "Loan";

    public static final String LOAN_PAYMENT_TABLE = "LoanPayment";
    public static final String COMPANY_NAME = "VSP Farm";
    public static final String LOAN = "LOAN";
    public static final String CASH = "CASH";
    public static final String DEFAULT = "DEFAULT";


    public static String USER_ID = "";
    public static String USER_NAME = "";

    public static String USER_ROLE = "";

    public static final String DELETED = "DELETED";
    public static final String MODIFIED = "MODIFIED";

    public static final String MODIFIED_ORIGINAL = "MODIFIED_ORIGINAL";
    public static final String NEW = "NEW";

    public static final String ADMIN = "ADMIN";
    public static final String CASHIER = "CASHIER";

    private Context context;

    public AppConstant(Context context) {
        this.context = context;
    }

    public void ErrorAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public void SuccessAlert(String message, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Success");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> context.startActivity(intent));
        builder.show();
    }

    public void ConfirmAlert(String title, String message, Runnable onYes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (onYes != null) {
                onYes.run();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void showBalancePopup(String title, String balance, Runnable onOkClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        TextView balanceView = new TextView(context);
        balanceView.setText(balance);
        balanceView.setTextSize(48);
        balanceView.setGravity(Gravity.CENTER);
        builder.setView(balanceView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Run the function passed to the method when "OK" is clicked
            if (onOkClick != null) {
                onOkClick.run();
            }
        });

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set a dismiss listener
        dialog.setOnDismissListener(dialogInterface -> {
            // Run the function passed to the method when the dialog is dismissed
            if (onOkClick != null) {
                onOkClick.run();
            }
        });

        // Show the dialog
        dialog.show();
    }


    public String generateReferenceNumber() {
        // Generate a three-digit random number


        // Get the counter from Shared Preferences
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        int counter = prefs.getInt("counter", 1001);

        // Increment the counter and save it back to Shared Preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("counter", counter + 1);
        editor.apply();

        // Combine the random number and counter to form the reference number
        String referenceNumberStr = DateTimeUtils.getCurrentDate() +"   "+ String.valueOf(counter);
        return referenceNumberStr;
    }



}
