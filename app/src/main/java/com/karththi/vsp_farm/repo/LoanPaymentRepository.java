package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.LoanPayment;

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


}
