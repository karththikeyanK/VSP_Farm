package com.karththi.vsp_farm.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.LoanPaymentDto;

import java.util.List;

public class LoanPaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<LoanPaymentDto> loanPaymentList;

    public LoanPaymentAdapter(List<LoanPaymentDto> loanPaymentList) {
        this.loanPaymentList = loanPaymentList;
    }

    // ViewHolder for header
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateHeader, customerHeader, paymentHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            dateHeader = itemView.findViewById(R.id.date);
            customerHeader = itemView.findViewById(R.id.customer);
            paymentHeader = itemView.findViewById(R.id.payment);
        }
    }

    // ViewHolder for item row
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, customerTextView, paymentTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            customerTextView = itemView.findViewById(R.id.customer);
            paymentTextView = itemView.findViewById(R.id.payment);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Return type based on position (0 for header, others for items)
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loan_payment_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loan_payment_row, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            // You can leave this empty or set up static text for the header if needed
            // (Already set in XML, so nothing to bind here)
        } else {
            // Cast the holder to ItemViewHolder for data rows
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            // Adjust position by -1 to account for the header
            LoanPaymentDto loanPayment = loanPaymentList.get(position - 1);

            // Bind data to views
            itemViewHolder.dateTextView.setText(loanPayment.getPaymentDate());
            itemViewHolder.customerTextView.setText(loanPayment.getCustomerName());
            itemViewHolder.paymentTextView.setText(String.valueOf(loanPayment.getPaymentAmount()));
        }
    }

    @Override
    public int getItemCount() {
        // Add 1 to the item count to account for the header row
        return loanPaymentList.size() + 1;
    }
}
