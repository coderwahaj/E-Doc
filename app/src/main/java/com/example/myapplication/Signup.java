
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // make sure this matches your XML file name

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        buttonSignUp = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressbar);

        buttonSignUp.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        updateUserInfo(name, firebaseUser);
                    } else {
                        Toast.makeText(Signup.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateUserInfo(String name, FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signup.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this, PatientProfile.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Signup.this, "Failed to update user profile.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
