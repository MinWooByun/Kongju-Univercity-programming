package com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment2 extends Fragment {
    private View view;

    public static ScreenSlidePageFragment2 newInstance() {
        ScreenSlidePageFragment2 ssp = new ScreenSlidePageFragment2();
        return ssp;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screen_slide_page2, container, false);

        return view;
    }
}

