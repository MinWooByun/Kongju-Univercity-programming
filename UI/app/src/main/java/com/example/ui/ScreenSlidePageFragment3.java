package com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment3 extends Fragment {
    private View view;

    public static ScreenSlidePageFragment3 newInstance() {
        ScreenSlidePageFragment3 ssp = new ScreenSlidePageFragment3();
        return ssp;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screen_slide_page3, container, false);

        return view;
    }
}
