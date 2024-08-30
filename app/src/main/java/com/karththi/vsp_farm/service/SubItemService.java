package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.repo.SubItemRepository;

import java.util.List;

public class SubItemService {
    private Context context;
    private SubItemRepository subItemRepository;

    private static final String TAG = "SubItemService";

    public SubItemService(Context context) {
        this.context = context;
        this.subItemRepository = new SubItemRepository(context);
    }

    public void create(SubItem subItem) {
        Log.d(TAG, TAG+"::create()::Adding sub item");
        if (subItemRepository.isSubItemExists(subItem.getSubItemName())) {
            Log.d(TAG, TAG+"::create()::Sub item already exists");
            Toast.makeText(context, "Sub item already exists with name: "+subItem.getSubItemName(), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            subItemRepository.addSubItemList(subItem);
        } catch (Exception e) {
            Log.e(TAG, TAG+"::create()::Error while adding sub item", e);
        }
    }

    public List<SubItem> getSubItemsByItemId(int itemId) {
        Log.d(TAG, TAG+"::getSubItemsByItemId()::Getting sub items by item id");
        List<SubItem> subItems = subItemRepository.getAllByItemId(itemId);
        Log.d(TAG, TAG+"::getSubItemsByItemId()::Sub items fetched successfully");
        return subItems;
    }

    public void delete(SubItem subItem) {
        Log.d(TAG, TAG+"::delete()::Deleting sub item");
        subItemRepository.deleteSubItem(subItem);
        Log.d(TAG, TAG+"::delete()::Sub item deleted successfully");
    }

    public void update(SubItem subItem) {
        Log.d(TAG, TAG+"::update()::Updating sub item");
        subItemRepository.updateSubItem(subItem);
        Log.d(TAG, TAG+"::update()::Sub item updated successfully");
    }

    public SubItem getSubItemById(int id) {
        Log.d(TAG, TAG + "::getSubItemById()::Getting sub item by id");
        SubItem subItem = subItemRepository.getSubItemById(id);
        Log.d(TAG, TAG + "::getSubItemById()::Sub item fetched successfully");
        return subItem;
    }

}
