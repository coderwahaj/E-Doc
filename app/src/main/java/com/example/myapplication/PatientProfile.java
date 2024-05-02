package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PatientProfile extends AppCompatActivity {

    private TextView txtPatientName, txtPatientAge, txtPatientGender, txtPatientMedicalHistory;
    private Button btnEditProfile;

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

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name", "N/A");
            String age = extras.getString("dateOfBirth", "N/A");
            String gender = extras.getString("gender", "N/A");
            String medicalHistory = extras.getString("medicalHistory", "No history provided.");

            // Set data to views
            txtPatientName.setText("Name: " + name);
            txtPatientAge.setText("Date of Birth: " + age);
            txtPatientGender.setText("Gender: " + gender);
            txtPatientMedicalHistory.setText("Medical History: " + medicalHistory);
        }

        // Handle edit profile button click
        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());

        // Populate past appointments table with dummy data
        populatePastAppointmentsTable();
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(PatientProfile.this, EditPatientProfile.class);
        intent.putExtra("name", txtPatientName.getText().toString());
        intent.putExtra("dateOfBirth", txtPatientAge.getText().toString());
        intent.putExtra("gender", txtPatientGender.getText().toString());
        intent.putExtra("medicalHistory", txtPatientMedicalHistory.getText().toString());
        startActivity(intent);
    }

    private void populatePastAppointmentsTable() {
        TableLayout tablePastAppointments = findViewById(R.id.tablePastAppointments);

        // Dummy data for past appointments
        String[] appointmentDates = {"2024-04-01", "2024-03-15", "2024-02-28"};
        String[] doctors = {"Dr. Smith", "Dr. Johnson", "Dr. Brown"};
        String[] descriptions = {"Checkup", "X-ray", "Blood test"};

        // Loop to add rows for each past appointment
        for (int i = 0; i < appointmentDates.length; i++) {
            TableRow row = new TableRow(this);
            row.setClickable(true);
            int finalI = i;
            row.setOnClickListener(v -> {
                // Handle row click, e.g., open a new activity with details
                final int j= finalI;
                openAppointmentDetailActivity(appointmentDates[j], doctors[j], descriptions[j]);
            });

            TextView dateTextView = new TextView(this);
            dateTextView.setText(appointmentDates[i]);
            dateTextView.setPadding(16, 16, 16, 16); // Increased padding
            dateTextView.setTextSize(18); // Increased text size
            row.addView(dateTextView);

            TextView doctorTextView = new TextView(this);
            doctorTextView.setText(doctors[i]);
            doctorTextView.setPadding(16, 16, 16, 16);
            doctorTextView.setTextSize(18);
            row.addView(doctorTextView);

            TextView descriptionTextView = new TextView(this);
            descriptionTextView.setText(descriptions[i]);
            descriptionTextView.setPadding(16, 16, 16, 16);
            descriptionTextView.setTextSize(18);
            row.addView(descriptionTextView);

            tablePastAppointments.addView(row);
        }
    }

    private void openAppointmentDetailActivity(String date, String doctor, String description) {
        Intent intent = new Intent(PatientProfile.this, AppointmentDetailActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("doctor", doctor);
        intent.putExtra("description", description);
        startActivity(intent);
    }
}

