package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Launchpage extends AppCompatActivity {

    Button btnAdmin, btnPatient, btnDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);

        btnAdmin = findViewById(R.id.btn_admin);
        btnPatient = findViewById(R.id.btn_patient);
        btnDoctor = findViewById(R.id.btn_doctor);

        btnAdmin.setOnClickListener(v -> navigateToLogin("Admin"));
        btnPatient.setOnClickListener(v -> navigateToLogin("Patient"));
        btnDoctor.setOnClickListener(v -> navigateToLogin("Doctor"));
    }

    private void navigateToLogin(String role) {
        Intent intent;
        switch (role) {
            case "Admin":
                intent = new Intent(Launchpage.this, Login_Admin.class);
                break;
            case "Patient":
                intent = new Intent(Launchpage.this, Login_Patient.class);
                break;
            case "Doctor":
                intent = new Intent(Launchpage.this, Login_Doctor.class);
                break;
            default:
                // If role doesn't match any case, can redirect to a general login page or handle the error
                throw new IllegalArgumentException("Invalid role: " + role);
        }
        intent.putExtra("role", role); // Pass the role to the respective login activity
        startActivity(intent);

    }
}