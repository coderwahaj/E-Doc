package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorProfile extends AppCompatActivity {

    private TextView txtDoctorName;
    private TextView txtDoctorCategory;
    private TextView txtDoctorPhone;
    private TextView txtDoctorQualification;
    private TextView txtDoctorExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // Initialize views
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtDoctorCategory = findViewById(R.id.txtDoctorCategory);
        txtDoctorPhone = findViewById(R.id.txtDoctorPhone);
        txtDoctorQualification = findViewById(R.id.txtDoctorQualification);
        txtDoctorExperience = findViewById(R.id.txtDoctorExperience);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email"); // Not used in UI but could be used for further features
            String name = extras.getString("name", "N/A");
            String category = extras.getString("category", "N/A");
            String phone = extras.getString("phone", "N/A");
            String qualification = extras.getString("qualification", "N/A");
            String experience = extras.getString("experience", "N/A");

            // Set data to views
            txtDoctorName.setText("Name: " + name);
            txtDoctorCategory.setText("Category: " + category);
            txtDoctorPhone.setText("Phone: " + phone);
            txtDoctorQualification.setText("Qualification: " + qualification);
            txtDoctorExperience.setText("Experience: " + experience);
        }
    }
}
