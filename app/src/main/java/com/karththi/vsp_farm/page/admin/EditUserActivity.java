package com.karththi.vsp_farm.page.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.PasswordUtils;
import com.karththi.vsp_farm.model.User;
import com.karththi.vsp_farm.service.UserService;

public class EditUserActivity extends AppCompatActivity {

    private UserService userService;

    private EditText username, name, password;

    private Spinner roleSpinner;

    private Button backButton, updateButton;
    private ImageView togglePasswordVisibility;

    private boolean isPasswordVisible = false;

    private boolean isPasswordChanged = false;

    private User existingUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userService = new UserService(this);

        roleSpinner = findViewById(R.id.role);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        int userId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            Intent intent = new Intent(this, ViewUserActivity.class);
            startActivity(intent);
        }else {
            existingUser = userService.getUserById(userId);
            if (existingUser != null) {
                username.setText(existingUser.getUsername());
                name.setText(existingUser.getName());
                roleSpinner.setSelection(adapter.getPosition(existingUser.getRole()));
            }
        }

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isPasswordChanged = true;
            }
        });

        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_open);
                }
                isPasswordVisible = !isPasswordVisible;
                password.setSelection(password.length()); // Move cursor to the end of the text
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserActivity.this, ViewUserActivity.class);
                startActivity(intent);
            }
        });
    }


    private void updateUser(){
        if (username.getText().toString().isEmpty()) {
            username.setError("Username is required");
            return;
        }
        User user = new User();
        user.setUsername(username.getText().toString());
        user.setName(name.getText().toString());
        user.setRole(roleSpinner.getSelectedItem().toString());
        if (isPasswordChanged) {
            user.setPassword(PasswordUtils.hashPassword(password.getText().toString()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        user.setId(existingUser.getId());
        userService.updateUser(user);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewUserActivity.class);
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
