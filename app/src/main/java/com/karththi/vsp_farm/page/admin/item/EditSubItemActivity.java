package com.karththi.vsp_farm.page.admin.item;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.SubItem;
import com.karththi.vsp_farm.service.SubItemService;

public class EditSubItemActivity extends AppCompatActivity {

    private EditText subItemNameEditText;
    private EditText subItemPriceEditText;
    private Button updateSubItemButton;
    private SubItemService subItemService;
    private SubItem subItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subitem);

        subItemNameEditText = findViewById(R.id.subItemNameEditText);
        subItemPriceEditText = findViewById(R.id.subItemPriceEditText);
        updateSubItemButton = findViewById(R.id.updateSubItemButton);

        subItemService = new SubItemService(this);

        int subItemId = getIntent().getIntExtra("SUB_ITEM_ID", -1);

        subItem = subItemService.getSubItemById(subItemId);

        if (subItem != null) {
            subItemNameEditText.setText(subItem.getSubItemName());
            subItemPriceEditText.setText(String.valueOf(subItem.getPrice()));
        }

        updateSubItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subItemName = subItemNameEditText.getText().toString();
                String subItemPriceStr = subItemPriceEditText.getText().toString();

                if (subItemName.isEmpty() || subItemPriceStr.isEmpty()) {
                    Toast.makeText(EditSubItemActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Double subItemPrice = Double.parseDouble(subItemPriceStr);

                subItem.setSubItemName(subItemName);
                subItem.setPrice(subItemPrice);

                subItemService.update(subItem);

                Toast.makeText(EditSubItemActivity.this, "Sub-Item updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
