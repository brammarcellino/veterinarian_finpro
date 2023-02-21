package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail ,edittextpwd;
    private  TextView textViewRegister;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    private static  String TAG = "LoginActivate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        getSupportActionBar().setTitle("Login");
        editTextEmail = findViewById(R.id.editText_login_email);
        edittextpwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);
        textViewRegister= findViewById(R.id.textView_register);

        auth = FirebaseAuth.getInstance();
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "you can register now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,RegiterActivity.class));
            }
        });

        TextView textViewforgetPassword = findViewById(R.id.textView_forgot_password);
        textViewforgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "You can reset your Passwod now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        ImageView imageView = findViewById(R.id.imageView_show_hide_pwd);

        imageView.setImageResource(R.drawable.ic_hide_pwd);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittextpwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edittextpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.ic_hide_pwd);
                }else {
                    edittextpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageView.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });


        auth = FirebaseAuth.getInstance();

        Button buttonlogin = findViewById(R.id.button_login);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String  textemail = editTextEmail.getText().toString();
               String textpwd = edittextpwd.getText().toString();

               if (TextUtils.isEmpty(textemail)){
                   Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                   editTextEmail.setError("email is required");
                   editTextEmail.requestFocus();

               }else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()){
                   Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                   editTextEmail.requestFocus();
               }else if(TextUtils.isEmpty(textpwd)) {
                   Toast.makeText(LoginActivity.this, "please enter your password", Toast.LENGTH_SHORT).show();
                   edittextpwd.setError("password is required");
                   edittextpwd.requestFocus();

                } else {
                   progressBar.setVisibility(View.VISIBLE);
                   loginuser(textemail,textpwd);
               }
            }
        });

        
    }

    private void loginuser(String email, String pwd) {
        auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "your are logged in now", Toast.LENGTH_SHORT).show();

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "you are logged in now", Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        auth.signOut();
                        showAletDialog();
                    }


                } else {
                    try {
                        throw  task.getException();

                    }catch (FirebaseAuthInvalidUserException e){
                        editTextEmail.setError("user does not exists or is no longer valid .please regiser again");
                        editTextEmail.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextEmail.setError("Invalid credentials kindly ,check and re-enter");
                        editTextEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    Toast.makeText(LoginActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }

        });
    }

    private void showAletDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not Verify");
        builder.setMessage("plese verify your email now .you can not without email verification ");
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!= null){
            Toast.makeText(LoginActivity.this, "Already logged in!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
        finish();
        }
        else  {
            Toast.makeText(this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}