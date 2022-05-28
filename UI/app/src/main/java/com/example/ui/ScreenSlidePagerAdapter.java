package com.example.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
    public ScreenSlidePagerAdapter(@NonNull FragmentManager fa) {
        super(fa);
    }
    private static final int NUM_PAGES = 5;

    //프래그먼트 교체 보여주는 처리 구현
    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return ScreenSlidePageFragment.newInstance();
            case 1:
                return ScreenSlidePageFragment2.newInstance();
            case 2:
                return ScreenSlidePageFragment3.newInstance();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {return 3;}

    //상단의 탭 레이아웃 indicator 쪽 텍스트 선언부
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "1번 프레임";
            case 1:
                return "2번 프레임";
            case 2:
                return "3번 프레임";
            default:
                return null;

        }
    }

}
