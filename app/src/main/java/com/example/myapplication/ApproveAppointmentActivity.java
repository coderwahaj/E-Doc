package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_appointment);

        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);
        Button okButton = findViewById(R.id.buttonOk);
        okButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
                fetchUserDetailsAndNavigate1(user.getEmail());

        });


        fetchAppointments();
    }

    private void fetchAppointments() {
        db.collection("pendingAppointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        appointments.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            appointment.put("id", document.getId());
                            appointments.add(appointment);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("FetchAppointments", "Error getting documents.", task.getException());
                    }
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> appointment = appointments.get(position);
            holder.textViewPatientName.setText((String) appointment.get("name"));
            holder.textViewPatientEmail.setText((String) appointment.get("email"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));

            holder.buttonAccept.setOnClickListener(v -> {
                moveAppointmentToCheckedAndRemove(appointment);
            });
            holder.buttonReject.setOnClickListener(v -> {
                moveAppointmentToCheckedAndRemove(appointment);
            });
            holder.buttonDone.setOnClickListener(v -> {
                moveAppointmentToCheckedAndRemove(appointment);
            });
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void moveAppointmentToCheckedAndRemove(Map<String, Object> appointment) {
            String appointmentId = (String) appointment.get("id");  // Ensure this ID is correctly set when fetching appointments
            if (appointmentId == null || appointmentId.isEmpty()) {
                Log.w("AppointmentError", "Invalid appointment ID.");
                Toast.makeText(ApproveAppointmentActivity.this, "Error: Invalid appointment ID.", Toast.LENGTH_SHORT).show();
                return;
            }

            // First, add the appointment to the "checked" collection
            db.collection("checked")
                    .add(appointment)
                    .addOnSuccessListener(documentReference -> {
                        // Successfully added to "checked", now delete from "ApprovedAppointments"
                        deleteAppointmentFromApproved(appointmentId);
                    })
                    .addOnFailureListener(e -> {
                        Log.w("AppointmentError", "Failed to move appointment to 'checked': ", e);
                        Toast.makeText(ApproveAppointmentActivity.this,"Failed to move appointment to checked.", Toast.LENGTH_SHORT).show();
                    });
        }

        private void deleteAppointmentFromApproved(String appointmentId) {
            db.collection("ApprovedAppointments").document(appointmentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("AppointmentDeleted", "Appointment successfully deleted from 'ApprovedAppointments'.");
                        fetchAppointments();  // Refresh the list to reflect the deletion
                    })
                    .addOnFailureListener(e -> {
                        Log.w("AppointmentError", "Failed to delete appointment from 'ApprovedAppointments': ", e);
                        Toast.makeText(ApproveAppointmentActivity.this, "Failed to delete appointment.", Toast.LENGTH_SHORT).show();
                    });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewPatientName, textViewPatientEmail, textViewAppointmentDate, textViewAppointmentTime;
            Button buttonAccept, buttonReject, buttonDone;

            ViewHolder(View itemView) {
                super(itemView);
                textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
                textViewPatientEmail = itemView.findViewById(R.id.textViewPatientEmail);
                textViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
                textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
                buttonAccept = itemView.findViewById(R.id.buttonAccept);
                buttonReject = itemView.findViewById(R.id.buttonReject);
                buttonDone = itemView.findViewById(R.id.buttonDone);
            }
        }
    }
    private void fetchUserDetailsAndNavigate1(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doctors")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String name = documentSnapshot.getString("name");
                        String category = documentSnapshot.getString("category");
                        String qualification = documentSnapshot.getString("qualification");
                        String email = documentSnapshot.getString("email");
                        String experience=documentSnapshot.getString("experience");
                        String phone= documentSnapshot.getString("phone");
                        Intent intent = new Intent(ApproveAppointmentActivity.this, DoctorProfile.class);
                        intent.putExtra("name", name);
                        intent.putExtra("category", category);
                        intent.putExtra("qualification", qualification);
                        intent.putExtra("experience",experience);
                        intent.putExtra("phone",phone);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ApproveAppointmentActivity.this, "Doctor's details not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(ApproveAppointmentActivity.this, "Failed to fetch doctor details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

