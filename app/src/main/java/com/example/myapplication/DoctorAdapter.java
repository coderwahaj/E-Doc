package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> implements Filterable {
    private List<Doctor> doctorList;
    private List<Doctor> doctorListFull;
    private String patientName;
    private String patientEmail;

    public DoctorAdapter(List<Doctor> doctorList, String patientName, String patientEmail) {
        this.doctorList = new ArrayList<>(doctorList);
        this.doctorListFull = new ArrayList<>(doctorList);
        this.patientName = patientName;
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.doctorNameTextView.setText(doctor.getName());
        holder.doctorCategoryTextView.setText(doctor.getCategory());
        holder.feeTextView.setText(String.format("Rs. %s", doctor.getFee()));
        holder.bookButton.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(holder.itemView.getContext(), ScheduleAppointment.class);
                intent.putExtra("doctorName", doctor.getName());
                intent.putExtra("doctorEmail", doctor.getEmail()); // Assuming Doctor model has getEmail()
                intent.putExtra("patientName", patientName);
                intent.putExtra("patientEmail", patientEmail);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    @Override
    public Filter getFilter() {
        return doctorFilter;
    }

    private Filter doctorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Doctor> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(doctorListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Doctor doctor : doctorListFull) {
                    if (doctor.getName().toLowerCase().contains(filterPattern) ||
                            doctor.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(doctor);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            doctorList.clear();
            doctorList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    // Method to update the list based on search results
    public void filterList(List<Doctor> filteredList) {
        doctorList.clear();
        doctorList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        public TextView doctorNameTextView;
        public TextView doctorCategoryTextView;
        public TextView feeTextView;
        public Button bookButton;
        private DoctorAdapter adapter;

        public DoctorViewHolder(@NonNull View itemView, DoctorAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            doctorNameTextView = itemView.findViewById(R.id.doctorName);
            doctorCategoryTextView = itemView.findViewById(R.id.doctorCategory);
            feeTextView = itemView.findViewById(R.id.fee);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}
