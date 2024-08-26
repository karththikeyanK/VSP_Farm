package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
public class ItemListActivity extends AppCompatActivity {

    private ListView itemListView;
    private Button addNewItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        itemListView = findViewById(R.id.itemListView);
        addNewItemButton = findViewById(R.id.addNewItemButton);

        displayItems();

        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemListActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayItems() {
        // Setup your ListView with items here
    }
}
