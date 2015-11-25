package com.example.linquas.myapplication;

// AIzaSyAPUZaXr3dXfVXB-MNmQkjXS-8g2KqStSo
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.LocationSource;

import java.util.List;


public class MainActivity extends AppCompatActivity  implements
         LocationListener , LocationSource , asyncTaskListener {
    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    // 記錄目前最新的位置
    private android.location.Location currentLocation;
    private OnLocationChangedListener mLocationChangerListener;
    private LocationManager manager;
    private List<Location> location;
    private String county = null;
    private Intent nextView= new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, ".onCreate()");

//        ConnectivityManager cm =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        TextView textView = (TextView) findViewById(R.id.textView2);

//        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Light.ttf");
//
//        TextView tempValue = (TextView)findViewById(R.id.temp_value);
//        tempValue.setTypeface(font);
//
//        TextView humidValue = (TextView)findViewById(R.id.humid_value);
//        humidValue.setTypeface(font);
//
//        TextView sunValue = (TextView)findViewById(R.id.sun_value);
//        sunValue.setTypeface(font);

        ImageButton btnGo = (ImageButton) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(btnGoOnClick);

        AsyncTask t = new getLocation(this).execute("a001");

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
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " -- ON START -- ");
        enableLocationUpdate();

        ImageView back = (ImageView) findViewById(R.id.background);
//        setPic(R.drawable.back,back);
        back.setImageResource(R.drawable.back);

        if(currentLocation!= null && county == null){
            AsyncTask<String, Integer, String> q = new getCounty(this).execute(
                    String.valueOf(currentLocation.getLatitude()),
                    String.valueOf(currentLocation.getLongitude()));
        }
//        Log.e(TAG, " -- COUNTY = " + county);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " -- ON RESUME -- ");
        enableLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, " -- ON PAUSE -- ");

        disableLocationUpdate();

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, " -- ON STOP -- ");

        disableLocationUpdate();

    }

    private View.OnClickListener btnGoOnClick = new View.OnClickListener(){
        public void onClick(View v){
            nextView.setClass(MainActivity.this, Main2Activity.class);
            startActivity(nextView);
        }
    };

    @Override
    public void updateConty(String result) {
        county = result;
        int len = location.size();
        float[] distance = new float[3];
        distance[0]=0;
        distance[1]=0;
        distance[2]=0;
        android.location.Location temp = new android.location.Location("tem");
        float distances;
        float a=2000000;
        float temparature=-1;
        for(int i =0; i < len;i++){
//            System.out.println(location.get(i).getCityName());
            if(location.get(i).getCityName().equals(county)){
                temp.setLatitude((double) location.get(i).getLattitude());
                temp.setLongitude((double) location.get(i).getLontitude());
                distances = currentLocation.distanceTo(temp);
                if(a>distances){
                    a = distances;
                    temparature = location.get(i).getTemp();
                }

                System.out.println(location.get(i).getCityName());
                System.out.println(String.valueOf(location.get(i).getTemp()));
            }
        }
        Bundle send = new Bundle();
        send.putString("County",county);
        send.putFloat("TEMP",temparature);
        nextView.putExtras(send);

    }

    public  void updateList(List<Location> result){
        this.location = result;
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

    private void enableLocationUpdate(){
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,  this);
        }else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5 ,  this);
        }

//            TextView mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
//            TextView mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);

        currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(currentLocation!=null){
//                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
//                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        }else{
            currentLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
//                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        }
    }

    private void disableLocationUpdate(){manager.removeUpdates(this);}

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

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);

    }
}

