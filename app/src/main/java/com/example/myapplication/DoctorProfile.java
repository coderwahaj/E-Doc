package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorProfile extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST = 2;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Update each field only if it's included in the intent
            if (data.hasExtra("name")) {
                txtDoctorName.setText("Name: " + data.getStringExtra("name"));
            }
            if (data.hasExtra("phone")) {
                txtDoctorPhone.setText("Phone #: " + data.getStringExtra("phone"));
            }
            if (data.hasExtra("qualification")) {
                txtDoctorQualification.setText("Qualification: " + data.getStringExtra("qualification"));
            }
            if (data.hasExtra("experience")) {
                txtDoctorExperience.setText("Experience: " + data.getStringExtra("experience"));
            }
        }
    }

    private void openEditProfileActivity() {
        Log.d("DoctorProfile", "Sending email: " + doctorEmail);
        Intent intent = new Intent(DoctorProfile.this, EditDoctorProfile.class);
        intent.putExtra("name", txtDoctorName.getText().toString());
        intent.putExtra("category", txtDoctorCategory.getText().toString());
        intent.putExtra("phone", txtDoctorPhone.getText().toString());
        intent.putExtra("qualification", txtDoctorQualification.getText().toString());
        intent.putExtra("experience", txtDoctorExperience.getText().toString());
        intent.putExtra("email", doctorEmail);
        startActivityForResult(intent, EDIT_PROFILE_REQUEST); // Start the activity for result
    }

    private void openViewPendingAppointmentActivity() {
        Intent intent = new Intent(DoctorProfile.this, ApproveAppointmentActivity.class);
        intent.putExtra("name", txtDoctorName.getText().toString());
        intent.putExtra("category", txtDoctorCategory.getText().toString());
        intent.putExtra("phone", txtDoctorPhone.getText().toString());
        intent.putExtra("qualification", txtDoctorQualification.getText().toString());
        intent.putExtra("experience", txtDoctorExperience.getText().toString());
        intent.putExtra("email", doctorEmail);
        startActivity(intent);
    }

    private void openViewPastAppointmentsActivity() {
        Intent intent = new Intent(DoctorProfile.this, UpcomingAppointmentsActivity.class);
        intent.putExtra("name", txtDoctorName.getText().toString());
        intent.putExtra("category", txtDoctorCategory.getText().toString());
        intent.putExtra("phone", txtDoctorPhone.getText().toString());
        intent.putExtra("qualification", txtDoctorQualification.getText().toString());
        intent.putExtra("experience", txtDoctorExperience.getText().toString());
        intent.putExtra("email", doctorEmail);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Login_Doctor.class);
        startActivity(intent);
        finish();
    }

}
