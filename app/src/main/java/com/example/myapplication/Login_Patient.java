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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class Login_Patient extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    TextView txtView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is logged in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_patient);
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        buttonLogin=findViewById(R.id.btn_login);
        progressbar=findViewById(R.id.progressbar);
        txtView=findViewById(R.id.signupNow);
        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SignupPatient.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String email, password;
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login_Patient.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login_Patient.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            progressbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Fetch additional user details from Firestore
                                    fetchUserDetailsAndNavigate(user.getUid());
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login_Patient.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Fetch user details from Firestore and navigate to PatientProfile activity
    private void fetchUserDetailsAndNavigate(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("patientsList").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // DocumentSnapshot contains the user's details
                String name = documentSnapshot.getString("name");
                String dateOfBirth = documentSnapshot.getString("dateOfBirth");
                String gender=documentSnapshot.getString("gender");
                String medicalHistory = documentSnapshot.getString("medicalHistory");

                // Create an intent to navigate to PatientProfile activity and pass the details
                Intent intent = new Intent(Login_Patient.this, PatientProfile.class);
                intent.putExtra("name", name);
                intent.putExtra("dateOfBirth", dateOfBirth);
                intent.putExtra("gender",gender);
                intent.putExtra("medicalHistory", medicalHistory);
                startActivity(intent);
                finish();
            } else {
                // Document does not exist, handle the case accordingly
                Toast.makeText(Login_Patient.this, "User details not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // Error fetching user details, handle the error
            Toast.makeText(Login_Patient.this, "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
