package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.veterinarian_finpro.Adapter.AdapterDogdoctor;
import com.example.veterinarian_finpro.model.Modeldoctordog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DogActivity extends AppCompatActivity {
    RecyclerView recyclerViewspec;
    AdapterDogdoctor adapterDogdoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);
        recyclerViewspec = findViewById(R.id.recycler_spec);
        recyclerViewspec.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Modeldoctordog>options = new FirebaseRecyclerOptions.Builder<Modeldoctordog>().setQuery(FirebaseDatabase.getInstance().getReference().child("Doctor_dog"),Modeldoctordog.class).build();
       adapterDogdoctor = new AdapterDogdoctor(options);
        recyclerViewspec.setAdapter(adapterDogdoctor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterDogdoctor.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterDogdoctor.stopListening();
    }
}