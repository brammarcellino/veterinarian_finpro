package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewautication;
    private  String useroldemail , usernewemail,userpwd;
    private Button buttonupdateemail;
    private EditText editTextnewemail,editTextnewpwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        getSupportActionBar().setTitle("Update Email");
        progressBar = findViewById(R.id.progressBar);
        editTextnewpwd = findViewById(R.id.editText_update_email_verify_password);
        editTextnewemail = findViewById( R.id.editText_update_email_new);
        textViewautication= findViewById(R.id.textView_update_email_authenticated);
        buttonupdateemail =findViewById(R.id.button_update_email);

        buttonupdateemail.setEnabled(false);
        editTextnewemail.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        useroldemail = firebaseUser.getEmail();
        TextView textviewoldEmail = findViewById(R.id.textView_update_email_old);
        textviewoldEmail.setText(useroldemail);

        if (firebaseUser.equals("")){
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong users details not availble", Toast.LENGTH_SHORT).show();
        } else {
            reAuthenticate(firebaseUser);
        }



    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
    Button buttonverifyuser = findViewById(R.id.button_authenticate_user);
    buttonverifyuser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        userpwd = editTextnewpwd .getText().toString();
        if (TextUtils.isEmpty(userpwd)){
            Toast.makeText(UpdateEmailActivity.this, "Password is needed to countinue", Toast.LENGTH_SHORT).show();
        editTextnewpwd.setError("please enter your password for aunthentication");
        editTextnewpwd.requestFocus();

        } else {
            progressBar.setVisibility(View.VISIBLE);

            AuthCredential credential = EmailAuthProvider.getCredential(useroldemail,userpwd);
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateEmailActivity.this, "Password has been verified you can update email now", Toast.LENGTH_SHORT).show();

                        textViewautication.setText("you are authentication you can update your email now");

                        editTextnewemail.setEnabled(true);
                        editTextnewpwd.setEnabled(false);
                        buttonverifyuser.setEnabled(false);
                        buttonupdateemail.setEnabled(true);

                        buttonupdateemail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,R.color.Lime));

                        buttonupdateemail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                usernewemail = editTextnewemail.getText().toString();
                                if (TextUtils.isEmpty(usernewemail)){
                                    Toast.makeText(UpdateEmailActivity.this, "New Email required", Toast.LENGTH_SHORT).show();
                                    editTextnewemail.setError("please enter new email");
                                    editTextnewemail.requestFocus();
                                } else  if (!Patterns.EMAIL_ADDRESS.matcher(usernewemail).matches()){
                                    Toast.makeText(UpdateEmailActivity.this, "please enter invalid email", Toast.LENGTH_SHORT).show();
                                    editTextnewemail.setError("please provide valid email");
                                    editTextnewemail.requestFocus();

                                } else  if(useroldemail.matches(usernewemail)){
                                    Toast.makeText(UpdateEmailActivity.this, "new email cannot be same as old email", Toast.LENGTH_SHORT).show();
                                    editTextnewemail.setError("please enter new email");
                                    editTextnewemail.requestFocus();

                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    updateEmail(firebaseUser);

                                }
                            }
                        });

                    }else {
                        try {
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        }
    });

    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(usernewemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UpdateEmailActivity.this, "Email has been update please Verify", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();
                } else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);

        }else if (id== R.id.menu_update_profile){
            Intent intent = new Intent(UpdateEmailActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(UpdateEmailActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(UpdateEmailActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(UpdateEmailActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(UpdateEmailActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else{
            Toast.makeText(UpdateEmailActivity.this, "Somthing went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}