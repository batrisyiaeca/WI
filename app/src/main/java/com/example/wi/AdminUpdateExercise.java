package com.example.wi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminUpdateExercise extends AppCompatActivity {

    Button btnUpdate,btnBack,btnDelete;
    EditText txtName,txtRepetition,txtDuration,txtSteps;
    CircleImageView imageView;
    Uri image_uri = null ;
    private static final  int GALLERY_IMAGE_CODE = 100 ;
    ProgressDialog pd ;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_exercise);

        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        txtSteps = findViewById(R.id.ex_etSteps);
        txtDuration = findViewById(R.id.ex_etDuration);
        txtName = findViewById(R.id.ex_etExerciseName);
        txtRepetition = findViewById(R.id.ex_etRepetition);
        imageView = findViewById(R.id.ex_ImageView);

        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait..");
        pd.setCanceledOnTouchOutside(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        String postFID = getIntent().getExtras().getString("ExId");

        ref = FirebaseDatabase.getInstance().getReference("Exercise")
                .child(postFID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String ExImage = dataSnapshot.child("ExImage").getValue().toString();
                    Glide.with(AdminUpdateExercise.this).load(ExImage).into(imageView);
                }
                catch(NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String ExName = dataSnapshot.child("ExName").getValue().toString();
                    txtName.setText(ExName);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String ExRepetition = dataSnapshot.child("ExRepetition").getValue().toString();
                    txtRepetition.setText(ExRepetition);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String ExDuration = dataSnapshot.child("ExDuration").getValue().toString();
                    txtDuration.setText(ExDuration);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String ExSteps = dataSnapshot.child("ExSteps").getValue().toString();
                    txtSteps.setText(ExSteps);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ExName = txtName.getText().toString().trim();
                String ExRepetition = txtRepetition.getText().toString().trim();
                String ExDuration = txtDuration.getText().toString().trim();
                String ExSteps = txtSteps.getText().toString().trim();
                String ExId = postFID;

                if (TextUtils.isEmpty(ExName)){
                    Toast.makeText(AdminUpdateExercise.this, "Please Enter Exercise Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExRepetition)){
                    Toast.makeText(AdminUpdateExercise.this, "Please Enter Exercise Repetition", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExDuration)){
                    Toast.makeText(AdminUpdateExercise.this, "Please Enter Duration For This Exercise", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExSteps)){
                    Toast.makeText(AdminUpdateExercise.this, "Please Enter The Steps For This Exercise", Toast.LENGTH_SHORT).show();
                } else {
                    uploadDate(ExName,ExRepetition,ExDuration,ExSteps,ExId);
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(AdminUpdateExercise.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Do you want to delete this Exercise ?")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(v -> deleteRecord(postFID));

            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUpdateExercise.this, Exercise.class);
            startActivity(intent);
            finish();            });

    }

    private void deleteRecord(String postFID) {
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Exercise")
                .child(postFID);
        Task<Void> mTask =DbRef.removeValue();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                startActivity(new Intent(AdminUpdateExercise.this,Exercise.class));
                Toast.makeText(AdminUpdateExercise.this,"Successfully Deleted",Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminUpdateExercise.this,"Not Successfully Deleted",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadDate(String exName, String exRepetition, String exDuration, String exSteps, String exId) {

        pd.setMessage("Updating Exercise");
        pd.show();
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filepath = "Exercise/"+"exercise"+timeStamp;

        if (imageView.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
            byte[] data = baos.toByteArray();

            StorageReference reference = FirebaseStorage.getInstance().getReference().child(filepath);
            reference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){

                                HashMap<String , Object> hashMap = new HashMap<>();
                                hashMap.put("ExName" , exName);
                                hashMap.put("ExRepetition" , exRepetition);
                                hashMap.put("ExDuration" , exDuration);
                                hashMap.put("ExSteps" , exSteps);
                                hashMap.put("ExId" , exId);
                                hashMap.put("ExImage" , downloadUri);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Exercise");
                                ref.child(exId).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(AdminUpdateExercise.this, "Exercise has been updated successfully!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AdminUpdateExercise.this, Exercise.class));

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(AdminUpdateExercise.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminUpdateExercise.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
        }

    }

    private void imagePickDialog() {
        String[] options = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    galleryPick();

                }
            }
        });

        builder.create().show();

    }

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , GALLERY_IMAGE_CODE);
    }

    private void permission() {
        Dexter.withContext(this)
                .withPermission(android.Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken toke) {
                        toke.continuePermissionRequest();
                    }
                }).check();
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_IMAGE_CODE){
                image_uri = data.getData();
                imageView.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}