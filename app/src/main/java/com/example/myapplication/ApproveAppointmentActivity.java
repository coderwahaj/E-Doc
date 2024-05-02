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

public class ApproveAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Map<String, Object>> appointments = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            Intent intent = new Intent(ApproveAppointmentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Optionally keep this if you want to remove this activity from the back stack
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
            db.collection(collectionName)
                    .add(appointment)
                    .addOnSuccessListener(documentReference -> {
                        int position = appointments.indexOf(appointment);
                        if (position != -1) {
                            appointments.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, appointments.size());
                        }
                        db.collection("pendingAppointments").document((String) appointment.get("id"))
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("DeleteAppointment", "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(e -> Log.w("DeleteAppointment", "Error deleting document", e));
                    })
                    .addOnFailureListener(e -> Log.w("MoveAppointment", "Error adding document", e));
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