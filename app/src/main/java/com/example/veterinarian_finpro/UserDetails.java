package com.example.veterinarian_finpro;

public class UserDetails {
    public  String dob,gender,mobile;

    public UserDetails(){};

    public UserDetails(String textDob,String textGender,String textMobile) {
        this.dob = textDob;
        this.gender= textGender;
        this.mobile = textMobile;
    }
}
