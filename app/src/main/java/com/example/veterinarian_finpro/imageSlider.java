package com.example.veterinarian_finpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class imageSlider extends SliderViewAdapter<imageSlider.Holder> {
    int [] image;
    public imageSlider(int[]image){
        this.image = image;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slideritem,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(image[position]);

    }

    @Override
    public int getCount() {
        return image.length;
    }

    public  class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public  Holder(View view){
            super(view);
            imageView = view.findViewById(R.id.image_view);

        }
}
}
