package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.helper.adapter.SubItemListAdapter;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.service.SubItemService;

import java.util.List;

public class SubItemListActivity extends AppCompatActivity {

    private ListView subItemListView;
    private Button addNewSubItemButton;
    private SubItemService subItemService;
    private List<SubItem> subItems;
    private SubItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subitemlist);

        subItemListView = findViewById(R.id.subItemListView);
        addNewSubItemButton = findViewById(R.id.addNewSubItemButton);
        subItemService = new SubItemService(this);

        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        subItems = subItemService.getSubItemsByItemId(itemId);
        adapter = new SubItemListAdapter(this, subItems, subItemService);
        subItemListView.setAdapter(adapter);

        addNewSubItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubItemListActivity.this, AddSubItemActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                startActivity(intent);
            }
        });
    }
}
