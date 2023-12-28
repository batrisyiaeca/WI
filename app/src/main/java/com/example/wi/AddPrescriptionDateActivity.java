package com.example.wi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wi.adapters.PrescriptionDateAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddPrescriptionDateActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<PrescriptionDate> prescriptionDateList = new ArrayList<>();
    private PrescriptionDateAdapter prescriptionDateAdapter;
    private ProgressDialog progressDialog;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private DatePicker datePicker;
    private EditText condition1EditText;
    private EditText condition2EditText;

    // PrescriptionDate.java

    public class PrescriptionDate {

        private String title;
        private String description;
        private String date;
        private String condition1;
        private String condition2;

        // Default constructor required for Firestore
        public PrescriptionDate() {
            // Default constructor is necessary for Firestore to deserialize objects
        }

        public PrescriptionDate(String title, String description, String date, String condition1, String condition2) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.condition1 = condition1;
            this.condition2 = condition2;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDate() {
            return date;
        }

        public String getCondition1() {
            return condition1;
        }

        public String getCondition2() {
            return condition2;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription_date);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        // RecyclerView setup
        prescriptionDateAdapter = new PrescriptionDateAdapter(prescriptionDateList);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHealthInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(prescriptionDateAdapter);

        // EditText setup
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        datePicker = findViewById(R.id.datePickerHealthInfo);
        condition1EditText = findViewById(R.id.editTextCondition1);
        condition2EditText = findViewById(R.id.editTextCondition2);

        Button addButton = findViewById(R.id.buttonAddHealthInfo);
        addButton.setOnClickListener(v -> addPrescriptionDate());
    }

    private void addPrescriptionDate() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String condition1 = condition1EditText.getText().toString().trim();
        String condition2 = condition2EditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(condition1) || TextUtils.isEmpty(condition2)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            savePrescriptionDate(title, description, condition1, condition2);
        }
    }

    private void savePrescriptionDate(String title, String description, String condition1, String condition2) {
        progressDialog.setMessage("Saving Prescription Date");
        progressDialog.show();

        // Get the selected date from DatePicker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Months are 0-indexed
        int year = datePicker.getYear();
        String date = day + "/" + month + "/" + year;

        // Get the current user's ID from Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();

            // Create a PrescriptionDate object
            PrescriptionDate prescriptionDate = new PrescriptionDate(title, description, date, condition1, condition2);

            // Add the PrescriptionDate object to the Firestore collection
            db.collection("users")
                    .document(userId)
                    .collection("prescriptionDates")
                    .add(prescriptionDate)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Toast.makeText(AddPrescriptionDateActivity.this, "Prescription date added successfully", Toast.LENGTH_SHORT).show();
                        // Clear the input fields or perform any other necessary actions
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AddPrescriptionDateActivity.this, "Failed to add prescription date", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
