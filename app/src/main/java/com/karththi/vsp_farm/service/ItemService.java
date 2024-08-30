package com.karththi.vsp_farm.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.model.Measurement;
import com.karththi.vsp_farm.page.admin.item.ItemListActivity;
import com.karththi.vsp_farm.repo.ItemRepository;

import java.io.ByteArrayOutputStream;

public class ItemService {

    private Context context;

    private ItemRepository itemRepository;
    public ItemService(Context context) {
        this.context = context;
        itemRepository = new ItemRepository(context);
    }

    public void addItem(Item item) {
        Log.i("ItemService", "ItemService::addItem():: is called");
        if (itemRepository.isItemExists(item.getName())) {
            Log.i("ItemService", "ItemService::addItem():: item already exists");
            Toast.makeText(context, "Item already exists with name : "+ item.getName(), Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            itemRepository.addItem(item);
            Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ItemListActivity.class);
            context.startActivity(intent);
        }catch (Exception e){
            Log.e("ItemService", "Error while adding item", e);
            Toast.makeText(context, "Error while adding item", Toast.LENGTH_SHORT).show();
        }
        Log.i("ItemService", "ItemService::addItem():: is successfully added");
    }

    public void update(String name, String measurement, Drawable itemImageView, Item item){
        Log.i("ItemService", "ItemService::update():: is called");
        if (itemRepository.isItemExists(name)) {
            Log.i("ItemService", "ItemService::addItem():: item already exists");
            Toast.makeText(context, "Item already exists with name : "+ name, Toast.LENGTH_SHORT).show();
            return;
        }
        item.setName(name);
        item.setMeasurement(Measurement.valueOf(measurement.toUpperCase()));

        Bitmap bitmap = ((BitmapDrawable) itemImageView).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        item.setImage(stream.toByteArray());

        itemRepository.updateItem(item);
        Log.i("ItemService", "ItemService::update():: is successfully updated");
        Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ItemListActivity.class);
        context.startActivity(intent);
    }

    public void deleteItem(Item item) {
        Log.i("ItemService", "ItemService::deleteItem():: is called");
        itemRepository.deleteItem(item);
        Log.i("ItemService", "ItemService::deleteItem():: is successfully deleted");
        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ItemListActivity.class);
        context.startActivity(intent);
    }

    public Item getById(int id) {
        Log.i("ItemService", "ItemService::getById():: is called");
        Item item = itemRepository.getById(id);
        Log.i("ItemService", "ItemService::getById():: is successfully fetched");
        return item;
    }


}
