package com.example.veterinarian_finpro.model;

public class Modeldoctordog {
    private String Contact, Doctor_Picture, Name_Doctor, experince, fee;


    public Modeldoctordog() {

    }

    public Modeldoctordog(String Contact, String Doctor_Picture, String Name_Doctor, String experince, String fee) {
        this.Contact = Contact;
        this.Doctor_Picture = Doctor_Picture;
        this.Name_Doctor = Name_Doctor;
        this.experince = experince;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDoctor_Picture() {
        return Doctor_Picture;
    }

    public void setDoctor_Picture(String doctor_Picture) {
        Doctor_Picture = doctor_Picture;
    }

    public String getName_Doctor() {
        return Name_Doctor;
    }

    public void setName_Doctor(String name_Doctor) {
        Name_Doctor = name_Doctor;
    }

    public String getExperince() {
        return experince;
    }

    public void setExperince(String experince) {
        this.experince = experince;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}