package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public DoctorAdapter(List<Doctor> doctorList)
    {
        this.doctorList = doctorList;
        doctorListFull = new ArrayList<>(doctorList);
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.doctorNameTextView.setText(doctor.getName());
        holder.doctorCategoryTextView.setText(doctor.getCategory());
        holder.feeTextView.setText(String.format("Rs. %s", doctor.getFee()));
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
                    if (doctor.getName().toLowerCase().contains(filterPattern)) {
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

    public void filterList(List<Doctor> filteredList) {
        doctorList = filteredList;
        notifyDataSetChanged();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        public TextView doctorNameTextView;
        public TextView doctorCategoryTextView;
        public TextView feeTextView;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorName);
            doctorCategoryTextView = itemView.findViewById(R.id.doctorCategory);
            feeTextView = itemView.findViewById(R.id.fee);
        }
    }

}