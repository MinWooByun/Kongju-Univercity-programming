package com.example.ui;

import android.content.ContentValues;
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+");";
        db.execSQL(sql);
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
