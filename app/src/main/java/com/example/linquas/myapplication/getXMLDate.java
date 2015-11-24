package com.example.linquas.myapplication;


import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class getXMLDate extends Thread{
    private Handler mHandler;
    private InputStream inputStream;
    private TextView textView;
    private InputStreamReader in;
    private BufferedReader i;


    private List<Location> list;
    private static String LINK = "http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0001-001" +
            "&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8";
    private static String XML_FILE = "XML_FILE";
    private AppCompatActivity app;

    public void run(){

        try{

            URL url = new URL(LINK);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", " application/xml; charset=utf-8");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            try {
                if (status == HttpURLConnection.HTTP_OK){
                    this.inputStream = urlConnection.getInputStream();
                }



//                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder result = new StringBuilder();
//                String line;
//                while((line = reader1.readLine()) != null) {
//                    result.append(line);
//                }
//                list = XMLparser.readXML(new ByteArrayInputStream(result.toString().getBytes()));

            }finally{
//                urlConnection.disconnect();
            }

            XMLparser XML = new XMLparser();
//            list = XML.readXML(this.inputStream);
//            urlConnection.disconnect();
            list = XML.readXML(R.xml.a001,this.app);
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


//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(sBuilder);
//                    }
//                });
            } else {

//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText("Fail......");
//                    }
//                });
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
    public void setList(List<Location> a){ this.list = a;}

}
