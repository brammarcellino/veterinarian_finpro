package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteProfileActivity extends AppCompatActivity {
private FirebaseAuth auth;
private FirebaseUser firebaseUser;
private EditText edittextuserpwd;
private TextView textviewAuthhenticate;
private ProgressBar progressBar;
private  String userpwd;
private Button buttonReAuthhenticate ,buttonDeleteUser;
private  static final  String TAG = "DeleteProofileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);
        getSupportActionBar().setTitle("Delete your profile");

        progressBar = findViewById(R.id.progressBar);
        edittextuserpwd = findViewById(R.id.editText_delete_user_pwd);
        textviewAuthhenticate= findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser = findViewById(R.id.button_delete_user);
        buttonReAuthhenticate = findViewById(R.id.button_delete_user_authenticate);

        buttonDeleteUser .setEnabled(false);
        auth = FirebaseAuth.getInstance();
        firebaseUser =  auth.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(DeleteProfileActivity.this, "Somthing went Wrong"+"User Details are not available at the moment ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();

        } else {
            reAuthenticateUser(firebaseUser);
        }



    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthhenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpwd = edittextuserpwd.getText().toString();
                if (TextUtils.isEmpty(userpwd)){
                    Toast.makeText(DeleteProfileActivity.this, "Passwor is needed", Toast.LENGTH_SHORT).show();
                    edittextuserpwd.setError("please enter your current password to authenticate");
                    edittextuserpwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userpwd);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                edittextuserpwd.setEnabled(false);
                                buttonReAuthhenticate.setEnabled(false);
                                buttonDeleteUser.setEnabled(true);

                                textviewAuthhenticate.setText("you are authenticated /verified you can delete your profile and related data now");
                                Toast.makeText(DeleteProfileActivity.this, "password has been verified"+"chage password now",Toast.LENGTH_SHORT).show();
                               buttonDeleteUser .setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfileActivity.this,R.color.Lime));

                                buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                       showAlertDialog();
                                    }
                                });

                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete user amd related data");
        builder.setMessage("plese verify your email now .you can not without email verification ");
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               deleteUserData(firebaseUser);
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
               alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });
    }

    private void deleteuser() {
        if (firebaseUser.getPhotoUrl()!= null){
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"OnSuccess:Photo Deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,e.getMessage());
                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    auth.signOut();
                    Toast.makeText(DeleteProfileActivity.this, "user has deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteProfileActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void deleteUserData(FirebaseUser firebaseUser) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Onsuccess:photo Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.getMessage());
                Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
              Log.d(TAG,"Onsuccess:User Data Deleted") ;

              deleteuser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Log.d(TAG,"OnSuccess :User Data Deleted");
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
            Intent intent = new Intent(DeleteProfileActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(DeleteProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(DeleteProfileActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(DeleteProfileActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(DeleteProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(DeleteProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteProfileActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else{
            Toast.makeText(DeleteProfileActivity.this, "Somthing went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}