package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.repo.ItemRepository;

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
            Toast.makeText(context, "Item already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            itemRepository.addItem(item);
            Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("ItemService", "Error while adding item", e);
            Toast.makeText(context, "Error while adding item", Toast.LENGTH_SHORT).show();
        }
        Log.i("ItemService", "ItemService::addItem():: is successfully added");
    }
}
