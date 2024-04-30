package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    TextView txtView;
    RadioGroup radioGroupRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressbar = findViewById(R.id.progressbar);
        txtView = findViewById(R.id.signupNow);
        radioGroupRole = findViewById(R.id.radioGroupRole); // Initialize the RadioGroup

        txtView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE); // Show progress bar
            buttonLogin.setVisibility(View.GONE); // Hide login button

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String role = ((RadioButton)findViewById(radioGroupRole.getCheckedRadioButtonId())).getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Enter both email and password.", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
                buttonLogin.setVisibility(View.VISIBLE);
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressbar.setVisibility(View.GONE);
                        buttonLogin.setVisibility(View.VISIBLE);

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            switch (role) {
                                case "Patient":
                                    checkEmailAndProceed("patientsList", email);
                                    break;
                                case "Doctor":
                                    checkEmailAndProceed("doctorsList", email);
                                    break;
                                case "Admin":
                                    checkEmailAndProceed("adminsList", email);
                                    break;
                                default:
                                    Toast.makeText(Login.this, "Invalid role selected.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkEmailAndProceed(String path, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(path)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Intent intent = createIntentBasedOnRole(path, document, email);
                            if (intent != null) {
                                startActivity(intent);
                                finish();
                                return; // Stop after finding the first match
                            }
                        }
                    } else {
                        Toast.makeText(Login.this, "No record found with this email.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private Intent createIntentBasedOnRole(String path, QueryDocumentSnapshot document, String email) {
        Intent intent = null;
        switch (path) {
            case "patientsList":
                intent = new Intent(Login.this, PatientProfile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", document.getString("name"));
                intent.putExtra("dateOfBirth", document.getString("dateOfBirth"));
                intent.putExtra("gender", document.getString("gender"));
                intent.putExtra("medicalHistory", document.getString("medicalHistory"));
                break;
            case "doctorsList":
                intent = new Intent(Login.this, DoctorProfile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", document.getString("name"));
                intent.putExtra("category", document.getString("category"));
                intent.putExtra("phone", document.getString("phone"));
                intent.putExtra("qualification", document.getString("qualification"));
                intent.putExtra("experience", document.getString("experience"));
                break;
            case "adminsList":
                intent = new Intent(Login.this, AdminProfile.class);
                intent.putExtra("email", email);
                intent.putExtra("name", document.getString("name"));
                break;
        }
        return intent;
    }
}

