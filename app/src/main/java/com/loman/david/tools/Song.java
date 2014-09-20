package com.loman.david.tools;

/**
 * Created by root on 14-8-21.
 */
public class Song {

    private int id;
    private int time;
    private String name = null;
    private String path = null;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public int getTime() {
        return this.time;
    }
}
