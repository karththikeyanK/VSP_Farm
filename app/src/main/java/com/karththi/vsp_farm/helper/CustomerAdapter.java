package com.karththi.vsp_farm.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.page.admin.CustomerListActivity;
import com.karththi.vsp_farm.page.admin.EditCustomerActivity;
import com.karththi.vsp_farm.service.CustomerService;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private final Context context;
    private final int resource;

    private CustomerService customerService;

    private AppConstant  appConstant;

    public CustomerAdapter(@NonNull Context context, int resource, @NonNull List<Customer> customers) {
        super(context, resource, customers);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        // Find views in rowView and set data
        TextView nameTextView = rowView.findViewById(R.id.customerNameTextView);
        TextView mobileTextView = rowView.findViewById(R.id.customerMobileTextView);
        TextView descriptionTextView = rowView.findViewById(R.id.customerDescriptionTextView);
        Button editButton = rowView.findViewById(R.id.editButton);
        Button deleteButton = rowView.findViewById(R.id.deleteButton);

        customerService = new CustomerService(context);
        appConstant = new AppConstant(this.context);

        // Set data to views
        Customer customer = getItem(position);
        nameTextView.setText(customer.getName());
        mobileTextView.setText(customer.getMobile());
        descriptionTextView.setText(customer.getDescription());

        // Set click listeners
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCustomerActivity.class);
            intent.putExtra("customerId", customer.getId());
            context.startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            appConstant.ConfirmAlert("Delete Customer", "Are You Sure?", () -> {
                customerService.deleteCustomer(customer.getId());
            });
        });

        return rowView;
    }
}

