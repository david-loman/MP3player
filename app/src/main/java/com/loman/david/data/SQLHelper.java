package com.loman.david.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 14-8-21.
 */
public class SQLHelper extends SQLiteOpenHelper {

    public SQLHelper(Context context) {
        super(context, "mp3player.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table songs (sId integer primary key , sName varchar(50), sPath varchar(50), sDuration varchar(10) )");
        db.execSQL("create table lists (lId integer primary key ,lName varchar(10), lCount integer,lInfo varchar(140),lClass varchar(20), lTag varchar(50))");
        db.execSQL("create table songLists(lsId Integer primary key,sId integer,sName varchar(50),sPath varchar(50), sDuration varchar(10),lId integer,lName varchar (10),lInfo varchar(140),lClass varchar(20), lTag varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table songs");
        db.execSQL("drop table lists");
        db.execSQL("drop table songLists");
        onCreate(db);
    }

}
