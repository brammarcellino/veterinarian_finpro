package com.example.veterinarian_finpro;

public class UserDetails {
    public  String dob,gender,mobile,name;

    public UserDetails(){};

    public UserDetails(String textDob,String textGender,String textMobile,String textfullname) {
        this.dob = textDob;
        this.gender= textGender;
        this.mobile = textMobile;
        this.name = textfullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getname() {
        return  name;
    }
}
