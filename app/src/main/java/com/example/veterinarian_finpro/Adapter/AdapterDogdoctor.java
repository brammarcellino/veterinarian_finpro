package com.example.veterinarian_finpro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.veterinarian_finpro.R;
import com.example.veterinarian_finpro.model.Modeldoctordog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterDogdoctor extends FirebaseRecyclerAdapter<Modeldoctordog, AdapterDogdoctor.myViewHolder> {

    public AdapterDogdoctor(@NonNull FirebaseRecyclerOptions<Modeldoctordog> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Modeldoctordog model) {
            holder.nama_doctor.setText("Name_Doctor : " + model.getName_Doctor());
            holder.Contact.setText("Contact : " + model.getContact());
            holder.fee.setText("Fee : " + model.getFee());

            Glide.with(holder.imgDoctor.getContext())
                    .load(model.getDoctor_Picture())
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imgDoctor);
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_adapter2, parent, false);
            return new myViewHolder(view);
        }

        class myViewHolder extends RecyclerView.ViewHolder {

            ImageView imgDoctor;
            TextView nama_doctor, Contact, fee;

            public myViewHolder(View itemView) {
                super(itemView);

                nama_doctor = itemView.findViewById(R.id.nameTextView);
                Contact = itemView.findViewById(R.id.emailTextView);
                imgDoctor = itemView.findViewById(R.id.imageView_doc);

            }
        }
    }


