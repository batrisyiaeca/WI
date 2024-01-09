package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserExerciseInfo extends AppCompatActivity {

    TextView txtName, txtRepetition, txtDuration, txtSteps;
    CircleImageView imageView;
    DatabaseReference ref;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exercise_info);

        txtSteps = findViewById(R.id.ex_etSteps);
        txtDuration = findViewById(R.id.ex_etDuration);
        txtName = findViewById(R.id.ex_etExerciseName);
        txtRepetition = findViewById(R.id.ex_etRepetition);
        imageView = findViewById(R.id.ex_ImageView);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserExerciseInfo.this, UserExerciseDashboard.class);
            startActivity(intent);
            finish();
        });

        // Check if "ExId" is passed as an extra
        String postFID = getIntent().getStringExtra("ExId");
        if (postFID == null) {
            // Handle the case where "ExId" is not passed
            // You may want to show an error message or take appropriate action.
            return;
        }

        ref = FirebaseDatabase.getInstance().getReference("Exercise").child(postFID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String exImage = dataSnapshot.child("ExImage").getValue(String.class);
                    if (exImage != null) {
                        Glide.with(UserExerciseInfo.this).load(exImage).into(imageView);
                    }
                } catch (NullPointerException e) {
                    Log.e("UserExerciseInfo", "Error loading ExImage", e);
                }

                // Similar pattern for other fields...
                setTextValue(dataSnapshot, "ExName", txtName);
                setTextValue(dataSnapshot, "ExRepetition", txtRepetition);
                setTextValue(dataSnapshot, "ExDuration", txtDuration);
                setTextValue(dataSnapshot, "ExSteps", txtSteps);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void setTextValue(DataSnapshot dataSnapshot, String key, TextView textView) {
        try {
            String value = dataSnapshot.child(key).getValue(String.class);
            if (value != null) {
                textView.setText(value);
            }
        } catch (NullPointerException e) {
            Log.e("UserExerciseInfo", "Error setting text for " + key, e);
        }
    }
}
