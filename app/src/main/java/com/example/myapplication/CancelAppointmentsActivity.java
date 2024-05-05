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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
   private  String currentUserId = currentUser.getUid(); // or currentUser.getEmail() if you are using emails
private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointments); // Ensure this layout contains the RecyclerView

        recyclerView = findViewById(R.id.textViewAppointmentDetails); // Correct ID for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Get email passed from PatientProfile
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            email = intent.getStringExtra("email"); // Store the email
        }
        if (currentUser != null) {
            fetchApprovedAppointments(currentUserId);
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            // Handle user not logged in scenario
        }
    }


    private void fetchApprovedAppointments(String userId) {
        db.collection("ApprovedAppointments")
                .whereEqualTo("userId", email) // Assuming 'userId' is the field in the Firestore documents
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            appointment.put("id", document.getId());
                            appointments.add(appointment);
                        }
                        adapter.setAppointments(appointments);
                    } else {
                        Log.w("FetchAppointments", "Error getting documents.", task.getException());
                        Toast.makeText(this, "Failed to fetch appointments.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void deleteAppointment(String appointmentId) {
        db.collection("ApprovedAppointments").document(appointmentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteAppointment", "Appointment successfully deleted.");
                    fetchApprovedAppointments(currentUserId);  // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Log.w("DeleteAppointment", "Error deleting appointment.", e);
                    Toast.makeText(this, "Failed to delete appointment.", Toast.LENGTH_SHORT).show();
                });
    }

    class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

        private List<Map<String, Object>> appointments;

        AppointmentAdapter(List<Map<String, Object>> appointments) {
            this.appointments = appointments;
        }

        void setAppointments(List<Map<String, Object>> appointments) {
            this.appointments = appointments;
            notifyDataSetChanged();
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
            holder.textViewPatientName.setText((String) appointment.get("name"));
            holder.textViewPatientEmail.setText((String) appointment.get("email"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));

            holder.buttonCancel.setOnClickListener(v -> {
                deleteAppointment((String) appointment.get("id"));
            });
        }



        @Override
        public int getItemCount() {
            return appointments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewPatientName;
            TextView textViewPatientEmail;
            TextView textViewAppointmentDate;
            TextView textViewAppointmentTime;
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


