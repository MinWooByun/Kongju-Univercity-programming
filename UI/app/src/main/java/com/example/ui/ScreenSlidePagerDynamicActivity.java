package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ScreenSlidePagerDynamicActivity extends FragmentActivity {
    //여기는 큰틀임
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    int NUM_PAGES = 3; //프래그먼트 갯수 조정 해보자
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter; // 이거 바꿔야할수도?? 모르겠다..

    int type;

    String u_id;
    String[] rnames;
    String[] rids;
    int[] pnums;
    int[] epays;
    int[] states;
    float[] s_states;
    float[] s_kindnesses;
    float[] s_terms;
    int[] u_checks;
    String[] rdetails;
    dbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal_back);
        Intent intent = getIntent();


        Intent gIntent = new Intent();
        gIntent = getIntent();
        u_id = gIntent.getExtras().getString("u_id");
        type = gIntent.getExtras().getInt("type");
        /*
        u_id="KWH";
        */
        //여기서 생성자 불러와서 실행
        helper = new dbHelper(this, 1);
        ArrayList<fragmentListItem> flist;
        if(type == 2) {
            flist = helper.getRepairSuggestionTableData(u_id);
        }
        else {//수리기사일때
            flist = helper.getRMRepairSuggetionTableData(u_id);
        }

        //}

        fragmentListItemAdapter fadapter = new fragmentListItemAdapter(this, flist);
        NUM_PAGES = fadapter.getCount();
        //uid는 위에서intent로 받아올것입니다.
        rnames = new String[NUM_PAGES];
        rids = new String[NUM_PAGES];
        pnums = new int[NUM_PAGES];
        epays = new int[NUM_PAGES];
        rdetails = new String[NUM_PAGES];
        states = new int[NUM_PAGES];
        s_states = new float[NUM_PAGES];
        s_kindnesses = new float[NUM_PAGES];
        s_terms = new float[NUM_PAGES];
        u_checks = new int[NUM_PAGES];
        for(int i=0; i < NUM_PAGES ; i++){
            rnames[i] = fadapter.getR_name(i);
            rids[i] = fadapter.getR_id(i);
            pnums[i] = fadapter.getP_num(i);
            epays[i] = fadapter.getE_pay(i);
            rdetails[i] = fadapter.getR_details(i);
            states[i] = fadapter.get_State(i);
            s_states[i] = fadapter.getS_State(i);
            s_kindnesses[i] = fadapter.getS_Kindness(i);
            s_terms[i] = fadapter.getS_Term(i);
            u_checks[i] = fadapter.getU_check(i);
        }

        //슬라이드 페이져에서 말고 각 프래그먼트에서 SUBMIT을 눌렀을떄 이동하도록 함
        mPager = (ViewPager) findViewById(R.id.viewPagerDynamic);
        pagerAdapter = new ScreenSlidePagerDynamicAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        finish();
    }/*
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }*/

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    //프래그먼트 어댑터 .. 여기서 각 프래그먼트별로 tv.setText를 해줘야함
    private class ScreenSlidePagerDynamicAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerDynamicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlideFragmentDynamic temp = new ScreenSlideFragmentDynamic(rnames[position],pnums[position],
                    epays[position],rdetails[position],
                    rids[position],states[position],
                    s_states[position],s_kindnesses[position],
                    s_terms[position],u_checks[position],type,u_id);
            return temp;
        }



        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}

