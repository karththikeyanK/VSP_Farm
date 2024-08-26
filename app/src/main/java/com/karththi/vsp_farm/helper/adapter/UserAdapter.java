package com.karththi.vsp_farm.helper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    // Constructor
    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    // ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView nameTextView;
        public TextView roleTextView;

        public UserViewHolder(View view) {
            super(view);
            usernameTextView = view.findViewById(R.id.usernameTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
            roleTextView = view.findViewById(R.id.roleTextView);
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the user item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        // Get current user
        User user = userList.get(position);

        // Bind data to views
        holder.usernameTextView.setText("Username: " + user.getUsername());
        holder.nameTextView.setText("Name: " + user.getName());
        holder.roleTextView.setText("Role: " + user.getRole());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
