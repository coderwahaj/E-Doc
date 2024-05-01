package com.example.myapplication;

public class Doctor {
    private String Name;
    private String Email;
    private String Phone;
    private String Qualification;
    private String Experience;
    private String Category;

    // Constructor
    public Doctor(String name, String email, String phone, String qualification, String experience, String category) {
        this.Name = name;
        this.Email = email;
        this.Phone = phone;
        this.Qualification = qualification;
        this.Experience = experience;
        this.Category = category;
    }

    // Getters and setters for each field
    public String getName() { return Name; }
    public String getEmail() { return Email; }
    public String getPhone() { return Phone; }
    public String getQualification() { return Qualification; }
    public String getExperience() { return Experience; }
    public String getCategory() { return Category; }

    // Required for Firestore to deserialize
    public Doctor() {}
}
