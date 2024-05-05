package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PatientProfile extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 1; // Define a request code

    private TextView txtPatientName, txtPatientAge, txtPatientGender, txtPatientMedicalHistory;
    private Button btnEditProfile, btnBookAppointment, btnViewPastAppointments;
    private String patientEmail;
    private TextView txtPatientEmail;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Initialize views
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAge = findViewById(R.id.txtPatientAge);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientMedicalHistory = findViewById(R.id.txtPatientMedicalHistory);
        txtPatientEmail = findViewById(R.id.txtPatientEmail); // Initialize email TextView
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        btnViewPastAppointments = findViewById(R.id.btnCancelAppointment);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name", "N/A");
            String age = extras.getString("dateOfBirth", "N/A");
            String gender = extras.getString("gender", "N/A");
            String medicalHistory = extras.getString("medicalHistory", "No history provided.");
            patientEmail = extras.getString("email", "Email not provided"); // Handle null email

            // Set data to views
            txtPatientName.setText("Name: " + name);
            txtPatientAge.setText("Date of Birth: " + age);
            txtPatientGender.setText("Gender: " + gender);
            txtPatientMedicalHistory.setText("Medical History: " + medicalHistory);
            txtPatientEmail.setText("Email: " + patientEmail); // Set email text
        }

        // Handle button clicks
        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());
        btnBookAppointment.setOnClickListener(v -> openBookAppointmentActivity());
        btnViewPastAppointments.setOnClickListener(v -> openViewPastAppointmentsActivity());
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(PatientProfile.this, EditPatientProfile.class);
        intent.putExtra("name", txtPatientName.getText().toString());
        intent.putExtra("dateOfBirth", txtPatientAge.getText().toString());
        intent.putExtra("gender", txtPatientGender.getText().toString());
        intent.putExtra("medicalHistory", txtPatientMedicalHistory.getText().toString());
        intent.putExtra("email", patientEmail); // Pass the email to the next activity
        startActivityForResult(intent, EDIT_PROFILE_REQUEST); // Start activity for result
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            // Refresh profile data here
            // For example, you can re-fetch the data from intent extras and update the views
        }
    }

    private void openBookAppointmentActivity() {
        Intent intent = new Intent(this, SearchDoctor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openViewPastAppointmentsActivity() {
        Intent intent = new Intent(this, CancelAppointmentsActivity.class);
        intent.putExtra("USER_EMAIL", patientEmail); // Correctly passing the patient's email
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
