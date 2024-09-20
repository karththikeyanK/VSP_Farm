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

    private List<BillItemsDetailDto> items;

    public DetailReportAdapter(List<BillItemsDetailDto> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_report_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BillItemsDetailDto dto = items.get(position);
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

        if (AppConstant.LOAN.equals(dto.getPaymentMethod())) holder.paymentColumn.setTextColor(Color.parseColor("#b2102b"));
        if (AppConstant.DELETED.equals(dto.getStatus())) holder.statusColumn.setTextColor(Color.parseColor("#b2102b"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateColumn, timeColumn, userColumn, itemColumn, subItemColumn,
                referenceNumberColumn, customerColumn, paymentColumn, statusColumn,
                discountColumn, quantityColumn, priceColumn;

        public ViewHolder(View itemView) {
            super(itemView);
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
