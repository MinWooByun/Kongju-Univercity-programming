package com.example.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {
    static final String DB_Name = "HL_DB.db";

    public dbHelper(Context context, int version){
        super(context, DB_Name, null, version);
    }

   @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public ArrayList<ListItem> getAllTitles(){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number, title FROM repairRequestTable");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        Log.v("call", "불러오기");
        while(cursor.moveToNext()){
            Log.v("item: ",cursor.getInt(0)+ cursor.getString(1));
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1));
            titles.add(item);
        }  
        return titles;
    } //관둘까

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+");";
        db.execSQL(sql);
    }//db바뀐거있자늠 그거ㄷ떄문일듯
}
