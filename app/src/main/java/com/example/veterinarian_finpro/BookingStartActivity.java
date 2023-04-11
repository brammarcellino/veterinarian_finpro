package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class BookingStartActivity extends AppCompatActivity {
    RelativeLayout btnajing, btncat, btnfish, btntutle, btnbird, btnreptile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_start);
        RecyclerView recyclerView_spec = (RecyclerView) findViewById(R.id.recycler_spec);


        btnajing = findViewById(R.id.btnanj);
        btncat = findViewById(R.id.btncat);
        btnfish = findViewById(R.id.btnfish);
        btntutle = findViewById(R.id.btntutle);
        btnbird = findViewById(R.id.btnbird);
        btnreptile = findViewById(R.id.btnreptile);

        btnajing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DogActivity.class));
            }
        });


        LinearLayoutManager layoutManager_spec = new LinearLayoutManager(
                BookingStartActivity.this, LinearLayoutManager.HORIZONTAL, false);


        Integer[] specialisation_pic = {R.drawable.cat, R.drawable.dog, R.drawable.fish, R.drawable.reptiled,
                R.drawable.tutle, R.drawable.bird,};

        String[] specialisation_type = {"Cat", "Dog", "Leprology", "Endocrinology & Diabetes", "Thyroid", "Hormone", "Immunology", "Rheumatology", "Neurology", "Ophthalmology", "Cardiac Sciences", "Cancer Care / Oncology", "Gastroenterology, Hepatology & Endoscopy", "Ear Nose Throat"};

        ArrayList<Main_Specialisation> main_specialisations = new ArrayList<>();

        for (int i = 0; i < specialisation_pic.length; i++) {
            Main_Specialisation specialisation = new Main_Specialisation(specialisation_pic[i], specialisation_type[i]);
            main_specialisations.add(specialisation);

            recyclerView_spec.setLayoutManager(layoutManager_spec);
            recyclerView_spec.setItemAnimator(new DefaultItemAnimator());

            Specialist_Adapter specialist_adapter = new Specialist_Adapter(main_specialisations, BookingStartActivity.this);
            recyclerView_spec.setAdapter(specialist_adapter);

            btnajing.setOnClickListener(v -> {
                Intent intent=new Intent(BookingStartActivity.this,AvalibleDoctorActivity.class);
                intent.putExtra("flag",0+"");
                startActivity(intent);
            });

        }


    }
}