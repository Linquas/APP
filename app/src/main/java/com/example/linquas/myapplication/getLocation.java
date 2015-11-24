package com.example.linquas.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Linquas on 2015/11/24.
 */


public class getLocation extends AsyncTask< String , Integer , List<Location>> {
    private InputStream inputStream;
    private List<Location> list,list2;
    private static String LINK = "http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0001-001" +
            "&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8";
    private static String XML_FILE = "XML_FILE";
    private MainActivity mainActivity;

    public getLocation(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Location> doInBackground(String... params) {

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
            list = XML.readXML(R.xml.a001,mainActivity);
            list2 = XML.readXML(R.xml.a002,mainActivity);
            list.addAll(list2);
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }


        return list;
    }

    @Override
    protected void onPostExecute(List<Location> locations) {
        super.onPostExecute(locations);
        this.mainActivity.updateList(locations);
    }
}
