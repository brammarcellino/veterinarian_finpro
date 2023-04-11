package com.example.veterinarian_finpro;

public class Booking_Appointments {

    private String mobile;
    private int value;

    public Booking_Appointments() {
    }

    public Booking_Appointments(int value, String mobile) {
        this.value = value;
        this.mobile = mobile;
    }

    public String getmobile() {
        return mobile;
    }

    public void setmobile(String mobile) {
        this.mobile = mobile;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
