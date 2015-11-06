package com.example.linquas.myapplication;

import android.os.Handler;

/**
 * Created by Linquas on 2015/11/6.
 */
public class getAddress extends Thread {
    private Handler mHandler;
    private String lon, lat;

    @Override
    public void run(){


    }

    public void setLon(String a){
        this.lon = a;
    }
    public void setLat(String a){
        this.lat = a;
    }
    public void setmHandler(Handler mHandler){this.mHandler = mHandler;}


}
