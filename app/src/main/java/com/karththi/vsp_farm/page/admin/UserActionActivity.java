package com.karththi.vsp_farm.page.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.page.CreateUserActivity;

public class UserActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_actions);

    }

    public void createUser(View view){
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::createUser():: is started");
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    public void viewUsers(View view) {
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::viewUsers():: is started");
        Intent intent = new Intent(this, ViewUserActivity.class);
        startActivity(intent);
    }

    public void addCustomer(View view) {
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::viewCustomers():: is started");
        Intent intent = new Intent(this, CreateCustomerActivity.class);
        startActivity(intent);
    }

    public void viewCustomers(View view) {
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::viewCustomers():: is started");
        Intent intent = new Intent(this, CustomerListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}
