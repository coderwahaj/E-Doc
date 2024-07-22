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

        // Initialize buttons
        btnAdmin = findViewById(R.id.btn_admin);
        btnPatient = findViewById(R.id.btn_patient);
        btnDoctor = findViewById(R.id.btn_doctor);

        // Set onClickListener for Admin button
        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(Launchpage.this, Login_Admin.class);
            intent.putExtra("role", "Admin");
            startActivity(intent);
        });

        // Set onClickListener for Patient button
        btnPatient.setOnClickListener(v -> {
            Intent intent = new Intent(Launchpage.this, Login_Patient.class);
            intent.putExtra("role", "Patient");
            startActivity(intent);
        });

        // Set onClickListener for Doctor button
        btnDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(Launchpage.this, Login_Doctor.class);
            intent.putExtra("role", "Doctor");
            startActivity(intent);
        });
    }
}
