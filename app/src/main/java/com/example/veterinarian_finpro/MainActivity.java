package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button buttonLogin = findViewById(R.id.button_loginUser);
        Button buttonLoginDoctor= findViewById(R.id.button_loginDoctor);
        Button buttonLoginAdmin = findViewById(R.id.button_loginAdmin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        buttonLoginDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,DoctorsLoginActivity.class);
                startActivity(intent);


            }
        });

        buttonLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent (MainActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        TextView textView = findViewById(R.id.textView_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegiterActivity.class);
                startActivity(intent);
            }
        });


    }
}