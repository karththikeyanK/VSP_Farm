package com.karththi.vsp_farm.helper;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public static final String LOAN = "CREDIT";
    public static final String CASH = "CASH";
    public static final String DEFAULT = "DEFAULT";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    public static  String PRINTER_TARGET = "";

    public static Integer USER_TABLE_ID =null;
    public static String USER_ID = "";
    public static String USER_NAME = "";

    public static String USER_ROLE = "";

    public static final String DELETED = "DELETED";
    public static final String MODIFIED = "MODIFIED";

    public static final String MODIFIED_ORIGINAL = "MODIFIED_ORIGINAL";
    public static final String NEW = "NEW";

    public static final String ADMIN = "ADMIN";
    public static final String CASHIER = "CASHIER";

    public static final String LOAN_PAYMENT_FOLDER = "LoanPayment";
    public static final String TODAY_SUMMARY_FOLDER = "TodaySummary";
    public static final String TODAY_DETAIL_FOLDER = "TodayDetail";

    public static final String GET_SUMMARY_FOLDER = "GetSummary";
    public static final String GET_DETAIL_FOLDER = "GetDetail";

    public static final String GET_CUSTOMER_FOLDER = "Customer";

    private Context context;

    public AppConstant(Context context) {
        this.context = context;
    }

    public void ShowAlert(String title, String message) {
        ((Activity) context).runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            builder.show();
        });
    }

    public void SuccessAlert(String type,String message, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(type);
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
        // Get the counter from Shared Preferences
        SharedPreferences prefs = context.getSharedPreferences("prefs", MODE_PRIVATE);
        int counter = prefs.getInt("counter", 1001);

        // Increment the counter and save it back to Shared Preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("counter", counter + 1);
        editor.apply();

        // Get the current date and format it as ddMMyy
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String formattedDate = formatter.format(date);

        // Combine the formatted date and counter to form the reference number
        String referenceNumberStr = formattedDate + " " + String.valueOf(counter);
        return referenceNumberStr;
    }

    public String formatAmount(double total) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String formattedTotal = nf.format(total);
        return formattedTotal.replace(',', ' ');
    }



}
