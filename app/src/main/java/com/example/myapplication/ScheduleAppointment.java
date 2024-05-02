package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private boolean dateSelected = false;
    private boolean timeSelected = false;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        TextView doctorNameTextView = findViewById(R.id.doctorNameTextView);
        TextView doctorCategoryTextView = findViewById(R.id.doctorCategoryTextView);
        TextView feeTextView = findViewById(R.id.feeTextView);

        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorCategory = getIntent().getStringExtra("doctorCategory");
        String fee = getIntent().getStringExtra("fee");

        doctorNameTextView.setText(doctorName);
        doctorCategoryTextView.setText(doctorCategory);
        feeTextView.setText(String.format("Rs. %s", fee));
        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
      //  checkCurrentUser();
    }

   /* private void checkCurrentUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Log.d("AuthCheck", "User not logged in");
            Intent intent = new Intent(this, Login_Patient.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("AuthCheck", "User is logged in: " + user.getEmail());
        }
    }*/

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    datePickerEditText.setText(selectedDate);
                    dateSelected = true;
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showMessageAndNavigate(String message) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            // Retrieve the user's name from the patientsList collection
            db.collection("patientsList").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot ->
                    {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");

                            // Continue with appointment scheduling
                            String selectedDate = datePickerEditText.getText().toString();
                            String selectedTime = timePicker.getHour() + ":" + timePicker.getMinute();

                            Map<String, Object> appointment = new HashMap<>();
                            appointment.put("name", userName);
                            appointment.put("email", userEmail);
                            appointment.put("date", selectedDate);
                            appointment.put("time", selectedTime);

                            db.collection("pendingAppointments").add(appointment)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(ScheduleAppointment.this, "Appointment saved!", Toast.LENGTH_SHORT).show();
                                        navigateToMain();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ScheduleAppointment.this, "Error saving appointment", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(ScheduleAppointment.this, "User not found in patient list", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(ScheduleAppointment.this, "Error fetching user data", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void checkAndDisplayMessage() {
        if (dateSelected && timeSelected) {
            confirmButton.setEnabled(true);
        }
    }
}