package com.loman.david.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;

import com.loman.david.data.SqlExcu;


public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SqlExcu sqlExcu =new SqlExcu(this);
        sqlExcu.initSQL(sqlExcu.SONGSTABLE,getContentResolver());
        sqlExcu.initSQL(sqlExcu.SONGLIST,getContentResolver());
        sqlExcu.initSQL(sqlExcu.LISTSTABLE,getContentResolver());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent =new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_LENGHT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
