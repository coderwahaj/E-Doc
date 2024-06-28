package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiveFeedback extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String patientName, patientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        recyclerView = findViewById(R.id.recyclerViewCancelAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);

        patientName = getIntent().getStringExtra("patientName");
        patientEmail = getIntent().getStringExtra("patientEmail");
        Log.d("GiveFeedback", "Received patientName: " + patientName + ", Email: " + patientEmail);

        Button okButton = findViewById(R.id.buttonOkCancel);
        okButton.setOnClickListener(v -> finish());

        fetchAppointments();
    }

    private void fetchAppointments() {
        db.collection("PastAppointments")
                .whereEqualTo("patientName", patientName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        appointments.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            appointment.put("id", document.getId());
                            fetchDoctorDetails(appointment);
                        }
                    } else {
                        Log.e("GiveFeedback", "Error getting documents or no documents found: ", task.getException());
                    }
                });
    }

    private void fetchDoctorDetails(Map<String, Object> appointment) {
        db.collection("doctors")
                .whereEqualTo("name", appointment.get("doctorName").toString())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String category = queryDocumentSnapshots.getDocuments().get(0).getString("category");
                        appointment.put("category", category);
                        checkFeedback(appointment);
                    } else {
                        // Fallback if category is not found
                        appointment.put("category", "Unknown Category");
                        checkFeedback(appointment);
                    }
                });
    }

    private void checkFeedback(Map<String, Object> appointment) {
        db.collection("feedback")
                .whereEqualTo("patientName", appointment.get("patientName").toString())
                .whereEqualTo("doctorName", appointment.get("doctorName").toString())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    appointment.put("feedbackRecorded", !queryDocumentSnapshots.isEmpty());
                    appointments.add(appointment);
                    adapter.notifyDataSetChanged();
                });
    }

    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

        private List<Map<String, Object>> appointments;

        AppointmentAdapter(List<Map<String, Object>> appointments) {
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbackitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> appointment = appointments.get(position);
            holder.textViewDoctorName.setText(appointment.get("doctorName").toString());
            holder.textViewAppointmentDate.setText(appointment.get("category").toString()); // Directly set category

            if ((boolean) appointment.get("feedbackRecorded")) {
                holder.textViewNoFeedback.setText("Feedback Recorded");
                holder.textViewNoFeedback.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.feedback_recorded));
                holder.buttonFeedback.setEnabled(false);
            } else {
                holder.textViewNoFeedback.setText("No Feedback Recorded");
                holder.textViewNoFeedback.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.no_feedback_recorded));
                holder.buttonFeedback.setEnabled(true);
            }

            holder.buttonFeedback.setOnClickListener(v -> openFeedbackActivity(appointment.get("patientName").toString(), appointment.get("doctorName").toString()));
        }


        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void openFeedbackActivity(String patientName, String doctorName) {
            Intent intent = new Intent(GiveFeedback.this, Feedback.class);
            intent.putExtra("patientName", patientName);
            intent.putExtra("doctorName", doctorName);
            startActivity(intent);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewDoctorName, textViewAppointmentDate, textViewAppointmentTime, textViewNoFeedback;
            Button buttonFeedback;

            ViewHolder(View itemView) {
                super(itemView);
                textViewDoctorName = itemView.findViewById(R.id.textViewDoctorName);
                textViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
                textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
                textViewNoFeedback = itemView.findViewById(R.id.textViewNoFeedback);
                buttonFeedback = itemView.findViewById(R.id.buttonFeedback);
            }
        }
    }
}
