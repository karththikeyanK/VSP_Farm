package com.karththi.vsp_farm.repo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.karththi.vsp_farm.db.DbHelper;
import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private DbHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        db.insert("users", null, values);
        db.close();
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.USER_TABLE, null, "username = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                cursor.close();
                return new User(id,username, name, password, role);
            } catch (IllegalArgumentException e) {
                // Handle the exception here
                Log.e("UserRepository", "Column does not exist", e);
            }
        }
        return null;
    }

    // get by id
    public User getUserById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.USER_TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                cursor.close();
                return new User(id,username, name, password, role);
            } catch (IllegalArgumentException e) {
                // Handle the exception here
                Log.e("UserRepository", "Column does not exist", e);
            }
        }
        return null;
    }

    // update
    public void updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());
        db.update(AppConstant.USER_TABLE, values, "id = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                "id",
                "username",
                "name",
                "role"
        };

        Cursor cursor = db.query(AppConstant.USER_TABLE, columns, null, null, null, null, "username ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                User user = new User(id,username, name,null, role);
                userList.add(user);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return userList;
    }

    public boolean isUserExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AppConstant.USER_TABLE, null, "username = ?", new String[]{username}, null, null, null);
        boolean isExists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isExists;
    }

}
