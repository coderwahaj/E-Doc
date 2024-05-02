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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        recyclerView = findViewById(R.id.doctorsRecyclerView);
        searchQueryEditText = findViewById(R.id.searchQueryEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorAdapter(doctorList);
        recyclerView.setAdapter(adapter);

        // Access Firebase Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get all doctors from the "doctors" collection
        db.collection("doctors").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                doctorList.clear(); // Clear the existing data in doctorList
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Doctor doctor = document.toObject(Doctor.class); // Convert the Firestore document into a Doctor object
                    doctorList.add(doctor); // Add the doctor to the list
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            } else {
                Log.w("SearchDoctorActivity", "Error getting documents: ", task.getException());
            }
        });

        // Implement search functionality
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

    // Filter doctors based on the search query
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