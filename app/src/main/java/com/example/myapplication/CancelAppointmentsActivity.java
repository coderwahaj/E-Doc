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
    private CancelAppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointments);

        recyclerView = findViewById(R.id.recyclerViewCancelAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CancelAppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);

        // testDisplayWithStaticData();

        Button okButton = findViewById(R.id.buttonOkCancel);
        okButton.setOnClickListener(v -> {
            finish(); // Close the CancelAppointmentsActivity
            Intent intent = new Intent(CancelAppointmentsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        checkAuthenticationAndFetch();
    }
    private void testDisplayWithStaticData() {
        Map<String, Object> testAppointment = new HashMap<>();
        testAppointment.put("name", "Test Name");
        testAppointment.put("email", "test@email.com");
        testAppointment.put("date", "2024-05-02");
        testAppointment.put("time", "12:00 PM");
        appointments.add(testAppointment);
        adapter.notifyDataSetChanged();
    }

    private void checkAuthenticationAndFetch() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            fetchAppointments(currentUser.getUid());
        } else {
            Log.w("CancelAppointmentsActivity", "User not logged in.");
        }
    }

    private void fetchAppointments(String userId) {
        Log.d("CancelAppointmentsActivity", "Fetching all appointments");
        db.collection("ApprovedAppointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("CancelAppointmentsActivity", "Query completed.");
                        appointments.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> appointment = new HashMap<>(document.getData());
                            if (appointment.get("userId") != null && appointment.get("userId").equals(userId)) {
                                appointment.put("id", document.getId());
                                appointments.add(appointment);
                                Log.d("CancelAppointmentsActivity", "Appointment for user added: " + appointment);
                            }
                        }
                        if (appointments.isEmpty()) {
                            Log.d("CancelAppointmentsActivity", "No appointments found for this user.");
                        } else {
                            adapter.notifyDataSetChanged();
                            Log.d("CancelAppointmentsActivity", "Adapter notified of data change.");
                        }
                    } else {
                        Log.e("CancelAppointmentsActivity", "Error getting documents: ", task.getException());
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
            holder.textViewPatientName.setText((String) appointment.get("name"));
            holder.textViewPatientEmail.setText((String) appointment.get("email"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));
            holder.buttonCancel.setOnClickListener(v -> removeAppointment(position));
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void removeAppointment(int position) {
            if (position < appointments.size()) {
                String appointmentId = (String) appointments.get(position).get("id");
                db.collection("ApprovedAppointments").document(appointmentId).delete()
                        .addOnSuccessListener(aVoid -> {
                            appointments.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, appointments.size());
                            Log.d("CancelAppointmentAdapter", "Appointment successfully canceled!");
                        })
                        .addOnFailureListener(e -> Log.w("CancelAppointmentAdapter", "Error canceling appointment", e));
            }
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