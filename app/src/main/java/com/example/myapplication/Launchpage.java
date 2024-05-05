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
                intent = new Intent(Launchpage.this, Login_Admin.class); // Intent for Admin login
                break;
            case "Patient":
                intent = new Intent(Launchpage.this, Login_Patient.class); // Intent for Patient login
                break;
            case "Doctor":
                intent = new Intent(Launchpage.this, Login_Doctor.class); // Intent for Doctor login
                break;
            default:
                throw new IllegalStateException("Unexpected role: " + role);
        }
        intent.putExtra("role", role);
        startActivity(intent);

    }

}
