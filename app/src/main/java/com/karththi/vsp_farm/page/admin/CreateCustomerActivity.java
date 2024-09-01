package com.karththi.vsp_farm.page.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.service.CustomerService;

public class CreateCustomerActivity extends AppCompatActivity {

    private Button backButton, saveButton;
    private EditText nameEditText, mobileEditText, descriptionEditText;

    private CustomerService customerService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer); // Reference to the layout file

        // Handle back button click
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        nameEditText = findViewById(R.id.name);
        mobileEditText = findViewById(R.id.mobile);
        descriptionEditText = findViewById(R.id.description);
        customerService = new CustomerService(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the activity and returns to the previous screen
            }
        });

        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the edit text fields
                String name = nameEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                customerService.createCustomer(name, mobile, description);
            }
        });
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }


}
