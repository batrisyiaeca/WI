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

public class AdminUpdateSupplementActivity extends AppCompatActivity {

    Button btnUpdate,btnBack,btnDelete;
    EditText txtName,txtVitamin,txtIngredient,txtDosage;
    CircleImageView imageView;
    Uri image_uri = null ;
    private static final  int GALLERY_IMAGE_CODE = 100 ;
    ProgressDialog pd ;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_supplement);

        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        txtDosage = findViewById(R.id.sup_etDosage);
        txtIngredient = findViewById(R.id.sup_etIngredient);
        txtName = findViewById(R.id.sup_etName);
        txtVitamin = findViewById(R.id.sup_etVitamin);
        imageView = findViewById(R.id.sup_ImageView);

        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait..");
        pd.setCanceledOnTouchOutside(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        String postFID = getIntent().getExtras().getString("SupId");

        ref = FirebaseDatabase.getInstance().getReference("Supplement")
                .child(postFID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String SupImage = dataSnapshot.child("SupImage").getValue().toString();
                    Glide.with(AdminUpdateSupplementActivity.this).load(SupImage).into(imageView);
                }
                catch(NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupName = dataSnapshot.child("SupName").getValue().toString();
                    txtName.setText(SupName);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupVitamin = dataSnapshot.child("SupVitamin").getValue().toString();
                    txtVitamin.setText(SupVitamin);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupIngredient = dataSnapshot.child("SupIngredient").getValue().toString();
                    txtIngredient.setText(SupIngredient);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupDosage = dataSnapshot.child("SupDosage").getValue().toString();
                    txtDosage.setText(SupDosage);
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

                String SupName = txtName.getText().toString().trim();
                String SupVitamin = txtVitamin.getText().toString().trim();
                String SupIngredient = txtIngredient.getText().toString().trim();
                String SupDosage = txtDosage.getText().toString().trim();
                String SupId = postFID;

                if (TextUtils.isEmpty(SupName)){
                    Toast.makeText(AdminUpdateSupplementActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(SupVitamin)){
                    Toast.makeText(AdminUpdateSupplementActivity.this, "Please Enter Vitamin", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(SupIngredient)){
                    Toast.makeText(AdminUpdateSupplementActivity.this, "Please Enter Ingredient", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(SupDosage)){
                    Toast.makeText(AdminUpdateSupplementActivity.this, "Please Enter Dosage", Toast.LENGTH_SHORT).show();
                } else {
                    uploadDate(SupName,SupVitamin,SupIngredient,SupDosage,SupId);
                }


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(AdminUpdateSupplementActivity.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Do you want to delete the Supplement ?")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(v -> deleteRecord(postFID));

            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUpdateSupplementActivity.this, AdminHomeActivity.class);
            startActivity(intent);
            finish();            });

    }

    private void deleteRecord(String postFID) {
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Supplement")
                .child(postFID);
        Task<Void> mTask =DbRef.removeValue();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                startActivity(new Intent(AdminUpdateSupplementActivity.this,AdminHomeActivity.class));
                Toast.makeText(AdminUpdateSupplementActivity.this,"Successfully Deleted",Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminUpdateSupplementActivity.this,"Not Successfully Deleted",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadDate(String supName, String supVitamin, String supIngredient, String supDosage, String supId) {

        pd.setMessage("Updating Supplement");
        pd.show();
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filepath = "Supplement/"+"supplement"+timeStamp;

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
                                hashMap.put("SupName" , supName);
                                hashMap.put("SupVitamin" , supVitamin);
                                hashMap.put("SupIngredient" , supIngredient);
                                hashMap.put("SupDosage" , supDosage);
                                hashMap.put("SupId" , supId);
                                hashMap.put("SupImage" , downloadUri);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Supplement");
                                ref.child(supId).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(AdminUpdateSupplementActivity.this, "Supplement has been Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AdminUpdateSupplementActivity.this, AdminHomeActivity.class));

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(AdminUpdateSupplementActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminUpdateSupplementActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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