package com.loman.david.mp3player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loman.david.data.SqlExcu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-9-2.
 */
public class PlayActivity extends Activity {

    private TextView songTextView;
    private TextView timeTextView;
    private ImageButton playButton;
    private ImageButton pasteButton;
    private ImageButton nextButton;
    private SqlExcu excu;
    private ListView listview;
    private List<Map<String, String>> list;
    private int lid;
    private int sid;
    private String url = null;
    private String time = null;
    private String name = null;
    private int color[] = new int[]{Color.argb(120, 255, 32, 2), Color.argb(120, 78, 255, 17), Color.argb(120, 54, 57, 255)};
    private int status = 0;
    private boolean isPlaying = false;
    private boolean playMode =true;
    private final int STATUS_STOP = 0;
    private final int STATUS_REPLAY = 2;
    private final int STATUS_PAUSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        //get data from Intent
        Intent intent = getIntent();
        lid = intent.getIntExtra("LID", 0);
        sid = intent.getIntExtra("SID", 0);
        //get data from sqlite
        excu = new SqlExcu(this);
        list = excu.QuerrySQL(excu.SONGLIST, excu.LISTID, String.valueOf(lid));
        //get UI content
        songTextView = (TextView) findViewById(R.id.songNameTextView);
        timeTextView = (TextView) findViewById(R.id.showTimeTextView);
        playButton = (ImageButton) findViewById(R.id.playButton);
        pasteButton = (ImageButton) findViewById(R.id.pasteSongButton);
        nextButton = (ImageButton) findViewById(R.id.nextSongButton);
        listview = (ListView) findViewById(R.id.songListView);
        //make ListView
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.songlist_item, new String[]{excu.SONGNAME, excu.SONGTIME}, new int[]{R.id.title, R.id.time});
        listview.setAdapter(adapter);
        //registerBroadcast
        IntentFilter servicefilter = new IntentFilter();
        servicefilter.addAction("PLAY");
        servicefilter.addAction("STOP");
        registerReceiver(receiver, servicefilter);
        //startService
        intent = new Intent(PlayActivity.this, PalyerService.class);
        startService(intent);
    }

    @Override
    protected void onRestart() {
        isPlaying = true;
        super.onRestart();
        Log.e("TEST", "RESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        isPlaying = false;
        Log.e("TEST", "START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //change songs
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sid = Integer.parseInt(list.get(position).get(excu.SONGID));
                init();
                playSong();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Resources res = getResources();
                    if (status == STATUS_STOP) {
                        playSong();
                    } else {
                        Intent sendIntent = new Intent();
                        if (status == STATUS_PAUSE) {
                            sendIntent.setAction("PAUSE");
                            Drawable drawable = res.getDrawable(R.drawable.play);
                            playButton.setImageDrawable(drawable);
                        } else {
                            sendIntent.setAction("REPLAY");
                            Drawable drawable = res.getDrawable(R.drawable.pause);
                            playButton.setImageDrawable(drawable);
                        }
                        status = (status + status) % 3;
                        sendBroadcast(sendIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid--;
                if (sid < 0) {
                    sid++;
                    Toast.makeText(PlayActivity.this, "Not File", Toast.LENGTH_SHORT).show();
                } else {
//                    isPlaying=false;
                    init();
                    playSong();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid++;
                if (sid >= list.size()) {
                    sid--;
                    Toast.makeText(PlayActivity.this, "Not File", Toast.LENGTH_SHORT).show();
                } else {
//                    isPlaying=false;
                    init();
                    playSong();
                }
            }
        });
        Log.e("TEST", "RESUME");
    }

    @Override
    protected void onPause() {
        Log.e("TEST", "PAUSE");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e("TEST", "STOP");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(PlayActivity.this, PalyerService.class);
        stopService(serviceIntent);
        unregisterReceiver(receiver);
        Log.e("TEST", "finsh");
        super.onDestroy();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("ACTION", action);
            if (action.equals("PLAY")) {
                time = intent.getStringExtra("TIME");
                timeTextView.setText(time);
            } else if (action.equals("STOP")) {
                nextSong();
            }

        }
    };

    //update UI
    private void init() {
        boolean hasFind = false;
        int textColor = 0;

        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            map = list.get(i);
            //init data
            if (String.valueOf(sid).equals(map.get(excu.SONGID))) {

                url = map.get(excu.SONGPATH);
                name = map.get(excu.SONGNAME);
                textColor = color[i % 3];
                if (!isPlaying) {
                    time = map.get(excu.SONGTIME);
                }
                hasFind = true;
            }
        }
        if (!hasFind) {
            Toast.makeText(this, "Song infomation has broken", Toast.LENGTH_SHORT).show();
        }

        timeTextView.setText(time);
        songTextView.setText(name);
        songTextView.setTextColor(textColor);
    }

    //play nextsong
    private void nextSong() {
        if (playMode==true) {
            sid++;
        }
        if (sid >= list.size()) {
            Toast.makeText(this, "Played", Toast.LENGTH_SHORT).show();
            finish();
        }
        init();
        playSong();
    }

    //playsong
    private void playSong() {
        Intent intent = new Intent();
        intent.putExtra("URL", url);
        intent.setAction("PLAY_SERVICE");
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.pause);
        playButton.setImageDrawable(drawable);
        sendBroadcast(intent);
        status = STATUS_PAUSE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_single) {
            playMode=false;
            return true;
        }else if (id == R.id.action_list){
            playMode=true;
            return true;
        }else if (id ==R.id.action_exit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
