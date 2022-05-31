package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //증명 여부 가져오기
    public int getIsproof(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT isproof FROM repairManTable WHERE id = '" + u_id +"'");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        return result;
    }

    public int getNumber(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number FROM repairRequestTable WHERE id = '" + u_id +"'");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        return result;
    }

    //모든 제목 가져오기
    public ArrayList<ListItem> getAllTitles(){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number, title FROM repairRequestTable ORDER BY number DESC");

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

    //카테고리로 제목들 가져오기
    public ArrayList<ListItem> getTitles_category(int position, int max_position, String u_id){
        StringBuffer sb = new StringBuffer();
        if(position != max_position){
            sb.append("SELECT number, title FROM repairRequestTable WHERE object = '" + position +"' ORDER BY number DESC");
        }
        else
            sb.append("SELECT number, title FROM repairRequestTable WHERE userId = '" + u_id +"' ORDER BY number DESC");

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
            sb.append("SELECT number, title FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' AND object = '" + position + "' ORDER BY number DESC");
        else
            sb.append("SELECT number, title FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' ORDER BY number DESC");

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

    //수리 의뢰 내용 가져오기
    public String getRequest(int number){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT userId, title, symptom, symptom_contents, object FROM repairRequestTable WHERE number = '" + number + "'");

        SQLiteDatabase db = getReadableDatabase();

        String result = "";
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext()){
            result += cursor.getString(0) + "##,#" + cursor.getString(1) + "##,#" + cursor.getInt(2) + "##,#" + cursor.getString(3)+ "##,#" + cursor.getInt(4);
        }
        return result;
    }

    //수리 의뢰 DB 저장
    public void insertRequest(SQLiteDatabase db, String u_id,  String title, int symptom, String symptom_contents, int object){
        String sql = "INSERT INTO repairRequestTable(userID,title,object,symptom,symptom_contents) VALUES ('"+u_id+"'"+","+"'"+title+"'"+","+object+","+symptom+","+"'"+symptom_contents+"'"+");";
        db.execSQL(sql);
    }

    //수리 의뢰 수정(업데이트)
    public void updateRequest(SQLiteDatabase db, String u_id,  String title, int symptom, String symptom_contents, int object, int number){
        String sql = "UPDATE repairRequestTable SET (title,object,symptom,symptom_contents) = ('"+title+"'"+","+object+","+symptom+","+"'"+symptom_contents+"'"+") WHERE number = '" +number+"';";
        db.execSQL(sql);
    }

    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+");";
        db.execSQL(sql);
    }

    //수리의뢰 삭제
    public void deleteRequest(int number){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM repairRequestTable WHERE number = '"+ number +"'";
        String sql2 = "UPDATE repairRequestTable SET number = number - 1 WHERE number > '" +number+"'";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    public boolean isProofApprove(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isproof", 1);
        db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean isProofRefusal(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isproof", 0);
        db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean userPwUpdate(String id, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean repairManPwUpdate(String id, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        return true;
    }
}
