package com.karththi.vsp_farm.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

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
    public static final String COMPANY_NAME = "VSP Farm";


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

}
