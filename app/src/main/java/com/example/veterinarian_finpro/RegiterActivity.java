package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegiterActivity extends AppCompatActivity {
    private EditText editTextRegistername,EditTextRegisterEmail,EditTextRegisterDob,EditTextRegisterMobile,
    EditTextRegisterPwd,editTextregisterConfirmPwd;
    private ProgressBar progressBar;
    private  DatePickerDialog picker;
    private RadioGroup radioGroupregisterGender;
    private RadioButton radioButtonregitergender;
    private  static  final  String TAG = "RegiterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter);



        Toast.makeText(RegiterActivity.this, "You can Register now", Toast.LENGTH_SHORT).show();
        progressBar = findViewById(R.id.progressBar);
        editTextRegistername = findViewById(R.id.editText_register_full_name);
        EditTextRegisterEmail = findViewById(R.id.editText_register_email);
        EditTextRegisterDob = findViewById(R.id.editText_register_dob);
        EditTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextregisterConfirmPwd = findViewById(R.id.editText_confim);
        EditTextRegisterPwd = findViewById(R.id.editText_register_password);

        radioGroupregisterGender =  findViewById(R.id.radio_group_register_gender);
        radioGroupregisterGender.clearCheck();

        EditText editnumber=findViewById(R.id.editText_register_mobile);
        editnumber.setText(String.format(
                "+62-%s", getIntent().getStringExtra("mobile")
        ));



        EditTextRegisterDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar .get(Calendar.DAY_OF_MONTH);
                int mounth = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(RegiterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditTextRegisterDob.setText(dayOfMonth+"/"+ (month+1)+"/"+year);



                    }

                },year,mounth,day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedgenderId= radioGroupregisterGender.getCheckedRadioButtonId();
                radioButtonregitergender = findViewById(selectedgenderId);


                String textfullname = editTextRegistername.getText().toString();
                String textemail = EditTextRegisterEmail.getText().toString();
                String textmobile = EditTextRegisterMobile.getText().toString();
                String textpwd = EditTextRegisterPwd.getText().toString();
                String textconfirmpwd = editTextregisterConfirmPwd.getText().toString();
                String textDob = EditTextRegisterDob.getText().toString();
                String textgender;

                String mobileRegex = "[6-9][0-12]{12}";
                Matcher mobilematcher;
                Pattern pattern  = Pattern.compile(mobileRegex);
                mobilematcher = pattern.matcher(textmobile);


                if (TextUtils.isEmpty(textfullname)){
                    Toast.makeText(RegiterActivity.this, "please enter your name", Toast.LENGTH_SHORT).show();
                editTextRegistername.setError("name is required");
                editTextRegistername.requestFocus();

                }else if (TextUtils.isEmpty(textemail)){
                    Toast.makeText(RegiterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    EditTextRegisterEmail.setError("Email required");
                    EditTextRegisterEmail.requestFocus();


                }else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()){
                    Toast.makeText(RegiterActivity.this, "please re-enter your email", Toast.LENGTH_SHORT).show();
                    EditTextRegisterEmail.setError("Email is required");
                    EditTextRegisterEmail.requestFocus();

                }else if (TextUtils.isEmpty(textDob)){
                    Toast.makeText(RegiterActivity.this, "Please your date of birth", Toast.LENGTH_SHORT).show();
                    EditTextRegisterDob.setError("Date of Birth is required");
                    EditTextRegisterDob.requestFocus();

                }else if (radioGroupregisterGender.getCheckedRadioButtonId()==-1){
                    Toast.makeText(RegiterActivity.this, "Please enter your gender", Toast.LENGTH_SHORT).show();
                    radioButtonregitergender.setError("gender is required");
                    radioButtonregitergender.requestFocus();

                }else if (TextUtils.isEmpty(textmobile)){
                    Toast.makeText(RegiterActivity.this, "Mobile number is required", Toast.LENGTH_SHORT).show();
                   EditTextRegisterMobile.setError("mobile Number is Required");
                    EditTextRegisterMobile.requestFocus();

                }else if (textmobile.length()!=12){
                    Toast.makeText(RegiterActivity.this, "please enter re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    EditTextRegisterMobile.setError("Mobile number should be 10 digits");
                    EditTextRegisterMobile.requestFocus();
                } else if (mobilematcher.find()){
                    Toast.makeText(RegiterActivity.this, "please enter re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    EditTextRegisterMobile.setError("Mobile number is required");
                    EditTextRegisterMobile.requestFocus();

                }

                else  if (TextUtils.isEmpty(textpwd)){
                    Toast.makeText(RegiterActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    EditTextRegisterPwd.setError("password is required");
                    EditTextRegisterPwd.requestFocus();

                } else if (textpwd.length() <6 ){
                    Toast.makeText(RegiterActivity.this, "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    EditTextRegisterPwd.setError("password too weak ");
                    EditTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textconfirmpwd)){
                    Toast.makeText(RegiterActivity.this, "password confirm is required", Toast.LENGTH_SHORT).show();
                    editTextregisterConfirmPwd.setError("Password Confirm is Required");
                    editTextregisterConfirmPwd.requestFocus();

                }else if (!textpwd.equals(textconfirmpwd)){
                    Toast.makeText(RegiterActivity.this, "please same password ", Toast.LENGTH_SHORT).show();
                    editTextregisterConfirmPwd.setError("password Confirmation is required");
                    editTextregisterConfirmPwd.requestFocus();

                    EditTextRegisterPwd.clearComposingText();
                    editTextregisterConfirmPwd.clearComposingText();
                } else {
                    textgender = radioButtonregitergender.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    
                    registeruser(textfullname,textemail,textDob,textmobile,textpwd,textgender);
                }


            }
        });

    }

    private void registeruser(String textfullname, String textemail, String textDoB, String textmobile, String textpwd,String textgender) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textemail,textpwd).addOnCompleteListener(RegiterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegiterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    UserDetails userDetails  = new UserDetails (textDoB,textgender,textmobile,textfullname);

                    firebaseUser.sendEmailVerification();
                    UserProfileChangeRequest userProfileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(textfullname).build();
                    firebaseUser.updateProfile(userProfileChangeRequest);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Register users");

                    reference.child(firebaseUser.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegiterActivity.this, "User register Successfully .please verivy your email", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegiterActivity.this , UserProfileActivity.class);
                                intent.putExtra("mobile", EditTextRegisterMobile.getText().toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                            } else  {
                                Toast.makeText(RegiterActivity.this, "User registered failed .please verify your email", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                } else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e ){
                       EditTextRegisterPwd.requestFocus();
                        EditTextRegisterPwd.setError("your password is too weak .kindly use a mix of alphabets, numbers and special characters");
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        EditTextRegisterPwd.setError("your email is invalid or already in use .kindly re-enter");
                        EditTextRegisterPwd.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e ){
                        EditTextRegisterPwd.setError("user is already registered with this email.use another email");
                        EditTextRegisterPwd.requestFocus();

                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegiterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

        });

    }
}