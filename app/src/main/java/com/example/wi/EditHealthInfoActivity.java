package com.example.wi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wi.databinding.ActivityEditHealthInfoBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditHealthInfoActivity extends AppCompatActivity {

    private ActivityEditHealthInfoBinding binding;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditHealthInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        // Retrieve the health info details passed from the previous activity
        Intent intent = getIntent();
        String healthInfoId = intent.getStringExtra("healthInfoId");
        String weight = intent.getStringExtra("weight");
        String height = intent.getStringExtra("height");
        String bloodPressure = intent.getStringExtra("bloodPressure");
        String heartRate = intent.getStringExtra("heartRate");
        String date = intent.getStringExtra("date");
        String healthInfoImage = intent.getStringExtra("healthInfoImage");

        // Populate the UI with the retrieved health info details
        binding.editWeight.setText(weight);
        binding.editHeight.setText(height);
        binding.editBloodPressure.setText(bloodPressure);
        binding.editHeartRate.setText(heartRate);
        binding.editDate.setText(date);

        // Load the health info image using Glide or another image-loading library
        // You need to implement this part based on your setup
        // Glide.with(this).load(healthInfoImage).into(binding.healthInfoImageView);

        binding.btnSaveEditHealthInfo.setOnClickListener(v -> saveHealthInfo(healthInfoId));
        binding.btnDeleteHealthInfo.setOnClickListener(v -> finish());

        binding.healthInfoImageView.setOnClickListener(v -> imagePickDialog());
    }

    private void saveHealthInfo(String healthInfoId) {
        String weight = binding.editWeight.getText().toString().trim();
        String height = binding.editHeight.getText().toString().trim();
        String bloodPressure = binding.editBloodPressure.getText().toString().trim();
        String heartRate = binding.editHeartRate.getText().toString().trim();
        String date = binding.editDate.getText().toString().trim();

        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(height)
                || TextUtils.isEmpty(bloodPressure) || TextUtils.isEmpty(heartRate) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            updateHealthInfo(healthInfoId, weight, height, bloodPressure, heartRate, date);
        }
    }

    private void updateHealthInfo(String healthInfoId, String weight, String height, String bloodPressure, String heartRate, String date) {
        progressDialog.setMessage("Updating Health Info");
        progressDialog.show();

        String filePath = "HealthInfo/" + "healthInfo" + healthInfoId;

        if (imageUri != null) {
            // If a new image is selected, upload the new image
            Bitmap bitmap = ((BitmapDrawable) binding.healthInfoImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference reference = FirebaseStorage.getInstance().getReference().child(filePath);
            reference.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;

                        String downloadUri = uriTask.getResult().toString();

                        if (uriTask.isSuccessful()) {
                            // Update health info details in Firestore
                            Map<String, Object> updatedHealthInfo = new HashMap<>();
                            updatedHealthInfo.put("weight", weight);
                            updatedHealthInfo.put("height", height);
                            updatedHealthInfo.put("bloodPressure", bloodPressure);
                            updatedHealthInfo.put("heartRate", heartRate);
                            updatedHealthInfo.put("date", date);
                            updatedHealthInfo.put("healthInfoImage", downloadUri);

                            firestore.collection("users")
                                    .document("user123") // Replace with the actual user ID or use FirebaseAuth to get the current user ID
                                    .collection("healthInfo")
                                    .document(healthInfoId)
                                    .update(updatedHealthInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditHealthInfoActivity.this, "Health info updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditHealthInfoActivity.this, "Failed to update health info", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditHealthInfoActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // If no new image is selected, update health info details without uploading a new image
            Map<String, Object> updatedHealthInfo = new HashMap<>();
            updatedHealthInfo.put("weight", weight);
            updatedHealthInfo.put("height", height);
            updatedHealthInfo.put("bloodPressure", bloodPressure);
            updatedHealthInfo.put("heartRate", heartRate);
            updatedHealthInfo.put("date", date);

            firestore.collection("users")
                    .document("user123") // Replace with the actual user ID or use FirebaseAuth to get the current user ID
                    .collection("healthInfo")
                    .document(healthInfoId)
                    .update(updatedHealthInfo)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditHealthInfoActivity.this, "Health info updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditHealthInfoActivity.this, "Failed to update health info", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void imagePickDialog() {
        String[] options = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                galleryPick();
            }
        });

        builder.create().show();
    }

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            // Set the selected image to the ImageView
            if (data != null) {
                imageUri = data.getData();
                binding.healthInfoImageView.setImageURI(imageUri);
            }
        }
    }
}
