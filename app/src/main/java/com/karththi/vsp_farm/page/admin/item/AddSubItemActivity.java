package com.karththi.vsp_farm.page.admin.item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subitem);

        subItemNameEditText = findViewById(R.id.subItemNameEditText);
        subItemPriceEditText = findViewById(R.id.subItemPriceEditText);
        saveSubItemButton = findViewById(R.id.saveSubItemButton);
        backButton = findViewById(R.id.backButton);

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

                Intent intent = new Intent(AddSubItemActivity.this, SubItemListActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSubItemActivity.this, SubItemListActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Get the view that has focus
        View view = getCurrentFocus();

        // Check if the touch event is outside the focused view (keyboard is open)
        if (view != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (view instanceof EditText) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                float x = ev.getRawX() + view.getLeft() - location[0];
                float y = ev.getRawY() + view.getTop() - location[1];

                // If the touch is outside the EditText, hide the keyboard
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                    hideKeyboard(view);
                    view.clearFocus();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
