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

    public static final String BILL_TABLE = AppConstant.BILL_TABLE;

    public static final String CUSTOMER_TABLE = AppConstant.CUSTOMER_TABLE;

    public static final String BILL_ITEM_TABLE = AppConstant.BILL_ITEM_TABLE;

    public static final String LOAN_TABLE = AppConstant.LOAN_TABLE;

    public static final String LOAN_PAYMENT_TABLE = AppConstant.LOAN_PAYMENT_TABLE;

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
            "role TEXT)";

    private static final String CREATE_BILL_TABLE = "CREATE TABLE " + BILL_TABLE + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "reference_number TEXT,"
            + "total_amount REAL,"
            + "customer_id INTEGER,"
            + "user_id INTEGER,"
            + "status TEXT,"
            + "payment_methode TEXT,"
            + "created_at TEXT,"
            + "create_time TEXT,"
            + "updated_at TEXT,"
            + "update_time TEXT,"
            + "modified_by TEXT," +
            "FOREIGN KEY(customer_id) REFERENCES Customer(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE, " +
            "FOREIGN KEY(user_id) REFERENCES " + USER_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";


    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "description TEXT, " +
            "mobile TEXT)";

    private static final String CREATE_BILL_ITEM_TABLE = "CREATE TABLE "+BILL_ITEM_TABLE+" (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "bill_id INTEGER, " + // Added bill_id column
            "sub_item_id INTEGER, " +
            "quantity REAL, " +
            "price REAL, " +
            "discount REAL, " +
            "FOREIGN KEY(bill_id) REFERENCES Bill(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";

    private static final String CREATE_LOAN_TABLE = "CREATE TABLE " + LOAN_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "customer_id INTEGER, " +
            "remaining_amount REAL, " +
            "updated_date TEXT, " +
            "FOREIGN KEY(customer_id) REFERENCES " + CUSTOMER_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";

    private static final String CREATE_LOAN_PAYMENT_TABLE = "CREATE TABLE " + LOAN_PAYMENT_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "loan_id INTEGER, " +
            "payment_amount REAL, " +
            "payment_date TEXT, " +
            "FOREIGN KEY(loan_id) REFERENCES " + LOAN_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_SUB_ITEMS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_BILL_TABLE);
        db.execSQL(CREATE_BILL_ITEM_TABLE);
        db.execSQL(CREATE_LOAN_TABLE);
        db.execSQL(CREATE_LOAN_PAYMENT_TABLE);
        createDefaultAdminUser(db);
        insertDefaultCustomer(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BILL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BILL_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+LOAN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+LOAN_PAYMENT_TABLE);
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
        values.put("role", AppConstant.ADMIN); // Set role to ADMIN
        db.insert(USER_TABLE, null, values);
    }

    public void insertDefaultCustomer(SQLiteDatabase db) {
        // Check if the default customer already exists
        String checkDefaultCustomerQuery = "SELECT COUNT(*) FROM " + CUSTOMER_TABLE + " WHERE name = ?";
        Cursor cursor = db.rawQuery(checkDefaultCustomerQuery, new String[]{"Default Customer"});
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            if (count > 0) {
                return; // Default customer already exists
            }
        }

        // Insert default customer
        ContentValues values = new ContentValues();
        values.put("name", "DEFAULT");
        values.put("description", "Default Customer");
        values.put("mobile", "0000000000");
        db.insert(CUSTOMER_TABLE, null, values);
    }
}
