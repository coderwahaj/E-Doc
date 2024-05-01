package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btn;
    TextView textView;
    FirebaseUser user;
    Connection con;
    String str;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        btn=findViewById(R.id.logout);
        textView=findViewById(R.id.user_details);
        user=auth.getCurrentUser();
        if(user==null){
            Intent intent =new Intent(getApplicationContext(),Launchpage.class);
            startActivity(intent);
            finish();

        }
        else{
            textView.setText(user.getEmail());
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(getApplicationContext(),AddDoctor.class);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Button button = findViewById(R.id.button);

        // Set click listener on the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to add data to Firestore
                addDataToFirestore("doctors", "John Doe");
            }
        });
    }
    private void addDataToFirestore(String collectionName, String name) {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");

        db.collection(collectionName).document("LA")
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data added to Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to add data to Firestore", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}