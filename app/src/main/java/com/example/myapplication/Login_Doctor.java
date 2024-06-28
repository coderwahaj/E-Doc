package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Doctor extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_doctor);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressbar = findViewById(R.id.progressbar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        progressbar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login_Doctor.this, "Enter email", Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Login_Doctor.this, "Enter password", Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                fetchUserDetailsAndNavigate1(user.getEmail());
                            }
                        } else {
                            Toast.makeText(Login_Doctor.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void fetchUserDetailsAndNavigate1(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctors")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String name = documentSnapshot.getString("name");
                        String category = documentSnapshot.getString("category");
                        String qualification = documentSnapshot.getString("qualification");
                        String email = documentSnapshot.getString("email");
                        String experience=documentSnapshot.getString("experience");
                        String phone= documentSnapshot.getString("phone");
                        Intent intent = new Intent(Login_Doctor.this, DoctorProfile.class);
                        intent.putExtra("name", name);
                        intent.putExtra("category", category);
                        intent.putExtra("qualification", qualification);
                        intent.putExtra("experience",experience);
                        intent.putExtra("phone",phone);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login_Doctor.this, "Doctor's details not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(Login_Doctor.this, "Failed to fetch doctor details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}