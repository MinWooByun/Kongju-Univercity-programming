package com.example.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proposal_Select extends AppCompatActivity {
    String u_id;
    String r_id;
    int p_num;
    int e_pay;
    String r_details;
    String url;
    TextView TvOpenLink;
    TextView TvCopyLink;
    dbHelper helper;
    SQLiteDatabase db;

    //팝업 띄우는 곳입니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal_select);
        Context context = this.getApplicationContext();
        //db로부터 오픈링크 받기
        // String url="https://open.kakao.com/o/srlWPsie";
        TvOpenLink = (TextView) findViewById(R.id.tvOpenLink);
        TvCopyLink = (TextView) findViewById(R.id.tvCopyLink);
        /*
        Intent gIntent = new Intent();
        gIntent = getIntent();

        r_id = gIntent.getStringExtra("r_id");
        */
        r_id="jms0813";
        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String url) {
                //여기 안에서 DB 참조, 오픈링크를 받아야 한다.
                helper = new dbHelper(Proposal_Select.this, 1);
                db = helper.getWritableDatabase();
                url = helper.getOpenLink(db,r_id);
                return url;
            }
        };
        Pattern pattern1 = Pattern.compile("Go to Link"); // 링크로 이동
        Linkify.addLinks(TvOpenLink, pattern1, "",null,mTransform);

        TvCopyLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){ //눌렀을 때 동작
                    //클립보드 사용 코드
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("ID",url); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                    clipboardManager.setPrimaryClip(clipData);
                    Toast myToast = Toast.makeText(context, "주소 복사 완료!", Toast.LENGTH_SHORT);
                    myToast.show();
                }
                return true;
            }
        });


    }
    public void mOnClose(View v) {
        //데이터 전달하기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥 레이어 클릭시 팝업 안닫히게 함
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed(){
        //안드로이드 백버튼 막기
        return;
    }

}