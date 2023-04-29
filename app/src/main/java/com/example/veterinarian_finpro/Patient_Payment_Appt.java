package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Patient_Payment_Appt extends AppCompatActivity {
    private String check,date_val,fees,mobile,email,chosen_time,question_data,pname,slot_val,fees_val;
    private TextView fees_show;
    private EditText tid;
    private DatabaseReference reference_user, reference_doctor, reference_booking, reference_patient, reference_details, reference_doctor_appt, reference_payment;
    private Button pay_app;
    private TextView paymentLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_payment_appt2);
        reference_doctor = FirebaseDatabase.getInstance().getReference("Doctors_Data");
        reference_doctor_appt = FirebaseDatabase.getInstance().getReference("Doctors_Appointments");
        reference_booking = FirebaseDatabase.getInstance().getReference("Doctors_Chosen_Slots");
        reference_patient = FirebaseDatabase.getInstance().getReference("Patient_Chosen_Slots");
        reference_details = FirebaseDatabase.getInstance().getReference("Register users");
        reference_payment = FirebaseDatabase.getInstance().getReference("Admin_Payment");
        Patient_Session_Management session = new Patient_Session_Management(Patient_Payment_Appt.this);
        mobile = session.getSession();
        pname = getIntent().getSerializableExtra("pname").toString();
        email = getIntent().getSerializableExtra("email").toString();
        chosen_time = getIntent().getSerializableExtra("chosen_time").toString();
        question_data = getIntent().getSerializableExtra("question").toString();
        slot_val = getIntent().getSerializableExtra("slot_val").toString();
        date_val = getIntent().getSerializableExtra("date").toString();
        fees_val = getIntent().getSerializableExtra("fees").toString();
        check = getIntent().getSerializableExtra("check").toString();
        tid = (EditText) findViewById(R.id.paymentsinput);
        fees_show = (TextView) findViewById(R.id.fees);
        pay_app = (Button) findViewById(R.id.book_button);
        paymentLink = (TextView) findViewById(R.id.linkPayment);
        fees_show.setText("Please Pay Rp. " + fees_val);

        paymentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_Payment_Appt.this,MainActivity.class);
                startActivity(intent);
            }
        });

        pay_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String transactionid = tid.getText().toString().trim();
                if (transactionid.isEmpty()) {
                   tid.setError("Transaction ID is a required field");
                    tid.requestFocus();
                   return;
                }
               String finalSlot_val = slot_val;

                reference_booking.child(email).child(date_val).child(slot_val).child("Count").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int count = snapshot.getValue(Integer.class);
                            count = count + 1;
                            Booking_Appointments booking_appointments = new Booking_Appointments(1, mobile);
                            //db chose doctor
                            reference_booking.child(email).child(date_val).child(slot_val).child(check).setValue(booking_appointments);
                            reference_booking.child(email).child(date_val).child(finalSlot_val).child("Count").setValue(count);
                            Patient_Chosen_Slot_Class patient = new Patient_Chosen_Slot_Class(chosen_time,0,question_data,pname,0,transactionid);
                          reference_patient.child(mobile).child(email).child(date_val).child(chosen_time).setValue(patient);
                            reference_doctor.child(email).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String dname = snapshot.getValue(String.class);
                                        Admin_Payment_Class payment = new Admin_Payment_Class(transactionid,dname,email,mobile,pname,0,date_val,chosen_time,0);
                                    reference_payment.child("Payment0").child(mobile).child(date_val).child(chosen_time).setValue(payment);
                                        Toast.makeText(Patient_Payment_Appt.this, "Payment Done! Please Wait for Confirmation!", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(Patient_Payment_Appt.this, HomeActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(Patient_Payment_Appt.this, "The Doctor is not available! Select other slot with the same transaction ID!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Patient_Payment_Appt.this, Patient_Booking_AppointmentsMainActivity.class);
                            intent.putExtra("Email ID", email);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Booking_Appointments booking_appointments = new Booking_Appointments(0, "null");
        reference_booking.child(email).child(date_val).child(slot_val).child(check).setValue(booking_appointments);
        startActivity(new Intent(Patient_Payment_Appt.this, HomeActivity.class));
    }
}


