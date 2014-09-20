package com.loman.david.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-21.
 */
public class SqlExcu {

    private int INT_LISTCOUNT=0;
    private Context context;
    private SQLHelper sqlHelper;
    private SQLiteDatabase db = null;
    private String sql = null;
    private Cursor cursor;
    private boolean addRecord=true;
    private final String DEFAULTLISTNAME="local_Song";
    private final int DEFAULTLISTID=0;
    private final String DEFAULTLISTINFO="Songs form my telephone";
    private final String DEFAULTLISTCLASS="local";
    private final String DEFAULTLISTTAG="local";

    public final String SONGID = "sId";
    public final String SONGNAME = "sName";
    public final String SONGPATH = "sPath";
    public final String SONGTIME = "sDuration";
    public final String LISTID = "lId";
    public final String LISTNAME = "lName";
    public final String LISTCOUNT = "lCount";
    public final String LISTINFO = "lInfo";
    public final String LISTCLASS = "lClass";
    public final String LISTTAG = "lTag";
    public final String LSID = "lsId";
    public final String LISTSTABLE = "LISTS";
    public final String SONGSTABLE = "SONGS";
    public final String SONGLIST = "SONGLISTS";

    public SqlExcu(Context context) {

        this.context = context;
        sqlHelper = new SQLHelper(context);

    }

    //Update Table Contents
    public void updateSQL(String table, ContentResolver contentResolver) {
        db = sqlHelper.getWritableDatabase();
        sql = "drop table " + table;
        db.execSQL(sql);
        if (table.equals(SONGSTABLE)){
            sql="create table songs (sId integer primary key , sName varchar(50), sPath varchar(50), sDuration integer )";
        }
        else if(table.equals(LISTSTABLE)){

        }
        else if (table.equals(SONGLIST)){

        }
        db.execSQL(sql);
        db.close();
        initSQL(table, contentResolver);
    }

