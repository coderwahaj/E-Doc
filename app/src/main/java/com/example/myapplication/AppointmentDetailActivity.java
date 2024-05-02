package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDetailActivity extends AppCompatActivity {

    private TextView txtDate, txtDoctor, txtDescription;
    private EditText feedbackInput;
    private Button btnSubmitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Initialize views
        txtDate = findViewById(R.id.txtDate);
        txtDoctor = findViewById(R.id.txtDoctor);
        txtDescription = findViewById(R.id.txtDescription);
        feedbackInput = findViewById(R.id.feedbackInput);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String date = extras.getString("date", "No date provided");
            String doctor = extras.getString("doctor", "No doctor specified");
            String description = extras.getString("description", "No description provided");

            // Set data to views
            txtDate.setText("Date: " + date);
            txtDoctor.setText("Doctor: " + doctor);
            txtDescription.setText("Description: " + description);
        }

        // Handle feedback submission
        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        String feedback = feedbackInput.getText().toString();
        if (!feedback.isEmpty()) {
            // Handle the feedback submission logic here
            // For example, sending feedback to a server or database
            Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_LONG).show();

            // Clear the input field after submission
            feedbackInput.setText("");
        } else {
            Toast.makeText(this, "Please enter some feedback before submitting.", Toast.LENGTH_LONG).show();
        }
    }
}
