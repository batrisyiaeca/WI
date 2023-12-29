package com.example.wi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFaqs extends AppCompatActivity {
    ImageView backBtn;
    UserFAQAdapter userFAQAdapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        if (userFAQAdapter != null) {
            userFAQAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userFAQAdapter != null) {
            userFAQAdapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_faqs);


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> startActivity(new Intent(UserFaqs.this, User_UserSupport.class)));

        recyclerView = findViewById(R.id.rv_u);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseReference = FirebaseDatabase.getInstance().getReference().child("FAQ");

        FirebaseRecyclerOptions<FAQModel> options =
                new FirebaseRecyclerOptions.Builder<FAQModel>()
                        .setQuery(databaseReference, FAQModel.class)
                        .build();

        userFAQAdapter = new UserFAQAdapter(options);
        recyclerView.setAdapter(userFAQAdapter);
    }
}
