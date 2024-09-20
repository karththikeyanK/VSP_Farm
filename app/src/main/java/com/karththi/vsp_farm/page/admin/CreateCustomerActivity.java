package com.karththi.vsp_farm.page.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.page.admin.item.EditSubItemActivity;
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
                Intent intent = new Intent(CreateCustomerActivity.this, UserActionActivity.class);
                startActivity(intent);
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

                if (name.isEmpty()) {
                    Toast.makeText(CreateCustomerActivity.this, "Please fill the name field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                customerService.createCustomer(name, mobile, description);
            }
        });
    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, UserActionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Get the view that has focus
        View view = getCurrentFocus();

        // Check if the touch event is outside the focused view (keyboard is open)
        if (view != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (view instanceof EditText) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                float x = ev.getRawX() + view.getLeft() - location[0];
                float y = ev.getRawY() + view.getTop() - location[1];

                // If the touch is outside the EditText, hide the keyboard
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                    hideKeyboard(view);
                    view.clearFocus();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
