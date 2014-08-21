package com.loman.david.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.loman.david.tools.Song;
import com.loman.david.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-21.
 */
public class SqlExcu {

    private Context context;
    private SQLHelper sqlHelper;
    private SQLiteDatabase db = null;
    private String sql = null;
    private Cursor cursor;
    private boolean addRecord=true;

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

            String id = map.get("ID");
            String time = map.get("TIME");
            String name = map.get("NAME");
            String path = map.get("PATH");

            sql = "insert into " + table + " values (?,?,?,?)";
            db.execSQL(sql,new String []{id,name,path,time});
        } else if (table.equals(LISTSTABLE)) {

        } else if (table.equals(SONGLIST)) {

        } else {
            Toast.makeText(context, "Table no exist", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //init Table Contents
    public void initSQL(String table, ContentResolver contentResolver) {
        setAddRecord(table);
        Map<String, String> map = new HashMap<String, String>();
        if (table.equals(SONGSTABLE) && addRecord) {

            String selections[] = new String[]{
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.DATA,
                    MediaStore.Audio.AudioColumns.DURATION};
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            cursor = contentResolver.query(uri, selections, null, null, null);

            int index_name = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            int index_path = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            int index_time = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

            int count = 0;
            while (cursor.moveToNext()) {
                map.put("ID", String.valueOf(count));
                map.put("TIME", changeToTime(cursor.getInt(index_time)));
                map.put("PATH", cursor.getString(index_path));
                map.put("NAME", cursor.getString(index_name));
                count++;
                InsertSQL(SONGSTABLE, map);
            }
            cursor.close();
        }
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
            Log.e("Qurry","id:"+index_id+" time:"+index_time+" path:"+index_path+" name:"+index_name);
            while (cursor.moveToNext()) {

                Map<String, String> map = new HashMap<String, String>();
                map.put("id", String.valueOf(cursor.getInt(index_id)));
                map.put("name", cursor.getString(index_name));
                map.put("path", cursor.getString(index_path));
                map.put("time", cursor.getString(index_time));
                list.add(map);
            }

        } else if (table.equals(LISTSTABLE)) {

        } else if (table.equals(SONGLIST)) {

        } else {
            Toast.makeText(context, "Table no exist", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        return list;
    }

    private void setAddRecord (String table){
        db=sqlHelper.getReadableDatabase();
        sql="Select * from "+table;
        cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            addRecord=false;
        }
        cursor.close();
        Log.e("HasCode","----->"+addRecord);
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
        if (secends > 1000) {
            s = secends / 1000;
        }
        if (h == 0) {
            time = m + ":" + s;
        } else {
            time = h + ":" + m + ":" + s;
        }
        return time;
    }
}
