package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;

public class StorePrescription extends AppCompatActivity {

    private LinearLayout llPrescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_prescription);

        llPrescriptionList = findViewById(R.id.llPrescriptionList);

        // Assuming you pass the prescription data as a serialized JSON array
        String jsonData = getIntent().getStringExtra("prescriptionData");
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                String itemDetails = jsonArray.getString(i);  // This should be formatted data
                TextView textView = new TextView(this);
                textView.setText(itemDetails);
                textView.setPadding(10, 10, 10, 10);
                llPrescriptionList.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
