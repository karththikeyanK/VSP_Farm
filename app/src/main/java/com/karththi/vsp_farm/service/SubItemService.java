package com.karththi.vsp_farm.service;

import android.content.Context;
import android.util.Log;

import com.karththi.vsp_farm.dto.PrintItemDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.repo.SubItemRepository;

import java.util.List;

public class SubItemService {
    private Context context;
    private SubItemRepository subItemRepository;
    private AppConstant appConstant;

    private static final String TAG = "SubItemService";

    public SubItemService(Context context) {
        this.context = context;
        appConstant = new AppConstant(context);
        this.subItemRepository = new SubItemRepository(context);
    }

    public void create(SubItem subItem) {
        Log.d(TAG, TAG+"::create()::Adding sub item");
        if (subItemRepository.isSubItemExists(subItem.getSubItemName())) {
            Log.d(TAG, TAG+"::create()::Sub item already exists");
            appConstant.ShowAlert(AppConstant.ERROR,"Sub Item already exist with the name: "+subItem.getSubItemName());
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

    public void update(SubItem subItem, SubItem old) {
        Log.d(TAG, TAG+"::update()::Updating sub item");
        if(isExistByName(subItem.getSubItemName())  && !old.getSubItemName().equals(subItem.getSubItemName())){
            appConstant.ShowAlert(AppConstant.ERROR, "Sub Item already exist with the name: " + subItem.getSubItemName());
        }
        subItemRepository.updateSubItem(subItem);
        Log.d(TAG, TAG+"::update()::Sub item updated successfully");
    }

    public SubItem getSubItemById(int id) {
        Log.d(TAG, TAG + "::getSubItemById()::Getting sub item by id");
        SubItem subItem = subItemRepository.getSubItemById(id);
        Log.d(TAG, TAG + "::getSubItemById()::Sub item fetched successfully");
        return subItem;
    }

    public boolean isExistByName(String name){
        return subItemRepository.isSubItemExists(name);
    }

    public PrintItemDto getPrintItem(int subItemId){
        Log.d(TAG, TAG + "::getPrintItem()::Getting print item by sub item id "+subItemId);
        PrintItemDto printItemDto = subItemRepository.getPrintItem(subItemId);
        Log.d(TAG, TAG + "::getPrintItem()::Print item fetched successfully");
        return printItemDto;
    }

    public List<SubItem> getAllSubItems() {
        Log.d(TAG, TAG + "::getAllSubItems()::Getting all sub items");
        List<SubItem> subItems = subItemRepository.getAllSubItems();
        Log.d(TAG, TAG + "::getAllSubItems()::Sub items fetched successfully");
        return subItems;
    }
}
