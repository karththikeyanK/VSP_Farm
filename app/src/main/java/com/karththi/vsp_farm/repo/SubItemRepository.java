package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.PrintItemDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.SubItem;

import java.util.ArrayList;
import java.util.List;

public class SubItemRepository {
    private DbHelper dbHelper;

    public SubItemRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addSubItemList(SubItem subItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("item_id", subItem.getItemId());
            values.put("name", subItem.getSubItemName());
            values.put("price", subItem.getPrice());
            db.insert(AppConstant.SUB_ITEM_TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteSubItem(SubItem subItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AppConstant.SUB_ITEM_TABLE, "name = ?", new String[]{subItem.getSubItemName()});
        db.close();
    }

    public void updateSubItem(SubItem subItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_id", subItem.getItemId());
        values.put("name", subItem.getSubItemName());
        values.put("price", subItem.getPrice());
        db.update(AppConstant.SUB_ITEM_TABLE, values, "id = ?", new String[]{String.valueOf(subItem.getId())});
        db.close();
    }

    public SubItem getSubItemById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.SUB_ITEM_TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int subItemId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
                cursor.close();
                return new SubItem(subItemId, name, price, itemId);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<SubItem> getAllByItemId(int itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.SUB_ITEM_TABLE, null, "item_id = ?", new String[]{String.valueOf(itemId)}, null, null, "name ASC");
        List<SubItem> subItemList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                subItemList.add(new SubItem(id, name, price, itemId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return subItemList;
    }

    public boolean isSubItemExists(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.SUB_ITEM_TABLE, null, "name = ?", new String[]{name}, null, null, null);
        boolean isExists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isExists;
    }



    // get sub item name, sub item price , item measurement
    public PrintItemDto getPrintItem(int subItemId){
        PrintItemDto printItemDto = null;
        Log.d("SubItemRepository", "getPrintItem: subItemId: " + subItemId);
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT si.name, si.price, i.measurement, i.name FROM "+AppConstant.SUB_ITEM_TABLE+" si INNER JOIN "+AppConstant.ITEM_TABLE+" i ON si.item_id = i.id WHERE si.id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(subItemId)});
            if (cursor != null && cursor.moveToFirst()) {
                String subItemName = cursor.getString(0);
                Double subItemPrice = cursor.getDouble(1);
                String itemMeasurement = cursor.getString(2);
                String itemName = cursor.getString(3);
               printItemDto = new PrintItemDto();
                printItemDto.setItemName(subItemName);
                printItemDto.setUnitPrice(subItemPrice);
                printItemDto.setMeasurement(itemMeasurement);
                printItemDto.setName(itemName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SubItemRepository", "getPrintItem: ", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return printItemDto;
    }

}
