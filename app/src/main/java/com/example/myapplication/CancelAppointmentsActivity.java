package com.example.myapplication;

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

public class CancelAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CancelAppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String patientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointments);

        recyclerView = findViewById(R.id.recyclerViewCancelAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CancelAppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);

        patientEmail = getIntent().getStringExtra("patientEmail");  // Ensure patientEmail is passed correctly
        Log.d("CancelAppointmentsActivity", "Received patientEmail: " + patientEmail);

        Button okButton = findViewById(R.id.buttonOkCancel);
        okButton.setOnClickListener(v -> {
            finish();  // This should take you back to the existing instance of PatientProfile
        });

        fetchAppointments(patientEmail);  // Fetch appointments based on patient email
    }

    private void fetchAppointments(String email) {
        db.collection("ApprovedAppointments")
                .whereEqualTo("userEmail", email)  // Use patientEmail to filter documents
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        appointments.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            appointment.put("id", document.getId());
                            appointments.add(appointment);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("CancelAppointmentsActivity", "Error getting documents or no documents found: ", task.getException());
                    }
                });
    }

    private class CancelAppointmentAdapter extends RecyclerView.Adapter<CancelAppointmentAdapter.ViewHolder> {

        private List<Map<String, Object>> appointments;

        CancelAppointmentAdapter(List<Map<String, Object>> appointments) {
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_appointment_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> appointment = appointments.get(position);
            holder.textViewPatientName.setText((String) appointment.get("doctorName"));
            holder.textViewPatientEmail.setText((String) appointment.get("doctorEmail"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));
            holder.buttonCancel.setOnClickListener(v -> removeAppointment(position, appointment.get("id").toString()));
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void removeAppointment(int position, String appointmentId) {
            db.collection("ApprovedAppointments").document(appointmentId).delete()
                    .addOnSuccessListener(aVoid -> {
                        appointments.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, appointments.size());
                    })
                    .addOnFailureListener(e -> Log.w("CancelAppointmentAdapter", "Error canceling appointment", e));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewPatientName, textViewPatientEmail, textViewAppointmentDate, textViewAppointmentTime;
            Button buttonCancel;

            ViewHolder(View itemView) {
                super(itemView);
                textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
                textViewPatientEmail = itemView.findViewById(R.id.textViewPatientEmail);
                textViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
                textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
                buttonCancel = itemView.findViewById(R.id.buttonCancel);
            }
        }
    }
}
