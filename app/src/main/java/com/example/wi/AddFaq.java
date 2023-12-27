package com.example.wi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFaq extends AppCompatActivity {
    RecyclerView recyclerView;
    List<FAQModel> faqModelList;
    EditText questionText;
    EditText answerText;
    Button addFaq;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq);
        recyclerView = findViewById(R.id.rv);
        AdminFAQAdapter adminFaqAdapter;
        faqModelList = new ArrayList<>();

        questionText = (EditText) findViewById(R.id.questionTxt);
        answerText = (EditText) findViewById(R.id.answerTxt);
        addFaq = (Button) findViewById(R.id.addFaq);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(view -> startActivity(new Intent(AddFaq.this, AdminFaqs.class)));

        addFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionText.getText().toString();
                String answer = answerText.getText().toString();

                if (question.isEmpty()) {
                    questionText.setError("Question Field cannot be empty");
                    return;
                }

                if (answer.isEmpty()) {
                    answerText.setError("Answer Field cannot be empty");
                    return;
                }
                addFAQtoDB(question, answer);
            }

            private void addFAQtoDB(String question, String answer) {
                HashMap<String, Object> faqHashmap = new HashMap<>();
                faqHashmap.put("question", question);
                faqHashmap.put("answer", answer);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference faqRef = database.getReference("FAQ");

                String faqID = faqRef.push().getKey();
                faqHashmap.put("faqID", faqID);

                faqRef.child(faqID).setValue(faqHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddFaq.this, "FAQ Added", Toast.LENGTH_SHORT).show();
                        questionText.getText().clear();
                        answerText.getText().clear();
                    }
                });
            }
        });

        //loadAll();


    }
}


   /* private void loadAll() {
        Query ref;

        ref = FirebaseDatabase.getInstance().getReference("TestSupport");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faqModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FAQModel faqModel = ds.getValue(FAQModel.class);
                    faqModelList.add(faqModel);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFAQ(String question, String answer) {
        DatabaseReference faqRef = FirebaseDatabase.getInstance().getReference("TestSupport");
        String faqId = faqRef.push().getKey();
        FAQModel newFAQ = new FAQModel(faqId, question, answer);
        //assert faqId != null;
        faqRef.child(faqId).setValue(newFAQ);
    }
}*/
