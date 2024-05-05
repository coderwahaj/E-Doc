package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditPatientProfile extends AppCompatActivity {

    private EditText editTextName, editTextMedicalHistory;
    private DatePicker datePickerAge;
    private RadioGroup radioGroupGender;
    private Button buttonUpdate;
    private String patientEmail; // Variable to store the email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextMedicalHistory = findViewById(R.id.editTextMedicalHistory);
        datePickerAge = findViewById(R.id.datePickerAge);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonUpdate = findViewById(R.id.btnUpdate);

        // Get email passed from PatientProfile
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            patientEmail = intent.getStringExtra("email"); // Store the email
        }

        // Handle update button click
        buttonUpdate.setOnClickListener(v -> updatePatientProfile());
    }

    private void updatePatientProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve updated values from views
        String updatedName = editTextName.getText().toString().trim();
        String updatedMedicalHistory = editTextMedicalHistory.getText().toString().trim();
        int updatedDay = datePickerAge.getDayOfMonth();
        int updatedMonth = datePickerAge.getMonth();
        int updatedYear = datePickerAge.getYear();
        String updatedGender = ((RadioButton)findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        // Construct the date string or use a Timestamp
        String dateOfBirth = updatedDay + "/" + (updatedMonth + 1) + "/" + updatedYear;

        // Query to find the document with the matching email
        db.collection("patientsList")
                .whereEqualTo("email", patientEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        if (!task.getResult().isEmpty()) {
                            // Assuming email is unique and only one document should match
                            String docId = task.getResult().getDocuments().get(0).getId();

                            // Update the found document
                            db.collection("patientsList").document(docId)
                                    .update("name", updatedName,
                                            "medicalHistory", updatedMedicalHistory,
                                            "dateOfBirth", dateOfBirth, // Updated to use Timestamp
                                            "gender", updatedGender)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EditPatientProfile.this, "Profile Updated successfully.", Toast.LENGTH_SHORT).show();
                                        Intent resultIntent = new Intent();
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EditPatientProfile.this, "Profile Update Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(EditPatientProfile.this, "No matching email found.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(EditPatientProfile.this, "Query failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
