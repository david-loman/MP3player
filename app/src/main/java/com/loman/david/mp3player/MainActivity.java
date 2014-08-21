package com.loman.david.mp3player;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private List<Map<String,String>> list;
    private Map<String,String> map;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list=new ArrayList<Map<String, String>>();
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
        list.clear();
        list=sqlExcu.QuerrySQL(sqlExcu.SONGSTABLE,null,null);
        SimpleAdapter songAdapter =new SimpleAdapter(this,list,R.layout.songlist_item,new String[]{"name","time"},new int[]{R.id.title,R.id.time});
        songList.setAdapter(songAdapter);
    }

    private void setListListView(){

    }
}
