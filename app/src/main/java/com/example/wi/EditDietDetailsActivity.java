package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
        Button saveButton = findViewById(R.id.saveButton);

        // Retrieve the passed data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String breakfastRecommendation = extras.getString("breakfastRecommendation");
            String lunchRecommendation = extras.getString("lunchRecommendation");
            String dinnerRecommendation = extras.getString("dinnerRecommendation");

            // Set the retrieved data to the EditTexts
            editBreakfast.setText(breakfastRecommendation);
            editLunch.setText(lunchRecommendation);
            editDinner.setText(dinnerRecommendation);
        }

        saveButton.setOnClickListener(v -> saveDietDetails());
    }

    private void saveDietDetails() {
        // Get the edited data from the EditTexts
        String editedBreakfast = editBreakfast.getText().toString().trim();
        String editedLunch = editLunch.getText().toString().trim();
        String editedDinner = editDinner.getText().toString().trim();

        // Create an Intent to pass the edited data back to ViewDietDetailsActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("editedBreakfast", editedBreakfast);
        resultIntent.putExtra("editedLunch", editedLunch);
        resultIntent.putExtra("editedDinner", editedDinner);

        // Set the result to OK and attach the Intent
        setResult(RESULT_OK, resultIntent);

        // Finish the current activity
        finish();
    }
}
