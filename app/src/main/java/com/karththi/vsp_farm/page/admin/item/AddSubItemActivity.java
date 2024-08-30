package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.service.SubItemService;

public class AddSubItemActivity extends AppCompatActivity {

    private EditText subItemNameEditText;
    private EditText subItemPriceEditText;
    private Button saveSubItemButton;
    private SubItemService subItemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subitem);

        subItemNameEditText = findViewById(R.id.subItemNameEditText);
        subItemPriceEditText = findViewById(R.id.subItemPriceEditText);
        saveSubItemButton = findViewById(R.id.saveSubItemButton);

        subItemService = new SubItemService(this);

        final int itemId = getIntent().getIntExtra("ITEM_ID", -1);

        saveSubItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subItemName = subItemNameEditText.getText().toString();
                String subItemPriceStr = subItemPriceEditText.getText().toString();

                if (subItemName.isEmpty() || subItemPriceStr.isEmpty()) {
                    Toast.makeText(AddSubItemActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Double subItemPrice = Double.parseDouble(subItemPriceStr);

                SubItem subItem = new SubItem();
                subItem.setSubItemName(subItemName);
                subItem.setPrice(subItemPrice);
                subItem.setItemId(itemId);

                subItemService.create(subItem);

                Toast.makeText(AddSubItemActivity.this, "Sub-Item added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddSubItemActivity.this, SubItemListActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                startActivity(intent);
            }
        });
    }
}
