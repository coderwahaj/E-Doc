package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupPatient extends AppCompatActivity {

    TextInputEditText editTextName, editTextEmail, editTextPassword, editTextMedicalHistory;
    DatePicker datePickerAge;
    RadioGroup radioGroupGender;
    Button buttonSignup;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_patient);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        datePickerAge = findViewById(R.id.datePickerAge);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        editTextMedicalHistory = findViewById(R.id.editTextMedicalHistory);
        buttonSignup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // Show progress bar
                buttonSignup.setVisibility(View.GONE); // Hide button
                signUpPatient();
            }
        });
    }

    private void signUpPatient() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String medicalHistory = editTextMedicalHistory.getText().toString().trim();
        final String dob = datePickerAge.getDayOfMonth() + "/" + (datePickerAge.getMonth() + 1) + "/" + datePickerAge.getYear();
        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty() || gender.isEmpty() || medicalHistory.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            buttonSignup.setVisibility(View.VISIBLE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", email);
                        user.put("dateOfBirth", dob);
                        user.put("gender", gender);
                        user.put("medicalHistory", medicalHistory);

                        db.collection("patientsList")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignupPatient.this, "Account created and data stored successfully.", Toast.LENGTH_SHORT).show();
                                    redirectToLogin();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SignupPatient.this, "Failed to store data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    buttonSignup.setVisibility(View.VISIBLE);
                                });
                    } else {
                        Toast.makeText(SignupPatient.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        buttonSignup.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

