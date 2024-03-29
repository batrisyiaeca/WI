package com.example.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AdminGuide extends AppCompatActivity {
    Button videoBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_guide);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> startActivity(new Intent(AdminGuide.this, AdminUserSupport.class)));
        videoBtn = findViewById(R.id.videoBtn);
        videoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String youtubeID = "MtR2IluFqMc";

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://youtu.be/" + youtubeID)
                );

                startActivity(intent);
            }
        });
    }
}