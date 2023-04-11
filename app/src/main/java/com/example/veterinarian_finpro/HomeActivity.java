package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout btndoctor,btnprofile,btnhistory,btnpetcare;
    SliderView sliderView;
    int[] image = {R.drawable.petshop,R.drawable.vaccine,R.drawable.veterinary,R.drawable.catfood,R.drawable.catfood,R.drawable.cat,R.drawable.dog};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sliderView = findViewById(R.id.image_slider);
        btndoctor = findViewById(R.id.btndoctor);
        btnprofile = findViewById(R.id.btnprofile);
        btnhistory = findViewById(R.id.btnhistory);
        btnpetcare = findViewById(R.id.btnpetcare);

        imageSlider imageSlider = new imageSlider(image);

        sliderView.setSliderAdapter(imageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        btndoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BookingStartActivity.class));
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });


        btnhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });






    }
}