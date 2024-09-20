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
import com.karththi.vsp_farm.model.BillItem;

import java.util.ArrayList;
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
        values.put("discount", billItem.getDiscount());
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
        values.put("discount", billItem.getDiscount());
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
        Cursor cursor = db.query(DbHelper.BILL_ITEM_TABLE, new String[]{"id", "bill_id", "sub_item_id", "quantity", "price","discount"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        BillItem billItem = null;
        if (cursor.moveToFirst()) {
            billItem = new BillItem();
            billItem.setId(cursor.getInt(0));
            billItem.setBillId(cursor.getInt(1));
            billItem.setSubItemId(cursor.getInt(2));
            billItem.setQuantity(cursor.getDouble(3));
            billItem.setPrice(cursor.getDouble(4));
            billItem.setDiscount(cursor.getDouble(5));
        }
        cursor.close();
        db.close();
        return billItem;
    }

    public List<BillItem> getBillItemByBillId(int billId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.BILL_ITEM_TABLE, new String[]{"id", "bill_id", "sub_item_id", "quantity", "price","discount"}, "bill_id = ?", new String[]{String.valueOf(billId)}, null, null, null);
        List<BillItem> billItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                BillItem billItem = new BillItem();
                billItem.setId(cursor.getInt(0));
                billItem.setBillId(cursor.getInt(1));
                billItem.setSubItemId(cursor.getInt(2));
                billItem.setQuantity(cursor.getDouble(3));
                billItem.setPrice(cursor.getDouble(4));
                billItem.setDiscount(cursor.getDouble(5));
                billItems.add(billItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billItems;
    }

    public List<BillItemsDetailDto> getAllBillDtoByDate(String date) {
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            String sql = "SELECT  bill.id,bill.reference_number,bill.status," +
                    "bill.payment_methode,bill.created_at,bill.create_time,bill.updated_at,bill.update_time," +
                    "cus.name,use.name,bi.quantity,bi.price,bi.discount*bi.quantity," +
                    "sub.name,it.name " +
                    "FROM "+AppConstant.BILL_ITEM_TABLE+" bi " +
                    "INNER JOIN "+AppConstant.BILL_TABLE+" bill ON bi.bill_id = bill.id " +
                    "INNER JOIN "+AppConstant.SUB_ITEM_TABLE+" sub ON bi.sub_item_id = sub.id "+
                    "INNER JOIN "+AppConstant.ITEM_TABLE+" it ON sub.item_id = it.id "+
                    "INNER JOIN "+AppConstant.CUSTOMER_TABLE+" cus ON bill.customer_id = cus.id "+
                    "INNER JOIN "+AppConstant.USER_TABLE+" use ON bill.user_id = use.id "+
                    "WHERE bill.created_at = ? ORDER BY bill.id ASC";

            cursor = db.rawQuery(sql, new String[]{date});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                   BillItemsDetailDto dto = new BillItemsDetailDto();
                   dto.setBillId(cursor.getInt(0));
                   dto.setReferenceNumber(cursor.getString(1));
                   dto.setStatus(cursor.getString(2));
                   dto.setPaymentMethod(cursor.getString(3));
                   dto.setCreatedAt(cursor.getString(4));
                   dto.setCreateTime(cursor.getString(5));
                   dto.setUpdateAt(cursor.getString(6));
                   dto.setUpdateTime(cursor.getString(7));
                   dto.setCustomerName(cursor.getString(8));
                   dto.setUserName(cursor.getString(9));
                   dto.setQuantity(cursor.getDouble(10));
                   dto.setBillItemPrice(cursor.getDouble(11));
                   dto.setDiscount(cursor.getDouble(12));
                   dto.setSubItemName(cursor.getString(13));
                   dto.setItemName(cursor.getString(14));
                   billItemsDetailDtoList.add(dto);
                }
            } else {
                Log.d("getAllBillDtoByDate", "No bills found for date: " + date);
            }

        } catch (Exception e) {
            Log.e("getAllBillDtoByDate", "Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return billItemsDetailDtoList;
    }

    public List<BillItemsDetailDto> getAllBillDtoByDateRange(String startDate, String endDate) {
        List<BillItemsDetailDto> billItemsDetailDtoList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            String sql = "SELECT  bill.id,bill.reference_number,bill.status," +
                    "bill.payment_methode,bill.created_at,bill.create_time,bill.updated_at,bill.update_time," +
                    "cus.name,use.name,bi.quantity,bi.price,bi.discount*bi.quantity," +
                    "sub.name,it.name " +
                    "FROM "+AppConstant.BILL_ITEM_TABLE+" bi " +
                    "INNER JOIN "+AppConstant.BILL_TABLE+" bill ON bi.bill_id = bill.id " +
                    "INNER JOIN "+AppConstant.SUB_ITEM_TABLE+" sub ON bi.sub_item_id = sub.id "+
                    "INNER JOIN "+AppConstant.ITEM_TABLE+" it ON sub.item_id = it.id "+
                    "INNER JOIN "+AppConstant.CUSTOMER_TABLE+" cus ON bill.customer_id = cus.id "+
                    "INNER JOIN "+AppConstant.USER_TABLE+" use ON bill.user_id = use.id "+
                    "WHERE" +
                    "    bill.created_at BETWEEN ? AND ? " +
                    "ORDER BY  bill.created_at ASC";

            cursor = db.rawQuery(sql, new String[]{startDate, endDate});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BillItemsDetailDto dto = new BillItemsDetailDto();
                    dto.setBillId(cursor.getInt(0));
                    dto.setReferenceNumber(cursor.getString(1));
                    dto.setStatus(cursor.getString(2));
                    dto.setPaymentMethod(cursor.getString(3));
                    dto.setCreatedAt(cursor.getString(4));
                    dto.setCreateTime(cursor.getString(5));
                    dto.setUpdateAt(cursor.getString(6));
                    dto.setUpdateTime(cursor.getString(7));
                    dto.setCustomerName(cursor.getString(8));
                    dto.setUserName(cursor.getString(9));
                    dto.setQuantity(cursor.getDouble(10));
                    dto.setBillItemPrice(cursor.getDouble(11));
                    dto.setDiscount(cursor.getDouble(12));
                    dto.setSubItemName(cursor.getString(13));
                    dto.setItemName(cursor.getString(14));
                    billItemsDetailDtoList.add(dto);
                }
            } else {
                Log.d("getAllBillDtoByDate", "No bills found for date: " + startDate + " - " + endDate);
            }

        } catch (Exception e) {
            Log.e("getAllBillDtoByDate", "Error fetching bills:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return billItemsDetailDtoList;
    }







}
