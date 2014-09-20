package com.loman.david.mp3player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class PalyerService extends Service {

    private String url = null;
    private boolean hasSong = false;
    private boolean play = false;
    private MediaPlayer mediaPlayer = null;
    private TimeChangeThread timeChangeThread = null;

    @Override
    public void onCreate() {
        //init Boolean
        hasSong = false;
        play = false;
        //init Broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("PLAY_SERVICE");
        intentFilter.addAction("PAUSE");
        intentFilter.addAction("REPLAY");
        registerReceiver(broadcastReceiver, intentFilter);
        Log.e("Create", "CreateService");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        threadColse();
        mediaPlayer.stop();
        hasSong = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //init Thread
        timeChangeThread = new TimeChangeThread();
        Log.e("Start", "StartService");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("SERVICE_ACTION", action);
            if (action.equals("PLAY_SERVICE")) {
                url = intent.getStringExtra("URL");
                if (url != null) {
                    initData();
                    mediaPlayer.start();
                    if (timeChangeThread.isAlive()) {
                        threadColse();
                        timeChangeThread = new TimeChangeThread();
                        timeChangeThread.start();
                    } else {
                        timeChangeThread.start();
                    }
                }
            } else if (action.equals("PAUSE")) {
                mediaPlayer.pause();
            } else if (action.equals("REPLAY")) {
                mediaPlayer.start();
            }
        }

    };

    private class TimeChangeThread extends Thread {
        String currentTime = null;
        int intTime = 0;

        volatile boolean exit = false;

        @Override
        public void run() {
            boolean hasEnd=false;
            while (!exit) {
                try {
                    TimeChangeThread.sleep(1000);
                    initTime();
                    currentTime = changeToTime(intTime);
                    Intent intent = new Intent();
                    if (hasEnd) {
                        mediaPlayer.stop();
                        hasSong = false;
                        intent.setAction("STOP");
                    }else{
                        intent.putExtra("TIME", currentTime);
                        intent.setAction("PLAY");
                        if (intTime <= 1000) {
                            hasEnd = true;
                        }
                    }
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void initTime() {
            intTime = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
        }
    }

    private void initData() {
        if (hasSong) {
            mediaPlayer.stop();
        }
        Uri uri = Uri.parse(url);
        mediaPlayer = MediaPlayer.create(this, uri);
        hasSong = true;
    }

    private void threadColse() {
        timeChangeThread.exit = true;
        timeChangeThread.interrupt();
    }

    private String changeToTime(int secends) {
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

    private String getNumber(int number) {
        StringBuffer result = new StringBuffer();
        if (number < 10) {
            result.append("0");
            result.append(number);
        } else {
            result.append(number);
        }
        return result.toString();
    }
}
