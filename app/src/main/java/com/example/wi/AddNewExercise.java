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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class AddNewExercise extends AppCompatActivity {

    Button btnSave,btnBack;
    EditText txtName,txtRepetition,txtDuration,txtSteps;
    CircleImageView imageView;
    Uri image_uri = null ;
    private static final  int GALLERY_IMAGE_CODE = 100 ;
    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ExName = txtName.getText().toString().trim();
                String ExRepetition = txtRepetition.getText().toString().trim();
                String ExDuration= txtDuration.getText().toString().trim();
                String ExSteps = txtSteps.getText().toString().trim();

                if (TextUtils.isEmpty(ExName)){
                    Toast.makeText(AddNewExercise.this, "Please Enter Exercise Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExRepetition)){
                    Toast.makeText(AddNewExercise.this, "Please Enter Exercise Repetition", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExDuration)){
                    Toast.makeText(AddNewExercise.this, "Please Enter Duration For This Exercise", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ExSteps)){
                    Toast.makeText(AddNewExercise.this, "Please Enter The Steps For This Exercise", Toast.LENGTH_SHORT).show();
                } else {
                    uploadDate(ExName,ExRepetition,ExDuration,ExSteps);
                }


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewExercise.this, Exercise.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void uploadDate(String exName, String exRepetition, String exDuration, String exSteps) {

        pd.setMessage("Saving Exercise");
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
                                hashMap.put("ExId" , timeStamp);
                                hashMap.put("ExImage" , downloadUri);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Exercise");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(AddNewExercise.this, "Exercise has been saved successfully!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddNewExercise.this, Exercise.class));

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(AddNewExercise.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewExercise.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}