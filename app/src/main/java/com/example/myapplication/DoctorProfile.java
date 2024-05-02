package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorProfile extends AppCompatActivity {

    private TextView txtDoctorName, txtDoctorCategory, txtDoctorPhone, txtDoctorQualification, txtDoctorExperience, txtDoctorEmail;
    private String doctorEmail;
    private Button btnEditProfile, btnBookAppointment, btnViewPastAppointments;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // Initialize views
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtDoctorCategory = findViewById(R.id.txtDoctorCategory);
        txtDoctorPhone = findViewById(R.id.txtDoctorPhone);
        txtDoctorQualification = findViewById(R.id.txtDoctorQualification);
        txtDoctorExperience = findViewById(R.id.txtDoctorExperience);
        txtDoctorEmail = findViewById(R.id.txtdoctorEmail); // Make sure the ID is correct
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        btnViewPastAppointments = findViewById(R.id.btnViewPastAppointments);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name", "N/A");
            String category = extras.getString("category", "N/A");
            String phone = extras.getString("phone", "N/A");
            String qualification = extras.getString("qualification", "N/A");
            String experience = extras.getString("experience", "N/A");
            doctorEmail = extras.getString("email", "N/A"); // Save the email to the doctorEmail variable

            // Set data to views
            txtDoctorName.setText("Name: " + name);
            txtDoctorCategory.setText("Category: " + category);
            txtDoctorPhone.setText("Phone #: " + phone);
            txtDoctorQualification.setText("Qualification: " + qualification);
            txtDoctorExperience.setText("Experience: " + experience);
            txtDoctorEmail.setText("Email: " + doctorEmail); // Display the email
        }

        btnEditProfile.setOnClickListener(v -> openEditProfileActivity());
        btnBookAppointment.setOnClickListener(v -> openViewPendingAppointmentActivity());
        btnViewPastAppointments.setOnClickListener(v -> openViewPastAppointmentsActivity());
    }

    private void openEditProfileActivity()
    {
        Log.d("DoctorProfile", "Sending email: " + doctorEmail);
        Intent intent = new Intent(DoctorProfile.this, EditDoctorProfile.class);
        intent.putExtra("email", doctorEmail);
        startActivity(intent);
    }

    private void openViewPendingAppointmentActivity() {
        Intent intent = new Intent(DoctorProfile.this, ApproveAppointmentActivity.class);
        intent.putExtra("email", doctorEmail);
        startActivity(intent);
    }

    private void openViewPastAppointmentsActivity() {
        Intent intent = new Intent(DoctorProfile.this, UpcomingAppointmentsActivity.class);
        intent.putExtra("email", doctorEmail);
        startActivity(intent);
    }
}
