package com.loman.david.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loman.david.data.SQLHelper;
import com.loman.david.data.SqlExcu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-22.
 */
public class ListManagerActivity extends Activity {

    private Button listEditButton, listDeleteButton, addSongsButton, deleteSongsButton;
    private EditText nameEditText, classEdtiText, tagEditText, infoEditText;
    private TextView countTextView;
    private ListView listView;
    private SqlExcu sqlExcu;
    private List<Map<String, String>> list = null;
    private Map<String, String> map = null;
    private int listID;
    private int requestCode;
    private int DEFAULTCOLOR= Color.argb(0, 255, 255, 255);
    private int SELECTCOLOR=Color.argb(100,246,255,13);
    private String lname,lcalss,ltag,linfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_information);

        list = new ArrayList<Map<String, String>>();
        listView = (ListView) findViewById(R.id.songInfoListView);
        listEditButton = (Button) findViewById(R.id.listEditor);
        listDeleteButton = (Button) findViewById(R.id.listDelete);
        addSongsButton = (Button) findViewById(R.id.addSongs);
        deleteSongsButton = (Button) findViewById(R.id.deleteSongs);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        classEdtiText = (EditText) findViewById(R.id.classEditText);
        tagEditText = (EditText) findViewById(R.id.tagEditText);
        infoEditText = (EditText) findViewById(R.id.infoEditText);
        countTextView = (TextView) findViewById(R.id.countTextView);
        sqlExcu = new SqlExcu(ListManagerActivity.this);
        Intent intent = getIntent();
        listID = intent.getIntExtra("id", 0);

        list = sqlExcu.QuerrySQL(sqlExcu.SONGLIST, sqlExcu.LISTID, String.valueOf(listID));

        SimpleAdapter adapter = new SimpleAdapter(ListManagerActivity.this, list, R.layout.songlist_item, new String[]{sqlExcu.SONGNAME, sqlExcu.SONGTIME}, new int[]{R.id.title, R.id.time});
        listView.setAdapter(adapter);

        map=new HashMap<String, String>();
        map=list.get(0);

        lname=map.get(sqlExcu.LISTNAME);
        lcalss=map.get(sqlExcu.LISTCLASS);
        ltag=map.get(sqlExcu.LISTTAG);
        linfo=map.get(sqlExcu.LISTINFO);

        nameEditText.setText(lname);
        classEdtiText.setText(lcalss);
        tagEditText.setText(ltag);
        infoEditText.setText(linfo);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        listEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListManagerActivity.this,"该功能未实现！",Toast.LENGTH_SHORT).show();
            }
        });

        listDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListManagerActivity.this,"该功能未实现！",Toast.LENGTH_SHORT).show();
            }
        });

        addSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(ListManagerActivity.this,"该功能未实现！",Toast.LENGTH_SHORT).show();
                requestCode=1;
                Intent intent=new Intent(ListManagerActivity.this,SongsEditActivity.class);
                intent.putExtra("delete",false);
                intent.putExtra("id",listID);
                startActivityForResult(intent,requestCode);
            }
        });

        deleteSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(ListManagerActivity.this,"该功能未实现！",Toast.LENGTH_SHORT).show();
                requestCode=2;
                Intent intent=new Intent(ListManagerActivity.this,SongsEditActivity.class);
                intent.putExtra("delete",true);
                intent.putExtra("id",listID);
                startActivityForResult(intent,requestCode);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==1){
            if (resultCode==1){
                Toast.makeText(ListManagerActivity.this,"歌曲添加完成！",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode==0){
                Toast.makeText(ListManagerActivity.this,"歌曲添加不成功！",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==2){

            if (resultCode==1){
                Toast.makeText(ListManagerActivity.this,"歌曲删除成功",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==0){
                Toast.makeText(ListManagerActivity.this,"歌曲删除不成功",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
