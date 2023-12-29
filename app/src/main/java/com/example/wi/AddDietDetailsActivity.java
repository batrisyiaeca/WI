package com.example.wi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddDietDetailsActivity extends AppCompatActivity {

    private EditText breakfastDescription, lunchDescription, dinnerDescription;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diet_details);

        breakfastDescription = findViewById(R.id.breakfastDescription);
        lunchDescription = findViewById(R.id.lunchDescription);
        dinnerDescription = findViewById(R.id.dinnerDescription);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveDietDetails());
    }

    private void saveDietDetails() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String breakfast = breakfastDescription.getText().toString().trim();
        String lunch = lunchDescription.getText().toString().trim();
        String dinner = dinnerDescription.getText().toString().trim();

        // Check if any of the fields are empty
        if (breakfast.isEmpty() || lunch.isEmpty() || dinner.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store the diet details
        Map<String, Object> dietDetails = new HashMap<>();
        dietDetails.put("breakfast", breakfast);
        dietDetails.put("lunch", lunch);
        dietDetails.put("dinner", dinner);

        // Save the diet details to Firestore
        db.collection("users")
                .document(userId)
                .collection("dietDetails")
                .document("currentDiet") // You may want to use a unique document ID here
                .set(dietDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddDietDetailsActivity.this, "Diet details saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddDietDetailsActivity.this, ViewDietDetailsActivity.class);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(AddDietDetailsActivity.this, "Failed to save diet details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
