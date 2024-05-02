package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth auth;
    private Button btn1, btn2, btn3, btn4, btn5, btnx;
    private TextView textView;
    private FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        btn1 = findViewById(R.id.logout);
        btn2 = findViewById(R.id.Doctor);
        btn3 = findViewById(R.id.Appointment);
        btn4 = findViewById(R.id.viewAppointmentsButton);
        btn5 = findViewById(R.id.viewUpcomingAppointments);
        btnx = findViewById(R.id.btnYourAppointments);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Launchpage.class);
            startActivity(intent);
            finish();
        } else {
            // Fetch user's name and email from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("patientsList").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    // Display user's name and email
                    textView.setText("Name: " + name + "\nEmail: " + email);
                } else {
                    Log.d(TAG, "No such document");
                }
            }).addOnFailureListener(e -> {
                Log.d(TAG, "Error fetching document: " + e);
            });
        }

        btn1.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), AddDoctor.class);
            startActivity(intent);
            finish();
        });
        btn2.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SearchDoctor.class);
            startActivity(intent);
            finish();
        });
        btn3.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), ScheduleAppointment.class);
            startActivity(intent);
            finish();
        });
        btn4.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), ApproveAppointmentActivity.class);
            startActivity(intent);
            finish();
        });
        btn5.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), UpcomingAppointmentsActivity.class);
            startActivity(intent);
            finish();
        });
        btnx.setOnClickListener(v -> {
            navigateToCancel();
        });

    }

    private void navigateToCancel() {
        Intent intent = new Intent(getApplicationContext(), CancelAppointmentsActivity.class);
        startActivity(intent);
        finish();
    }
}