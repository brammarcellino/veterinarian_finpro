package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeActivity extends AppCompatActivity {
    SliderView sliderView;
    int[] image = {R.drawable.petshop,R.drawable.vaccine,R.drawable.veterinary,R.drawable.catfood,R.drawable.catfood,R.drawable.cat,R.drawable.dog};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sliderView = findViewById(R.id.image_slider);

        imageSlider imageSlider = new imageSlider(image);

        sliderView.setSliderAdapter(imageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();




    }
}