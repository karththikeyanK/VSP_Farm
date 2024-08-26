package com.karththi.vsp_farm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.PasswordUtils;

public class DbHelper extends SQLiteOpenHelper {

    public static final String ITEM_TABLE = AppConstant.ITEM_TABLE;
    public static final String USER_TABLE = AppConstant.USER_TABLE;
    public static final String SUB_ITEM_TABLE = AppConstant.SUB_ITEM_TABLE;

    public DbHelper(Context context) {
        super(context, AppConstant.DATABASE_NAME, null, AppConstant.DATABASE_VERSION);
    }

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE " + ITEM_TABLE + " (" +
            "id INTEGER PRIMARY KEY," +
            "name TEXT," +
            "measurement TEXT," +
            "image BLOB)";

    private static final String CREATE_SUB_ITEMS_TABLE = "CREATE TABLE " + SUB_ITEM_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "price DOUBLE, " +
            "item_id INTEGER, " +
            "FOREIGN KEY(item_id) REFERENCES " + ITEM_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT, " +
            "name TEXT, " +
            "password TEXT, " +
            "role TEXT)"; // Added role column

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_SUB_ITEMS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        createDefaultAdminUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    private void createDefaultAdminUser(SQLiteDatabase db) {
        // Check if the admin user already exists
        String checkAdminUserQuery = "SELECT COUNT(*) FROM " + USER_TABLE + " WHERE username = ?";
        Cursor cursor = db.rawQuery(checkAdminUserQuery, new String[]{"1010"});
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            if (count > 0) {
                return; // Admin user already exists
            }
        }

        // Insert default admin user
        String hash_password = PasswordUtils.hashPassword("1234");
        ContentValues values = new ContentValues();
        values.put("username", "1010");
        values.put("name", "Admin");
        values.put("password", hash_password); // Consider hashing passwords for security
        values.put("role", "ADMIN"); // Set role to ADMIN
        db.insert(USER_TABLE, null, values);
    }
}
