package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PatientProfile extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 1;

    private TextView txtPatientName, txtPatientAge, txtPatientGender, txtPatientMedicalHistory, txtPatientEmail;
    private Button btnEditProfile, btnBookAppointment, btnCancel, btnChat, btnPastAppointments;
    private String patientEmail;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        initializeViews();
        retrieveAndDisplayData();
        setupButtonListeners();
    }

    private void initializeViews() {
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAge = findViewById(R.id.txtPatientAge);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientMedicalHistory = findViewById(R.id.txtPatientMedicalHistory);
        txtPatientEmail = findViewById(R.id.txtPatientEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        btnCancel = findViewById(R.id.btnCancelAppointment);
        btnChat = findViewById(R.id.doctchat);
        btnPastAppointments = findViewById(R.id.btnPastAppointments);
    }

    private void retrieveAndDisplayData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            refreshData(extras);
        }
    }

    private void refreshData(Bundle extras) {
        txtPatientName.setText("Name: " + extras.getString("name", "N/A"));
        txtPatientAge.setText("Date of Birth: " + extras.getString("dateOfBirth", "N/A"));
        txtPatientGender.setText("Gender: " + extras.getString("gender", "N/A"));
        txtPatientMedicalHistory.setText("Medical History: " + extras.getString("medicalHistory", "No history provided."));
        patientEmail = extras.getString("email", "Email not provided");
        txtPatientEmail.setText("Email: " + patientEmail);
    }

    private void setupButtonListeners() {
        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());
        btnBookAppointment.setOnClickListener(v -> openBookAppointmentActivity());
        btnCancel.setOnClickListener(v -> openCancelAppointmentsActivity());
        btnChat.setOnClickListener(v -> openChatActivity());
        btnPastAppointments.setOnClickListener(v -> openPastAppointmentsActivity());
    }

    private void openPastAppointmentsActivity() {
        Intent intent = new Intent(this, GiveFeedback.class);
        intent.putExtra("patientName", txtPatientName.getText().toString().replace("Name: ", ""));
        intent.putExtra("patientEmail", patientEmail);
        startActivity(intent);
    }

    private void openChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("patientName", txtPatientName.getText().toString().replace("Name: ", ""));
        intent.putExtra("patientEmail", patientEmail);
        startActivity(intent);
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(this, EditPatientProfile.class);
        intent.putExtra("email", patientEmail);
        startActivityForResult(intent, EDIT_PROFILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("name")) {
                txtPatientName.setText("Name: " + data.getStringExtra("name"));
            }
            if (data.hasExtra("medicalHistory")) {
                txtPatientMedicalHistory.setText("Medical History: " + data.getStringExtra("medicalHistory"));
            }
            if (data.hasExtra("dateOfBirth")) {
                txtPatientAge.setText("Date of Birth: " + data.getStringExtra("dateOfBirth"));
            }
            if (data.hasExtra("gender")) {
                txtPatientGender.setText("Gender: " + data.getStringExtra("gender"));
            }
        }
    }

    private void openBookAppointmentActivity() {
        Intent intent = new Intent(this, SearchDoctor.class);
        intent.putExtra("patientName", txtPatientName.getText().toString().replace("Name: ", ""));
        intent.putExtra("patientEmail", patientEmail);
        startActivity(intent);
    }

    private void openCancelAppointmentsActivity() {
        Intent intent = new Intent(this, CancelAppointmentsActivity.class);
        intent.putExtra("patientEmail", patientEmail);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Login_Patient.class);
        startActivity(intent);
        finish();
    }
}
