package com.example.myapplication;

public class Invoice {
    private String id;
    private String name; // Updated field
    private String email; // Updated field
    private String doctorname; // Updated field
    private String date; // Updated field
    private String time; // Updated field
    private String fee; // Updated field
    private String userId;

    public Invoice() {
        // Default constructor required for calls to DataSnapshot.getValue(Invoice.class)
    }

    public Invoice(String id, String patientName, String email, String doctorName, String date, String time, String fee, String userId) {
        this.id = id;
        this.name = patientName;
        this.email = email;
        this.doctorname = doctorName;
        this.date = date;
        this.time = time;
        this.fee = fee;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDoctorName() {
        return doctorname;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFee() {
        return fee;
    }

    public String getUserId() {
        return userId;
    }
}
