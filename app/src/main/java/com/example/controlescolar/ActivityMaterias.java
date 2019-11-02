package com.example.controlescolar;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ActivityMaterias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);

        ViewPager viewPager = findViewById(R.id.viewPager);
        FragmentPagerAdapter adaptadorVP = new FragmentPagerAdapterEstudiantes(this, getSupportFragmentManager(), this);
        viewPager.setAdapter(adaptadorVP);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
