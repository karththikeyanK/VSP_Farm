package com.karththi.vsp_farm.page;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.PasswordUtils;
import com.karththi.vsp_farm.model.User;
import com.karththi.vsp_farm.repo.UserRepository;
import com.karththi.vsp_farm.service.UserService;

public class CreateUserActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner;
    private Button signupButton;
    private ImageView togglePasswordVisibility;
    private boolean isPasswordVisible = false;

    private UserService userService;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.role);
        signupButton = findViewById(R.id.signupButton);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Setup the role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
         userService = new UserService(this);

        // Set the signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });

        // Set the toggle password visibility click listener
        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_open);
                }
                isPasswordVisible = !isPasswordVisible;
                passwordEditText.setSelection(passwordEditText.length()); // Move cursor to the end of the text
            }
        });
    }

    private void performSignup() {
        String username = usernameEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        // Perform input validation
        if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
            Toast.makeText(CreateUserActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        userService.addUser(new User(username, name, password, role));
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }


}
