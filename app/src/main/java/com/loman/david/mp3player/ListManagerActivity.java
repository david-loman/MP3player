package com.loman.david.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loman.david.data.SQLHelper;

/**
 * Created by root on 14-8-22.
 */
public class ListManagerActivity extends Activity {

    private Button listEditButton,listDeleteButton,addSongsButton,deleteSongsButton;
    private EditText nameEditText,classEdtiText,tagEditText,infoEditText;
    private TextView countTextView;
    private SQLHelper sqlHelper;
    private int listID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_information);

        listEditButton=(Button)findViewById(R.id.listEditor);
        listDeleteButton=(Button)findViewById(R.id.listDelete);
        addSongsButton=(Button)findViewById(R.id.addSongs);
        deleteSongsButton=(Button)findViewById(R.id.deleteSongs);
        nameEditText=(EditText)findViewById(R.id.nameEditText);
        classEdtiText=(EditText)findViewById(R.id.classEditText);
        tagEditText=(EditText)findViewById(R.id.tagEditText);
        infoEditText=(EditText)findViewById(R.id.infoEditText);
        countTextView=(TextView)findViewById(R.id.countTextView);
        sqlHelper=new SQLHelper(this);
        Intent intent=getIntent();
        listID=intent.getIntExtra("id",0);
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

        addSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ListManagerActivity.this,SongsEditActivity.class);
                startActivity(intent);
            }
        });

        deleteSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListManagerActivity.this,SongsEditActivity.class);
                startActivity(intent);
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
}
