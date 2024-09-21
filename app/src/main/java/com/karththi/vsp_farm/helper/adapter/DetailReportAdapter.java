package com.karththi.vsp_farm.helper.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.helper.AppConstant;

import java.util.List;

public class DetailReportAdapter extends RecyclerView.Adapter<DetailReportAdapter.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<BillItemsDetailDto> items;

    public DetailReportAdapter(List<BillItemsDetailDto> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;  // Header at position 0
        }
        return VIEW_TYPE_ITEM;  // Data items
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_report_header, parent, false);  // Inflate header layout
            return new ViewHolder(view, VIEW_TYPE_HEADER);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detail_report_row, parent, false);  // Inflate item layout
            return new ViewHolder(view, VIEW_TYPE_ITEM);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            // Leave the header as is, no binding needed
            return;
        }

        // Adjust position to skip the header (position - 1)
        BillItemsDetailDto dto = items.get(position - 1);
        holder.dateColumn.setText(dto.getCreatedAt());
        holder.timeColumn.setText(dto.getCreateTime());
        holder.userColumn.setText(dto.getUserName());
        holder.itemColumn.setText(dto.getItemName());
        holder.subItemColumn.setText(dto.getSubItemName());
        holder.referenceNumberColumn.setText(dto.getReferenceNumber());
        holder.customerColumn.setText(dto.getCustomerName());
        holder.paymentColumn.setText(dto.getPaymentMethod());
        holder.statusColumn.setText(dto.getStatus());
        holder.discountColumn.setText(String.valueOf(dto.getDiscount()));
        holder.quantityColumn.setText(String.valueOf(dto.getQuantity()));
        holder.priceColumn.setText(String.valueOf(dto.getBillItemPrice()));

        // Set colors for Loan and Deleted bills
        if (AppConstant.LOAN.equals(dto.getPaymentMethod())) {
            holder.paymentColumn.setTextColor(Color.parseColor("#b2102b"));
        }
        if (AppConstant.DELETED.equals(dto.getStatus())) {
            holder.statusColumn.setTextColor(Color.parseColor("#b2102b"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;  // Add 1 for the header
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateColumn, timeColumn, userColumn, itemColumn, subItemColumn,
                referenceNumberColumn, customerColumn, paymentColumn, statusColumn,
                discountColumn, quantityColumn, priceColumn;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_ITEM) {
                dateColumn = itemView.findViewById(R.id.dateColumn);
                timeColumn = itemView.findViewById(R.id.timeColumn);
                userColumn = itemView.findViewById(R.id.userColumn);
                itemColumn = itemView.findViewById(R.id.itemColumn);
                subItemColumn = itemView.findViewById(R.id.subItemColumn);
                referenceNumberColumn = itemView.findViewById(R.id.referenceNumberColumn);
                customerColumn = itemView.findViewById(R.id.customerColumn);
                paymentColumn = itemView.findViewById(R.id.paymentColumn);
                statusColumn = itemView.findViewById(R.id.statusColumn);
                discountColumn = itemView.findViewById(R.id.discountColumn);
                quantityColumn = itemView.findViewById(R.id.quantityColumn);
                priceColumn = itemView.findViewById(R.id.priceColumn);
            }
        }
    }
}
