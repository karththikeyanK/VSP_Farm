package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karththi.vsp_farm.R;
import com.karththi.vsp_farm.model.Item;
import com.karththi.vsp_farm.model.Measurement;
import com.karththi.vsp_farm.service.ItemService;

public class EditItemActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private ImageView itemImageView;
    private Button saveButton;
    private Button changeImageButton;

    private Spinner measurementSpinner;

    private ItemService itemService;
    private Item item;

    private Button backButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        itemImageView = findViewById(R.id.itemImageView);
        saveButton = findViewById(R.id.saveButton);
        changeImageButton = findViewById(R.id.changeImageButton);
        backButton = findViewById(R.id.backButton);

        ArrayAdapter<Measurement> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementSpinner.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        itemService = new ItemService(this);

        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        item = itemService.getById(itemId);

        if (item != null) {
            itemNameEditText.setText(item.getName());

            // Set the spinner to the item's measurement
            for (int i = 0; i < Measurement.values().length; i++) {
                if (Measurement.values()[i] == item.getMeasurement()) {
                    measurementSpinner.setSelection(i);
                    break;
                }
            }

            byte[] imageBytes = item.getImage();
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                itemImageView.setImageBitmap(bitmap);
            }
        }

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open image picker
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        String name = itemNameEditText.getText().toString();
        Measurement measurement = (Measurement) measurementSpinner.getSelectedItem();

        itemService.update(name, measurement, itemImageView.getDrawable(), item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                itemImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
        // Optionally, you could add additional logic here
    }

}
