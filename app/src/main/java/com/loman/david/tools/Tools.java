package com.loman.david.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-21.
 */
public class Tools {

    //
    public static String changeToTime (int secends){
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
