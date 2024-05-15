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

public class ApproveAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approve_appointment);

        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);
        Button okButton = findViewById(R.id.buttonOk);
        okButton.setOnClickListener(v -> {
            finish();
        });

        fetchAppointments();
    }

    private void fetchAppointments() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String doctorEmail = currentUser.getEmail();
            db.collection("pendingAppointments")
                    .whereEqualTo("doctorEmail", doctorEmail)
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
        } else {
            Log.w("FetchAppointments", "No user logged in.");
        }
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
            holder.textViewPatientName.setText((String) appointment.get("patientName"));
            holder.textViewPatientEmail.setText((String) appointment.get("userEmail"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));
            holder.buttonAccept.setOnClickListener(v -> {
                moveAppointment(appointment, "ApprovedAppointments");
            });
            holder.buttonReject.setOnClickListener(v -> {
                moveAppointment(appointment, "TrashAppointments");
            });
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void moveAppointment(Map<String, Object> appointment, String collectionName) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                appointment.put("userId", userId);
                db.collection(collectionName)
                        .add(appointment)
                        .addOnSuccessListener(documentReference -> {
                            String appointmentId = (String) appointment.get("id");
                            appointments.remove(appointment);
                            notifyItemRemoved(appointments.indexOf(appointment));
                            db.collection("pendingAppointments").document(appointmentId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> Log.d("DeleteAppointment", "DocumentSnapshot successfully deleted!"))
                                    .addOnFailureListener(e -> Log.w("DeleteAppointment", "Error deleting document", e));
                        })
                        .addOnFailureListener(e -> Log.w("MoveAppointment", "Error adding document", e));
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewPatientName, textViewPatientEmail, textViewAppointmentDate, textViewAppointmentTime;
            Button buttonAccept, buttonReject;

            ViewHolder(View itemView) {
                super(itemView);
                textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
                textViewPatientEmail = itemView.findViewById(R.id.textViewPatientEmail);
                textViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
                textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
                buttonAccept = itemView.findViewById(R.id.buttonAccept);
                buttonReject = itemView.findViewById(R.id.buttonReject);
            }
        }
    }

}
