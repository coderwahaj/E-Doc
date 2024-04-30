package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientProfile extends AppCompatActivity {

    private TextView txtPatientName;
    private TextView txtPatientAge;
    private TextView txtPatientGender;
    private TextView txtPatientMedicalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        // Initialize views
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAge = findViewById(R.id.txtPatientAge);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientMedicalHistory = findViewById(R.id.txtPatientMedicalHistory);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name", "N/A");
            String dob = extras.getString("dateOfBirth", "N/A");
            String gender = extras.getString("gender", "N/A");
            String medicalHistory = extras.getString("medicalHistory", "No history provided.");

            Log.d("PatientProfile", "Name: " + name);
            Log.d("PatientProfile", "Date of Birth: " + dob);
            Log.d("PatientProfile", "Gender: " + gender);
            Log.d("PatientProfile", "Medical History: " + medicalHistory);

            // Set data to views
            txtPatientName.setText("Name: " + name);
            txtPatientAge.setText("Date of Birth: " + dob);
            txtPatientGender.setText("Gender: " + gender);
            txtPatientMedicalHistory.setText("Medical History: " + medicalHistory);
        } else {
            Log.d("PatientProfile", "No extras found!");
        }
    }
}