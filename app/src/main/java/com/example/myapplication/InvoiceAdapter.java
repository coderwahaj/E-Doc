package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    private List<Invoice> invoices;
    private Context context;

    public InvoiceAdapter(List<Invoice> invoices) {
        this.context = context;
        this.invoices = invoices;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_item, parent, true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        holder.nameTextView.setText(invoice.getName());
        holder.emailTextView.setText(invoice.getEmail());
        holder.dateTextView.setText(invoice.getDate());
        holder.timeTextView.setText(invoice.getTime());
        holder.doctorNameTextView.setText(invoice.getDoctorName());
        holder.feeTextView.setText(invoice.getFee());
        holder.userIdTextView.setText(invoice.getUserId());
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView doctorNameTextView;
        TextView feeTextView;
        TextView userIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txtPatientName);
            emailTextView = itemView.findViewById(R.id.txtPatientEmail);
            dateTextView = itemView.findViewById(R.id.txtAppointmentDate);
            timeTextView = itemView.findViewById(R.id.txtAppointmentTime);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            feeTextView = itemView.findViewById(R.id.txtDoctorFee);
        }
    }
}
