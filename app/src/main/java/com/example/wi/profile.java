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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    TextView fullName, email, phone, verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    Button resendCode, resetPassLocal, changeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        resendCode = findViewById(R.id.resendcode);
        verifyMsg = findViewById(R.id.verifyMsg);

        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if (user.isEmailVerified()) {
            // Email is verified, hide verifyMsg and resendCode
            verifyMsg.setVisibility(View.GONE);
            resendCode.setVisibility(View.GONE);
        } else {
            // Email is not verified, show verifyMsg and resendCode
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(profile.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(profile.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
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
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalleryIntent, 1000);
                }
            });

        }

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
