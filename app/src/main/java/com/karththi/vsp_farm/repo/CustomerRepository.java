package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_DESCRIPTION = "description";

    private DbHelper dbHelper;

    public CustomerRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addCustomer(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_MOBILE, customer.getMobile());
        values.put(COLUMN_DESCRIPTION, customer.getDescription());
        db.insert(AppConstant.CUSTOMER_TABLE, null, values);
    }

    public void deleteCustomer(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AppConstant.CUSTOMER_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void updateCustomer(Customer customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_MOBILE, customer.getMobile());
        values.put(COLUMN_DESCRIPTION, customer.getDescription());
        db.update(AppConstant.CUSTOMER_TABLE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(customer.getId())});
    }

    public Customer getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(AppConstant.CUSTOMER_TABLE, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                customer.setMobile(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE)));
                customer.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(AppConstant.CUSTOMER_TABLE, null, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    customer.setMobile(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE)));
                    customer.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    customers.add(customer);
                } while (cursor.moveToNext());
            }
        }
        return customers;
    }

    // check customer exists with name
    public boolean checkCustomerExists(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(AppConstant.CUSTOMER_TABLE, null, COLUMN_NAME + " = ?", new String[]{name}, null, null, null)) {
            return cursor != null && cursor.moveToFirst();
        }
    }

    public Customer getByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(AppConstant.CUSTOMER_TABLE, null, COLUMN_NAME + " = ?", new String[]{name}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                customer.setMobile(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE)));
                customer.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                return customer;
            }
        }
        return null;
    }
}