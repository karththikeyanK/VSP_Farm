package com.karththi.vsp_farm.helper.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.karththi.vsp_farm.R;

public class LoadingDialog {
    private AlertDialog dialog;
    private TextView messageTextView;

    public LoadingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout for the loading dialog
        View view = inflater.inflate(R.layout.loading_popup, null);

        // Initialize the message TextView from the custom layout
        messageTextView = view.findViewById(R.id.loading_message);

        builder.setView(view);
        builder.setCancelable(false);  // Prevent dialog dismissal when touching outside

        // Create the dialog
        dialog = builder.create();
    }

    // Show the loading dialog with a message
    public void show(String message) {
        messageTextView.setText(message);
        dialog.show();
    }

    // Dismiss the loading dialog
    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
