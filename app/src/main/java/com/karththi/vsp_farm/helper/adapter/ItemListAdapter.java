package com.karththi.vsp_farm.helper.adapter;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.page.admin.item.EditItemActivity;
import com.karththi.vsp_farm.page.admin.item.SubItemListActivity;
import com.karththi.vsp_farm.service.ItemService;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private List<Item> items;
    private LayoutInflater inflater;

    private ItemService itemService;

    private LinearLayout itemLayout;

    public ItemListAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.itemService = new ItemService(context);
        itemLayout = new LinearLayout(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_row, parent, false);
        }

        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        itemLayout = convertView.findViewById(R.id.itemListRowLayout);

        final Item item = items.get(position);

        // Set item image
        byte[] imageBytes = item.getImage();
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            itemImageView.setImageBitmap(bitmap);
        }

        // Set item name
        itemNameTextView.setText(item.getName());

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubItemListActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                context.startActivity(intent);
            }
        });



        // Edit button functionality
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                context.startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform the deletion
                                itemService.deleteItem(item);
                                items.remove(position);
                                notifyDataSetChanged(); // Refresh the list
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

        });

        return convertView;
    }
}
