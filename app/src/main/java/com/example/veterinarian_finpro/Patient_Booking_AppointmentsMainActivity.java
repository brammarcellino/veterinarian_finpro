package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.veterinarian_finpro.model.Appointment_notif;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Patient_Booking_AppointmentsMainActivity extends AppCompatActivity {
    private String mobile,email,date_val,chosen_time="",question_data,fees;
    private TextView doctor_name, speciality;
    private HorizontalCalendar horizontalCalendar;
    private DatabaseReference reference_user, reference_doctor, reference_booking, reference_patient, reference_details, reference_doctor_appt, reference_payment;
    private ArrayList<String> dates;
    private CircleImageView circle_image;
    private Doctor_Images doctor_images;
    private TextInputLayout time_layout;
    private AutoCompleteTextView time_view;
    private ArrayAdapter<String> time_adapter;
    private EditText question,patient_name, tid;
    private Button book_app;
    private Set<String> set_timeSlot;
    private TextView paymentLink;
    private boolean isresumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_booking_appointments_main);
        circle_image = (CircleImageView) findViewById(R.id.profile_image);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        speciality = (TextView) findViewById(R.id.doctor_speciality);

        reference_doctor = FirebaseDatabase.getInstance().getReference("Doctors_Data");
        reference_doctor_appt = FirebaseDatabase.getInstance().getReference("Doctors_Appointments");
        dates = new ArrayList<>();
        set_timeSlot=new HashSet<String>();
        time_layout = (TextInputLayout) findViewById(R.id.timeLayout);
        time_view = (AutoCompleteTextView) findViewById(R.id.timeview);
        question = (EditText) findViewById(R.id.questioninput);
        book_app = (Button) findViewById(R.id.book_button);
       
        patient_name = (EditText) findViewById(R.id.name_patient_input);
        email = getIntent().getSerializableExtra("Email ID").toString();
        email = email.replace(".", ",");
        Patient_Session_Management session = new Patient_Session_Management(Patient_Booking_AppointmentsMainActivity.this);
        mobile = session.getSession();
        isresumed = true;

        reference_booking = FirebaseDatabase.getInstance().getReference("Doctors_Chosen_Slots");
        reference_patient = FirebaseDatabase.getInstance().getReference("Patient_Chosen_Slots");
        reference_details = FirebaseDatabase.getInstance().getReference("Register users");
        reference_payment = FirebaseDatabase.getInstance().getReference("Admin_Payment");

        reference_doctor.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    doctor_name.setText(snapshot.child("name").getValue(String.class));
                    speciality.setText(snapshot.child("desc").getValue(String.class));
                    doctor_images = snapshot.child("doc_pic").getValue(Doctor_Images.class);
                    fees = snapshot.child("fees").getValue(String.class);
                    if (doctor_images != null) {
                        Picasso.get().load(doctor_images.getUrl()).into(circle_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        String[] monthName = {"Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};
        date_val = DateFormat.format("dd MMM yyyy", startDate).toString();
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.date_View)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();


        reference_booking.child(email).child(date_val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dates = new ArrayList<>();
                chosen_time = "";
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!(dataSnapshot.getKey().equals("Count"))) {
                            int start = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[0]);
                            int end = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[1]);
                            int start_time = start;
                            int slots = (end - start) * 2;
                            String date = "";
                            String check = "";
                            for (int i = 0; i < slots; i++) {
                                if (i % 2 == 0) {
                                    date = start + ":00 - " + start + ":30";
                                    check = start + ":00";
                                } else {
                                    date = start + ":30";
                                    check = date;
                                    start += 1;
                                    date = date + " - " + start + ":00";
                                }
                                String finalDate = date;
                                String complete_slot = start_time + " - " + end;
                                set_timeSlot.add(complete_slot);
                                reference_booking.child(email).child(date_val).child(complete_slot).child(check).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Booking_Appointments booking = snapshot.getValue(Booking_Appointments.class);
                                            int val = booking.getValue();
                                            if (val == 0) {
                                                dates.add(finalDate);
                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                } else {
                    if (dates.size() == 0 && isIsresumed()) {
                        Toast.makeText(Patient_Booking_AppointmentsMainActivity.this, "No Slots Available!", Toast.LENGTH_SHORT).show();
                    }
                }
                time_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, dates);
                time_view.setAdapter(time_adapter);
                time_view.setThreshold(1);
                time_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosen_time = time_adapter.getItem(position);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                date_val = DateFormat.format("dd MMM yyyy", date).toString();
                chosen_time = "";
                time_view.setText("");
        reference_booking.child(email).child(date_val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dates = new ArrayList<>();
                set_timeSlot=new HashSet<String>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!(dataSnapshot.getKey().equals("Count"))) {
                            int start = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[0]);
                            int end = Integer.parseInt(dataSnapshot.getKey().split(" - ", 5)[1]);
                            int start_time = start;
                            int slots = (end - start) * 2;
                            String date = "";
                            String check = "";
                            String complete_slot = start_time + " - " + end;
                            set_timeSlot.add(complete_slot);
                            for (int i = 0; i < slots; i++) {
                                if (i % 2 == 0) {
                                    date = start + ":00 - " + start + ":30";
                                    check = start + ":00";
                                } else {
                                    date = start + ":30";
                                    check = date;
                                    start += 1;
                                    date = date + " - " + start + ":00";
                                }
                                String finalDate = date;
                                reference_booking.child(email).child(date_val).child(complete_slot).child(check).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Booking_Appointments booking = snapshot.getValue(Booking_Appointments.class);
                                            int val = booking.getValue();
                                            if (val == 0) {
                                                dates.add(finalDate);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                }
                else{
                    if(dates.size() == 0 && isIsresumed()) {
                        Toast.makeText(Patient_Booking_AppointmentsMainActivity.this, "No Slots Available!", Toast.LENGTH_SHORT).show();
                    }
                }
                time_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_gender, dates );
                time_view.setAdapter(time_adapter);
                time_view.setThreshold(1);
                time_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosen_time = time_adapter.getItem(position);
                    }
                });

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            }
        });
        ///ini funcituon q
        book_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(chosen_time.equals("")){
                            Toast.makeText(Patient_Booking_AppointmentsMainActivity.this, "Please Select the Time Slot!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        question_data = question.getText().toString().trim();
                        String pname = patient_name.getText().toString().trim();

                        if(pname.isEmpty()){
                            patient_name.setError("Patient's Name is a required field");
                            patient_name.requestFocus();
                            return;
                        }
                        String check = chosen_time.split(" - ", 5)[0];
                        Booking_Appointments booking_appointments = new Booking_Appointments(1, mobile);
                        String slot_val="";
                        for(String item: set_timeSlot){
                            int s = Integer.parseInt(item.split(" - ",5)[0]);
                            int e = Integer.parseInt(item.split(" - ",5)[1]);
                            int t = Integer.parseInt(chosen_time.split(":",5)[0]);

                            if(s <= t && t < e){
                                slot_val=item;
                                break;
                            }
                        }
                        Booking_Appointments booking = new Booking_Appointments(1, mobile);
                        String finalSlot_val = slot_val;
                        reference_booking.child(email).child(date_val).child(slot_val).child(check).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    Booking_Appointments booking = snapshot.getValue(Booking_Appointments.class);

                                    if (booking.getValue() == 1) {
                                        Toast.makeText(Patient_Booking_AppointmentsMainActivity.this, "The Slot is already Booked. Please Choose other Slot!", Toast.LENGTH_SHORT).show();
                                        return;

                                    } else {
                                        Booking_Appointments booking_appointments = new Booking_Appointments(1, mobile);
                                        reference_booking.child(email).child(date_val).child(finalSlot_val).child(check).setValue(booking_appointments);
                                        Intent intent = new Intent(Patient_Booking_AppointmentsMainActivity.this, Patient_Payment_Appt.class);
                                        intent.putExtra("pname", pname);
                                        intent.putExtra("email", email);
                                        intent.putExtra("chosen_time", chosen_time);
                                        intent.putExtra("question", question_data);
                                        intent.putExtra("slot_val", finalSlot_val);
                                        intent.putExtra("date", date_val);
                                        intent.putExtra("fees", fees);
                                        intent.putExtra("check", check);
                                        startActivity(intent);
                                    }
                                }
                                else {
                                    Toast.makeText(Patient_Booking_AppointmentsMainActivity.this, "the Doctor is not available! select other slot!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                       // Appointment_notif appointment_notif = new Appointment_notif("", date_val, chosen_time, question_data, mobile, pname);
                       // reference_doctor_appt.child(email).child(date_val).child(chosen_time).setValue(appointment_notif);
                    }
                });

            }
    @Override
    protected void onResume() {
        super.onResume();
        isresumed = true;

    }
    @Override
    protected void onPause() {
        super.onPause();
        isresumed = false;
    }

    private boolean isIsresumed() {
        return isresumed;
    }
}