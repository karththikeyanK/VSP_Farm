package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.dto.BillItemsDetailDto;
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

    public List<BillItemsDetailDto> getAllBillDtoByDate(String date){
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT b.id AS bill_id, b.reference_number, b.total_amount, b.status, " +
                "b.payment_methode, b.created_at, b.create_time, c.name AS customer_name, " +
                "u.username AS user_name, bi.quantity, bi.price AS bill_item_price, bi.discount, " +
                "si.name AS sub_item_name, i.name AS item_name " +
                "FROM "+AppConstant.BILL_TABLE+" b " +
                "JOIN "+AppConstant.CUSTOMER_TABLE+" c ON b.customer_id = c.id " +
                "JOIN "+AppConstant.USER_TABLE+" u ON b.user_id = u.id " +
                "JOIN "+AppConstant.BILL_ITEM_TABLE+" bi ON bi.bill_id = b.id " +
                "JOIN "+AppConstant.SUB_ITEM_TABLE+" si ON bi.sub_item_id = si.id " +
                "JOIN "+AppConstant.ITEM_TABLE+" i ON si.item_id = i.id";


        Cursor cursor = db.rawQuery(sql, new String[]{date});

        while (cursor.moveToNext()) {
            BillItemsDetailDto billItemsDetailDto = new BillItemsDetailDto();
            billItemsDetailDto.setBillId(cursor.getInt(0));
            billItemsDetailDto.setReferenceNumber(cursor.getString(1));
            billItemsDetailDto.setTotalAmount(cursor.getDouble(2));
            billItemsDetailDto.setStatus(cursor.getString(3));
            billItemsDetailDto.setPaymentMethod(cursor.getString(4));
            billItemsDetailDto.setCreatedAt(cursor.getString(5));
            billItemsDetailDto.setCreateTime(cursor.getString(6));
            billItemsDetailDto.setCustomerName(cursor.getString(7));
            billItemsDetailDto.setUserName(cursor.getString(8));
            billItemsDetailDto.setQuantity(cursor.getDouble(9));
            billItemsDetailDto.setBillItemPrice(cursor.getDouble(10));
            billItemsDetailDto.setDiscount(cursor.getDouble(11));
            billItemsDetailDto.setSubItemName(cursor.getString(12));
            billItemsDetailDto.setItemName(cursor.getString(13));

            billItemsDetailDtoList.add(billItemsDetailDto);
        }

        cursor.close();
        db.close();
        return billItemsDetailDtoList;
    }
}
