package com.karththi.vsp_farm.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.karththi.vsp_farm.helper.PasswordUtils;
import com.karththi.vsp_farm.model.User;
import com.karththi.vsp_farm.page.admin.UserActionActivity;
import com.karththi.vsp_farm.repo.UserRepository;

public class UserService {

    private Context context;
    private UserRepository userRepository;
    public UserService(Context context) {
        this.context = context;
        userRepository = new UserRepository(context);

    }

    public void addUser(User user) {
        Log.i("UserService", "UserService::addUser()::is called");
        if (userRepository.isUserExists(user.getUsername())) {
            Toast.makeText(context,
                    "Username already exists",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.addUser(user);
        Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show();
        Log.i("UserService", "UserService::addUser()::Signup successful!");
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
        Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, UserActionActivity.class);
        context.startActivity(intent);
    }
}