package com.example.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button profileButton = findViewById(R.id.profileBtn); // Corrected variable name

        profileButton.setOnClickListener(new View.OnClickListener() { // Added the missing setOnClickListener method
            @Override
            public void onClick(View v) {
                // Create an Intent to open the ProfileActivity (assuming that's the name of the class)
                Intent intent = new Intent(home.this, profile.class);
                startActivity(intent);
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
