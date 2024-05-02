package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class EditPatientProfile extends AppCompatActivity {

    private EditText editTextName, editTextMedicalHistory;
    private DatePicker datePickerAge;
    private RadioGroup radioGroupGender;
    private Button buttonUpdate;

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

        // Handle update button click
        buttonUpdate.setOnClickListener(v -> updatePatientProfile());
    }

    private void updatePatientProfile() {
        // Retrieve updated values from views
        String updatedName = editTextName.getText().toString().trim();
        String updatedMedicalHistory = editTextMedicalHistory.getText().toString().trim();
        int updatedDay = datePickerAge.getDayOfMonth();
        int updatedMonth = datePickerAge.getMonth();
        int updatedYear = datePickerAge.getYear();
        String updatedGender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        // Perform update operations, such as saving to database
        // Example:
        // patientDatabase.updateProfile(updatedName, updatedMedicalHistory, updatedDay, updatedMonth, updatedYear, updatedGender);

        // Finish activity and return to PatientProfile activity
        finish();
    }
}
