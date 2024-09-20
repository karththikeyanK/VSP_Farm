package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
import com.karththi.vsp_farm.dto.BillSummary;
import com.karththi.vsp_farm.dto.Sale;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Bill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    private DbHelper dbHelper;
    public BillRepository(Context context) {
        dbHelper = new DbHelper(context);
    }



    public int createBill(Bill bill) {
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
        long id = db.insert(DbHelper.BILL_TABLE, null, values);
        db.close();
        return (int) id;
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

    public List<Bill> getBillsByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"},
                "created_at = ?", new String[]{date}, null, null, null);

        // Initialize the list before using it
        List<Bill> bills = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
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

                // Add the bill to the list
                bills.add(bill);
            } while (cursor.moveToNext());  // Move through all rows
        }

        cursor.close();
        db.close();

        return bills;  // Will return an empty list if no bills are found
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

    public List<Bill> getLoanBillsByCustomerIdAndDateRange(int customerId, String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE,
                new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"},
                "customer_id = ? AND payment_methode = ? AND created_at BETWEEN ? AND ?",
                new String[]{String.valueOf(customerId), AppConstant.LOAN, startDate, endDate},
                null, null, null);

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

    // get all by status and date and payment method
    public List<Bill> getAllByStatusAndDateAndPaymentMethod(String status, String date, String paymentMethod) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "status = ? AND created_at = ? AND payment_methode = ?", new String[]{status, date, paymentMethod}, null, null, null);
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

    public List<Bill> getAllDeletedBIllsByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_TABLE, new String[]{"id", "total_amount", "customer_id", "user_id","status",  "payment_methode","created_at","create_time","updated_at","update_time","modified_by","reference_number"}, "status = ? AND created_at = ?", new String[]{AppConstant.DELETED, date}, null, null, null);
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

    public List<Bill> getBillsBetweenDates(String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Modify query to fetch bills between two dates using BETWEEN clause
        Cursor cursor = db.query(DbHelper.BILL_TABLE,
                new String[]{"id", "total_amount", "customer_id", "user_id", "status", "payment_methode",
                        "created_at", "create_time", "updated_at", "update_time", "modified_by", "reference_number"},
                "created_at BETWEEN ? AND ?",
                new String[]{startDate, endDate},
                null, null, null);

        List<Bill> bills = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
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

                // Add the bill to the list
                bills.add(bill);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return bills; // Return the list of bills within the date range
    }

    public List<BillSummary> getSummaryByDateAndPaymentMethode(String date, String paymentMethode) {
        Log.i("BillRepository", "BillRepository::getSummaryByDateAndPaymentMethode()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        String sql = "SELECT " +
                "    sub.name AS sub_item_name," +
                "    it.name AS item_name," +
                "    SUM(bi.quantity) AS total_quantity," +
                "    SUM(bi.price ) AS total_price," +
                "    SUM(bi.discount*bi.quantity) AS total_discount " +
                "FROM "+AppConstant.BILL_ITEM_TABLE+" bi "+
                "INNER JOIN " +
                "   " +AppConstant.BILL_TABLE+" bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    "+AppConstant.SUB_ITEM_TABLE+" sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN  " +
                "    "+AppConstant.ITEM_TABLE+" it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at = ?  " +
                "    AND bill.status != '"+AppConstant.DELETED+"' " +
                "AND bill.payment_methode = ? "+
                "GROUP BY " +
                "    sub.name, it.name, it.id " +
                "ORDER BY  " +
                "    it.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{date,paymentMethode});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setSubItemName(cursor.getString(0));
                    dto.setItemName(cursor.getString(1));
                    dto.setTotalQuantity(cursor.getDouble(2));
                    dto.setTotalPrice(cursor.getDouble(3));
                    dto.setTotalDiscount(cursor.getDouble(4));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getSummaryByDateAndPaymentMethode()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public List<BillSummary> getSummaryByDate(String date) {
        Log.i("BillRepository", "BillRepository::getSummaryByDate()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total quantity, price, and discount by item
        String sql = "SELECT " +
                "    it.name AS item_name, " +
                "    SUM(bi.quantity) AS total_quantity, " +
                "    SUM(bi.price ) AS total_price, " +
                "    SUM(bi.discount * bi.quantity) AS total_discount " +
                "FROM " + AppConstant.BILL_ITEM_TABLE + " bi " +
                "INNER JOIN " +
                "    " + AppConstant.BILL_TABLE + " bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    " + AppConstant.SUB_ITEM_TABLE + " sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN " +
                "    " + AppConstant.ITEM_TABLE + " it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at = ? " +
                "    AND bill.status != '"+AppConstant.DELETED+"' " +
                "GROUP BY " +
                "    it.name, it.id " +
                "ORDER BY " +
                "    it.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{date});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setItemName(cursor.getString(0));        // Set the item name
                    dto.setSubItemName(null);                    // Set sub-item name as null
                    dto.setTotalQuantity(cursor.getDouble(1));   // Total quantity of all sub-items
                    dto.setTotalPrice(cursor.getDouble(2));      // Total price of all sub-items
                    dto.setTotalDiscount(cursor.getDouble(3));   // Total discount of all sub-items
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getSummaryByDate()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public List<BillSummary> getSummaryByDateRange(String startDate, String endDate) {
        Log.i("BillRepository", "BillRepository::getSummaryByDateRange()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total quantity, price, and discount by item within the date range
        String sql = "SELECT " +
                "    it.name AS item_name, " +
                "    SUM(bi.quantity) AS total_quantity, " +
                "    SUM(bi.price ) AS total_price, " +
                "    SUM(bi.discount * bi.quantity) AS total_discount " +
                "FROM " + AppConstant.BILL_ITEM_TABLE + " bi " +
                "INNER JOIN " +
                "    " + AppConstant.BILL_TABLE + " bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    " + AppConstant.SUB_ITEM_TABLE + " sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN " +
                "    " + AppConstant.ITEM_TABLE + " it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at BETWEEN ? AND ? " +
                "    AND bill.status != '"+AppConstant.DELETED+"' " +
                "GROUP BY " +
                "    it.name, it.id " +
                "ORDER BY " +
                "    it.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setItemName(cursor.getString(0));        // Set the item name
                    dto.setSubItemName(null);                    // Set sub-item name as null
                    dto.setTotalQuantity(cursor.getDouble(1));   // Total quantity of all sub-items
                    dto.setTotalPrice(cursor.getDouble(2));      // Total price of all sub-items
                    dto.setTotalDiscount(cursor.getDouble(3));   // Total discount of all sub-items
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getSummaryByDateRange()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public List<BillSummary> getSubItemSummaryByDateRange(String startDate, String endDate) {
        Log.i("BillRepository", "BillRepository::getSubItemSummaryByDateRange()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total quantity, price, and discount by sub-item within the date range
        String sql = "SELECT " +
                "    it.name AS item_name, " +
                "    sub.name AS sub_item_name, " +
                "    SUM(bi.quantity) AS total_quantity, " +
                "    SUM(bi.price) AS total_price, " +
                "    SUM(bi.discount * bi.quantity) AS total_discount " +
                "FROM " + AppConstant.BILL_ITEM_TABLE + " bi " +
                "INNER JOIN " +
                "    " + AppConstant.BILL_TABLE + " bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    " + AppConstant.SUB_ITEM_TABLE + " sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN " +
                "    " + AppConstant.ITEM_TABLE + " it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at BETWEEN ? AND ? " +
                "    AND bill.status != '" + AppConstant.DELETED + "' " +
                "GROUP BY " +
                "    it.name, sub.name, sub.id " +
                "ORDER BY " +
                "    it.id ASC, sub.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setItemName(cursor.getString(0));        // Set the item name
                    dto.setSubItemName(cursor.getString(1));     // Set the sub-item name
                    dto.setTotalQuantity(cursor.getDouble(2));   // Total quantity of the sub-item
                    dto.setTotalPrice(cursor.getDouble(3));      // Total price of the sub-item
                    dto.setTotalDiscount(cursor.getDouble(4));   // Total discount of the sub-item
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getSubItemSummaryByDateRange()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }


    public List<BillSummary> getSubItemDetailSummaryByDateRange(String startDate, String endDate) {
        Log.i("BillRepository", "BillRepository::getSubItemDetailSummaryByDateRange()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total quantity, price, and discount by sub-item within the date range
        String sql = "SELECT " +
                "    bill.created_at AS bill_date, " +
                "    it.name AS item_name, " +
                "    sub.name AS sub_item_name, " +
                "    SUM(bi.quantity) AS total_quantity, " +
                "    SUM(bi.price) AS total_price, " +
                "    SUM(bi.discount * bi.quantity) AS total_discount " +
                "FROM " + AppConstant.BILL_ITEM_TABLE + " bi " +
                "INNER JOIN " +
                "    " + AppConstant.BILL_TABLE + " bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    " + AppConstant.SUB_ITEM_TABLE + " sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN " +
                "    " + AppConstant.ITEM_TABLE + " it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at BETWEEN ? AND ? " +
                "    AND bill.status != '" + AppConstant.DELETED + "' " +
                "GROUP BY " +
                "    bill.created_at,it.name, sub.name, sub.id " +
                "ORDER BY " +
                "   bill.created_at ASC, it.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setDate(cursor.getString(0));
                    dto.setItemName(cursor.getString(1));        // Set the item name
                    dto.setSubItemName(cursor.getString(2));     // Set the sub-item name
                    dto.setTotalQuantity(cursor.getDouble(3));   // Total quantity of the sub-item
                    dto.setTotalPrice(cursor.getDouble(4));      // Total price of the sub-item
                    dto.setTotalDiscount(cursor.getDouble(5));   // Total discount of the sub-item
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getSubItemDetailSummaryByDateRange()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }


    public List<BillSummary> getDetailedSummaryByDateRange(String startDate, String endDate) {
        Log.i("BillRepository", "BillRepository::getDetailedSummaryByDateRange()::is called..");
        List<BillSummary> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total quantity, price, and discount by item and group by date
        String sql = "SELECT " +
                "    bill.created_at AS bill_date, " +
                "    it.name AS item_name, " +
                "    SUM(bi.quantity) AS total_quantity, " +
                "    SUM(bi.price ) AS total_price, " +
                "    SUM(bi.discount * bi.quantity) AS total_discount " +
                "FROM " + AppConstant.BILL_ITEM_TABLE + " bi " +
                "INNER JOIN " +
                "    " + AppConstant.BILL_TABLE + " bill ON bi.bill_id = bill.id " +
                "INNER JOIN " +
                "    " + AppConstant.SUB_ITEM_TABLE + " sub ON bi.sub_item_id = sub.id " +
                "INNER JOIN " +
                "    " + AppConstant.ITEM_TABLE + " it ON sub.item_id = it.id " +
                "WHERE " +
                "    bill.created_at BETWEEN ? AND ? " +
                "    AND bill.status != '"+AppConstant.DELETED+"' " +
                "GROUP BY " +
                "    bill.created_at, it.name, it.id " +
                "ORDER BY " +
                "    bill.created_at ASC, it.id ASC;";

        try {
            cursor = db.rawQuery(sql, new String[]{startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillSummary dto = new BillSummary();
                    dto.setDate(cursor.getString(0));            // Set the bill date
                    dto.setItemName(cursor.getString(1));        // Set the item name
                    dto.setSubItemName(null);                    // Set sub-item name as null
                    dto.setTotalQuantity(cursor.getDouble(2));   // Total quantity of all sub-items
                    dto.setTotalPrice(cursor.getDouble(3));      // Total price of all sub-items
                    dto.setTotalDiscount(cursor.getDouble(4));   // Total discount of all sub-items
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            Log.e("billItemRepository", "billItemRepository::getDetailedSummaryByDateRange()::Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public List<Sale> getSalesByDateRange(String startDate, String endDate) {
        Log.i("BillRepository", "BillRepository::getSalesByDateRange()::is called..");
        List<Sale> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        // SQL query to get the total cash, loan, and deleted sales by date within the date range
        String sql = "SELECT " +
                "    bill.created_at AS date, " +
                "    SUM(CASE WHEN bill.payment_methode = ? AND bill.status != ? THEN bill.total_amount ELSE 0 END) AS total_cash, " +
                "    SUM(CASE WHEN bill.payment_methode = ? AND bill.status != ? THEN bill.total_amount ELSE 0 END) AS total_loan, " +
                "    SUM(CASE WHEN bill.status = ? THEN bill.total_amount ELSE 0 END) AS total_deleted " +
                "FROM " + AppConstant.BILL_TABLE + " bill " +
                "WHERE bill.created_at BETWEEN ? AND ? " +
                "GROUP BY bill.created_at " +
                "ORDER BY bill.created_at ASC;";




        try {
            cursor = db.rawQuery(sql, new String[]{AppConstant.CASH, AppConstant.DELETED, AppConstant.LOAN, AppConstant.DELETED, AppConstant.DELETED, startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Sale sale = new Sale();
                    sale.setDate(cursor.getString(0));            // Set the date
                    sale.setCash(cursor.getDouble(1));            // Set total cash sales
                    sale.setLoan(cursor.getDouble(2));            // Set total loan sales
                    sale.setDelete(cursor.getDouble(3));          // Set total deleted sales
                    list.add(sale);
                }
            }
        } catch (Exception e) {
            Log.e("BillRepository", "BillRepository::getSalesByDateRange()::Error fetching sales:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }


}
