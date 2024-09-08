package com.karththi.vsp_farm.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.BillItem;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.service.CustomerService;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.List;

public class BillItemAdapter {

    private Context context;
    private TableLayout tableLayout;
    private List<BillItem> billItems;

    private SubItemService subItemService;

    public BillItemAdapter(Context context, TableLayout tableLayout, List<BillItem> billItems) {
        this.context = context;
        this.tableLayout = tableLayout;
        this.billItems = billItems;
        subItemService = new SubItemService(context);
    }

    public void populateTable() {
        // Clear existing rows (if any)
        tableLayout.removeAllViews();

        // Add the table header
        TableRow headerRow = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_bill_items_header, null);
        tableLayout.addView(headerRow);


        // Populate the table with bill items
        for (BillItem item : billItems) {
            TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.table_bill_items_row, null);

            // Item Name
            TextView itemName = row.findViewById(R.id.item_name);
            itemName.setText(getSubItemName(item.getSubItemId())); // Assume a method to get sub-item name

            // Quantity
            TextView quantity = row.findViewById(R.id.item_quantity);
            quantity.setText(String.valueOf(item.getQuantity()));

            // Price
            TextView price = row.findViewById(R.id.item_price);
            price.setText(String.format("LKR %.2f", item.getPrice()));

            // Discount (Per Unit)
            TextView discount = row.findViewById(R.id.item_discount);
            discount.setText(String.format("LKR %.2f", item.getDiscount()));

            // Add the row to the table
            tableLayout.addView(row);
        }
    }

    private String getSubItemName(int subItemId) {
        SubItem subItem = subItemService.getSubItemById(subItemId);
        return subItem.getSubItemName();
    }
}
