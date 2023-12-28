package com.example.wi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wi.databinding.ActivityAddHealthInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddHealthInfoActivity extends AppCompatActivity {

    private ActivityAddHealthInfoBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUri = data.getData();
                        binding.healthInfoImageView.setImageURI(imageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHealthInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.btnSaveHealthInfo.setOnClickListener(v -> saveHealthInfo());
        binding.backButtonHealthInfo.setOnClickListener(v -> finish());

        binding.healthInfoImageView.setOnClickListener(v -> imagePickDialog());
    }

    private void saveHealthInfo() {
        String weight = binding.weightInput.getText().toString().trim();
        String height = binding.heightInput.getText().toString().trim();
        String bloodPressure = binding.bloodPressureInput.getText().toString().trim();
        String heartRate = binding.heartRateInput.getText().toString().trim();
        String date = binding.dateInput.getText().toString().trim();

        if (TextUtils.isEmpty(weight))  TextUtils.isEmpty(height);
        TextUtils.isEmpty(bloodPressure);  TextUtils.isEmpty(heartRate);  TextUtils.isEmpty(date); {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } {
            uploadHealthInfoImage(weight, height, bloodPressure, heartRate, date);
        }
    }

    private void uploadHealthInfoImage(String weight, String height, String bloodPressure, String heartRate, String date) {
        progressDialog.setMessage("Uploading Health Info Image");
        progressDialog.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePath = "HealthInfo/healthInfo" + timeStamp;

        Bitmap bitmap = ((BitmapDrawable) binding.healthInfoImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = FirebaseStorage.getInstance().getReference().child(filePath);
        reference.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri uri = taskSnapshot.getStorage().getDownloadUrl().getResult();
                    String downloadUri = uri.toString();
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();

                        Map<String, Object> healthInfo = new HashMap<>();
                        healthInfo.put("weight", weight);
                        healthInfo.put("height", height);
                        healthInfo.put("bloodPressure", bloodPressure);
                        healthInfo.put("heartRate", heartRate);
                        healthInfo.put("date", date);
                        healthInfo.put("healthInfoId", timeStamp);
                        healthInfo.put("healthInfoImage", downloadUri);

                        db.collection("users")
                                .document(userId)
                                .collection("healthInfo")
                                .add(healthInfo)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddHealthInfoActivity.this, "Health info added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddHealthInfoActivity.this, "Failed to add health info", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddHealthInfoActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddHealthInfoActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
        galleryLauncher.launch(intent);
    }
}