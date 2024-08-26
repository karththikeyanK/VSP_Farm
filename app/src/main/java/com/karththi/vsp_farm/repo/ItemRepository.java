package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.model.Measurement;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private DbHelper dbHelper;


    public ItemRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addItem(Item item) throws Exception {
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("measurement", item.getMeasurement().toString());
            values.put("image", item.getImage());
            db.insert(AppConstant.ITEM_TABLE, null, values);
            db.close();
        }catch (Exception e){
            Log.e("ItemRepository", "Error while adding item", e);

        }
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AppConstant.ITEM_TABLE, "name = ?", new String[]{item.getName()});
        db.close();
    }

    public void updateItem(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("measurement", item.getMeasurement().toString());
        values.put("image", item.getImage());
        db.update(AppConstant.ITEM_TABLE, values, "id = ?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public Item getItemByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.ITEM_TABLE, null, "name = ?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String measurement = cursor.getString(cursor.getColumnIndexOrThrow("measurement"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                cursor.close();
                return new Item(id,name, Measurement.valueOf(measurement), image);
            } catch (IllegalArgumentException e) {
                Log.e("ItemRepository", "Column does not exist", e);
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.ITEM_TABLE, null, null, null, null, null, "name ASC");
        List<Item> itemList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String measurement = cursor.getString(cursor.getColumnIndexOrThrow("measurement"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                itemList.add(new Item(id ,name, Measurement.valueOf(measurement), image));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return itemList;
    }

    public boolean isItemExists(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.ITEM_TABLE, null, "name = ?", new String[]{name}, null, null, null);
        boolean isExists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isExists;
    }
}
