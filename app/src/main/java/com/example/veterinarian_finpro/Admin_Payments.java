package com.example.veterinarian_finpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.veterinarian_finpro.Adapter.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Admin_Payments extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payments);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        Toast.makeText(Admin_Payments.this, "Swipe to mark the payment!", Toast.LENGTH_LONG).show();
        getTabs();
    }

    private void getTabs() {
        final FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                fragmentPagerAdapter.addFragment(Admin_Payments_Previous.getInstance(),"Completed");
                fragmentPagerAdapter.addFragment(Admin_Payments_Current.getInstance(),"Upcoming");

                viewPager.setAdapter(fragmentPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}