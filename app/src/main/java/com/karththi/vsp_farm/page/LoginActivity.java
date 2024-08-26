package com.karththi.vsp_farm.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.PasswordUtils;
import com.karththi.vsp_farm.model.User;
import com.karththi.vsp_farm.page.admin.AdminDashboardActivity;
import com.karththi.vsp_farm.page.cashier.ItemPageActivity;
import com.karththi.vsp_farm.repo.UserRepository;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        userRepository = new UserRepository(this);

        // Set the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Perform input validation
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<User> users = userRepository.getAllUsers();

        // Get the user from the database

        User user = userRepository.getUserByUsername(username);

        if (user != null && PasswordUtils.hashPassword(password).equals(user.getPassword())) {
            // Check the user's role and navigate accordingly
            if (user.getRole().equals("ADMIN")) {
                // Navigate to Admin Dashboard
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } else if (user.getRole().equals("CASHIER")) {
                // Navigate to Item Page
                Intent intent = new Intent(LoginActivity.this, ItemPageActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            // Show an error message if login fails
            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
