package com.example.linquas.myapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Linquas on 2015/11/24.
 * Use Google reverse geolocation service to get the name of the county of the current location
 */
public class getCounty extends AsyncTask<String , Integer , String> {

    private String get;
    private String lon, lat, link;
    private InputStream inputStream;
    private asyncTaskListener listener;
    public getCounty(asyncTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        lat = params[0];
        lon = params[1];

        link = "https://maps.googleapis.com/maps/api/geocode/json?" +
                "latlng="+lat+","+lon+"&key=AIzaSyAPUZaXr3dXfVXB-MNmQkjXS-8g2KqStSo&language=zh-tw";
        try{
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            try {
                if (status == HttpURLConnection.HTTP_OK){
                    this.inputStream = urlConnection.getInputStream();
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = reader1.readLine()) != null) {
                        result.append(line);
                    }
//                    System.out.println(result.toString());

                    JSONObject j = new JSONObject(result.toString());
                    //Get JSON Array called "results" and then get the 0th complete object as JSON
                    JSONObject obj = j.getJSONArray("results").getJSONObject(0);

                    JSONObject j2 = obj.getJSONArray("address_components").getJSONObject(4);

                    this.get = j2.getString("long_name");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                urlConnection.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return get;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.updateCounty(result);
    }



}
