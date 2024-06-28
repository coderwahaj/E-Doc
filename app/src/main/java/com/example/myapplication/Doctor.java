package com.example.myapplication;

public class Doctor {
    private String name;
    private String email;
    private String phone;
    private String qualification;
    private String experience;
    private String category;
    private String password;
    private String confirmPassword;
    private String fee;
    // Constructor
    public Doctor(String name, String email, String phone, String qualification, String experience, String category, String password, String confirmPassword,String fee) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.qualification = qualification;
        this.experience = experience;
        this.category = category;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fee=fee;
    }


    // Getters and setters for each field
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getQualification() { return qualification; }
    public String getExperience() { return experience; }
    public String getCategory() { return category; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getFee(){return  fee;}
    // Required for Firestore to deserialize
    public Doctor() {}
}