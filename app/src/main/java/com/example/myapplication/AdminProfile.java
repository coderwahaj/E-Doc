package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ResourceBundle;

public class AdminProfile extends AppCompatActivity {

    private TextView txtAdminName;
    private Button btnGenerateInvoice, btnViewReports, btnSettings;

    private String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        // Initialize views
        txtAdminName = findViewById(R.id.txtAdminName);

        btnGenerateInvoice = findViewById(R.id.btnGenerateInvoice);
        btnViewReports = findViewById(R.id.btnViewReports);
        btnSettings = findViewById(R.id.btnSettings);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            txtAdminName.setText("Name: " + extras.getString("name", "N/A"));
            adminEmail = extras.getString("email");
        }

        // Set up button listeners
        btnGenerateInvoice.setOnClickListener(v -> openGenerateInvoiceActivity());
        btnViewReports.setOnClickListener(v -> openViewReportsActivity());
        btnSettings.setOnClickListener(v -> openAddDoctor());
    }

    private void openGenerateInvoiceActivity() {
        // Start activity to generate invoices

    }

    private void openViewReportsActivity()
    {

    }

    private void openAddDoctor() {
        Intent intent = new Intent(AdminProfile.this, AddDoctor.class);
        startActivity(intent);
        finish();
    }
}
