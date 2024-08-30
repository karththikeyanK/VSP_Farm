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
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        final SubItem subItem = subItems.get(position);

        subItemNameTextView.setText(subItem.getSubItemName());
        subItemPriceTextView.setText(String.valueOf(subItem.getPrice()));

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
                new AlertDialog.Builder(context)
                        .setTitle("Delete Sub-Item")
                        .setMessage("Are you sure you want to delete this sub-item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                subItemService.delete(subItem);
                                subItems.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Sub-Item deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return convertView;
    }
}
