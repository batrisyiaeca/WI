package com.example.wi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFaqs extends AppCompatActivity {
    ImageButton add, backBtn;
    AdminFAQAdapter adminFAQAdapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        if (adminFAQAdapter != null) {
            adminFAQAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adminFAQAdapter != null) {
            adminFAQAdapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faqs);

        add = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn);
        add.setOnClickListener(v -> startActivity(new Intent(AdminFaqs.this, AddFaq.class)));
        backBtn.setOnClickListener(view -> startActivity(new Intent(AdminFaqs.this, AdminUserSupport.class)));

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseReference = FirebaseDatabase.getInstance().getReference().child("FAQ");

        FirebaseRecyclerOptions<FAQModel> options =
                new FirebaseRecyclerOptions.Builder<FAQModel>()
                        .setQuery(databaseReference, FAQModel.class)
                        .build();

        adminFAQAdapter = new AdminFAQAdapter(options);
        recyclerView.setAdapter(adminFAQAdapter);
    }
}
