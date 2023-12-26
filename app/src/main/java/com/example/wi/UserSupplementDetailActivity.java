package com.example.wi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSupplementDetailActivity extends AppCompatActivity {

    TextView txtName,txtVitamin,txtIngredient,txtDosage;
    CircleImageView imageView;
    DatabaseReference ref;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_supplement_detail);

        txtDosage = findViewById(R.id.sup_etDosage);
        txtIngredient = findViewById(R.id.sup_etIngredient);
        txtName = findViewById(R.id.sup_etName);
        txtVitamin = findViewById(R.id.sup_etVitamin);
        imageView = findViewById(R.id.sup_ImageView);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserSupplementDetailActivity.this, UserHomeActivity.class);
            startActivity(intent);
            finish();            });

        String postFID = getIntent().getExtras().getString("SupId");

        ref = FirebaseDatabase.getInstance().getReference("Supplement")
                .child(postFID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String SupImage = Objects.requireNonNull(dataSnapshot.child("SupImage").getValue()).toString();
                    Glide.with(UserSupplementDetailActivity.this).load(SupImage).into(imageView);
                }
                catch(NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupName = Objects.requireNonNull(dataSnapshot.child("SupName").getValue()).toString();
                    txtName.setText(SupName);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupVitamin = Objects.requireNonNull(dataSnapshot.child("SupVitamin").getValue()).toString();
                    txtVitamin.setText(SupVitamin);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupIngredient = Objects.requireNonNull(dataSnapshot.child("SupIngredient").getValue()).toString();
                    txtIngredient.setText(SupIngredient);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
                try {
                    String SupDosage = Objects.requireNonNull(dataSnapshot.child("SupDosage").getValue()).toString();
                    txtDosage.setText(SupDosage);
                } catch (NullPointerException e) {
                    System.out.println("NullPointerException thrown!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}