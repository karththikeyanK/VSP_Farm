package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.adapter.ItemListAdapter;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.repo.ItemRepository;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private ListView itemListView;
    private Button addNewItemButton;
    private ItemRepository itemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        itemListView = findViewById(R.id.itemListView);
        addNewItemButton = findViewById(R.id.addNewItemButton);
        itemRepository = new ItemRepository(this);

        displayItems();

        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemListActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selectedItem = (Item) parent.getItemAtPosition(position);
                Intent intent = new Intent(ItemListActivity.this, SubItemListActivity.class);
                intent.putExtra("item_id", selectedItem.getId());
                startActivity(intent);
            }
        });
    }

    private void displayItems() {
        List<Item> items = itemRepository.getAllItems();
        ItemListAdapter adapter = new ItemListAdapter(this, items);
        itemListView.setAdapter(adapter);
    }
}
