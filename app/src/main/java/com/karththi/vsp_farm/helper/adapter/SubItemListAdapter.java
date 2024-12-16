package com.karththi.vsp_farm.helper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.page.admin.item.EditSubItemActivity;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.List;

public class SubItemListAdapter extends BaseAdapter {

    private Context context;
    private List<SubItem> subItems;
    private LayoutInflater inflater;
    private SubItemService subItemService;

    public SubItemListAdapter(Context context, List<SubItem> subItems, SubItemService subItemService) {
        this.context = context;
        this.subItems = subItems;
        this.inflater = LayoutInflater.from(context);
        this.subItemService = subItemService;
    }

    @Override
    public int getCount() {
        return subItems.size();
    }

    @Override
    public Object getItem(int position) {
        return subItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subItems.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subitem_list_row, parent, false);
        }

        TextView subItemNameTextView = convertView.findViewById(R.id.subItemNameTextView);
        TextView subItemPriceTextView = convertView.findViewById(R.id.subItemPriceTextView);
        TextView subItemStatusTextView = convertView.findViewById(R.id.subItemStatusTextView);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button statusButton = convertView.findViewById(R.id.statusButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        final SubItem subItem = subItems.get(position);

        subItemNameTextView.setText(subItem.getSubItemName());
        subItemPriceTextView.setText(String.valueOf(subItem.getPrice()));
        subItemStatusTextView.setText(subItem.getStatus());

        if ("ENABLE".equals(subItem.getStatus())) {
            statusButton.setText("DISABLE");
            statusButton.setTextColor(context.getResources().getColor(R.color.holo_red_dark));
        } else {
            statusButton.setText("ENABLE");
            statusButton.setTextColor(context.getResources().getColor(R.color.green));
        }

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubItem updatedSubItem = new SubItem();
                updatedSubItem.setId(subItem.getId());
                updatedSubItem.setSubItemName(subItem.getSubItemName());
                updatedSubItem.setPrice(subItem.getPrice());
                updatedSubItem.setItemId(subItem.getItemId());
                if ("ENABLE".equals(subItem.getStatus())) {
                    updatedSubItem.setStatus("DISABLE");
                } else {
                    updatedSubItem.setStatus("ENABLE");
                }
                if (subItemService.update(updatedSubItem, subItem)) {
                    subItems.set(position, updatedSubItem);
                }
                notifyDataSetChanged();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditSubItemActivity.class);
                intent.putExtra("SUB_ITEM_ID", subItem.getId());
                context.startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Sub Item");
                builder.setMessage("Are you sure you want to delete this sub item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (subItemService.delete(subItem)) {
                            subItems.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Sub Item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete sub item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        if (AppConstant.USER_ROLE.equals(AppConstant.CASHIER)) {
            if (!AppConstant.EDIT_SUB_ITEM_PERMISSION) {
                editButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
            }

            if (!AppConstant.DISABLE_OR_ENABLE_ITEM_PERMISSION) {
                statusButton.setEnabled(false);
            } else {
                statusButton.setEnabled(true);
            }
            deleteButton.setEnabled(false);
        }else {
            editButton.setEnabled(true);
            statusButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }

        return convertView;
    }
}
