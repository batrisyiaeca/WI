package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

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

        // Find the CardView, ImageView, and TextView by their IDs
        CardView profileCardView = findViewById(R.id.profileCardView);

        CardView logoutCardView = findViewById(R.id.logoutCardView);

        CardView supplementCardView = findViewById(R.id.suppCardView);
        CardView supportCardView = findViewById(R.id.supportCardView);

        CardView excerCardView = findViewById(R.id.excerCardView);

        supplementCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Supplement Card clicked");

                // Fetch the user's role for the supplement page
                fetchUserSupplementRole();
            }
        });

        excerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Exercise Card clicked");

                // Fetch the user's role for the supplement page
                fetchUserExerciseRole();
            }
        });

        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Profile Card clicked");

                // Fetch the user's role from Firestore
                fetchUserRole();
            }
        });

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Logout Card clicked");
                logoutUser();
            }
        });

        supportCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "Support Card clicked");

                // Fetch the user's role from Firestore
                fetchUserSupportRole();
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
                                } else if ("manager".equals(userRole)) {
                                    // If the user's role is 'manager' or 'admin', go to the AdminProfile page
                                    Intent intent = new Intent(home.this, profile.class);
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

    private void fetchUserSupplementRole() {
        String userId = fAuth.getCurrentUser().getUid();

        if (userId != null) {
            fstore.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String userRole = document.getString("role");
                                Log.d("UserRole", "User role: " + userRole);

                                // Check the user's role and navigate accordingly
                                if ("member".equals(userRole)) {
                                    // If the user's role is 'member' or 'manager', go to the Supplement page
                                    Intent intent = new Intent(home.this, UserHomeActivity.class);
                                    startActivity(intent);
                                } else if ("manager".equals(userRole)) {
                                    // If the user's role is 'manager' or 'admin', go to the AdminProfile page
                                    Intent intent = new Intent(home.this, AdminHomeActivity.class);
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

    private void fetchUserExerciseRole() {
        String userId = fAuth.getCurrentUser().getUid();

        if (userId != null) {
            fstore.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String userRole = document.getString("role");
                                Log.d("UserRole", "User role: " + userRole);

                                // Check the user's role and navigate accordingly
                                if ("member".equals(userRole)) {
                                    // If the user's role is 'member' or 'manager', go to the Supplement page
                                    Intent intent = new Intent(home.this, UserExerciseDashboard.class);
                                    startActivity(intent);
                                } else if ("manager".equals(userRole)) {
                                    // If the user's role is 'manager' or 'admin', go to the AdminProfile page
                                    Intent intent = new Intent(home.this, Exercise.class);
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

    private void fetchUserSupportRole() {
        String userId = fAuth.getCurrentUser().getUid();

        if (userId != null) {
            fstore.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String userRole = document.getString("role");
                                Log.d("UserRole", "User role: " + userRole);

                                // Check the user's role and navigate accordingly
                                if ("member".equals(userRole)) {
                                    // If the user's role is 'member' or 'manager', go to the Supplement page
                                    Intent intent = new Intent(home.this, User_UserSupport.class);
                                    startActivity(intent);
                                } else if ("manager".equals(userRole)) {
                                    // If the user's role is 'manager' or 'admin', go to the AdminProfile page
                                    Intent intent = new Intent(home.this, AdminUserSupport.class);
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
    private void logoutUser() {
        fAuth.signOut();
        Intent intent = new Intent(home.this, MainActivity.class); // Replace LoginActivity with the actual login activity class
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
