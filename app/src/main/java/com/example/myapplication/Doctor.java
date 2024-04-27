package com.example.myapplication;



public class Doctor {
    private String name;
    private String email;
    private String phone;
    private String qualification;
    private String experience;

    public Doctor() {
        // Default constructor required for Firestore
    }

    public Doctor(String name, String email, String phone, String qualification, String experience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.qualification = qualification;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}

