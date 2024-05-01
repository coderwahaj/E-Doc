package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button logoutButton, scheduleAppointmentButton, viewAppointmentsButton, viewUpcomingAppointmentsButton;
    private TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        scheduleAppointmentButton = findViewById(R.id.scheduleAppointmentButton);
        viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        viewUpcomingAppointmentsButton = findViewById(R.id.viewUpcomingAppointments);
        textView = findViewById(R.id.user_details);

        checkUserAuthentication();

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            navigateToScheduleAppointment(); // Navigate to ScheduleAppointment after logging out
        });

        scheduleAppointmentButton.setOnClickListener(v -> {
            navigateToScheduleAppointment(); // Navigate to ScheduleAppointment
        });

        viewAppointmentsButton.setOnClickListener(v -> {
            navigateToApproveAppointmentActivity(); // Navigate to ApproveAppointmentActivity
        });

        viewUpcomingAppointmentsButton.setOnClickListener(v -> {
            navigateToUpcomingAppointmentsActivity(); // Navigate to UpcomingAppointmentsActivity
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(v -> addDataToFirestore("doctors", "John Doe"));
    }

    private void navigateToApproveAppointmentActivity() {
        Intent intent = new Intent(this, ApproveAppointmentActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void navigateToScheduleAppointment() {
        Intent intent = new Intent(getApplicationContext(), ScheduleAppointment.class);
        startActivity(intent);
        finish();
    }

    private void navigateToUpcomingAppointmentsActivity() {
        Intent intent = new Intent(this, UpcomingAppointmentsActivity.class);
        startActivity(intent);
        finish(); // Consider whether you really want to call finish() here
    }

    private void checkUserAuthentication() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            navigateToLogin();
        } else {
            // Displaying the user's email and name (if available)
            String userDetails = "Email: " + user.getEmail();
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                userDetails += "\nName: " + user.getDisplayName();
            }
            textView.setText(userDetails);
        }
    }

    private void addDataToFirestore(String collectionName, String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        db.collection(collectionName).document("LA")
                .set(data)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Data added to Firestore", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to add data to Firestore", Toast.LENGTH_SHORT).show());
    }
}
