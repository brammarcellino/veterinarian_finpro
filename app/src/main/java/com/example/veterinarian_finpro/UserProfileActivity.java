package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class UserProfileActivity extends AppCompatActivity {
private TextView textViewwelcome, textviewfulname,textViewemail,textViewDob,textViewGender,textViewMobile;
private ProgressBar progressBar;
private String fullname,email,dob,gender,mobile;
private ImageView imageView;
private FirebaseAuth auth;
private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        
        swipeTorefresh();

        textViewwelcome = findViewById(R.id.textView_show_welcome);
        textviewfulname = findViewById(R.id.textView_show_full_name);
        textViewemail = findViewById(R.id.textView_show_email);
        textViewDob = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
       imageView = findViewById(R.id.imageView_profile_dp);
       progressBar = findViewById(R.id.progress_bar);

        TextView textMobile = findViewById(R.id.textView_show_mobile);
        textMobile.setText(String.format("+62-%s", getIntent().getStringExtra(mobile)
                ));



       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent= new Intent(UserProfileActivity.this,UpdateProfilePictureActivity.class);
               startActivity(intent);
           }
       });


        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser==null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong User's details are not avalible at the moment", Toast.LENGTH_SHORT).show();


        }else {
            checkEmailVerify(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }



    }

    private void swipeTorefresh() {
        swipeContainer = findViewById(R.id.SwipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_red_light );

    }

    private void checkEmailVerify(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlerDialog();
        }
    }

    private void showAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Register users");

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);
                if (userDetails != null){
                   fullname = firebaseUser.getDisplayName();
                   email = firebaseUser.getEmail();
                   dob = userDetails.dob;
                   gender = userDetails.gender;
                   mobile = userDetails.mobile;


                   String phone=  mobile = userDetails.mobile;
                    Patient_Phone_No patientPhoneNo = new  Patient_Phone_No(phone);
                    Patient_Session_Management session_management = new Patient_Session_Management(UserProfileActivity.this);
                    session_management.saveSession(patientPhoneNo);




                   textViewwelcome.setText("welcome,"+fullname+"!");
                   Toast.makeText(UserProfileActivity.this, "Everything is correct!", Toast.LENGTH_SHORT).show();

                   textviewfulname.setText(fullname);
                   textViewemail.setText(email);
                   textViewDob.setText(dob);
                   textViewGender.setText(gender);
                   textViewMobile.setText(mobile);

                    //Uri uri = firebaseUser.getPhotoUrl();

                    //Picasso.get().load(uri).into(imageView);


                }else {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        if (id== android.R.id.home)
            NavUtils.navigateUpFromSameTask(UserProfileActivity.this);
        if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);

        }else if (id== R.id.menu_update_profile){
            Intent intent = new Intent(UserProfileActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(UserProfileActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(UserProfileActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            
        } else{
            Toast.makeText(UserProfileActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
