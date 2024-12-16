package com.karththi.vsp_farm.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.Loan;
import com.karththi.vsp_farm.model.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionRepository {
    private DbHelper dbHelper;

    private Context context;

    public PermissionRepository(Context context) {
        dbHelper = new DbHelper(context);
        this.context = context;
    }

    public void addPermission(Permission permission) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("permission", permission.getPermission());
            values.put("is_enabled", permission.getIsGranted());
            db.insert(AppConstant.PERMISSION_TABLE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updatePermission(String permission, int isGranted) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("is_enabled", isGranted);
            db.update(AppConstant.PERMISSION_TABLE, values, "permission = ?", new String[]{permission});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
            Toast.makeText(context, "Permission updated for "+permission, Toast.LENGTH_SHORT).show();
        }
    }

    public List<Permission> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Permission> permissions = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AppConstant.PERMISSION_TABLE, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    Permission permission = new Permission();
                    permission.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    permission.setPermission(cursor.getString(cursor.getColumnIndexOrThrow("permission")));
                    permission.setIsGranted(cursor.getInt(cursor.getColumnIndexOrThrow("is_enabled")));
                    permissions.add(permission);
                } while (cursor.moveToNext());
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("PermissionRepository", "Error fetching permissions"+e.getMessage());
        }finally {
            cursor.close();
            db.close();
        }
        Log.d("PermissionRepository", "Permissions fetched successfully with size "+permissions.size());
        return permissions;
    }
}
