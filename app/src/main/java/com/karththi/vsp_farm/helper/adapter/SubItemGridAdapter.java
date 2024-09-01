package com.karththi.vsp_farm.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.SubItem;

import java.util.List;

public class SubItemGridAdapter extends BaseAdapter {

    private Context context;
    private List<SubItem> subItemList;

    public SubItemGridAdapter(Context context, List<SubItem> subItemList) {
        this.context = context;
        this.subItemList = subItemList;
    }

    @Override
    public int getCount() {
        return subItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return subItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_sub_item, parent, false);
        }

        // Get the current sub-item
        SubItem subItem = subItemList.get(position);

        // Set the sub-item name and image (if any)
        TextView subItemNameTextView = convertView.findViewById(R.id.subItemNameTextView);
        ImageView subItemImageView = convertView.findViewById(R.id.subItemImageView);

        subItemNameTextView.setText(subItem.getSubItemName());


        return convertView;
    }
}
