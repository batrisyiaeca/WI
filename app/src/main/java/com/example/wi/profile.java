package com.example.wi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    TextView fullName, email, phone, verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    Button resendCode, resetPassLocal, changeProfileImage, deleteProfile;
    FirebaseUser user;
    ImageView profileImage, backIV;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);
        resendCode = findViewById(R.id.resendcode);
        verifyMsg = findViewById(R.id.verifyMsg);
        backIV = findViewById(R.id.backIV);
        deleteProfile = findViewById(R.id.deleteBtn);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to dashboard
                startActivity(new Intent(getApplicationContext(), home.class));
            }
        });

        StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationEmail();
            }
        });

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordResetDialog();
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteProfileDialog();
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profile.this, EditProfile.class);
                i.putExtra("fullName", fullName.getText().toString());
                i.putExtra("email", email.getText().toString());
                startActivity(i);
            }
        });


        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));

                    // Check if the email is verified
                    if (user != null && user.isEmailVerified()) {
                        verifyMsg.setText("Email Verified");
                        resendCode.setVisibility(View.GONE);
                        resendCode.setEnabled(false); // Disable the button
                    } else {
                        verifyMsg.setText("Email Not Verified. Click Resend Code to verify.");
                        resendCode.setVisibility(View.VISIBLE);
                        resendCode.setEnabled(true); // Enable the button
                    }
                }
            }
        });
    }

    private void sendVerificationEmail() {
        if (user != null) {
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(profile.this, "Verification Email Has been sent", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("tag", "onFailure: Email not sent" + e.getMessage());
                }
            });
        }
    }

    private void showPasswordResetDialog() {
        final EditText resetPassword = new EditText(this);
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(this);
        passwordResetDialog.setTitle("Reset password?");
        passwordResetDialog.setMessage("Enter New Password > 6 characters long");
        passwordResetDialog.setView(resetPassword);

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset password?");
                passwordResetDialog.setMessage("Enter your email to receive a reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(profile.this, "Reset Link Sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profile.this, "Error! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }


    private void showDeleteProfileDialog() {
        AlertDialog.Builder deleteProfileDialog = new AlertDialog.Builder(this);
        deleteProfileDialog.setTitle("Delete Profile");
        deleteProfileDialog.setMessage("Are you sure you want to delete your profile?");

        deleteProfileDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform deletion
                deleteProfile();
            }
        });

        deleteProfileDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close dialog
                dialog.dismiss();
            }
        });

        deleteProfileDialog.show();
    }

    private void deleteProfile() {
        // Delete profile data in Firestore
        fstore.collection("users").document(userID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Delete profile image in storage (if applicable)
                        StorageReference profileImageRef = storageReference.child("users/" + userID + "/profile.jpg");
                        profileImageRef.delete();

                        // Delete user account in Firebase Authentication
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Sign out and navigate to login or home screen
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(profile.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(profile.this, "Failed to delete account.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(profile.this, "Failed to delete profile data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
