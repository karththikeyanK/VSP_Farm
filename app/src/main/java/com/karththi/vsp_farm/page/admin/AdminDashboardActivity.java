package com.karththi.vsp_farm.page.admin;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.page.CreateUserActivity;
import com.karththi.vsp_farm.page.admin.item.ItemListActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Example: Display a welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome to the Admin Dashboard!");
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

    public void viewItems(View view) {
        Log.d("AdminDashboardActivity", "AdminDashboardActivity::viewItems():: is started");
        Intent intent = new Intent(this, ItemListActivity.class);
        startActivity(intent);
    }
}
