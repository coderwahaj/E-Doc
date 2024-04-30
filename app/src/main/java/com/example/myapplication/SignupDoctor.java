package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupDoctor extends AppCompatActivity {

    TextInputEditText editTextName, editTextEmail, editTextPassword, editTextPhone, editTextQualification, editTextExperience;
    Spinner spinnerCategory;
    Button buttonSignup;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_doctor);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextQualification = findViewById(R.id.editTextQualification);
        editTextExperience = findViewById(R.id.editTextExperience);
        buttonSignup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.doctor_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonSignup.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE); // Show progress bar
            buttonSignup.setVisibility(View.GONE); // Hide button
            signUpDoctor();
        });
    }

    private void signUpDoctor() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String qualification = editTextQualification.getText().toString().trim();
        String experience = editTextExperience.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || qualification.isEmpty() || experience.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            buttonSignup.setVisibility(View.VISIBLE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> doctorData = new HashMap<>();
                        doctorData.put("name", name);
                        doctorData.put("email", email);
                        doctorData.put("phone", phone);
                        doctorData.put("qualification", qualification);
                        doctorData.put("experience", experience);
                        doctorData.put("category", category);

                        db.collection("doctorsList")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(doctorData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignupDoctor.this, "Doctor profile created successfully.", Toast.LENGTH_SHORT).show();
                                    redirectToLogin();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SignupDoctor.this, "Failed to create doctor profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    buttonSignup.setVisibility(View.VISIBLE);
                                });
                    } else {
                        Toast.makeText(SignupDoctor.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        buttonSignup.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();  // Ensure this activity is cleared from the stack
    }
}
