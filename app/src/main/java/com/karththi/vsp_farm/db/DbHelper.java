package com.karththi.vsp_farm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.karththi.vsp_farm.helper.AppConstant;
import com.karththi.vsp_farm.helper.PasswordUtils;

import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String ITEM_TABLE = AppConstant.ITEM_TABLE;
    public static final String USER_TABLE = AppConstant.USER_TABLE;
    public static final String SUB_ITEM_TABLE = AppConstant.SUB_ITEM_TABLE;

    public static final String BILL_TABLE = AppConstant.BILL_TABLE;

    public static final String CUSTOMER_TABLE = AppConstant.CUSTOMER_TABLE;

    public static final String BILL_ITEM_TABLE = AppConstant.BILL_ITEM_TABLE;

    public static final String LOAN_TABLE = AppConstant.LOAN_TABLE;

    public static final String LOAN_PAYMENT_TABLE = AppConstant.LOAN_PAYMENT_TABLE;

    public static final String EXPENSE_TABLE = AppConstant.EXPENSE_TABLE;

    public static final String PERMISSION_TABLE = AppConstant.PERMISSION_TABLE;

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
            "status TEXT, "+
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

    private static final String CREATE_EXPENSE_TABLE = "CREATE TABLE " + EXPENSE_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "expense_name TEXT, " +
            "expense_amount REAL, " +
            "expense_date TEXT, " +
            "expense_time TEXT, " +
            "user_id INTEGER, " +
            "FOREIGN KEY(user_id) REFERENCES " + USER_TABLE + "(id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE)";


    private static final String CREATE_PERMISSION_TABLE = "CREATE TABLE " + PERMISSION_TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "permission TEXT, " +
            "is_enabled INTEGER DEFAULT 0)";



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
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_PERMISSION_TABLE);
        createDefaultAdminUser(db);
        insertDefaultCustomer(db);
        insertPermission(db);
//        testData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentValues values = new ContentValues();
        values.put("permission", "VIEW_LOAN_PAYMENT_PERMISSION");
        values.put("is_enabled", 0);
        db.insert(PERMISSION_TABLE, null, values);
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

    public void insertPermission(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        List<String> permissions = Arrays.asList(
                "TODAY_SUMMARY_REPORT_PERMISSION",
                "TODAY_DETAIL_REPORT_PERMISSION",
                "GET_SUMMARY_REPORT_PERMISSION",
                "GET_DETAIL_REPORT_PERMISSION",
                "GET_CUSTOMER_REPORT_PERMISSION",
                "ADD_CUSTOMER_PERMISSION",
                "EDIT_CUSTOMER_PERMISSION",
                "ADD_ITEM_PERMISSION",
                "EDIT_ITEM_PERMISSION",
                "ADD_SUB_ITEM_PERMISSION",
                "EDIT_SUB_ITEM_PERMISSION",
                "DISABLE_OR_ENABLE_ITEM_PERMISSION",
                "VIEW_LOAN_PAYMENT_PERMISSION"
        );

        for (String permission : permissions) {
            values.put("permission", permission);
            values.put("is_enabled", 0);
            db.insert(PERMISSION_TABLE, null, values);
            values.clear(); // Clear values for the next iteration to avoid data overlap
        }
    }


    public void testData(SQLiteDatabase db) {
        // Insert role: CASHIER
        ContentValues userValues = new ContentValues();
        String cashierPassword = PasswordUtils.hashPassword("0000"); // Ensure passwords are hashed
        userValues.put("username", "1111");
        userValues.put("name", "Cashier");
        userValues.put("password", cashierPassword);
        userValues.put("role", "CASHIER");
        db.insert(USER_TABLE, null, userValues);

        // Insert 3 customers
        String[] customerNames = {"Customer A", "Customer B", "Customer C"};
        for (String customerName : customerNames) {
            ContentValues customerValues = new ContentValues();
            customerValues.put("name", customerName);
            customerValues.put("description", "Description of " + customerName);
            customerValues.put("mobile", "1234567890"); // Mock phone number
            db.insert(CUSTOMER_TABLE, null, customerValues);
        }

        // Insert Item: Chicken
        ContentValues chickenValues = new ContentValues();
        chickenValues.put("name", "Chicken");
        chickenValues.put("measurement", "KG"); // Assuming measurement is weight-based
        long chickenItemId = db.insert(ITEM_TABLE, null, chickenValues);

        String[] chickenSubItems = {"Broiler", "Kalperd", "Parents"};
        for (String subItemName : chickenSubItems) {
            ContentValues subItemValues = new ContentValues();
            subItemValues.put("name", subItemName);
            subItemValues.put("price", 0.0); // Default price
            subItemValues.put("item_id", chickenItemId);
            subItemValues.put("status", "ACTIVE");
            db.insert(SUB_ITEM_TABLE, null, subItemValues);
        }

        // Insert Item: Egg
        ContentValues eggValues = new ContentValues();
        eggValues.put("name", "Egg");
        eggValues.put("measurement", "PIECE"); // Assuming measurement is quantity-based
        long eggItemId = db.insert(ITEM_TABLE, null, eggValues);

        // Insert sub-items for Egg
        String[] eggSubItems = {"White", "Village"};
        for (String subItemName : eggSubItems) {
            ContentValues subItemValues = new ContentValues();
            subItemValues.put("name", subItemName);
            subItemValues.put("price", 45); // Default price
            subItemValues.put("item_id", eggItemId);
            subItemValues.put("status", "ACTIVE");
            db.insert(SUB_ITEM_TABLE, null, subItemValues);
        }
    }


}
