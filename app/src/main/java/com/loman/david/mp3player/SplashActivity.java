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

    private final int SPLASH_DISPLAY_LENGHT=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SqlExcu sqlExcu =new SqlExcu(SplashActivity.this);
                sqlExcu.initSQL(sqlExcu.SONGSTABLE,getContentResolver());
                sqlExcu.initSQL(sqlExcu.SONGLIST,getContentResolver());
                sqlExcu.initSQL(sqlExcu.LISTSTABLE,getContentResolver());

                Intent mainIntent =new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
