package com.karththi.vsp_farm.page.cashier;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;

public class ItemPageActivity extends AppCompatActivity {
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome to the Cahsier Dashboard!");
    }


}
