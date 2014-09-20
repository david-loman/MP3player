package com.loman.david.mp3player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.loman.david.data.SqlExcu;
import com.loman.david.tools.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-20.
 */
public class MainActivity extends Activity {

    private TabHost tabhost;
    private Button update;
    private Button manage;
    private ListView songList;
    private ListView listList;
    private SqlExcu sqlExcu;
    private List<Map<String,String>> sList;
    private List<Map<String,String>> lList;
    private Map<String,String> map;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sList=new ArrayList<Map<String, String>>();
        lList=new ArrayList<Map<String, String>>();
        map=new HashMap<String, String>();
        contentResolver=getContentResolver();

        tabhost=(TabHost)findViewById(R.id.tabHost);
        tabhost.setup();
        TabSpec tabSpec1=tabhost.newTabSpec("tab1").setIndicator("本地歌曲").setContent(R.id.songs);
        tabhost.addTab(tabSpec1);

        TabSpec tabSpec2 =tabhost.newTabSpec("tab2").setIndicator("列表管理").setContent(R.id.lists);
        tabhost.addTab(tabSpec2);

        update=(Button)findViewById(R.id.updateSongs);
        manage=(Button)findViewById(R.id.managerLists);
        songList=(ListView)findViewById(R.id.songsListView);
        listList=(ListView)findViewById(R.id.listsListView);
        sqlExcu=new SqlExcu(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSongListView();
        setListListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlExcu.updateSQL(sqlExcu.SONGSTABLE,contentResolver);
                setSongListView();
                Toast.makeText(MainActivity.this,"Update Song list !",Toast.LENGTH_SHORT).show();
            }
        });

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra("LID",0);
                intent.putExtra("SID",position);
                startActivity(intent);
                finish();
            }
        });

        songList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String,String> map=sList.get(position);
                String name=map.get(sqlExcu.SONGNAME);
                String time=map.get(sqlExcu.SONGTIME);
                String path=map.get(sqlExcu.SONGPATH);
                String msg="Song name:"+" "+name+"\nSong time:"+" "+time+"\nSong path:"+" "+path;
                createDialog("Song informations",msg,true,false);
                return true;
            }
        });

//        listList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent=new Intent(MainActivity.this,ListManagerActivity.class);
//                intent.putExtra("id",position);
//                startActivity(intent);
//
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setSongListView(){
        sList.clear();
        sList=sqlExcu.QuerrySQL(sqlExcu.SONGSTABLE,null,null);
        SimpleAdapter songAdapter =new SimpleAdapter(this,sList,R.layout.songlist_item,new String[]{sqlExcu.SONGNAME,sqlExcu.SONGTIME},new int[]{R.id.title,R.id.time});
        songList.setAdapter(songAdapter);
    }

    private void setListListView(){
        lList.clear();
        lList=sqlExcu.QuerrySQL(sqlExcu.LISTSTABLE,null,null);
        SimpleAdapter listAdapter=new SimpleAdapter(this,lList,R.layout.songlist_item,new String[]{sqlExcu.LISTNAME,sqlExcu.LISTCOUNT},new int []{R.id.title,R.id.time});
        listList.setAdapter(listAdapter);
    }

    private void createDialog(String title ,String messege,boolean postive,boolean negetive){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(messege);
        if (postive){
            builder.setPositiveButton("Yes",null);
        }
        if (negetive){
            builder.setNegativeButton("Cancel",null);
        }
        builder.create().show();
    }

}
