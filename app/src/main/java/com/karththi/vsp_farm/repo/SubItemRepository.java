package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.SubItem;

import java.util.ArrayList;
import java.util.List;

public class SubItemRepository {
    private DbHelper dbHelper;

    public SubItemRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addSubItemList(List<SubItem> subItems) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (SubItem subItem : subItems) {
                ContentValues values = new ContentValues();
                values.put("item_id", subItem.getItemId());
                values.put("name", subItem.getSubItemName());
                values.put("price", subItem.getPrice());
                db.insert(AppConstant.SUB_ITEM_TABLE, null, values);
            }
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

    public SubItem getSubItemByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.SUB_ITEM_TABLE, null, "name = ?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
                cursor.close();
                return new SubItem(id, name, price, itemId);
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
}
