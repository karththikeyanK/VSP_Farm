package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanRepository {
    private DbHelper dbHelper;

    public LoanRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public int crateLoan(Loan loan){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_id", loan.getCustomerId());
        values.put("remaining_amount", loan.getRemainingAmount());
        values.put("updated_date", loan.getUpdatedDate());
        int id = (int) db.insert(AppConstant.LOAN_TABLE, null, values);
        db.close();
        return id;
    }

    public void updateLoan(Loan loan){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_id", loan.getCustomerId());
        values.put("remaining_amount", loan.getRemainingAmount());
        values.put("updated_date", loan.getUpdatedDate());
        db.update(AppConstant.LOAN_TABLE, values, "id = ?", new String[]{String.valueOf(loan.getId())});
        db.close();
    }

    public void deleteLoan(Loan loan){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AppConstant.LOAN_TABLE, "id = ?", new String[]{String.valueOf(loan.getId())});
        db.close();
    }

    public Loan getLoanById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.LOAN_TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Loan loan = null;
        if (cursor.moveToFirst()) {
            loan = new Loan();
            loan.setId(cursor.getInt(0));
            loan.setCustomerId(cursor.getInt(1));
            loan.setRemainingAmount(cursor.getDouble(2));
            loan.setUpdatedDate(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return loan;
    }

    public List<Loan> getAllLoans(){
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.LOAN_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Loan loan = new Loan();
                loan.setId(cursor.getInt(0));
                loan.setCustomerId(cursor.getInt(1));
                loan.setRemainingAmount(cursor.getDouble(2));
                loan.setUpdatedDate(cursor.getString(3));
                loans.add(loan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return loans;
    }

    public Loan getLoansByCustomerId(int customerId){
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.LOAN_TABLE, null, "customer_id = ?", new String[]{String.valueOf(customerId)}, null, null, null);
        Loan loan = null;
        if (cursor.moveToFirst()) {
            loan = new Loan();
            loan.setId(cursor.getInt(0));
            loan.setCustomerId(cursor.getInt(1));
            loan.setRemainingAmount(cursor.getDouble(2));
            loan.setUpdatedDate(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return loan;
    }

    public boolean isExistByCustomerID(int customerId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.LOAN_TABLE, null, "customer_id = ?", new String[]{String.valueOf(customerId)}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }


}
