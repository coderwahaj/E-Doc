/*package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button logoutButton, scheduleAppointmentButton, viewAppointmentsButton, chatWithDoctorButton, btnx;
    private TextView textView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        scheduleAppointmentButton = findViewById(R.id.scheduleAppointmentButton);
        viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        btnx=findViewById(R.id.btnYourAppointments);
        //viewUpcomingAppointmentsButton = findViewById(R.id.viewUpcomingAppointments);
        chatWithDoctorButton = findViewById(R.id.btnChatWithDoctor);
        textView = findViewById(R.id.user_details);


        checkUserAuthentication();

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            navigateToLogin();
        });

        scheduleAppointmentButton.setOnClickListener(v -> {
            navigateToScheduleAppointment();
        });

        viewAppointmentsButton.setOnClickListener(v -> {
            navigateToApproveAppointmentActivity();
        });

        btnx.setOnClickListener(v -> {
            navigateToCancel();
        });



        chatWithDoctorButton.setOnClickListener(v -> {
            openChatActivity();
        });

    }

    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void navigateToCancel() {
        Intent intent = new Intent(getApplicationContext(), CancelAppointmentsActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToScheduleAppointment() {
        Intent intent = new Intent(getApplicationContext(), ScheduleAppointment.class);
        startActivity(intent);
    }

    private void navigateToApproveAppointmentActivity() {
        Intent intent = new Intent(this, ApproveAppointmentActivity.class);
        startActivity(intent);
    }



    private void openChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void checkUserAuthentication() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            navigateToLogin();
        } else {
            String userDetails = "Email: " + user.getEmail();
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                userDetails += "\nName: " + user.getDisplayName();
            }
            textView.setText(userDetails);
        }
    }
}*/
