package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.veterinarian_finpro.Adapter.Admin_All_Doctor_Adapter;
import com.example.veterinarian_finpro.model.Doctors_Profile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Activity extends AppCompatActivity {
    private RecyclerView rv;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private DatabaseReference reference_doc,patient;
    private int flag;
    private String encodedemail;
    private Admin_All_Doctor_Adapter adapter ;
    private EditText search;
    private Toast backToast;
    private long backPressedTime;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private List<com.example.veterinarian_finpro.model.Doctors_Profile> listData;
    private List<String> emaildata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        search = findViewById(R.id.editTextSearch_appointment);
        Doctors_Session_Mangement doctors_session_mangement = new Doctors_Session_Mangement(this);
        encodedemail = doctors_session_mangement.getDoctorSession()[0].replace(".", ",");
        rv = (RecyclerView) findViewById(R.id.recycler_doc);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        emaildata = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(Admin_Activity.this, AskRoleActivity.class));
                        break;
                    case R.id.appointment_doc:
                        startActivity(new Intent(Admin_Activity.this, Admin_Activity.class));
                        break;
                    case R.id.payment_doc:
                        startActivity(new Intent(Admin_Activity.this, Admin_Payments.class));
                        break;
                    case R.id.chat:
                        startActivity(new Intent(Admin_Activity.this, MainActivity.class));
                        break;
                    case R.id.Add_doc:
                        startActivity(new Intent(Admin_Activity.this, Admin_addDoctorActivity.class));
                        break;
                    case R.id.logout:
                        Doctors_Session_Mangement doctors_session_mangement = new Doctors_Session_Mangement(Admin_Activity.this);
                        doctors_session_mangement.removeSession();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(Admin_Activity.this, AskRoleActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });
        navigationView.setCheckedItem(R.id.nav_home);
        //database
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Doctors_Data");
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData = new ArrayList<>();
                emaildata = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        String email = npsnapshot.getKey();
                        email = email.replace(",", ".");
                        Doctors_Profile l = npsnapshot.getValue(Doctors_Profile.class);
                        listData.add(l);
                        emaildata.add(email);
                    }

                    adapter = new Admin_All_Doctor_Adapter(listData, emaildata, Admin_Activity.this);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<com.example.veterinarian_finpro.model.Doctors_Profile> filterdNames = new ArrayList<>();
        for (com.example.veterinarian_finpro.model.Doctors_Profile doc_data: listData) {
            //if the existing elements contains the search input
            if (doc_data.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(doc_data);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);


    }
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            if(backPressedTime+2000>System.currentTimeMillis())
            {
                finishAffinity();
                backToast.cancel();
                super.onBackPressed();
                return;
            }
            else
            {
                backToast = Toast.makeText(getBaseContext(),"Press Back again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}
