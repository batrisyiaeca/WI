package com.example.wi;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter staffAdapter;
    private List<StaffMember> staffList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admin);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        staffList = new ArrayList<>();

        // Call the method to retrieve data from Firestore
        retrieveAdminsFromFirestore();

        staffAdapter = new StaffAdapter(staffList);
        recyclerView.setAdapter(staffAdapter);
    }

    private void retrieveAdminsFromFirestore() {
        db.collection("staffMembers")
                .whereEqualTo("role", "admin")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            String fName = document.getString("fName");
                            String role = document.getString("role");

                            StaffMember staffMember = new StaffMember(email, fName, role);
                            staffList.add(staffMember);
                        }

                        staffAdapter.notifyDataSetChanged();
                    } else {
                        // Handle errors
                    }
                });
    }
}
