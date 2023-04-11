package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctors_View_Profile extends AppCompatActivity {
    private TextView name,gender,email,speciality,bio,experience, fees;
    private String email_id;
    private ImageView update,sign;
    private CircleImageView circle_image;
    private Doctor_Images doctor_images, sign_images;
    private DatabaseReference reference_user, reference_doctor;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_view_profile);
        progressBar=(ProgressBar) findViewById(R.id.progressbar_view_profile);
        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        email = (TextView) findViewById(R.id.email);
        experience = (TextView) findViewById(R.id.experience);
        speciality = (TextView) findViewById(R.id.speciality);
        bio = (TextView) findViewById(R.id.bio);
        fees = (TextView) findViewById(R.id.consultation);
        update = (ImageView) findViewById(R.id.imageView5);
        sign = (ImageView) findViewById(R.id.signImage);
        circle_image = (CircleImageView) findViewById(R.id.profileImage);
        Doctors_Session_Mangement doctors_session_mangement = new Doctors_Session_Mangement(this);
        email_id = doctors_session_mangement.getDoctorSession()[0].replace(".",",");

        progressBar.setVisibility(View.VISIBLE);
        reference_doctor = FirebaseDatabase.getInstance().getReference("Doctors_Data");
        reference_user = FirebaseDatabase.getInstance().getReference("Users");

        reference_doctor.child(email_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference_doctor.child(email_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    gender.setText(snapshot.child("gender").getValue(String.class));
                    speciality.setText(snapshot.child("type").getValue(String.class));
                    bio.setText(snapshot.child("desc").getValue(String.class));
                    experience.setText(snapshot.child("experience").getValue(String.class)+ " years");
                    fees.setText("Rp. " + snapshot.child("fees").getValue(String.class));
                    doctor_images = snapshot.child("doc_pic").getValue(Doctor_Images.class);
                    sign_images = snapshot.child("sign_pic").getValue(Doctor_Images.class);
                    if(doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(circle_image);
                    }
                    if(sign_images != null){
                        Picasso.get().load(sign_images.getUrl()).into(sign);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}