package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_addDoctorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView registerText,registerUser;
    private EditText editTextFullName,editTextEmail,editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_doctor);
        progressBar=(ProgressBar) findViewById(R.id.progressbar_doc_add);
        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.registerUser);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }
        });
    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(fullName.isEmpty())
        {
            editTextFullName.setError("Full Name is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextFullName.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide Valid Email !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is a required field !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Minimum length of password should be 6 characters !");
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.requestFocus();
            return;
        }
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Users user = new Users(fullName,email, "offline", "Doctor");
                            String encodedemail = EncodeString(email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(encodedemail)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {    progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(Admin_addDoctorActivity.this,"Doctor has been registered successfully !",Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(Admin_addDoctorActivity.this,activity_doctor_update_profileActivity.class));
                                            }
                                            else
                                            {   progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(Admin_addDoctorActivity.this,"Failed to Register. Doctor already exists !",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {   progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Admin_addDoctorActivity.this,"Failed to Register. Doctor already exists !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    }
