package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddDoctor extends AppCompatActivity {
    private EditText etDoctorName, etDoctorEmail, etDoctorPhone, etDoctorQualification,
            etDoctorExperience, etDoctorCategory, etDoctorPassword, etDoctorConfirmPassword;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        etDoctorName = findViewById(R.id.et_doctor_name);
        etDoctorEmail = findViewById(R.id.et_doctor_email);
        etDoctorPhone = findViewById(R.id.et_doctor_phone);
        etDoctorQualification = findViewById(R.id.et_doctor_qualification);
        etDoctorExperience = findViewById(R.id.et_doctor_experience);
        etDoctorCategory = findViewById(R.id.et_doctor_category);
        etDoctorPassword = findViewById(R.id.et_doctor_password);
        etDoctorConfirmPassword = findViewById(R.id.et_doctor_confirm_password);
        Button btnAddDoctor = findViewById(R.id.btn_add_doctor);

        btnAddDoctor.setOnClickListener(v -> {
            String name = etDoctorName.getText().toString().trim();
            String email = etDoctorEmail.getText().toString().trim();
            String phone = etDoctorPhone.getText().toString().trim();
            String qualification = etDoctorQualification.getText().toString().trim();
            String experience = etDoctorExperience.getText().toString().trim();
            String category = etDoctorCategory.getText().toString().trim();
            String password = etDoctorPassword.getText().toString().trim();
            String confirmPassword = etDoctorConfirmPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || qualification.isEmpty() ||
                    experience.isEmpty() || category.isEmpty() || password.isEmpty() || !password.equals(confirmPassword) || password.length() < 6) {
                Toast.makeText(AddDoctor.this, "Please ensure all fields are filled correctly and passwords match.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Doctor doctor = new Doctor(name, email, phone, qualification, experience, category);
                            addDoctorToFirestore(doctor);
                        } else {
                            Toast.makeText(AddDoctor.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void addDoctorToFirestore(Doctor doctor) {
        db.collection("doctors").add(doctor)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddDoctor.this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Optional: Close the activity on success
                })
                .addOnFailureListener(e -> {
                    Log.e("AddDoctor", "Error adding doctor to Firestore", e);
                    Toast.makeText(AddDoctor.this, "Error adding doctor to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
