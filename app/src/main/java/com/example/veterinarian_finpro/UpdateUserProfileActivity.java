package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateUserProfileActivity extends AppCompatActivity {
    private EditText editTextupdateName, editTextupdateDob, editTextupdatemobile;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonGenderselected;
    private String textfullname, textDob, textGender, textMobile;
    private FirebaseAuth auth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        getSupportActionBar().setTitle("update profile details");
        progressBar = findViewById(R.id.progressBar);
        editTextupdateName = findViewById(R.id.editText_update_profile_name);
        editTextupdatemobile = findViewById(R.id.editText_update_profile_mobile);
        radioGroupGender = findViewById(R.id.radio_group_update_profile_gender);
        editTextupdateDob = findViewById(R.id.editText_update_profile_dob);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        showProfile(firebaseUser);
        TextView textViewupdateEmail = findViewById(R.id.textView_profile_update_email);
        textViewupdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateUserProfileActivity.this,UpdateEmailActivity.class);
              startActivity(intent);
              finish();
            }
        });


        TextView textViewUploadPic = findViewById(R.id.textView_profile_upload_pic);
        textViewUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateUserProfileActivity.this, UpdateProfilePictureActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editTextupdateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSADob[] = textDob.split("/");


                int day = Integer.parseInt(textSADob[0]);
                int mounth = Integer.parseInt(textSADob[1])-1;
                int year = Integer.parseInt(textSADob[2]);
                DatePickerDialog picker;
                picker = new DatePickerDialog(UpdateUserProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextupdateDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);


                    }

                }, year, mounth, day);
                picker.show();
            }
        });
        Button buttonupdateprofile = findViewById(R.id.button_update_profile);
        buttonupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedgenderid = radioGroupGender.getCheckedRadioButtonId();
        radioButtonGenderselected = findViewById(selectedgenderid);
        String mobileRegex = "[6-9][0-9]{9}";
        Matcher mobilematcher;
        Pattern pattern  = Pattern.compile(mobileRegex);
        mobilematcher = pattern.matcher(textMobile);


        if (TextUtils.isEmpty(textfullname)){
            Toast.makeText(UpdateUserProfileActivity.this, "please enter your name", Toast.LENGTH_SHORT).show();
            editTextupdatemobile.setError("name is required");
            editTextupdatemobile.requestFocus();
        }else if (TextUtils.isEmpty(textDob)){
            Toast.makeText(UpdateUserProfileActivity.this, "Please your date of birth", Toast.LENGTH_SHORT).show();
            editTextupdateDob.setError("Date of Birth is required");
            editTextupdateDob.requestFocus();

        }else if (TextUtils.isEmpty(radioButtonGenderselected.getText())){
            Toast.makeText(UpdateUserProfileActivity.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
            radioButtonGenderselected.setError("gender is required");
            radioButtonGenderselected.requestFocus();
        }else if (TextUtils.isEmpty(textMobile)){
            Toast.makeText(UpdateUserProfileActivity.this, "Mobile number is required", Toast.LENGTH_SHORT).show();
            editTextupdatemobile.requestFocus();

        }else if (textMobile.length()!=9){
            Toast.makeText(UpdateUserProfileActivity.this, "please enter re-enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextupdatemobile.setError("Mobile number should be 10 digits");
            editTextupdatemobile.requestFocus();
        } else if (mobilematcher.find()){
            Toast.makeText(UpdateUserProfileActivity.this, "please enter re-enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextupdatemobile.setError("Mobile number is required");
            editTextupdatemobile.requestFocus();

        } else {
            textGender = radioButtonGenderselected.getText().toString();
            textfullname = editTextupdateName.getText().toString();
            textDob = editTextupdateDob.getText().toString();
            textMobile = editTextupdatemobile.getText().toString();

            UserDetails userDetails= new UserDetails(textDob,textMobile,textMobile,textfullname);
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userId= firebaseUser.getUid();
            reference.child(userId).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textfullname).build();
                        firebaseUser.updateProfile(profileChangeRequest);
                        Toast.makeText(UpdateUserProfileActivity.this, "update succesfull", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(UpdateUserProfileActivity.this,UpdateUserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try {
                            throw task.getException();

                        }catch (Exception e){
                            Toast.makeText(UpdateUserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });


        }

    }

    private void showProfile (FirebaseUser firebaseUser){
            String useridRegistered = firebaseUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            progressBar.setVisibility(View.VISIBLE);
            reference.child(useridRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserDetails userDetails = snapshot.getValue(UserDetails.class);
                    if (userDetails != null) {
                        textfullname = firebaseUser.getDisplayName();
                        textDob = userDetails.dob;
                        textMobile = userDetails.gender;
                        editTextupdateName.setText(textfullname);
                        editTextupdateDob.setText(textDob);
                        editTextupdatemobile.setText(textMobile);

                        if (textGender.equals("male")) {
                            radioButtonGenderselected = findViewById(R.id.radio_male);
                        } else {
                            radioButtonGenderselected = findViewById(R.id.radio_female);
                        }
                        radioButtonGenderselected.setChecked(true);

                    } else {
                        Toast.makeText(UpdateUserProfileActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
            Intent intent = new Intent(UpdateUserProfileActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(UpdateUserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(UpdateUserProfileActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(UpdateUserProfileActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(UpdateUserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(UpdateUserProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateUserProfileActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else{
            Toast.makeText(UpdateUserProfileActivity.this, "Somthing went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}

