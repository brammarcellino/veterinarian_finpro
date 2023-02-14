package com.example.veterinarian_finpro;

public class UserDetails {
    public  String fullname ,dob,gender,mobile;

    public UserDetails(String textDob, String textMobile, String textfullname){};
    public UserDetails(String textfullname,String textDob,String textGender,String textMobile) {
        this.fullname = textfullname;
        this.dob = textDob;
        this.gender= textGender;
        this.mobile = textMobile;
    }
}
