package com.example.myapplication;

import android.content.Intent;
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
            etDoctorExperience, etDoctorCategory, etDoctorPassword, etDoctorConfirmPassword,doctorFeeEditText;
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
        doctorFeeEditText = findViewById(R.id.doctorFeeEditText);

        Button btnAddDoctor = findViewById(R.id.btn_add_doctor);



        btnAddDoctor.setOnClickListener(v -> {
            String Name = etDoctorName.getText().toString().trim();
            String Email = etDoctorEmail.getText().toString().trim();
            String Phone = etDoctorPhone.getText().toString().trim();
            String Qualification = etDoctorQualification.getText().toString().trim();
            String Experience = etDoctorExperience.getText().toString().trim();
            String Category = etDoctorCategory.getText().toString().trim();
            String Password = etDoctorPassword.getText().toString().trim();
            String ConfirmPassword = etDoctorConfirmPassword.getText().toString().trim();
            String fee = doctorFeeEditText.getText().toString().trim();
            if (Name.isEmpty() || Email.isEmpty() || Phone.isEmpty() || Qualification.isEmpty() ||
                    Experience.isEmpty() || Category.isEmpty() || Password.isEmpty() || !Password.equals(ConfirmPassword) || Password.length() < 6) {
                Toast.makeText(AddDoctor.this, "Please ensure all fields are filled correctly and passwords match.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Doctor doctor = new Doctor(Name, Email, Phone, Qualification, Experience, Category,Password,ConfirmPassword,fee);
                            addDoctorToFirestore(doctor);
                        } else {
                            Toast.makeText(AddDoctor.this, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void addDoctorToFirestore(Doctor doctor) {
        db.collection("doctors").add(doctor)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddDoctor.this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddDoctor.this, AdminProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("AddDoctor", "Error adding doctor to Firestore", e);
                    Toast.makeText(AddDoctor.this, "Error adding doctor to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AdminProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Optional: Close this activity
    }


}