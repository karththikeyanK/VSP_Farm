package com.karththi.vsp_farm.helper.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Bill;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.Customer;
import com.karththi.vsp_farm.page.cashier.BillDetailsActivity;
import com.karththi.vsp_farm.service.BillService;
import com.karththi.vsp_farm.service.CustomerService;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> bills;
    private Context context;

    private CustomerService customerService;


    public BillAdapter(Context context, List<Bill> bills) {
        this.context = context;
        this.bills = bills;
        customerService = new CustomerService(context);
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false);
        return new BillViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        Customer customer = customerService.getCustomerById(bills.get(position).getCustomerId());
        Bill bill = bills.get(position);
        holder.referenceNumber.setText(bill.getReferenceNumber());
        holder.totalAmount.setText(String.valueOf(bill.getTotalAmount()));
        holder.paymentMethod.setText(bill.getPaymentMethod());
        if (customer != null) {
            holder.customerName.setText(customer.getName());
        } else {
            holder.customerName.setText("Unknown");
        }
        holder.dateTime.setText(bill.getCreatedDate() + " " + bill.getCreateTime());

        if (bill.getPaymentMethod().equals(AppConstant.LOAN)){
            holder.paymentMethod.setTextColor(Color.parseColor("#b2102b"));
        }

        Log.i("BillSatus:",bill.getStatus());


        holder.itemView.setOnClickListener(v -> {
            // Send the bill ID to BillDetailsActivity
            Intent intent = new Intent(context, BillDetailsActivity.class);
            intent.putExtra("bill_id", bill.getId());  // Pass the bill ID
            intent.putExtra("customer_name", customer.getName());
            intent.putExtra("reference_number", bill.getReferenceNumber());
            intent.putExtra("total_amount", bill.getTotalAmount().toString());
            intent.putExtra("payment_method", bill.getPaymentMethod());
            intent.putExtra("created_date", holder.dateTime.getText());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        TextView referenceNumber, totalAmount, customerName, dateTime, paymentMethod;

        BillViewHolder(View itemView) {
            super(itemView);
            referenceNumber = itemView.findViewById(R.id.reference_number);
            totalAmount = itemView.findViewById(R.id.total_amount);
            customerName = itemView.findViewById(R.id.customer_name);
            dateTime = itemView.findViewById(R.id.date_time);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);

        }
    }
}