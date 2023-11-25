package com.example.wi;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wi.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProfile extends AppCompatActivity {
    TextView fullName, email, adminRole;
    ImageView getAddMemberImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    FirebaseUser admin;
    Button resetPassLocal, changeProfileImage;
    ImageView profileImage;
    StorageReference storageReference;
    LinearLayout linearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        adminRole = findViewById(R.id.adminRole);

        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);

        View addMember = findViewById(R.id.addmember);
        
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Intent intent = new Intent(AdminProfile.this, ManageAdmin.class);
                startActivity(intent);
            }
        });

        linearView = findViewById(R.id.linearView);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        admin = fAuth.getCurrentUser();  // Assign the current user to 'admin'
        userID = admin.getUid();  // Get the user ID

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("admin/" + userID + "/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset password?");
                passwordResetDialog.setMessage("Enter New Password > 6 characters long");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Reset password
                        String newPassword = resetPassword.getText().toString();
                        admin.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminProfile.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminProfile.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });

                passwordResetDialog.show(); // Show the dialog
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdminProfile", "Change Profile button clicked");
                Intent i = new Intent(v.getContext(), EditProfile.class);
                i.putExtra("fullName", fullName.getText().toString());
                i.putExtra("email", email.getText().toString());
                i.putExtra("role", adminRole.getText().toString());
                startActivity(i);
            }
        });

        // Fetch and display admin details
        fetchAdminDetails();

        // Fetch and dynamically display staff member details
        fetchAndDisplayStaffMembers();
    }

    private void fetchAdminDetails() {
        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("FirestoreError", "Listener failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("FirestoreDebug", "DocumentSnapshot data: " + documentSnapshot.getData());
                    adminRole.setText(documentSnapshot.getString("role"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                } else {
                    Log.e("FirestoreError", "Document does not exist or is null");
                }
            }
        });
    }

    private void fetchAndDisplayStaffMembers() {
        String userRole = getUserRole();

        if ("admin".equals(userRole) || "manager".equals(userRole)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("users").whereEqualTo("role", "admin");

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot document : documents) {
                        StaffMember staffMember = document.toObject(StaffMember.class);

                        if (staffMember != null) {
                            addStaffMemberView(staffMember);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FirestoreError", "Error fetching staff members", e);
                    Toast.makeText(AdminProfile.this, "Error fetching staff members", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User is not authorized to fetch staff members", Toast.LENGTH_SHORT).show();
        }
    }

    private void addStaffMemberView(StaffMember staffMember) {
        // Create a new TextView for each staff member
        TextView staffTextView = new TextView(this);
        staffTextView.setText("Staff Name: " + staffMember.getfName() + "\nEmail Address: " + staffMember.getEmail());
        linearView.addView(staffTextView);

        // Customize the layout parameters if needed
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        staffTextView.setLayoutParams(layoutParams);

        // Add any additional styling or customization as necessary
    }

    private String getUserRole() {
        // Assume you have a method to retrieve the user's role
        // Replace this with your actual logic to get the user's role
        // For testing purposes, return "admin" here
        return "admin";
    }
}
