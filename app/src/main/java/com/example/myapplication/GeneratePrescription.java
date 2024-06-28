package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneratePrescription extends AppCompatActivity {

    private String doctorName;
    private String patientName;
    private String doctorEmail;
    private String patientEmail;
    private String appointmentId;

    private TextView txtDoctorName;
    private TextView txtPatientName;
    private EditText edtItemName, edtItemQuantity, edtItemDosage;
    private LinearLayout layoutItems;
    private Button btnAddItem, btnStorePrescription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_prescription);

        initializeUIComponents();
        fetchIntentData();
        setUpListeners();
    }

    private void initializeUIComponents() {
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtPatientName = findViewById(R.id.txtPatientName);
        edtItemName = findViewById(R.id.edtItemName);
        edtItemQuantity = findViewById(R.id.edtItemQuantity);
        edtItemDosage = findViewById(R.id.edtItemDosage);
        layoutItems = findViewById(R.id.layoutItems);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnStorePrescription = findViewById(R.id.btnStorePrescription);
        progressBar = findViewById(R.id.progressBar);
    }

    private void fetchIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doctorName = extras.getString("doctorName");
            patientName = extras.getString("patientName");
            txtDoctorName.setText("Doctor: " + doctorName);
            txtPatientName.setText("Patient: " + patientName);
            appointmentId = extras.getString("appointmentId");
        }
    }

    private void setUpListeners() {
        btnAddItem.setOnClickListener(v -> {
            addItem();
            updateVisibility();
        });

        btnStorePrescription.setOnClickListener(v -> showConfirmationDialog());
    }

    private void addItem() {
        String itemName = edtItemName.getText().toString().trim();
        String itemQuantity = edtItemQuantity.getText().toString().trim();
        String itemDosage = edtItemDosage.getText().toString().trim();

        if (!itemName.isEmpty() && !itemQuantity.isEmpty() && !itemDosage.isEmpty()) {
            View rowView = LayoutInflater.from(this).inflate(R.layout.prescription_item_row, layoutItems, false);
            TextView txtName = rowView.findViewById(R.id.txtItemName);
            TextView txtQuantity = rowView.findViewById(R.id.txtItemQuantity);
            TextView txtDosage = rowView.findViewById(R.id.txtItemDosage);
            Button btnDelete = rowView.findViewById(R.id.btnDelete);

            txtName.setText(itemName);
            txtQuantity.setText(itemQuantity);
            txtDosage.setText(itemDosage);
            btnDelete.setOnClickListener(v -> deleteItem(rowView));

            layoutItems.addView(rowView);
            edtItemName.setText("");
            edtItemQuantity.setText("");
            edtItemDosage.setText("");
        }
    }

    private void deleteItem(View rowView) {
        layoutItems.removeView(rowView);
        updateVisibility();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to store the prescription?")
                .setPositiveButton("Yes", (dialog, which) -> storePrescriptionData())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void storePrescriptionData() {
        showLoading();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < layoutItems.getChildCount(); i++) {
            View child = layoutItems.getChildAt(i);
            try {
                JSONObject itemDetails = new JSONObject();
                itemDetails.put("itemName", ((TextView) child.findViewById(R.id.txtItemName)).getText().toString());
                itemDetails.put("itemQuantity", ((TextView) child.findViewById(R.id.txtItemQuantity)).getText().toString());
                itemDetails.put("itemDosage", ((TextView) child.findViewById(R.id.txtItemDosage)).getText().toString());

                jsonArray.put(itemDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> prescription = new HashMap<>();
        prescription.put("doctorName", doctorName);
        prescription.put("patientName", patientName);
        prescription.put("prescriptionData", convertJsonArrayToList(jsonArray));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PastAppointments").add(prescription)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Prescription stored successfully.", Toast.LENGTH_SHORT).show();
                    removeAppointmentFromApproved(appointmentId);
                    navigateToUpcomingAppointments();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error storing prescription.", Toast.LENGTH_SHORT).show();
                    hideLoading();
                });
    }

    private ArrayList<Map<String, String>> convertJsonArrayToList(JSONArray jsonArray) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Map<String, String> item = new HashMap<>();
                item.put("itemName", obj.getString("itemName"));
                item.put("itemQuantity", obj.getString("itemQuantity"));
                item.put("itemDosage", obj.getString("itemDosage"));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void removeAppointmentFromApproved(String appointmentId) {
        FirebaseFirestore.getInstance().collection("ApprovedAppointments")
                .document(appointmentId)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Appointment removed.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove appointment.", Toast.LENGTH_SHORT).show());
    }

    private void navigateToUpcomingAppointments() {
        Intent intent = new Intent(this, UpcomingAppointmentsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void updateVisibility() {
        LinearLayout tableHeader = findViewById(R.id.tableHeader);
        if (layoutItems.getChildCount() == 0) {
            tableHeader.setVisibility(View.GONE);
            btnStorePrescription.setVisibility(View.GONE);
        } else {
            tableHeader.setVisibility(View.VISIBLE);
            btnStorePrescription.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnStorePrescription.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnStorePrescription.setVisibility(View.VISIBLE);
    }
}
