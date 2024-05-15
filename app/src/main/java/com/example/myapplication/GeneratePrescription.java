////package com.example.myapplication;
////
////import android.os.Bundle;
////import android.widget.TextView;
////import androidx.appcompat.app.AppCompatActivity;
////
////public class GeneratePrescription extends AppCompatActivity {
////
////    private String doctorName;
////    private String patientName;
////
////    private String doctorEmail;
////    private String patientEmail;
////    // UI elements for displaying names
////    private TextView txtDoctorName;
////    private TextView txtPatientName;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_generate_prescription);  // Ensure this layout file is updated
////
////        // Initialize UI components for names
////        txtDoctorName = findViewById(R.id.txtDoctorName);
////        txtPatientName = findViewById(R.id.txtPatientName);
////
////        // Get data from the intent that started this activity
////        Bundle extras = getIntent().getExtras();
////        if (extras != null) {
////            doctorEmail = extras.getString("doctorEmail");
////            patientEmail = extras.getString("patientEmail");
////            doctorName = extras.getString("doctorName");
////            patientName = extras.getString("patientName");
////
////            // Update UI with the received names
////            txtDoctorName.setText("Doctor: " + doctorName);
////            txtPatientName.setText("Patient: " + patientName);
////        }
////    }
////
////
////}
//
//package com.example.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//
//public class GeneratePrescription extends AppCompatActivity {
//
//    private String doctorName;
//    private String patientName;
//    private String doctorEmail;
//    private String patientEmail;
//
//    // UI elements for displaying names
//    private TextView txtDoctorName;
//    private TextView txtPatientName;
//
//    // UI elements for item details
//    private EditText edtItemName, edtItemQuantity, edtItemDosage;
//    private LinearLayout layoutItems;
//    private Button btnAddItem, btnStorePrescription;
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_generate_prescription);
////
////        // Initialize UI components
////        txtDoctorName = findViewById(R.id.txtDoctorName);
////        txtPatientName = findViewById(R.id.txtPatientName);
////        edtItemName = findViewById(R.id.edtItemName);
////        edtItemQuantity = findViewById(R.id.edtItemQuantity);
////        edtItemDosage = findViewById(R.id.edtItemDosage);
////        layoutItems = findViewById(R.id.layoutItems);
////        btnAddItem = findViewById(R.id.btnAddItem);
////        btnStorePrescription = findViewById(R.id.btnStorePrescription);
////
////        // Set the names from intent extras
////        Bundle extras = getIntent().getExtras();
////        if (extras != null) {
////            doctorName = extras.getString("doctorName");
////            patientName = extras.getString("patientName");
////            txtDoctorName.setText("Doctor: " + doctorName);
////            txtPatientName.setText("Patient: " + patientName);
////        }
////
////        // Add items to the prescription
////        btnAddItem.setOnClickListener(v -> addItem());
////
////        // Store the prescription
////        btnStorePrescription.setOnClickListener(v -> openStorePrescriptionActivity());
////    }
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_generate_prescription);
//
//    // Initialize UI components
//    txtDoctorName = findViewById(R.id.txtDoctorName);
//    txtPatientName = findViewById(R.id.txtPatientName);
//    edtItemName = findViewById(R.id.edtItemName);
//    edtItemQuantity = findViewById(R.id.edtItemQuantity);
//    edtItemDosage = findViewById(R.id.edtItemDosage);
//    layoutItems = findViewById(R.id.layoutItems);
//    btnAddItem = findViewById(R.id.btnAddItem);
//    btnStorePrescription = findViewById(R.id.btnStorePrescription);
//    LinearLayout tableHeader = findViewById(R.id.tableHeader);  // Make sure this ID matches your XML
//    Button generatePrescriptionButton = findViewById(R.id.btnStorePrescription);
//
//    // Set visibility based on whether there are items
//    updateVisibility();
//
//    // Set the names from intent extras
//    Bundle extras = getIntent().getExtras();
//    if (extras != null) {
//        doctorName = extras.getString("doctorName");
//        patientName = extras.getString("patientName");
//        txtDoctorName.setText("Doctor: " + doctorName);
//        txtPatientName.setText("Patient: " + patientName);
//    }
//
//    // Add items to the prescription
//    btnAddItem.setOnClickListener(v -> {
//        addItem();
//        updateVisibility();  // Update visibility after an item is added
//    });
//
//    // Store the prescription
//    btnStorePrescription.setOnClickListener(v -> {
//        openStorePrescriptionActivity();
//        updateVisibility();  // Update visibility after storing
//    });
//}
//
//    private void updateVisibility() {
//        LinearLayout tableHeader = findViewById(R.id.tableHeader);  // Adjust if using a different ID
//        Button generatePrescriptionButton = findViewById(R.id.btnStorePrescription);
//
//        // Check if there are any children (items) in the layoutItems
//        if (layoutItems.getChildCount() == 0) {
//            tableHeader.setVisibility(View.GONE);
//            generatePrescriptionButton.setVisibility(View.GONE);
//        } else {
//            tableHeader.setVisibility(View.VISIBLE);
//            generatePrescriptionButton.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void addItem() {
//        String itemName = edtItemName.getText().toString().trim();
//        String itemQuantity = edtItemQuantity.getText().toString().trim();
//        String itemDosage = edtItemDosage.getText().toString().trim();
//
//        if (!itemName.isEmpty() && !itemQuantity.isEmpty() && !itemDosage.isEmpty()) {
//            View rowView = LayoutInflater.from(this).inflate(R.layout.prescription_item_row, layoutItems, false);
//            TextView txtName = rowView.findViewById(R.id.txtItemName);
//            TextView txtQuantity = rowView.findViewById(R.id.txtItemQuantity);
//            TextView txtDosage = rowView.findViewById(R.id.txtItemDosage);
//            Button btnDelete = rowView.findViewById(R.id.btnDelete);
//
//            txtName.setText(itemName);
//            txtQuantity.setText(itemQuantity);
//            txtDosage.setText(itemDosage);
//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    layoutItems.removeView(rowView);
//                }
//            });
//
//            layoutItems.addView(rowView);
//            // Clear fields
//            edtItemName.setText("");
//            edtItemQuantity.setText("");
//            edtItemDosage.setText("");
//        }
//    }
//
//    private void deleteItem(View rowView) {
//        layoutItems.removeView(rowView);
//        updateVisibility();  // Update visibility after an item is deleted
//    }
//
//    private void openStorePrescriptionActivity() {
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < layoutItems.getChildCount(); i++) {
//            View child = layoutItems.getChildAt(i);
//            String name = ((TextView) child.findViewById(R.id.txtItemName)).getText().toString();
//            String quantity = ((TextView) child.findViewById(R.id.txtItemQuantity)).getText().toString();
//            String dosage = ((TextView) child.findViewById(R.id.txtItemDosage)).getText().toString();
//            String itemDetails = "Name: " + name + ", Quantity: " + quantity + ", Dosage: " + dosage;
//            jsonArray.put(itemDetails);
//        }
//        Intent intent = new Intent(GeneratePrescription.this, StorePrescription.class);
//        intent.putExtra("prescriptionData", jsonArray.toString());
//        startActivity(intent);
//    }
//
//}
//

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

public class GeneratePrescription extends AppCompatActivity {

    private String doctorName;
    private String patientName;
    private String doctorEmail;
    private String patientEmail;

    // UI elements for displaying names
    private TextView txtDoctorName;
    private TextView txtPatientName;

    // UI elements for item details
    private EditText edtItemName, edtItemQuantity, edtItemDosage;
    private LinearLayout layoutItems;
    private Button btnAddItem, btnStorePrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_prescription);

        // Initialize UI components
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtPatientName = findViewById(R.id.txtPatientName);
        edtItemName = findViewById(R.id.edtItemName);
        edtItemQuantity = findViewById(R.id.edtItemQuantity);
        edtItemDosage = findViewById(R.id.edtItemDosage);
        layoutItems = findViewById(R.id.layoutItems);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnStorePrescription = findViewById(R.id.btnStorePrescription);

        // Set visibility based on whether there are items
        updateVisibility();

        // Set the names from intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doctorName = extras.getString("doctorName");
            patientName = extras.getString("patientName");
            txtDoctorName.setText("Doctor: " + doctorName);
            txtPatientName.setText("Patient: " + patientName);
        }

        // Add items to the prescription
        btnAddItem.setOnClickListener(v -> {
            addItem();
            updateVisibility();  // Update visibility after an item is added
        });

        // Store the prescription
        btnStorePrescription.setOnClickListener(v -> {
            openStorePrescriptionActivity();
            updateVisibility();  // Update visibility after storing
        });
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
            // Clear fields
            edtItemName.setText("");
            edtItemQuantity.setText("");
            edtItemDosage.setText("");
        }
    }

    private void deleteItem(View rowView) {
        layoutItems.removeView(rowView);
        updateVisibility();  // Update visibility after an item is deleted
    }

    private void openStorePrescriptionActivity() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < layoutItems.getChildCount(); i++) {
            View child = layoutItems.getChildAt(i);
            String name = ((TextView) child.findViewById(R.id.txtItemName)).getText().toString();
            String quantity = ((TextView) child.findViewById(R.id.txtItemQuantity)).getText().toString();
            String dosage = ((TextView) child.findViewById(R.id.txtItemDosage)).getText().toString();
            String itemDetails = "Name: " + name + ", Quantity: " + quantity + ", Dosage: " + dosage;
            jsonArray.put(itemDetails);
        }
        Intent intent = new Intent(GeneratePrescription.this, StorePrescription.class);
        intent.putExtra("prescriptionData", jsonArray.toString());
        startActivity(intent);
    }

    private void updateVisibility() {
        LinearLayout tableHeader = findViewById(R.id.tableHeader);
        Button generatePrescriptionButton = findViewById(R.id.btnStorePrescription);

        if (layoutItems.getChildCount() == 0) {
            tableHeader.setVisibility(View.GONE);
            generatePrescriptionButton.setVisibility(View.GONE);
        } else {
            tableHeader.setVisibility(View.VISIBLE);
            generatePrescriptionButton.setVisibility(View.VISIBLE);
        }
    }
}

