package com.example.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Proposal_Back extends AppCompatActivity {
    private FragmentPagerAdapter fragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        ViewPager viewPager = findViewById(R.id.vp2);
        fragmentPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        TabLayout tab_Layout = findViewById(R.id.tab_Layout2);
        viewPager.setAdapter(fragmentPagerAdapter);
        tab_Layout.setupWithViewPager(viewPager);
    }
}
