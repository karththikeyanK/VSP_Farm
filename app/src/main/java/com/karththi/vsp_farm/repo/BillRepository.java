package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    private DbHelper dbHelper;
    public BillRepository(Context context) {
        dbHelper = new DbHelper(context);
    }



    public void createBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reference_number", bill.getReferenceNumber());
        values.put("total_amount", bill.getTotalAmount());
        values.put("customer_id", bill.getCustomerId());
        values.put("user_id", bill.getUserId());
        values.put("status", bill.getStatus());
        values.put("payment_methode", bill.getPaymentMethod());
        values.put("created_at", bill.getCreatedDate());
        values.put("create_time", bill.getCreateTime());
        values.put("updated_at", bill.getUpdatedDate());
        values.put("update_time", bill.getUpdateTime());
        db.insert(DbHelper.BILL_TABLE, null, values);
        db.close();
    }

    public void updateBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reference_number", bill.getReferenceNumber());
        values.put("total_amount", bill.getTotalAmount());
        values.put("customer_id", bill.getCustomerId());
        values.put("user_id", bill.getUserId());
        values.put("status", bill.getStatus());
        values.put("payment_methode", bill.getPaymentMethod());
        values.put("created_at", bill.getCreatedDate());
        values.put("create_time", bill.getCreateTime());
        values.put("updated_at", bill.getUpdatedDate());
        values.put("update_time", bill.getUpdateTime());
        values.put("modified_by", bill.getModifiedBy());
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
        Cursor cursor = db.query(DbHelper.BILL_TABLE,
                new String[]{"id", "total_amount", "customer_id", "user_id", "status", "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"},
                "customer_id = ?",
                new String[]{String.valueOf(customerId)},
                null,
                null,
                null);

        Bill bill = null;
        if (cursor.moveToFirst()) {
            // Adjust the order to match the Bill constructor
            bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));

        }
        cursor.close();
        db.close();
        return bill;
    }


    //get by user id
    public List<Bill> getBillByUserIdAndDate(int userId, String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        List<Bill> bills = null;
        if (cursor.moveToFirst()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }

    public Bill getBillById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Bill bill = null;
        if (cursor.moveToFirst()) {
            bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
        }
        cursor.close();
        db.close();
        return bill;
    }

    // get all bills by date
    public List<Bill> getBillsByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "created_at = ?", new String[]{date}, null, null, null);
        List<Bill> bills = null;
        if (cursor.moveToFirst()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }

    List<Bill> getAllByStatusAndDate(String status, String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "status = ? AND created_at = ?", new String[]{status, date}, null, null, null);
        List<Bill> bills = null;
        if (cursor.moveToFirst()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }

    public List<Bill> getAllByMonth(String month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "strftime('%m', created_at) = ?", new String[]{month}, null, null, null);
        List<Bill> bills = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }

    public List<Bill> getAllByMonthAndPaymentMethod(String month, String paymentMethod) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "strftime('%m', created_at) = ? AND payment_methode = ?", new String[]{month, paymentMethod}, null, null, null);
        List<Bill> bills = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }

    public List<Bill> getAllByCustomerIdAndPaymentMethodeAndDate(int customerId, String paymentMethode, String month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "customer_id = ? AND payment_methode = ? AND strftime('%m', created_at) =  ?", new String[]{String.valueOf(customerId), paymentMethode, month}, null, null, null);
        List<Bill> bills = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bill bill = new Bill();
            bill.setId(cursor.getInt(0));
            bill.setTotalAmount(cursor.getDouble(1));
            bill.setCustomerId(cursor.getInt(2));
            bill.setUserId(cursor.getInt(3));
            bill.setStatus(cursor.getString(4));
            bill.setPaymentMethod(cursor.getString(5));
            bill.setCreatedDate(cursor.getString(6));
            bill.setCreateTime(cursor.getString(7));
            bill.setUpdatedDate(cursor.getString(8));
            bill.setUpdateTime(cursor.getString(9));
            bill.setModifiedBy(cursor.getString(10));
            bill.setReferenceNumber(cursor.getString(11));
            bills.add(bill);
        }
        cursor.close();
        db.close();
        return bills;
    }
}
