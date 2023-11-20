package com.example.wi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class home extends AppCompatActivity {

    String userID;
    FirebaseUser user;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // Retrieve user information
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        Button profileButton = findViewById(R.id.profileBtn);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Button clicked");

                // Fetch the user's role from Firestore
                fetchUserRole();
            }
        });
    }

    private void fetchUserRole() {
        String userId = fAuth.getCurrentUser().getUid();

        if (userId != null) {
            // Check if userId is not null
            fstore.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Make sure document is not null before accessing data
                                String userRole = document.getString("role");
                                Log.d("UserRole", "User role: " + userRole);

                                // Check the user's role and navigate accordingly
                                if ("member".equals(userRole)) {
                                    // If the user's role is 'member', go to the member page
                                    Intent intent = new Intent(home.this, profile.class);
                                    startActivity(intent);
                                } else if ("manager".equals(userRole) || "admin".equals(userRole)) {
                                    // If the user's role is 'manager' or 'admin', go to the AdminProfile page
                                    Intent intent = new Intent(home.this, AdminProfile.class);
                                    startActivity(intent);
                                } else {
                                    Log.w("UserRole", "Unknown user role: " + userRole);
                                    // Handle the case where the user role is unknown
                                }
                            } else {
                                Log.e("FirestoreError", "Document does not exist or is null");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error fetching user role", e);
                    });
        } else {
            // Handle the case where userId is null
            Log.e("UserIDError", "User ID is null");
        }
    }
}
