
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.appcompat.app.AppCompatActivity;

public class PatientProfile extends AppCompatActivity {

    private TextView txtPatientName, txtPatientAge, txtPatientGender, txtPatientMedicalHistory;
    private Button btnEditProfile, btnBookAppointment, btnViewPastAppointments;
    private String patientEmail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Initialize views
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAge = findViewById(R.id.txtPatientAge);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientMedicalHistory = findViewById(R.id.txtPatientMedicalHistory);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        btnViewPastAppointments = findViewById(R.id.btnViewPastAppointments);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name", "N/A");
            String age = extras.getString("dateOfBirth", "N/A");
            String gender = extras.getString("gender", "N/A");
            String medicalHistory = extras.getString("medicalHistory", "No history provided.");
            patientEmail = extras.getString("email"); // Store the email passed from Login

            // Set data to views
            txtPatientName.setText("Name: " + name);
            txtPatientAge.setText("Date of Birth: " + age);
            txtPatientGender.setText("Gender: " + gender);
            txtPatientMedicalHistory.setText("Medical History: " + medicalHistory);
        }

        // Handle edit profile button click
        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());

        // Handle book appointment button click
        btnBookAppointment.setOnClickListener(v -> openBookAppointmentActivity());

        // Handle view past appointments button click
        btnViewPastAppointments.setOnClickListener(v -> openViewPastAppointmentsActivity());
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(PatientProfile.this, EditPatientProfile.class);
        intent.putExtra("name", txtPatientName.getText().toString());
        intent.putExtra("dateOfBirth", txtPatientAge.getText().toString());
        intent.putExtra("gender", txtPatientGender.getText().toString());
        intent.putExtra("medicalHistory", txtPatientMedicalHistory.getText().toString());
        intent.putExtra("email", patientEmail); // Pass the email to the next activity
        startActivity(intent);
    }


    private void openBookAppointmentActivity() {
        // Implement opening the activity for booking appointments
    }

    private void openViewPastAppointmentsActivity() {
        // Implement opening the activity for viewing past appointments
    }
}

