package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import android.app.DatePickerDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAppointment extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView datePickerEditText;
    private TimePicker timePicker;
    private Button confirmButton;
    private boolean dateSelected = false;
    private boolean timeSelected = false;
    private String doctorName, doctorEmail;
    private String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        doctorName = getIntent().getStringExtra("doctorName");
        doctorEmail = getIntent().getStringExtra("doctorEmail");
        patientName = getIntent().getStringExtra("patientName");
        datePickerEditText = findViewById(R.id.datePickerEditText);
        timePicker = findViewById(R.id.timePicker);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setEnabled(false);

        datePickerEditText.setOnClickListener(v -> showDatePicker());
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            timeSelected = true;
            checkAndDisplayMessage();
        });

        confirmButton.setOnClickListener(v -> {
            if (dateSelected && timeSelected) {
                showMessageAndNavigate("Request forwarded");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Immediately check the user status
        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login_Patient.class);
            startActivity(intent);
            finish();
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    datePickerEditText.setText(selectedDate);
                    dateSelected = true;
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showMessageAndNavigate(String message) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> appointment = new HashMap<>();
            appointment.put("userId", user.getUid());
            appointment.put("userEmail", user.getEmail());
            appointment.put("date", datePickerEditText.getText().toString());
            appointment.put("time", timePicker.getHour() + ":" + timePicker.getMinute());
            appointment.put("doctorName", doctorName);  // Using the passed doctor name
            appointment.put("doctorEmail", doctorEmail);  // Using the passed doctor email
            appointment.put("patientName", patientName);  // Storing the patient's name without email

            db.collection("pendingAppointments").add(appointment)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ScheduleAppointment.this, "Appointment saved!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    })
                    .addOnFailureListener(e -> Toast.makeText(ScheduleAppointment.this, "Error saving appointment", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, PatientProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void checkAndDisplayMessage() {
        if (dateSelected && timeSelected) {
            confirmButton.setEnabled(true);
        }
    }
}
