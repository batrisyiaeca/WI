package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    FloatingActionButton add;
    Button btnBack;
    RecyclerView recyclerView;
    AdminSupplementAdapter adminSupplementAdapter;
    List<SupplementModel> supplementModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        add = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        add.setOnClickListener(v -> startActivity(new Intent(AdminHomeActivity.this,AdminAddSupplementActivity.class)));

        btnBack.setOnClickListener(v -> startActivity(new Intent(AdminHomeActivity.this,home.class)));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        supplementModels = new ArrayList<>();

        loadAll();

    }

    private void loadAll() {

        Query ref;

        ref = FirebaseDatabase.getInstance().getReference("Supplement");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplementModels.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    SupplementModel supplementModel = ds.getValue(SupplementModel.class);
                    supplementModels.add(supplementModel);
                    adminSupplementAdapter = new AdminSupplementAdapter(getApplicationContext(), supplementModels);
                    recyclerView.setAdapter(adminSupplementAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}