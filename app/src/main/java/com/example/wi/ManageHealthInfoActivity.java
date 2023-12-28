package com.example.wi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wi.adapters.HealthInfoAdapter;
import com.example.wi.models.HealthInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageHealthInfoActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<HealthInfo> healthInfoList = new ArrayList<>();
    private HealthInfoAdapter healthInfoAdapter;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    private Context EditHealthInfoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManageHealthInfoBinding binding = ActivityManageHealthInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        // RecyclerView setup
        healthInfoAdapter = new HealthInfoAdapter(healthInfoList);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(healthInfoAdapter);

        binding.btnAddHealthInfo.setOnClickListener(v -> navigateToAddHealthInfo());

        // Load health info data
        loadHealthInfoData();
    }

    private void navigateToAddHealthInfo() {
        Intent intent = new Intent(this, AddHealthInfoActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadHealthInfoData() {
        progressDialog.setMessage("Loading Health Info");
        progressDialog.show();

        // Get the current user ID from Firebase Authentication
        String currentUserId = getCurrentUserId();

        // Query to get health info data for the current user
        Query query = db.collection("users")
                .document(currentUserId)
                .collection("healthInfo")
                .orderBy("date", Query.Direction.DESCENDING);

        query.addSnapshotListener((value, error) -> {
            progressDialog.dismiss();
            if (error != null) {
                Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            healthInfoList.clear(); // Clear the list before adding new data

            for (DocumentChange documentChange : Objects.requireNonNull(value).getDocumentChanges()) {
                QueryDocumentSnapshot doc = documentChange.getDocument();
                HealthInfo healthInfo = doc.toObject(HealthInfo.class);
                healthInfoList.add(healthInfo);
            }

            healthInfoAdapter.notifyDataSetChanged();
        });
    }
    private String getCurrentUserId() {
        // Get the current user ID from Firebase Authentication
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            return "";
        }
    }
    private void showOptionsDialog(HealthInfo healthInfo) {
        // Customize this dialog with options like Edit and Delete
        // You can use AlertDialog or any other custom dialog implementation

        // Example AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Launch the EditHealthInfoActivity with the selected health info data
                    launchEditActivity(healthInfo);
                    break;
                case 1:
                    // Show delete confirmation dialog
                    showDeleteConfirmationDialog(healthInfo);
                    break;
            }
        });
        builder.create().show();
    }

    private void launchEditActivity(HealthInfo healthInfo) {
        Intent intent = new Intent(this, EditHealthInfoActivity.class);
        // Pass the selected health info data to the editing activity
        intent.putExtra("healthInfo", String.valueOf(healthInfo));
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(HealthInfo healthInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this health info?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call a method to delete the health info
                deleteHealthInfo(healthInfo);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog if canceled
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteHealthInfo(HealthInfo healthInfo) {
        progressDialog.setMessage("Deleting Health Info");
        progressDialog.show();

        // Implement the logic to delete the health info from Firestore here
        // You need to remove the document with the corresponding healthInfoId
        firestore.collection("users")
                .document("user123") // Replace with the actual user ID or use FirebaseAuth to get the current user ID
                .collection("healthInfo")
                .document(healthInfo.getHealthInfoId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(EditHealthInfoActivity, "Health info deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditHealthInfoActivity, "Failed to delete health info", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setFirestore(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void setEditHealthInfoActivity(Context editHealthInfoActivity) {
        EditHealthInfoActivity = editHealthInfoActivity;
    }
}
