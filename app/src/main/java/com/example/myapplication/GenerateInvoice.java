package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GenerateInvoice extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_invoice);

        recyclerView = findViewById(R.id.recyclerViewInvoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("ApprovedAppointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Invoice> invoices = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Invoice invoice = new Invoice(
                                    document.getId(),
                                    document.getString("name"),
                                    document.getString("email"),
                                    document.getString("time"),
                                    document.getString("date"),
                                    document.getString("doctorName"),
                                    document.getString("fee"),
                                    document.getString("userId")
                            );
                            invoices.add(invoice);
                            // Save the Invoice object to the "Invoices" collection
                            db.collection("Invoices").document(document.getId()).set(invoice);
                        }
                        adapter = new InvoiceAdapter(invoices);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
