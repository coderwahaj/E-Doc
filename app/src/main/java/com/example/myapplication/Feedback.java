package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity {

    private RadioGroup radioGroup1, radioGroup5, radioGroup3, radioGroup4;
    private EditText additionalCommentsInput;
    private Button submitFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Views
        radioGroup1 = findViewById(R.id.radio_group1);
        radioGroup5 = findViewById(R.id.radio_group5);
        radioGroup3 = findViewById(R.id.radio_group3);
        radioGroup4 = findViewById(R.id.radio_group4);
        additionalCommentsInput = findViewById(R.id.additional_comments_input);
        submitFeedbackButton = findViewById(R.id.submit_feedback_button);

        // Submit Feedback Button Click Listener
        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected options from RadioGroups
                int selectedOption1Id = radioGroup1.getCheckedRadioButtonId();
                int selectedOption5Id = radioGroup5.getCheckedRadioButtonId();
                int selectedOption3Id = radioGroup3.getCheckedRadioButtonId();
                int selectedOption4Id = radioGroup4.getCheckedRadioButtonId();

                // Check if any option is not selected
                if (selectedOption1Id == -1 || selectedOption5Id == -1 || selectedOption3Id == -1 || selectedOption4Id == -1) {
                    Toast.makeText(Feedback.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
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

                // Display feedback details
                displayFeedbackDetails(option1Text, option5Text, option3Text, option4Text, additionalComments);
            }
        });
    }

    private void displayFeedbackDetails(String option1Text, String option5Text, String option3Text, String option4Text, String additionalComments) {
        // Display feedback details (you can replace this with your desired action)
        String feedbackDetails = "Question 1: " + option1Text + "\n"
                + "Question 5: " + option5Text + "\n"
                + "Question 3: " + option3Text + "\n"
                + "Question 4: " + option4Text + "\n"
                + "Additional Comments: " + additionalComments;

        Toast.makeText(this, feedbackDetails, Toast.LENGTH_LONG).show();
    }
}
