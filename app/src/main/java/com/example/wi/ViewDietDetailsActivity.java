package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewDietDetailsActivity extends AppCompatActivity {

    private TextView breakfastText, lunchText, dinnerText;
    private TextView breakfastRecommendation, lunchRecommendation, dinnerRecommendation;
    private Button dietSwitch, addToFavouriteButton, editButton;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String currentDocumentId = "currentDiet"; // You may want to use a unique document ID here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_diet_details);

        breakfastText = findViewById(R.id.breakfastText);
        lunchText = findViewById(R.id.lunchText);
        dinnerText = findViewById(R.id.dinnerText);

        breakfastRecommendation = findViewById(R.id.breakfastRecommendation);
        lunchRecommendation = findViewById(R.id.lunchRecommendation);
        dinnerRecommendation = findViewById(R.id.dinnerRecommendation);

        dietSwitch = findViewById(R.id.dietSwitch);
        addToFavouriteButton = findViewById(R.id.addToFavouriteButton);
        editButton = findViewById(R.id.editButton);


        // Load diet details on activity creation
        loadDietDetails();

        addToFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewDietDetailsActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDietDetailsActivity.this, EditDietDetailsActivity.class);

                // Get the current diet details
                String breakfastRecommendation = breakfastText.getText().toString();
                String lunchRecommendation = lunchText.getText().toString();
                String dinnerRecommendation = dinnerText.getText().toString();

                // Pass data to the editing activity
                intent.putExtra("breakfast", breakfastRecommendation);
                intent.putExtra("lunch", lunchRecommendation);
                intent.putExtra("dinner", dinnerRecommendation);

                startActivity(intent);
            }
        });
    }

    private void loadDietDetails() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("dietDetails")
                .document(currentDocumentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve diet details and update UI
                                String breakfast = document.getString("breakfast");
                                String lunch = document.getString("lunch");
                                String dinner = document.getString("dinner");

                                updateUI(breakfast, lunch, dinner);
                            } else {
                                Toast.makeText(ViewDietDetailsActivity.this, "No diet details found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewDietDetailsActivity.this, "Error retrieving diet details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(String breakfast, String lunch, String dinner) {

        breakfastRecommendation.setText(breakfast);
        lunchRecommendation.setText(lunch);
        dinnerRecommendation.setText(dinner);
    }
}
