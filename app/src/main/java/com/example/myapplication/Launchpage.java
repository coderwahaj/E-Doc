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
        Intent intent = new Intent(Launchpage.this, Login_Admin.class);
        intent.putExtra("role", role); // Pass the role to the login activity
        startActivity(intent);
    }
}
