package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminUserSupport extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_support);
        ImageView backBtn = findViewById(R.id.backBtn);
        ImageButton faqBtn = findViewById(R.id.faqBtn);
        ImageButton guideBtn = findViewById(R.id.guideBtn);

        backBtn.setOnClickListener(view -> startActivity(new Intent(AdminUserSupport.this, home.class)));
        faqBtn.setOnClickListener(view -> startActivity(new Intent(AdminUserSupport.this, AdminFaqs.class)));
        guideBtn.setOnClickListener(view -> startActivity(new Intent(AdminUserSupport.this, AdminGuide.class)));
    }
}
