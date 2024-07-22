package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    private RadioGroup radioGroup1, radioGroup5, radioGroup3, radioGroup4;
    private EditText additionalCommentsInput;
    private Button submitFeedbackButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String patientName, doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Extract data from Intent
        patientName = getIntent().getStringExtra("patientName");
        doctorName = getIntent().getStringExtra("doctorName");

        // Initialize Views
        radioGroup1 = findViewById(R.id.radio_group1);
        radioGroup5 = findViewById(R.id.radio_group5);
        radioGroup3 = findViewById(R.id.radio_group3);
        radioGroup4 = findViewById(R.id.radio_group4);
        additionalCommentsInput = findViewById(R.id.additional_comments_input);
        submitFeedbackButton = findViewById(R.id.submit_feedback_button);

        // Submit Feedback Button Click Listener
        submitFeedbackButton.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        // Get selected options from RadioGroups
        int selectedOption1Id = radioGroup1.getCheckedRadioButtonId();
        int selectedOption5Id = radioGroup5.getCheckedRadioButtonId();
        int selectedOption3Id = radioGroup3.getCheckedRadioButtonId();
        int selectedOption4Id = radioGroup4.getCheckedRadioButtonId();

        // Check if any option is not selected
        if (selectedOption1Id == -1 || selectedOption5Id == -1 || selectedOption3Id == -1 || selectedOption4Id == -1) {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected option text
        RadioButton selectedOption1 = findViewById(selectedOption1Id);
        RadioButton selectedOption5 = findViewById(selectedOption5Id);
        RadioButton selectedOption3 = findViewById(selectedOption3Id);
        RadioButton selectedOption4 = findViewById(selectedOption4Id);
        String option1Text = selectedOption1.getText().toString();
        String option5Text = selectedOption5.getText().toString();
        String option3Text = selectedOption3.getText().toString();
        String option4Text = selectedOption4.getText().toString();

        // Get additional comments
        String additionalComments = additionalCommentsInput.getText().toString();

        // Prepare feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("patientName", patientName);
        feedbackData.put("doctorName", doctorName);
        feedbackData.put("option1", option1Text);
        feedbackData.put("option5", option5Text);
        feedbackData.put("option3", option3Text);
        feedbackData.put("option4", option4Text);
        feedbackData.put("additionalComments", additionalComments);

        // Add feedback to Firestore and navigate back
        db.collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Feedback Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    // Set result to indicate success and pass back any needed data
                    Intent data = new Intent();
                    data.putExtra("feedbackSubmitted", true);
                    setResult(RESULT_OK, data);
                    finish(); // Finish this activity and return to GiveFeedback
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error submitting feedback", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED); // In case of failure, just cancel
                });
    }
}
