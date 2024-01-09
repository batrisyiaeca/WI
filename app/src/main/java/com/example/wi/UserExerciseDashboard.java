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

public class UserExerciseDashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    UserExerciseAdapter userExerciseAdapter;
    List<ExerciseModel> exerciseModels;
    Button search, btnBack;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exercise_dashboard);

        search = findViewById(R.id.btnSearch);
        txtSearch = findViewById(R.id.txtSearch);
        btnBack = findViewById(R.id.btnBack); // Add this line

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        exerciseModels = new ArrayList<>();

        loadAll();

        search.setOnClickListener(v -> {
            String getSearch = txtSearch.getText().toString().trim();
            finSearch(getSearch);
        });

        btnBack.setOnClickListener(v -> startActivity(new Intent(UserExerciseDashboard.this, home.class)));
    }


    private void finSearch(String getSearch) {

        Query ref;

        ref = FirebaseDatabase.getInstance().getReference("Exercise");
        Query firebaseQuery= ref.orderByChild("ExName").startAt(getSearch).endAt(getSearch +"\uf8ff");

        exerciseModels = new ArrayList<>();
        userExerciseAdapter = new UserExerciseAdapter(this,exerciseModels);
        recyclerView.setAdapter(userExerciseAdapter);

        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ExerciseModel exerciseModel = dataSnapshot.getValue(ExerciseModel.class);
                    exerciseModels.add(exerciseModel);

                }
                userExerciseAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                    userExerciseAdapter = new UserExerciseAdapter(getApplicationContext(), exerciseModels);
                    recyclerView.setAdapter(userExerciseAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}