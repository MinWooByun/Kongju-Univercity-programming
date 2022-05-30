package com.example.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {
    static final String DB_Name = "HL_DB.db";
    private static final String TABLE_NAME_userTable = "userTable";
    private static final String TABLE_NAME_repairManTable = "repairManTable";

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
    }

    //카테고리로 제목들 가져오기
    public ArrayList<ListItem> getTitles_category(int position, int max_position, String u_id){
        StringBuffer sb = new StringBuffer();
        if(position != max_position){
            sb.append("SELECT number, title FROM repairRequestTable WHERE object = '" + position +"'");
        }
        else
            sb.append("SELECT number, title FROM repairRequestTable WHERE userId = '" + u_id +"'");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1));
            titles.add(item);
        }
        return titles;
    }

    //검색으로 제목 가져오기
    public ArrayList<ListItem> getTitles_search(int position, String search_text){
        StringBuffer sb = new StringBuffer();

        if(position!=0)
            sb.append("SELECT number, title FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' AND object = '" + position + "'");
        else
            sb.append("SELECT number, title FROM repairRequestTable WHERE title LIKE '%" + search_text +"%'");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1));
            titles.add(item);
        }
        return titles;
    }

    public String getRequest(int number){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT title, symptom, symptom_contents, object FROM repairRequestTable WHERE number = '" + number + "'");

        SQLiteDatabase db = getReadableDatabase();

        String result = "";
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext()){
            result += cursor.getString(0) + "##,#" + cursor.getInt(1) + "##,#" + cursor.getString(2) + "##,#" + cursor.getInt(3);
        }
        return result;
    }

    public void insertRequest(SQLiteDatabase db, String u_id,  String title, int symptom, String symptom_contents, int object){
        Log.v("symptom:", String.valueOf(symptom));
        String sql = "INSERT INTO repairRequestTable(u_id,title,object,symptom,symptom_contents) VALUES"+"("+"'"+u_id+"'"+","+"'"+title+"'"+","+object+","+symptom+","+"'"+symptom_contents+"'"+");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+");";
        db.execSQL(sql);
    }
}
