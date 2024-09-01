package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.model.BillItem;

import java.util.List;

public class BillItemRepository {

    private DbHelper dbHelper;

    public BillItemRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addBillItem(BillItem billItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_id", billItem.getBillId());
        values.put("sub_item_id", billItem.getSubItemId());
        values.put("quantity", billItem.getQuantity());
        values.put("price", billItem.getPrice());
        db.insert(DbHelper.BILL_ITEM_TABLE, null, values);
        db.close();

    }

    public void updateBillItem(BillItem billItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_id", billItem.getBillId());
        values.put("sub_item_id", billItem.getSubItemId());
        values.put("quantity", billItem.getQuantity());
        values.put("price", billItem.getPrice());
        db.update(DbHelper.BILL_ITEM_TABLE, values, "id = ?", new String[]{String.valueOf(billItem.getId())});
        db.close();
    }

    public void deleteBillItem(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.BILL_ITEM_TABLE, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public BillItem getBillItemById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_ITEM_TABLE, new String[]{"id", "bill_id", "sub_item_id", "quantity", "price"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        BillItem billItem = null;
        if (cursor.moveToFirst()) {
            billItem = new BillItem();
            billItem.setId(cursor.getInt(0));
            billItem.setBillId(cursor.getInt(1));
            billItem.setSubItemId(cursor.getInt(2));
            billItem.setQuantity(cursor.getDouble(3));
            billItem.setPrice(cursor.getDouble(4));
        }
        cursor.close();
        db.close();
        return billItem;
    }

    public List<BillItem> getBillItemByBillId(int billId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_ITEM_TABLE, new String[]{"id", "bill_id", "sub_item_id", "quantity", "price"}, "bill_id = ?", new String[]{String.valueOf(billId)}, null, null, null);
        List<BillItem> billItems = null;
        if (cursor.moveToFirst()) {
            do {
                BillItem billItem = new BillItem();
                billItem.setId(cursor.getInt(0));
                billItem.setBillId(cursor.getInt(1));
                billItem.setSubItemId(cursor.getInt(2));
                billItem.setQuantity(cursor.getDouble(3));
                billItem.setPrice(cursor.getDouble(4));
                billItems.add(billItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billItems;
    }


}
