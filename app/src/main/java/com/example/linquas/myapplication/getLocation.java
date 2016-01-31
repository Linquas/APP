package com.example.linquas.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Linquas on 2015/11/24.
 * download the data from the weather bureau and save it
 */


public class getLocation extends AsyncTask< String , Integer , List<Location>> {
    private InputStream inputStream;
    private List<Location> list;
    private DevicesScanActivity mainActivity;

    public getLocation(DevicesScanActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Location> doInBackground(String... params) {

        try{
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", " application/xml; charset=utf-8");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            try {
                if (status == HttpURLConnection.HTTP_OK){
                    this.inputStream = urlConnection.getInputStream();
                }
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader1.readLine()) != null) {
                    result.append(line);
//                    System.out.println(line);
                }
                line = result.toString();

                list = XMLparser.readXML(line);

            }finally{
                urlConnection.disconnect();
            }

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