    //Insert into Table
    public void InsertSQL(String table, Map<String, String> map) {
        db = sqlHelper.getWritableDatabase();
        if (table.equals(SONGSTABLE)) {

            String id = map.get(SONGID);
            String time = map.get(SONGTIME);
            String name = map.get(SONGNAME);
            String path = map.get(SONGPATH);

            sql = "insert into " + table + " values (?,?,?,?)";
            db.execSQL(sql,new String []{id,name,path,time});
        } else if (table.equals(LISTSTABLE)) {
            //ne
            String id=map.get(LISTID);
            String name=map.get(LISTNAME);
            String count=map.get(LISTCOUNT);
            String lclass=map.get(LISTCLASS);
            //if not exist
            String info=map.get(LISTINFO);
            String tag=map.get(LISTTAG);
            if (info == null){
                info=DEFAULTLISTINFO;
            }
            if (tag==null){
                tag=DEFAULTLISTTAG;
            }

            sql="insert into "+table+" values (?,?,?,?,?,?)";
            db.execSQL(sql,new String []{id,name,count,info,lclass,tag});
        } else if (table.equals(SONGLIST)) {
            String id=map.get(LSID);
            String sid=map.get(SONGID);
            String sname=map.get(SONGNAME);
            String stime = map.get(SONGTIME);
            String spath = map.get(SONGPATH);
            String lid=map.get(LISTID);
            String lname=map.get(LISTNAME);
            String lclass=map.get(LISTCLASS);
            String linfo=map.get(LISTINFO);
            String ltag=map.get(LISTTAG);

            sql="insert into "+table+" values(?,?,?,?,?,?,?,?,?,?)";
            db.execSQL(sql,new String []{id,sid,sname,spath,stime,lid,lname,linfo,lclass,ltag});
        } else {
            Toast.makeText(context, "Insert Table no exist", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //init Table Contents
    public void initSQL(String table, ContentResolver contentResolver) {
        setAddRecord(table);
        Map<String, String> map = new HashMap<String, String>();

        String selections[] = new String[]{
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DURATION};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(uri, selections, null, null, null);

        int index_name = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int index_path = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        int index_time = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

        if (table.equals(SONGSTABLE) && addRecord) {

            int count = 0;
            while (cursor.moveToNext()) {
                map.clear();
                map.put(SONGID, String.valueOf(count));
                map.put(SONGTIME, changeToTime(cursor.getInt(index_time)));
                map.put(SONGPATH, cursor.getString(index_path));
                map.put(SONGNAME, cursor.getString(index_name));
                count++;
                InsertSQL(SONGSTABLE, map);
            }


        }else if (table.equals(LISTSTABLE) && addRecord){
            map.clear();
            map.put(LISTID,String.valueOf(DEFAULTLISTID));
            map.put(LISTNAME,DEFAULTLISTNAME);
            map.put(LISTCOUNT,String.valueOf(INT_LISTCOUNT));
            map.put(LISTINFO,DEFAULTLISTINFO);
            map.put(LISTCLASS,DEFAULTLISTCLASS);
            map.put(LISTTAG,DEFAULTLISTTAG);
            InsertSQL(table,map);
        }
        else if (table.equals(SONGLIST) && addRecord){
            int count=0;
            while (cursor.moveToNext()){
                map.clear();
                map.put(LSID, String.valueOf(count));
                map.put(SONGID,String.valueOf(count));
                map.put(SONGNAME,cursor.getString(index_name));
                map.put(SONGPATH,cursor.getString(index_path));
                map.put(SONGTIME, changeToTime(cursor.getInt(index_time)));
                map.put(LISTNAME,DEFAULTLISTNAME);
                map.put(LISTID,String.valueOf(DEFAULTLISTID));
                map.put(LISTINFO,DEFAULTLISTINFO);
                map.put(LISTCLASS,DEFAULTLISTCLASS);
                map.put(LISTTAG,DEFAULTLISTTAG);
                InsertSQL(table, map);
                count++;
                INT_LISTCOUNT=count;
            }
        }
        else if (addRecord) {
            Toast.makeText(context,"Init Table not exist",Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public List<Map<String,String>> QuerrySQL(String table, String object, String option) {

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        db = sqlHelper.getReadableDatabase();

        if (table.equals(SONGSTABLE)) {

            sql = "Select * from " + table;

            cursor = db.rawQuery(sql, null);

            int index_id = cursor.getColumnIndex(SONGID);
            int index_time = cursor.getColumnIndex(SONGTIME);
            int index_path = cursor.getColumnIndex(SONGPATH);
            int index_name = cursor.getColumnIndex(SONGNAME);

            while (cursor.moveToNext()) {

                Map<String, String> map = new HashMap<String, String>();
                map.put(SONGID, String.valueOf(cursor.getInt(index_id)));
                map.put(SONGNAME, cursor.getString(index_name));
                map.put(SONGPATH, cursor.getString(index_path));
                map.put(SONGTIME, cursor.getString(index_time));
                list.add(map);
            }

        } else if (table.equals(LISTSTABLE)) {

            sql="select * from "+table;

            cursor=db.rawQuery(sql,null);

            int index_id=cursor.getColumnIndex(LISTID);
            int index_name=cursor.getColumnIndex(LISTNAME);
            int index_count=cursor.getColumnIndex(LISTCOUNT);
            int index_info=cursor.getColumnIndex(LISTINFO);
            int index_class=cursor.getColumnIndex(LISTCLASS);
            int index_tag=cursor.getColumnIndex(LISTTAG);

            while (cursor.moveToNext()){

                Map<String,String> map=new HashMap<String, String>();
                map.put(LISTID,String.valueOf(cursor.getInt(index_id)));
                map.put(LISTNAME,cursor.getString(index_name));
                map.put(LISTCOUNT,String.valueOf(cursor.getInt(index_count)));
                map.put(LISTINFO,cursor.getString(index_info));
                map.put(LISTCLASS,cursor.getString(index_class));
                map.put(LISTTAG,cursor.getString(index_tag));
                list.add(map);
            }

        } else if (table.equals(SONGLIST)) {

            if (object != null){
                sql="select * from "+SONGLIST+" where "+object+" = "+option;

                cursor=db.rawQuery(sql,null);
            }

            int index_sid = cursor.getColumnIndex(SONGID);
            int index_stime = cursor.getColumnIndex(SONGTIME);
            int index_spath = cursor.getColumnIndex(SONGPATH);
            int index_sname = cursor.getColumnIndex(SONGNAME);
            int index_lid=cursor.getColumnIndex(LISTID);
            int index_lname=cursor.getColumnIndex(LISTNAME);
            int index_linfo=cursor.getColumnIndex(LISTINFO);
            int index_lclass=cursor.getColumnIndex(LISTCLASS);
            int index_ltag=cursor.getColumnIndex(LISTTAG);

            while (cursor.moveToNext()){

                Map<String,String> map=new HashMap<String, String>();
                map.put(SONGID, String.valueOf(cursor.getInt(index_sid)));
                map.put(SONGNAME, cursor.getString(index_sname));
                map.put(SONGPATH, cursor.getString(index_spath));
                map.put(SONGTIME, cursor.getString(index_stime));
                map.put(LISTID,String.valueOf(cursor.getInt(index_lid)));
                map.put(LISTNAME,cursor.getString(index_lname));
                map.put(LISTINFO,cursor.getString(index_linfo));
                map.put(LISTCLASS,cursor.getString(index_lclass));
                map.put(LISTTAG,cursor.getString(index_ltag));
                list.add(map);
//                Log.e("SongLists","yes songname:"+cursor.getString(index_sname)+" time :"+cursor.getString(index_stime));
            }

        } else {
            Toast.makeText(context, "Update Table no exist", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        return list;
    }

    private void setAddRecord (String table){
        addRecord=true;
        db=sqlHelper.getReadableDatabase();
        sql="Select * from "+table;
        cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            addRecord=false;
            return;
        }
        cursor.close();
        db.close();
        Log.e("HasCode","----->"+addRecord);
    }

    public void SQLForList (){

    }


    private String changeToTime (int secends){
        String time = null;
        int h, m, s;
        h = m = s = 0;
        if (secends > 1000 * 60 * 60) {
            h = secends / (1000 * 60 * 60);
            secends = secends % (1000 * 60 * 60);
        }
        if (secends > 1000 * 60) {
            m = secends / (1000 * 60);
            secends = secends % (1000 * 60);
        }
        if (secends >= 1000) {
            s = secends / 1000;
        }
        if (h == 0) {
            time = m + ":" + getNumber(s);
        } else {
            time = h + ":" + getNumber(m) + ":" + getNumber(s);
        }
        return time;

    }

    private String getNumber (int number){
        StringBuffer result=new StringBuffer();
        if (number<10){
            result.append("0");
            result.append(number);
        }
        else{
            result.append(number);
        }
        return result.toString();
    }
}
