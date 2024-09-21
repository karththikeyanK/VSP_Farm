package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.dto.LoanPaymentDto;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.LoanPayment;

import java.util.ArrayList;
import java.util.List;

public class LoanPaymentRepository {
    private DbHelper dbHelper;

    public LoanPaymentRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public int addLoanPayment(LoanPayment loanPayment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("loan_id", loanPayment.getLoanId());
        values.put("payment_amount", loanPayment.getPaymentAmount());
        values.put("payment_date", loanPayment.getPaymentDate());
        int id = (int)db.insert(AppConstant.LOAN_PAYMENT_TABLE, null, values);
        db.close();
        return id;
    }

    public void deleteLoanPayment(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AppConstant.LOAN_PAYMENT_TABLE, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // using loanId i want to get last payment

    public LoanPayment getLastPaymentByLoanId(int loanId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AppConstant.LOAN_PAYMENT_TABLE+" WHERE loan_id = ? ORDER BY id DESC LIMIT 1", new String[]{String.valueOf(loanId)});
        LoanPayment loanPayment = null;
        if (cursor.moveToFirst()) {
            loanPayment = new LoanPayment();
            loanPayment.setId(cursor.getInt(0));
            loanPayment.setLoanId(cursor.getInt(1));
            loanPayment.setPaymentAmount(cursor.getDouble(2));
            loanPayment.setPaymentDate(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return loanPayment;
    }


    public List<LoanPaymentDto> getAllCustomerLoanPaymentsByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<LoanPaymentDto> loanPaymentDtoList = new ArrayList<>();

        String query = "SELECT lp.id, lp.loan_id, lp.payment_amount, lp.payment_date, c.name " +
                "FROM " + AppConstant.LOAN_PAYMENT_TABLE + " lp " +
                "INNER JOIN " + AppConstant.LOAN_TABLE + " l ON lp.loan_id = l.id " +
                "INNER JOIN " + AppConstant.CUSTOMER_TABLE + " c ON l.customer_id = c.id " +
                "WHERE lp.payment_date = ? "+
                "ORDER BY lp.id DESC";

        Cursor cursor = db.rawQuery(query, new String[]{date});

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                LoanPaymentDto loanPaymentDto = new LoanPaymentDto();
                loanPaymentDto.setId(cursor.getInt(0));
                loanPaymentDto.setLoanId(cursor.getInt(1));
                loanPaymentDto.setPaymentAmount(cursor.getDouble(2));
                loanPaymentDto.setPaymentDate(cursor.getString(3));
                loanPaymentDto.setCustomerName(cursor.getString(4));
                loanPaymentDtoList.add(loanPaymentDto);
            }
        } else {
            Log.d("LoanPaymentRepository", "getAllCustomerLoanPaymentsByDate()::No payments found");
        }

        cursor.close();
        db.close();
        return loanPaymentDtoList;
    }


    public List<LoanPaymentDto> getLoanPaymentListByDateRange(int customerId, String fromDate, String toDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<LoanPaymentDto> loanPaymentDtoList = new ArrayList<>();

        String query;
        String[] selectionArgs;

        // Modify the query based on customerId
        if (customerId == 999999) {
            query = "SELECT lp.id, lp.loan_id, lp.payment_amount, lp.payment_date, c.name " +
                    "FROM " + AppConstant.LOAN_PAYMENT_TABLE + " lp " +
                    "INNER JOIN " + AppConstant.LOAN_TABLE + " l ON lp.loan_id = l.id " +
                    "INNER JOIN " + AppConstant.CUSTOMER_TABLE + " c ON l.customer_id = c.id " +
                    "WHERE lp.payment_date BETWEEN ? AND ? "+
                    "ORDER BY lp.id DESC";
            selectionArgs = new String[]{fromDate, toDate};
        } else {
            query = "SELECT lp.id, lp.loan_id, lp.payment_amount, lp.payment_date, c.name " +
                    "FROM " + AppConstant.LOAN_PAYMENT_TABLE + " lp " +
                    "INNER JOIN " + AppConstant.LOAN_TABLE + " l ON lp.loan_id = l.id " +
                    "INNER JOIN " + AppConstant.CUSTOMER_TABLE + " c ON l.customer_id = c.id " +
                    "WHERE c.id = ? AND lp.payment_date BETWEEN ? AND ? "+
                    "ORDER BY lp.id DESC";
            selectionArgs = new String[]{String.valueOf(customerId), fromDate, toDate};
        }

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                LoanPaymentDto loanPaymentDto = new LoanPaymentDto();
                loanPaymentDto.setId(cursor.getInt(0));
                loanPaymentDto.setLoanId(cursor.getInt(1));
                loanPaymentDto.setPaymentAmount(cursor.getDouble(2));
                loanPaymentDto.setPaymentDate(cursor.getString(3));
                loanPaymentDto.setCustomerName(cursor.getString(4));
                loanPaymentDtoList.add(loanPaymentDto);
            }
        } else {
            Log.d("LoanPaymentRepository", "getLoanPaymentListByDateRange()::No payments found");
        }

        cursor.close();
        db.close();
        return loanPaymentDtoList;
    }






}
