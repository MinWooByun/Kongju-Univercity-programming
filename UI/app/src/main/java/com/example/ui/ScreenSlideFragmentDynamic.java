package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ScreenSlideFragmentDynamic extends Fragment{
    //여기는 생성되는 작은 틀들임
    dbHelper helper;
    SQLiteDatabase db;
    Context ct;
    TextView tvProposalRN;
    TextView tvProposalEP;
    TextView tvProposalSC;
    //TextView tvCancel;
    TextView tvSubmit;
    TextView tvOpenTalk;
    TextView tvProposalS;
    TextView tvProposalSS;
    TextView tvProposalSK;
    TextView tvProposalST;


    public String u_id;
    private String r_name;
    private String r_id;
    private int p_num;
    private int e_pay;
    private String r_details;

    private int state;
    private float s_state;
    private float s_kindness;
    private float s_term;
    private int u_check;

    private int type;
/*
    public static ScreenSlideFragmentDynamic newInstance() {
        ScreenSlideFragmentDynamic ssp = new ScreenSlideFragmentDynamic();
        return ssp;
    }
 */
    public ScreenSlideFragmentDynamic(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.proposal, container, false);
        ct = container.getContext();
        tvOpenTalk = rootView.findViewById(R.id.tvOpenTalk);
        //tvCancel =  rootView.findViewById(R.id.tvCancel);
        tvSubmit = rootView.findViewById(R.id.tvSubmit);

        tvProposalS = rootView.findViewById(R.id.tvProposalProgression);
        tvProposalSS = rootView.findViewById(R.id.tvProposalSState);
        tvProposalSK = rootView.findViewById(R.id.tvProposalSKindness);
        tvProposalST = rootView.findViewById(R.id.tvProposalSTerm);
        tvProposalRN = rootView.findViewById(R.id.tvProposalRequestName);
        tvProposalEP = rootView.findViewById(R.id.tvProposalExpectPayment);
        tvProposalSC = rootView.findViewById(R.id.tvProposalSuggestContent);

        switch(state){
            case 0: {
                tvProposalS.setText("의뢰중");
                break;
            }
            case 1: {
                tvProposalS.setText("진행중");
                break;
            }
            case 2: {
                tvProposalS.setText("완료");
                break;
            }
            default:{
                tvProposalS.setText("불러올수 없습니다");
                break;}
        }

        tvProposalSS.setText(String.format("%.2f",s_state));
        tvProposalSK.setText(String.format("%.2f",s_kindness));
        tvProposalST.setText(String.format("%.2f",s_term));
        tvProposalRN.setText(r_name);
        tvProposalEP.setText(Integer.toString(e_pay));
        tvProposalSC.setText(r_details);

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String url) {
                //여기 안에서 DB 참조, 오픈링크를 받아야 한다.
                helper = new dbHelper(ct, 1);
                db = helper.getWritableDatabase();
                url = helper.getOpenLink(db,r_id);
                return url;
            }
        };
        Pattern pattern1 = Pattern.compile("Open Link"); // 링크로 이동
        Linkify.addLinks(tvOpenTalk, pattern1, "",null,mTransform);
        /*
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ct, noticeBoardActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", type);
                startActivity(intent);
                getActivity().finish();
            }
        });

         */
        //u_check, state에 따라 다르게 처리해야 한다. 그 상황별 버튼 설정
        if(type==2) {
            if (u_check == 0) {//submit
                if (state == 0) { // 의뢰진행도, 견적 선택유무 둘다 0일땐 submit을 활성화한다.
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnPopupClick(r_id, p_num);
                        }
                    });
                } else if (state == 1) { //의뢰 진행도가 1일때는 submit이 사라지게해야한다.
                    tvSubmit.setVisibility(View.GONE);
                }
            } else if (u_check == 1) {//의뢰 진행도가 1, 선택되서 진행중이다.
                if (state == 1) {//의뢰 진행도가 1이고 선택된 견적제시면 선택을 취소해야 한다. DB접해야 할수있다.
                    tvSubmit.setText("선택 철회");
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnCancelClick(r_id, p_num, type);
                        }
                    });
                }
            /*
            else if(state==0){//견적제시가 1인데 진행도가 0일수가 없다.
            }
            */
            }
        }else {
            tvSubmit.setText("수정 하기");
            if(u_check == 0) {
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ct, Proposal_SuggestFix.class);
                        intent.putExtra("r_id", r_id);
                        intent.putExtra("p_num", p_num);
                        //intent.putExtra("e_pay", e_pay);
                        //intent.putExtra("r_detail",r_details);
                        intent.putExtra("type", type);
                        startActivity(intent);
                    }
                });
            }else if(u_check == 1){
                tvSubmit.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    public void mOnCancelClick(String r_id, int p_num, int type){
        helper.updateStatesToZero(r_id,p_num);
        Toast myToast = Toast.makeText(ct, "철회 하였습니다.",Toast.LENGTH_SHORT);
        //intent로 페이지 재출력 가능?
        Intent intent = new Intent(ct, ScreenSlidePagerDynamicActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", type);
        startActivity(intent);
        getActivity().finish();

    }
    public void mOnPopupClick(String r_id,int p_num){
        helper.updateStatesToOne(r_id, p_num);
        Toast myToast = Toast.makeText(ct, "의뢰를 진행하였습니다.",Toast.LENGTH_SHORT);
        //데이터 담아서 팝업 액티비티 호출
        Intent intent = new Intent(ct, ScreenSlidePagerDynamicActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", type);
        startActivity(intent);
        getActivity().finish();


        /*
        Intent intent = new Intent(ct, Proposal_Select.class);
        intent.putExtra("r_id",r_id); //
        startActivityForResult(intent, 1);
         */

    }
    public ScreenSlideFragmentDynamic(String r_name,int p_num, int e_pay,String r_details,String r_id,int state, float s_state, float s_kindness, float s_term,int u_check,int type,String u_id){
        this.r_name = r_name;
        this.p_num = p_num;
        this.e_pay = e_pay;
        this.r_details = r_details;
        this.r_id = r_id;
        this.state = state;
        this.s_state = s_state;
        this.s_kindness = s_kindness;
        this.s_term = s_term;
        this.u_check = u_check;
        this.type= type;
        this.u_id = u_id;
    }

}
