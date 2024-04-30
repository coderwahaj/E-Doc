package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminProfile extends AppCompatActivity {

    private TextView txtAdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        txtAdminName = findViewById(R.id.txtAdminName);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email"); // Not used in UI but could be used for further features
            String name = extras.getString("name", "N/A");

            // Set data to views
            txtAdminName.setText("Name: " + name);
        }
    }
}
