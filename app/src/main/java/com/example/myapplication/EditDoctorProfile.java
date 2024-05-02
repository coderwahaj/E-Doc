
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditDoctorProfile extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextQualification, editTextExperience, editTextFee;
    private Spinner spinnerCategory;
    private Button buttonUpdate;
    private String doctorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        //spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextQualification = findViewById(R.id.editTextQualification);
        editTextExperience = findViewById(R.id.editTextExperience);
        buttonUpdate = findViewById(R.id.btnUpdate);

        // Get email passed from DoctorProfile
        Log.d("EditDoctorProfile", "Received email: " + doctorEmail);
        Intent intent = getIntent();
        doctorEmail = intent.getStringExtra("email");
        if (doctorEmail == null || doctorEmail.isEmpty()) {
            Toast.makeText(this, "Email nottt provided.", Toast.LENGTH_LONG).show();
            return;
        }
        // Handle update button click
        buttonUpdate.setOnClickListener(v -> updateDoctorProfile());


    }
    private void updateDoctorProfile() {
        if (doctorEmail == null || doctorEmail.isEmpty()) {
            Toast.makeText(this, "Email not provided.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String updatedName = editTextName.getText().toString().trim();
        String updatedPhone = editTextPhone.getText().toString().trim();
        String updatedQualification = editTextQualification.getText().toString().trim();
        String updatedExperience = editTextExperience.getText().toString().trim();

        if (updatedName.isEmpty() || updatedPhone.isEmpty() || updatedQualification.isEmpty() || updatedExperience.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!updatedPhone.matches("\\d+") || !updatedExperience.matches("\\d+")) {
            Toast.makeText(this, "Please enter valid numbers for phone and experience.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("doctors")
                .whereEqualTo("email", doctorEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String docId = task.getResult().getDocuments().get(0).getId();
                        db.collection("doctors").document(docId)
                                .update("name", updatedName, "phone", updatedPhone,
                                        "qualification", updatedQualification, "experience", updatedExperience)
                                .addOnSuccessListener(aVoid -> Toast.makeText(EditDoctorProfile.this, "Profile Updated successfully.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(EditDoctorProfile.this, "Profile Update Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(EditDoctorProfile.this, "No matching email found.", Toast.LENGTH_LONG).show();
                    }
                });
    }

}

