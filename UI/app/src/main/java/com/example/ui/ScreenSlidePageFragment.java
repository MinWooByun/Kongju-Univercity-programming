package com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment extends Fragment {
    private View view;

    public static ScreenSlidePageFragment newInstance() {
        ScreenSlidePageFragment ssp = new ScreenSlidePageFragment();
        return ssp;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        return view;
    }
}
