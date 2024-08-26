package com.karththi.vsp_farm.page.admin.item;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private Spinner measurementSpinner;
    private ImageView itemImageView;
    private Button selectImageButton;
    private Button saveItemButton;

    private ItemService itemService;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        measurementSpinner = findViewById(R.id.measurementSpinner);
        itemImageView = findViewById(R.id.itemImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveItemButton = findViewById(R.id.saveItemButton);

        itemService = new ItemService(this);

        // Setup the spinner
        ArrayAdapter<Measurement> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Measurement.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurementSpinner.setAdapter(adapter);

        saveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void saveItem() {
        String name = itemNameEditText.getText().toString().trim();
        Measurement measurement = (Measurement) measurementSpinner.getSelectedItem();
        byte[] image = imageToByteArray(selectedImageBitmap);

        if (name.isEmpty() || measurement == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemService.addItem(new Item(0, name, measurement, image));
        finish();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                itemImageView.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] imageToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
