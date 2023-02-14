package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {
private Button buttonreset;
private EditText editTextpwdResetEmail;
private ProgressBar progressBar;
private FirebaseAuth auth;
private final static String TAG = "Forgot Password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextpwdResetEmail = findViewById(R.id.editText_password_reset_email);
        buttonreset = findViewById(R.id.button_password_reset);
        progressBar = findViewById(R.id.progressBar);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot password");

   buttonreset.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           String email = editTextpwdResetEmail.getText().toString();
      if (TextUtils.isEmpty(email)){
          Toast.makeText(ForgotPasswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
          editTextpwdResetEmail.setText("Email is required");
          editTextpwdResetEmail.requestFocus();
      } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
          Toast.makeText(ForgotPasswordActivity.this, "please enter valid email", Toast.LENGTH_SHORT).show();
          editTextpwdResetEmail.setError("valid email is required");
          editTextpwdResetEmail.requestFocus();
      } else{
          progressBar.setVisibility(View.VISIBLE);
          resetpassword(email);
      }
       }
   });
    }

    private void resetpassword(String email) {
        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        editTextpwdResetEmail.setError("User does not exists or isno longer valid .please register again");
                    } catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                    }

                }
                progressBar.setVisibility(View.GONE);
             }
        });
    }
}