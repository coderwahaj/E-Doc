package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ResourceBundle;

public class Login_Admin extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    TextView txtView;
    // Hardcoded credentials for the admin
    private static final String ADMIN_EMAIL = "wahajasif488@gmail.com";
    private static final String ADMIN_PASSWORD = "Wahaj@1";


    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is logged in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Login_Admin.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_admin);
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        buttonLogin=findViewById(R.id.btn_login);
        progressbar=findViewById(R.id.progressbar);


        buttonLogin.setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            String email, password;
            email=String.valueOf(editTextEmail.getText());
            password=String.valueOf(editTextPassword.getText());

            if (!email.equals(ADMIN_EMAIL) || !password.equals(ADMIN_PASSWORD)) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login_Admin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AdminProfile.class);
                intent = new Intent(Login_Admin.this, AdminProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}