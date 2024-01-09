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

public class Exercise extends AppCompatActivity {

    FloatingActionButton add;
    RecyclerView recyclerView;
    Button btnBack;
    AdminExerciseAdapter adminExerciseAdapter;
    List<ExerciseModel> exerciseModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        add = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        add.setOnClickListener(v -> startActivity(new Intent(Exercise.this,AddNewExercise.class)));

        btnBack.setOnClickListener(v -> startActivity(new Intent(Exercise.this,home.class)));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        exerciseModels = new ArrayList<>();

        loadAll();

    }

    private void loadAll() {

        Query ref;

        ref = FirebaseDatabase.getInstance().getReference("Exercise");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseModels.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ExerciseModel exerciseModel = ds.getValue(ExerciseModel.class);
                    exerciseModels.add(exerciseModel);
                    adminExerciseAdapter = new AdminExerciseAdapter(getApplicationContext(), exerciseModels);
                    recyclerView.setAdapter(adminExerciseAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}