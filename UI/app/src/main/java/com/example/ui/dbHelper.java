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
    private static final String TABLE_NAME_satisficationTable = "satisficationTable";
    private static final String TABLE_NAME_repairRequestTable = "repairRequestTable";

    public dbHelper(Context context, int version){
        super(context, DB_Name, null, version);
    }

   @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public int getRequestCount(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT Count(userID) FROM repairRequestTable WHERE userID = '" + u_id +"'AND state != 2");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext())
            result = cursor.getInt(0);
        return result;
    }

    public int getSuggestionCount(String u_id, int type){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        if(type==1){
            sb.append("SELECT Count(r_id) FROM repairSuggestionTable WHERE r_id = '" + u_id +"'");
            Cursor cursor = db.rawQuery(sb.toString(), null);
            while(cursor.moveToNext())
                result = cursor.getInt(0);
        }
        else if(type==2){
            StringBuffer sb2 = new StringBuffer();
            sb.append("SELECT number FROM repairRequestTable WHERE userID = '" + u_id +"' AND state != 2");
            Cursor cursor = db.rawQuery(sb.toString(), null);
            while(cursor.moveToNext()){
                sb2.append("SELECT Count(r_id) FROM repairSuggestionTable WHERE p_num = '" + cursor.getInt(0) +"'");
                Cursor cursor2 = db.rawQuery(sb2.toString(), null);
                while(cursor2.moveToNext()){
                    result = cursor.getInt(0);
                }
            }


        }

        return result;
    }

    //증명 여부 가져오기
    public int getIsproof(String u_id){
        int result = 0;
        SQLiteDatabase db = getReadableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT isproof FROM repairManTable WHERE id = '" + u_id +"'");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext())
            result = cursor.getInt(0);
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
        sb.append("SELECT number, title, state FROM repairRequestTable ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }  
        return titles;
    }

    //카테고리로 제목들 가져오기
    public ArrayList<ListItem> getTitles_category(int position, int max_position, String u_id){
        StringBuffer sb = new StringBuffer();
        if(position != max_position){
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE object = '" + position +"' ORDER BY number DESC");
        }
        else
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE userId = '" + u_id +"' ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }
        return titles;
    }

    //검색으로 제목 가져오기
    public ArrayList<ListItem> getTitles_search(int position, String search_text){
        StringBuffer sb = new StringBuffer();

        if(position!=0)
            sb.append("SELECT number, titl, state FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' AND object = '" + position + "' ORDER BY number DESC");
        else
            sb.append("SELECT number, title, state FROM repairRequestTable WHERE title LIKE '%" + search_text +"%' ORDER BY number DESC");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        while(cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            titles.add(item);
        }
        return titles;
    }

    //수리 의뢰 내용 가져오기
    public String getRequest(int number){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT userId, title, symptom, symptom_contents, object, state FROM repairRequestTable WHERE number = '" + number + "'");

        SQLiteDatabase db = getReadableDatabase();

        String result = "";
        Cursor cursor = db.rawQuery(sb.toString(), null);
        while(cursor.moveToNext()){
            result += cursor.getString(0) + "##,#" + cursor.getString(1) + "##,#" + cursor.getInt(2) + "##,#" + cursor.getString(3)+ "##,#" + cursor.getInt(4)+ "##,#" + cursor.getInt(5);
        }
        return result;
    }

    //수리 의뢰 DB 저장
    public void insertRequest( String u_id,  String title, int symptom, String symptom_contents, int object, int tag){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO repairRequestTable(userID,title,object,symptom,symptom_contents,state) VALUES ('"+u_id+"',"+"'"+title+"','"+object+"','"+symptom+"','"+symptom_contents+"','"+tag+"');";
        db.execSQL(sql);
    }

    //수리 의뢰 수정(업데이트)
    public void updateRequest(SQLiteDatabase db, String u_id,  String title, int symptom, String symptom_contents, int object, int number){
        String sql = "UPDATE repairRequestTable SET (title,object,symptom,symptom_contents) = ('"+title+"'"+","+object+","+symptom+","+"'"+symptom_contents+"'"+") WHERE number = '" +number+"';";
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
        db.close();
        return true;
    }

    public boolean isProofRefusal(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isproof", 0);
        db.update(TABLE_NAME_repairManTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public boolean userUpdate(String id, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        db.close();
        return true;
    }

    public boolean repairManUpdate(String id, String pw , String openLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pw", pw);
        db.update(TABLE_NAME_userTable, contentValues, "id = ?", new String[] {id});
        db.execSQL("UPDATE '"+TABLE_NAME_repairManTable+"' SET openlink = '"+openLink+"' WHERE id = '"+id+"'");
        db.close();
        return true;
    }

    public long satisfiedUpdate(String id, int number, int statePopup, int kindnessPopup, int termPopup, int pricePopup) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r_id FROM repairSuggestionTable WHERE p_num = '"+number+"' AND u_checked = 1",null);

        String r_id = "";
        while (cursor.moveToNext()) {
            r_id = cursor.getString(0);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("r_id", r_id);
        contentValues.put("p_num", number);
        contentValues.put("S_State", statePopup);
        contentValues.put("S_Kindness", kindnessPopup);
        contentValues.put("S_Term", termPopup);
        contentValues.put("price", pricePopup);
        long result = db.insert(TABLE_NAME_satisficationTable, null, contentValues);
        db.execSQL("UPDATE '"+TABLE_NAME_repairRequestTable+"' SET state = 2 WHERE number = '"+number+"'");
        db.close();

        return result;
    }
    //수리기사 견적확인
    public ArrayList<fragmentListItem> getRMRepairSuggetionTableData(String r_id){
        //db 다시 생각해야함 u_id에 해당하는 repairRequestTable 의뢰제목 내용 모두 가져와야함
        //동시에 repairSuggetionTable에서 그 의뢰에 해당하는 e_pay, r_details를 (p_num)으로 검색

        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        SQLiteDatabase db3 = getReadableDatabase();
        ArrayList<fragmentListItem> proposalList = new ArrayList<fragmentListItem>();
        /*
        ArrayList<requestListItem> getRequestList = getRequestName(u_id);
        Cursor.requestCursor = db.rawQuery
        */

        StringBuffer sb = new StringBuffer();//2,3,4가 state kindness termavg
        sb.append("SELECT p_num, e_pay, r_details, r_id,u_checked  from repairSuggestionTable Where r_id="+"'"+r_id+"'"+";");
        Cursor rS_cursor = db.rawQuery(sb.toString(),null);
        while(rS_cursor.moveToNext()){
            StringBuffer sb2 = new StringBuffer();
            int p_num = rS_cursor.getInt(0);
            sb2.append("SELECT number, title, state from repairRequestTable Where number="+p_num+" and state!="+2+";");
            Cursor rR_cursor = db2.rawQuery(sb2.toString(), null);
            while(rR_cursor.moveToNext()){//db에서 자료 가져와서 한 row객체<<당 4개의 자료(r_id, p_num, e_pay, r_details +추가
                StringBuffer sb3 = new StringBuffer();
                sb3.append("SELECT AVG(S_State), AVG(S_Kindness), AVG(S_Term) FROM satisficationTable Where r_id="+"'"+rS_cursor.getString(3)+"'"+";");
                Cursor rM_cursor = db3.rawQuery(sb3.toString(),null);
                while(rM_cursor.moveToNext()){
                    fragmentListItem item = new fragmentListItem(rR_cursor.getString(1),
                            rR_cursor.getInt(0),rS_cursor.getInt(1),
                            rS_cursor.getString(2), rS_cursor.getString(3),
                            rR_cursor.getInt(2),rM_cursor.getFloat(0),rM_cursor.getFloat(1),
                            rM_cursor.getFloat(2),rS_cursor.getInt(4));
                    proposalList.add(item);
                }
            }
        }
        return proposalList;
    }
    public void updateSuggstionTableData(String r_id, int p_num, int e_pay, String r_details){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE repairSuggestionTable SET e_pay="+e_pay+","+"  r_details="+"'"+r_details+"'"+" WHERE r_id="+"'"+r_id+"' and p_num="+p_num+";";
        db.execSQL(sql);
    }
    public int getEpaySuggetionTableData(String r_id, int p_num){
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT e_pay from repairSuggestionTable Where r_id="+"'"+r_id+"'"+" and p_num="+p_num+";");
        Cursor cursor = db.rawQuery(sb.toString(),null);
        int result=0;
        while(cursor.moveToNext()){
            result=cursor.getInt(0);
        }
        return result;
    }
    public String getRdetailsSuggetionTableData(String r_id, int p_num){
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT r_details from repairSuggestionTable Where r_id="+"'"+r_id+"'"+" and p_num="+p_num+";");
        Cursor cursor = db.rawQuery(sb.toString(),null);
        String result="";
        while(cursor.moveToNext()){
            result=cursor.getString(0);
        }
        return result;
    }
    //의뢰제목도 찾음
    public ArrayList<fragmentListItem> getRepairSuggestionTableData(String u_id){
        //db 다시 생각해야함 u_id에 해당하는 repairRequestTable 의뢰제목 내용 모두 가져와야함
        //동시에 repairSuggetionTable에서 그 의뢰에 해당하는 e_pay, r_details를 (p_num)으로 검색
        //
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        SQLiteDatabase db3 = getReadableDatabase();
        ArrayList<fragmentListItem> proposalList = new ArrayList<fragmentListItem>();
        /*
        ArrayList<requestListItem> getRequestList = getRequestName(u_id);
        Cursor.requestCursor = db.rawQuery
        */
        StringBuffer sb2 = new StringBuffer();//2,3,4가 state kindness termavg
        sb2.append("SELECT number, title, state from repairRequestTable Where userID="+"'"+u_id+"'and state!="+2+";"); //satis
        Cursor rR_cursor = db.rawQuery(sb2.toString(),null);

        while(rR_cursor.moveToNext()){

            StringBuffer sb = new StringBuffer();
            int p_num = rR_cursor.getInt(0);
            String r_name = rR_cursor.getString(1);
            sb.append("SELECT p_num, e_pay, r_details, r_id,u_checked  from repairSuggestionTable Where p_num="+p_num+";");
            Cursor rS_cursor = db2.rawQuery(sb.toString(), null);
            while(rS_cursor.moveToNext()){//db에서 자료 가져와서 한 row객체<<당 4개의 자료(r_id, p_num, e_pay, r_details +추가

                StringBuffer sb3 = new StringBuffer();
                sb3.append("SELECT AVG(S_State), AVG(S_Kindness), AVG(S_Term) FROM satisficationTable Where r_id="+"'"+rS_cursor.getString(3)+"'"+";");
                Cursor rM_cursor = db3.rawQuery(sb3.toString(),null);
                while(rM_cursor.moveToNext()){
                    fragmentListItem item = new fragmentListItem(rR_cursor.getString(1),rR_cursor.getInt(0),rS_cursor.getInt(1),
                            rS_cursor.getString(2), rS_cursor.getString(3),
                            rR_cursor.getInt(2),rM_cursor.getFloat(0),rM_cursor.getFloat(1),
                            rM_cursor.getFloat(2),rS_cursor.getInt(4));
                    proposalList.add(item);
                }

            }
        }
        return proposalList;
    }
    public void updateStatesToZero(String r_id, int p_num){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        String sql = "UPDATE repairSuggestionTable SET u_checked=0 WHERE r_id="+"'"+r_id+"'and p_num="+p_num+";";
        db.execSQL(sql);
        String sql2 = "UPDATE repairRequestTable SET state=0 WHERE number="+p_num+";";
        db2.execSQL(sql2);
    }
    public void updateStatesToOne(String r_id, int p_num) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db2 = getReadableDatabase();
        String sql = "UPDATE repairSuggestionTable SET u_checked=1 WHERE r_id="+"'"+r_id+"'and p_num="+p_num+";";
        db.execSQL(sql);
        String sql2 = "UPDATE repairRequestTable SET state=1 WHERE number="+p_num+";";
        db2.execSQL(sql2);
    }

    public String getOpenLink(SQLiteDatabase db, String r_id) {//오픈링크 받아오는 메소드
        String sql= "SELECT openlink from repairManTable Where id="+"'"+r_id+"'"+";";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToNext();
        String openlink = cursor.getString(0);
        return openlink;
    }

    public void insertProposal(SQLiteDatabase db, String r_id, int p_num, int e_pay, String r_details){
        String sql = "INSERT INTO repairSuggestionTable VALUES"+"("+"'"+r_id+"'"+","+p_num+","+e_pay+","+"'"+r_details+"'"+","+0+");";
        db.execSQL(sql);
    }
    //수리기사가 이미 신고한 글인지 확인
    public boolean checkReport(String id, Integer number){//수리기사 id, 게시글 번호
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM declarTable WHERE r_id = '"+id+"' AND p_num = '"+number+"';", null);
        while(cursor.moveToNext()){
            //신고이력이 존재하면 카우튼가 0보다 클것
            //true 리턴
            if (cursor.getCount() > 0) {
                db.close();
                return true;
            }
        }
        //존재하지 않으면 false를 리턴
        db.close();
        return false;
    }

    //기사가 게시글 신고
    public boolean reportRequest(String id, Integer number){//게시글 번호
        SQLiteDatabase db = getWritableDatabase();
        String sql1 = "UPDATE repairRequestTable SET declar = declar + 1 WHERE number = " +number+"";
        String sql2 = "INSERT INTO declarTable VALUES('"+id+"','"+number+"')";
        db.execSQL(sql1);
        db.execSQL(sql2);
        Cursor cursor = db.rawQuery("SELECT declar FROM repairRequestTable WHERE number = "+number+";", null);
        while(cursor.moveToNext()){
            //신고가 3번 누적되면 자동으로 게시글 삭제
            if (cursor.getInt(0) >= 3) {
                String sql3 = "DELETE FROM repairRequestTable WHERE number = "+number+";";
                String sql4 = "DELETE FROM declarTable WHERE p_num = "+number+";";
                db.execSQL(sql3);
                db.execSQL(sql4);
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }
    //이미지 제거
    public void imgDelete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM imgTable WHERE id = '"+ id +"';";
        db.execSQL(sql);
    }
}

