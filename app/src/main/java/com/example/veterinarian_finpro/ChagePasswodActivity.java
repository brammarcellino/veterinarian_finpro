package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChagePasswodActivity extends AppCompatActivity {
private FirebaseAuth auth;
private EditText edittextpwdcurr,editTextpwdnew,getedittextconfirmnew;
private TextView textViewauthenticated;
private Button buttonchangepwd,buttonReauthenticate;
private ProgressBar progressBar;
private String userpwdcurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chage_passwod);

        getSupportActionBar().setTitle("Change Password");
        editTextpwdnew = findViewById(R.id.editText_change_pwd_new);
        edittextpwdcurr = findViewById(R.id.editText_change_pwd_current);
        textViewauthenticated = findViewById(R.id.textView_change_pwd_authenticated);
        getedittextconfirmnew= findViewById(R.id.editText_confirm_pwd);
        progressBar = findViewById(R.id.progressBar);
        buttonchangepwd = findViewById(R.id.button_change_pwd);
        buttonReauthenticate = findViewById(R.id.button_change_pwd_authenticate);

        editTextpwdnew.setEnabled(false);
        getedittextconfirmnew.setEnabled(false);
        buttonchangepwd.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser.equals("")){
            Toast.makeText(ChagePasswodActivity.this, "Something went wrong user's details not avalible ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChagePasswodActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
            reAuthenticateUser(firebaseUser);

            }
        }



    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReauthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpwdcurr = edittextpwdcurr.getText().toString();
                if (TextUtils.isEmpty(userpwdcurr)){
                    Toast.makeText(ChagePasswodActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    edittextpwdcurr.setError("please enter your current password to authenticate");
                    edittextpwdcurr.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userpwdcurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               progressBar.setVisibility(View.GONE);

                               edittextpwdcurr.setEnabled(false);
                               editTextpwdnew.setEnabled(true);
                               getedittextconfirmnew.setEnabled(true);

                               buttonReauthenticate.setEnabled(false);
                               buttonchangepwd.setEnabled(true);

                               textViewauthenticated.setText("you are authenticated /verified you can change password now");
                               Toast.makeText(ChagePasswodActivity.this, "password has been verified"+"chage password now",Toast.LENGTH_SHORT).show();
                           buttonchangepwd.setBackgroundTintList(ContextCompat.getColorStateList(ChagePasswodActivity.this,R.color.Lime));

                           buttonchangepwd.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   chagepwd (firebaseUser);
                               }
                           });

                           }else {
                               try {
                                   throw task.getException();
                               }catch (Exception e){
                                   Toast.makeText(ChagePasswodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                           progressBar.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });
    }

    private void chagepwd(FirebaseUser firebaseUser) {
        String userpwdnew = editTextpwdnew.getText().toString();
        String userpwdconfirmnew = getedittextconfirmnew.getText().toString();
        if (TextUtils.isEmpty(userpwdnew)){
            Toast.makeText(ChagePasswodActivity.this, "New password is needed", Toast.LENGTH_SHORT).show();
            editTextpwdnew.setError("please enter your password");
            editTextpwdnew.requestFocus();
        }else  if(TextUtils.isEmpty(userpwdconfirmnew)){
            Toast.makeText(ChagePasswodActivity.this, "please confim", Toast.LENGTH_SHORT).show();
            getedittextconfirmnew.setError("please enter you new password");
            getedittextconfirmnew.requestFocus();
        } else  if (!userpwdnew.matches(userpwdconfirmnew)){
            Toast.makeText(ChagePasswodActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            getedittextconfirmnew.setError("please enter your new password");
            getedittextconfirmnew.requestFocus();
        } else if (userpwdcurr.matches(userpwdnew)){
            Toast.makeText(ChagePasswodActivity.this, "New password cannot same as old password", Toast.LENGTH_SHORT).show();
            editTextpwdnew.setError("please enter your new Password");
            editTextpwdnew.requestFocus();
        } else  {
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userpwdnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChagePasswodActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChagePasswodActivity.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try{
                            throw task.getException();
                            
                        }catch (Exception e){
                            Toast.makeText(ChagePasswodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
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
            Intent intent = new Intent(ChagePasswodActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(ChagePasswodActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(ChagePasswodActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(ChagePasswodActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } /*else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(UserProfileActivity.this,DeleteProfile.class);
            startActivity(intent);
        }*/ else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(ChagePasswodActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChagePasswodActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else{
            Toast.makeText(ChagePasswodActivity.this, "Somthing went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
