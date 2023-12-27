package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class User_UserSupport extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_user_support);
        ImageButton userBackBtn = findViewById(R.id.backBtn);
        ImageButton userFaqBtn = findViewById(R.id.faqBtn);
        ImageButton userGuideBtn = findViewById(R.id.guideBtn);

        userBackBtn.setOnClickListener(view -> startActivity(new Intent(User_UserSupport.this, home.class)));
        userFaqBtn.setOnClickListener(view -> startActivity(new Intent(User_UserSupport.this, UserFaqs.class)));
        userGuideBtn.setOnClickListener(view -> startActivity(new Intent(User_UserSupport.this, UserGuide.class)));
    }
}
