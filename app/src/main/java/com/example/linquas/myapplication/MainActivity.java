package com.example.linquas.myapplication;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.LocationSource;


public class MainActivity extends AppCompatActivity  implements
         LocationListener , LocationSource {

    private Handler mHandler = new Handler();

    // 記錄目前最新的位置
    private android.location.Location currentLocation;

    private OnLocationChangedListener mLocationChangerListener;

    private LocationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        ConnectivityManager cm =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);



        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final TextView textView = (TextView) findViewById(R.id.textView2);

        getXMLDate x = new getXMLDate();
        x.setmHandler(mHandler);
        x.setTextView(textView);
        x.setAPP(this);
        x.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






   @Override
    public void onLocationChanged(android.location.Location location1) {
        // 位置改變
       if(mLocationChangerListener != null){
           mLocationChangerListener.onLocationChanged(location1);
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String str = provider;
        switch(status){
            case LocationProvider.OUT_OF_SERVICE:
                str += "定位功能失敗";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                str += "暫時無法定位";
                break;
        }
        TextView gps = (TextView) findViewById(R.id.gpsStatus);
        gps.setText(str);
    }

    @Override
    public void onProviderEnabled(String provider) {
        enableLocationUpdate();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    @Override
    protected void onStart() {
        super.onStart();
        enableLocationUpdate();


    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocationUpdate();


    }
    @Override
    protected void onPause() {
        super.onPause();

        disableLocationUpdate();

    }
    @Override
    protected void onStop() {
        super.onStop();

        disableLocationUpdate();

    }

    private void enableLocationUpdate(){

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,  this);
            }else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5 ,  this);
            }

            TextView mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
            TextView mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);

            currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(currentLocation!=null){
                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
            }else{
                currentLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
            }
        }
    }

    private void disableLocationUpdate(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.removeUpdates(this);
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangerListener = onLocationChangedListener;
        enableLocationUpdate();
    }

    @Override
    public void deactivate() {
        mLocationChangerListener = null;
        disableLocationUpdate();
    }
}

