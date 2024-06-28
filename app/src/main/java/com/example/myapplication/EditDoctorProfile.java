package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditDoctorProfile extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextQualification, editTextExperience, editTextFee;
    private Button buttonUpdate;
    private String doctorEmail;
    private String originalName, originalPhone, originalQualification, originalExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextQualification = findViewById(R.id.editTextQualification);
        editTextExperience = findViewById(R.id.editTextExperience);
        buttonUpdate = findViewById(R.id.btnUpdate);

        // Get email passed from DoctorProfile
        Intent intent = getIntent();
        doctorEmail = intent.getStringExtra("email");
        originalName = intent.getStringExtra("name");
        originalPhone = intent.getStringExtra("phone");
        originalQualification = intent.getStringExtra("qualification");
        originalExperience = intent.getStringExtra("experience");

        // Set original values to editTexts
        editTextName.setText(originalName);
        editTextPhone.setText(originalPhone);
        editTextQualification.setText(originalQualification);
        editTextExperience.setText(originalExperience);

        // Handle update button click
        buttonUpdate.setOnClickListener(v -> updateDoctorProfile());
    }

    private void updateDoctorProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String updatedName = editTextName.getText().toString().trim();
        String updatedPhone = editTextPhone.getText().toString().trim();
        String updatedQualification = editTextQualification.getText().toString().trim();
        String updatedExperience = editTextExperience.getText().toString().trim();

        if (updatedName.equals(originalName) && updatedPhone.equals(originalPhone) &&
                updatedQualification.equals(originalQualification) && updatedExperience.equals(originalExperience)) {
            // No fields were updated
            Toast.makeText(this, "No fields were updated.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare update data
        Map<String, Object> updates = new HashMap<>();
        if (!updatedName.equals(originalName)) {
            updates.put("name", updatedName);
        }
        if (!updatedPhone.equals(originalPhone)) {
            updates.put("phone", updatedPhone);
        }
        if (!updatedQualification.equals(originalQualification)) {
            updates.put("qualification", updatedQualification);
        }
        if (!updatedExperience.equals(originalExperience)) {
            updates.put("experience", updatedExperience);
        }

        db.collection("doctors")
                .whereEqualTo("email", doctorEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String docId = task.getResult().getDocuments().get(0).getId();
                        db.collection("doctors").document(docId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(EditDoctorProfile.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                    // Return updated data to DoctorProfile activity
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("name", updatedName);
                                    resultIntent.putExtra("phone", updatedPhone);
                                    resultIntent.putExtra("qualification", updatedQualification);
                                    resultIntent.putExtra("experience", updatedExperience);
                                    setResult(RESULT_OK, resultIntent);
                                    finish(); // Close activity after updating
                                })
                                .addOnFailureListener(e -> Toast.makeText(EditDoctorProfile.this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(EditDoctorProfile.this, "No matching email found.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
