package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.model.Bill;

public class BillRepository {
    private DbHelper dbHelper;
    public BillRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    /*
    private static final String CREATE_BILL_TABLE = "CREATE TABLE "+BILL_TABLE+" (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "total_amount DOUBLE, " +
            "customer_id INTEGER, " +
            "user_id INTEGER, " +
            "FOREIGN KEY(customer_id) REFERENCES Customer(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE, " +
            "FOREIGN KEY(user_id) REFERENCES " + USER_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";
     */

    public void createBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("total_amount", bill.getTotalAmount());
        values.put("customer_id", bill.getCustomerId());
        values.put("user_id", bill.getUserId());
        db.insert(DbHelper.BILL_TABLE, null, values);
        db.close();
    }

    public void updateBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("total_amount", bill.getTotalAmount());
        values.put("customer_id", bill.getCustomerId());
        values.put("user_id", bill.getUserId());
        db.update(DbHelper.BILL_TABLE, values, "id = ?", new String[]{String.valueOf(bill.getId())});
        db.close();
    }

    public void deleteBill(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.BILL_TABLE, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    //get by customer id
    public Bill getBillByCustomerId(int customerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id"}, "customer_id = ?", new String[]{String.valueOf(customerId)}, null, null, null);
        Bill bill = null;
        if (cursor.moveToFirst()) {
            bill = new Bill(cursor.getInt(0), cursor.getDouble(1), cursor.getInt(2), cursor.getInt(3), "NEW");
        }
        cursor.close();
        db.close();
        return bill;
    }

    //get by user id
    public Bill getBillByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        Bill bill = null;
        if (cursor.moveToFirst()) {
            bill = new Bill(cursor.getInt(0), cursor.getDouble(1), cursor.getInt(2), cursor.getInt(3), "NEW");
        }
        cursor.close();
        db.close();
        return bill;
    }

    public Bill getBillById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Bill bill = null;
        if (cursor.moveToFirst()) {
            bill = new Bill(cursor.getInt(0), cursor.getDouble(1), cursor.getInt(2), cursor.getInt(3), "NEW");
        }
        cursor.close();
        db.close();
        return bill;
    }




}
