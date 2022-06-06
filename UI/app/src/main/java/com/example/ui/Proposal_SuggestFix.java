package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Scanner;

public class Proposal_SuggestFix extends AppCompatActivity {
    EditText EdtPay;
    EditText EdtContent;
    Button BtnSuggest;
    String r_id;
    int p_num;
    int e_pay;
    int type;
    String r_details;
    dbHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal_suggest);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        EdtPay = (EditText) findViewById(R.id.edtPay);
        EdtContent = (EditText) findViewById(R.id.edtContent);
        BtnSuggest = (Button) findViewById(R.id.btnSuggest);
        BtnSuggest.setText("수정");
        // intent가 r_id, p_num을 줄 것임, 일단 테스트는 직접 작성으로 함
        Intent gIntent = getIntent();
        r_id = gIntent.getStringExtra("r_id");
        p_num = gIntent.getIntExtra("p_num",0);
        type = gIntent.getIntExtra("type", 0);

        helper = new dbHelper(this, 1);
        e_pay = helper.getEpaySuggetionTableData(r_id,p_num);
        r_details = helper.getRdetailsSuggetionTableData(r_id,p_num);

        EdtPay.setText(Integer.toString(e_pay));
        EdtContent.setText(r_details);
//        helper.updateSuggetionTableData(r_id, p_num);



        Context context = this.getApplicationContext();
        BtnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pay_check = EdtPay.getText().toString();
                String details = EdtContent.getText().toString();

                //if(Integer.parseInt(EdtPay.getText()))
                if(isStringInteger(pay_check,10)) {
                    int pay = Integer.parseInt(EdtPay.getText().toString()); //스트링을변환한거임
                    if(!details.matches("")) {
                        helper.updateSuggstionTableData(r_id, p_num, Integer.parseInt(pay_check), details);
                        Toast myToast = Toast.makeText(context, "견적 제시를 수정했습니다.", Toast.LENGTH_SHORT);
                        myToast.show();
                        Intent intent = new Intent(Proposal_SuggestFix.this,ScreenSlidePagerDynamicActivity.class);
                        //Intent intent = new Intent(MainActivity.this,Proposal_Select.class);
                        //사실 DB에 넣어야 되는 내용물들임 버튼눌렀을떄 실행함
                        intent.putExtra("u_id", r_id);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast myToast = Toast.makeText(context, "견적 내용을 작성해 주세요", Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                }
                else {
                    Toast myToast = Toast.makeText(context, "비용 산정을 작성해주세요 또는 비용 산정에 숫자만 입력해주세요", Toast.LENGTH_SHORT);
                    myToast.show();
                }



            }
        });
    }
    public static boolean isStringInteger(String stringToCheck, int radix) {
        Scanner sc = new Scanner(stringToCheck.trim());
        if(!sc.hasNextInt(radix)) return false;
        sc.nextInt(radix);
        return !sc.hasNext();
    }
}