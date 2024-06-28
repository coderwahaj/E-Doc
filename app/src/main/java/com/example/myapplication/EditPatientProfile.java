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

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        patientEmail = intent.getStringExtra("email");

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
        RadioButton selectedRadioButton = findViewById(radioGroupGender.getCheckedRadioButtonId());
        String updatedGender = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        // Construct the date string
        String dateOfBirth = updatedDay + "/" + (updatedMonth + 1) + "/" + updatedYear;

        db.collection("patientsList")
                .whereEqualTo("email", patientEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String docId = task.getResult().getDocuments().get(0).getId();

                        // Prepare a map to hold updated data
                        Map<String, Object> updates = new HashMap<>();
                        if (!updatedName.isEmpty()) updates.put("name", updatedName);
                        if (!updatedMedicalHistory.isEmpty()) updates.put("medicalHistory", updatedMedicalHistory);
                        updates.put("dateOfBirth", dateOfBirth);
                        if (!updatedGender.isEmpty()) updates.put("gender", updatedGender);

                        db.collection("patientsList").document(docId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(EditPatientProfile.this, "Profile Updated successfully.", Toast.LENGTH_SHORT).show();
                                    Intent resultIntent = new Intent();
                                    // Add only updated fields to the intent
                                    if (!updatedName.isEmpty()) resultIntent.putExtra("name", updatedName);
                                    if (!updatedMedicalHistory.isEmpty()) resultIntent.putExtra("medicalHistory", updatedMedicalHistory);
                                    resultIntent.putExtra("dateOfBirth", dateOfBirth);
                                    if (!updatedGender.isEmpty()) resultIntent.putExtra("gender", updatedGender);
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(EditPatientProfile.this, "Profile Update Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(EditPatientProfile.this, "No matching email found.", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
