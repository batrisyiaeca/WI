package com.example.wi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserSupplementAdapter userSupplementAdapter;
    List<SupplementModel> supplementModels;
    Button search, btnBack;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        search = findViewById(R.id.btnSearch);
        txtSearch = findViewById(R.id.txtSearch);
        btnBack = findViewById(R.id.btnBack);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        supplementModels = new ArrayList<>();

        loadAll();

        search.setOnClickListener(v -> {
            String getSearch = txtSearch.getText().toString().trim();

            finSearch(getSearch);
        });

        btnBack.setOnClickListener(v -> startActivity(new Intent(UserHomeActivity.this,home.class)));


    }

    private void finSearch(String getSearch) {

        Query ref;

        ref = FirebaseDatabase.getInstance().getReference("Supplement");
        Query firebaseQuery= ref.orderByChild("SupName").startAt(getSearch).endAt(getSearch +"\uf8ff");

        supplementModels = new ArrayList<>();
        userSupplementAdapter = new UserSupplementAdapter(this,supplementModels);
        recyclerView.setAdapter(userSupplementAdapter);

        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    SupplementModel supplementModel = dataSnapshot.getValue(SupplementModel.class);
                    supplementModels.add(supplementModel);

                }
                userSupplementAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                    userSupplementAdapter = new UserSupplementAdapter(getApplicationContext(), supplementModels);
                    recyclerView.setAdapter(userSupplementAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}