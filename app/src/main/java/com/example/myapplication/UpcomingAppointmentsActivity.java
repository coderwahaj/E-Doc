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

public class UpcomingAppointmentsActivity extends AppCompatActivity {

    private UpcomingAppointmentAdapter adapter;
    private final List<Map<String, Object>> appointments = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button generatepres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_appointments);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UpcomingAppointmentAdapter(appointments);
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
            db.collection("ApprovedAppointments")
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
                            Log.w("FetchUpcomingAppointments", "Error getting documents.", task.getException());
                        }
                    });
        } else {
            Log.w("FetchAppointments", "No user logged in.");
        }
    }

    private static class UpcomingAppointmentAdapter extends RecyclerView.Adapter<UpcomingAppointmentAdapter.ViewHolder> {

        private List<Map<String, Object>> appointments;

        UpcomingAppointmentAdapter(List<Map<String, Object>> appointments) {
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_appointment_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Map<String, Object> appointment = appointments.get(position);
            holder.textViewPatientName.setText((String) appointment.get("patientName"));
            holder.textViewPatientEmail.setText((String) appointment.get("userEmail"));
            holder.textViewAppointmentDate.setText((String) appointment.get("date"));
            holder.textViewAppointmentTime.setText((String) appointment.get("time"));

            holder.buttonDone.setOnClickListener(v -> {
                removeAppointment(position);  // Keep this if you want to remove the item from the list when 'Done' is clicked

                // Start the GeneratePrescription activity
                Intent intent = new Intent(v.getContext(), GeneratePrescription.class);

                // Put extra data
                intent.putExtra("appointmentId", (String) appointment.get("id"));
                intent.putExtra("patientName", (String) appointment.get("patientName"));
                intent.putExtra("patientEmail", (String) appointment.get("userEmail"));
                intent.putExtra("doctorName", (String) appointment.get("doctorName")); // Add doctorName

                v.getContext().startActivity(intent);
            });
        }


        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void removeAppointment(int position) {
            if (position < appointments.size()) {
                appointments.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, appointments.size());
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewPatientName, textViewPatientEmail, textViewAppointmentDate, textViewAppointmentTime;
            Button buttonDone;

            ViewHolder(View itemView) {
                super(itemView);
                textViewPatientName = itemView.findViewById(R.id.textViewPatientName);
                textViewPatientEmail = itemView.findViewById(R.id.textViewPatientEmail);
                textViewAppointmentDate = itemView.findViewById(R.id.textViewAppointmentDate);
                textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);
                buttonDone = itemView.findViewById(R.id.buttonDone);
            }
        }
    }
}
