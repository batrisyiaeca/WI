package com.example.wi;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditDietDetailsActivity extends AppCompatActivity {

    private EditText editBreakfast, editLunch, editDinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diet_details);

        editBreakfast = findViewById(R.id.editTextBreakfast);
        editLunch = findViewById(R.id.editTextLunch);
        editDinner = findViewById(R.id.editTextDinner);

        // Retrieve the passed data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String breakfast = extras.getString("breakfast");
            String lunch = extras.getString("lunch");
            String dinner = extras.getString("dinner");

            // Set the retrieved data to the EditTexts
            editBreakfast.setText(breakfast);
            editLunch.setText(lunch);
            editDinner.setText(dinner);
        }
    }
}
