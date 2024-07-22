package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class SearchDoctor extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private List<Doctor> doctorList = new ArrayList<>();
    private EditText searchQueryEditText;
    private String patientName;
    private String patientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        // Retrieve patient name and email from Intent extras
        patientName = getIntent().getStringExtra("patientName");
        patientEmail = getIntent().getStringExtra("patientEmail");

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.doctorsRecyclerView);
        searchQueryEditText = findViewById(R.id.searchQueryEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Instantiate the DoctorAdapter with patient name and email
        adapter = new DoctorAdapter(doctorList, patientName, patientEmail);
        recyclerView.setAdapter(adapter);

        // Fetch doctors from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctors").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                doctorList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Doctor doctor = document.toObject(Doctor.class);
                    doctorList.add(doctor);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.w("SearchDoctorActivity", "Error getting documents: ", task.getException());
            }
        });

        // Setup a listener for text changes in the search box
        searchQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Method to filter doctors based on search query
    private void filterDoctors(String query) {
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(doctor);
            }
        }
        adapter.filterList(filteredList);
    }
}
