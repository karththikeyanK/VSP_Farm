package com.karththi.vsp_farm.page.admin;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.adapter.UserAdapter;
import com.karththi.vsp_farm.model.User;
import com.karththi.vsp_farm.repo.UserRepository;

import java.util.List;

public class ViewUserActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private UserRepository userRepository;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        // Initialize views
        userRecyclerView = findViewById(R.id.userRecyclerView);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize repository
        userRepository = new UserRepository(this);

        // Fetch users from database
        List<User> userList = userRepository.getAllUsers();

        if (userList != null && !userList.isEmpty()) {
            // Initialize adapter
            userAdapter = new UserAdapter(userList);

            // Setup RecyclerView
            userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            userRecyclerView.setAdapter(userAdapter);
        } else {
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }




}
