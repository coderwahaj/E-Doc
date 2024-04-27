package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDoctor extends AppCompatActivity
{
    private EditText etDoctorName, etDoctorEmail, etDoctorPhone, etDoctorQualification, etDoctorExperience;
    private Button btnAddDoctor;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etDoctorName = findViewById(R.id.et_doctor_name);
        etDoctorEmail = findViewById(R.id.et_doctor_email);
        etDoctorPhone = findViewById(R.id.et_doctor_phone);
        etDoctorQualification = findViewById(R.id.et_doctor_qualification);
        etDoctorExperience = findViewById(R.id.et_doctor_experience);
        btnAddDoctor = findViewById(R.id.btn_add_doctor);

        // Add Doctor Button Click Listener
        btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get doctor details from EditText fields
                String name = etDoctorName.getText().toString().trim();
                String email = etDoctorEmail.getText().toString().trim();
                String phone = etDoctorPhone.getText().toString().trim();
                String qualification = etDoctorQualification.getText().toString().trim();
                String experience = etDoctorExperience.getText().toString().trim();

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || qualification.isEmpty() || experience.isEmpty()) {
                    Toast.makeText(AddDoctor.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Doctor object
                Doctor doctor = new Doctor(name, email, phone, qualification, experience);

                // Add doctor to Firestore
                addDoctorToFirestore(doctor);
            }
        });
    }

    private void addDoctorToFirestore(Doctor doctor) {
        // Add a new document with a generated ID to the "doctors" collection
        db.collection("doctors")
                .add(doctor)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddDoctor.this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish activity after adding doctor
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddDoctor.this, "Error adding doctor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
