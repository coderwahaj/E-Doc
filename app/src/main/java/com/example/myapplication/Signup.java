package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    private Button buttonSignUpPatient;
    private Button buttonSignUpDoctor;
    private TextView textViewBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSignUpPatient = findViewById(R.id.buttonSignUpPatient);
        buttonSignUpDoctor = findViewById(R.id.buttonSignUpDoctor);
        textViewBackToLogin = findViewById(R.id.textViewBackToLogin);

        buttonSignUpPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open the patient sign-up form
                Intent intent = new Intent(Signup.this, SignupPatient.class);
                startActivity(intent);
            }
        });

        buttonSignUpDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open the doctor sign-up form
                Intent intent = new Intent(Signup.this, SignupDoctor.class);
                startActivity(intent);
            }
        });

        textViewBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the login page
                finish();  // Assuming this activity was started from LoginActivity
            }
        });
    }
}
