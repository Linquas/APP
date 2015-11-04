package com.example.linquas.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class getXMLDate extends Thread{
    private Handler mHandler;
    private InputStream inputStream;
    private TextView textView;
    private InputStreamReader in;
    private BufferedReader i;
    private AppCompatActivity app;

    public void run(){
        String a = "http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0001-001&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8";
        //String a = "http://opendata.cwb.gov.tw/opendata/DIV4/O-A0001-001.xml";
        try{
            URL url = new URL("https://tw.news.yahoo.com/rss/travel");
//            URL url = new URL("http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0001-001&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", " application/xml; charset=utf-8");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            try {
                if (status == HttpURLConnection.HTTP_OK){
                    this.inputStream = urlConnection.getInputStream();
                }

                byte[] data = new byte[1024];
                int idx = this.inputStream.read(data);
                String str = new String(data, 0, idx);
                System.out.println(str);
                //inputStream.close();


            }finally{
//                urlConnection.disconnect();
            }

            XMLparser XML = new XMLparser();
            List<Location> list = XML.readXML(this.inputStream);
            urlConnection.disconnect();
//            List<Location> list = XML.readXML("a001",this.app);
            final StringBuilder sBuilder = new StringBuilder();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Location location = list.get(i);
                    sBuilder.append("第-" + i + "-條記錄：\n");
                    sBuilder.append("City--" + location.getCityName() + "\n");
                    sBuilder.append("Town--" + location.getTownName() + "\n");
                    sBuilder.append("TEMP--" + location.getTemp() + "\n");
                    sBuilder.append("HUMD--" + location.getHumid() + "\n");
                }

                //textView.setText(sBuilder);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(sBuilder);
                    }
                });
            } else {
                //textView.setText("Fail......");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Fail......");
                    }
                });
            }
            inputStream.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setmHandler(Handler h){
        mHandler = h;
    }
    public void setTextView(TextView v){
        textView = v;
    }
    public void setAPP(AppCompatActivity a){
        this.app = a;
    }
}
