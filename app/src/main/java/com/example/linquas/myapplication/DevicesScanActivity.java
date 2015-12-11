package com.example.linquas.myapplication;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.LocationSource;

import java.util.ArrayList;
import java.util.List;

import tw.org.cic.morsensor.MorSensorConnection;
import tw.org.cic.morsensor.connection.MorSensorBleService;
import tw.org.cic.morsensor.connection.MorSensorConnectionCallback;

public class DevicesScanActivity extends AppCompatActivity implements
        LocationListener, LocationSource, asyncTaskListener {
    private static final String TAG = "DevicesScanActivity";
    public static Activity mDevicesScanActivity;

    // 這個activity的實體，用於callback裡面
    Context mainContect = this; // use for building the device spinner

    // 宣告MorSensor相關的物件
    MorSensorConnection mBT; //  = new MorSensorBleService(this);

    // 藍芽搜尋到的裝置清單
    static List<BluetoothDevice> sDeviceList = new ArrayList<BluetoothDevice>();

//    private LeDeviceListAdapter mLeDeviceListAdapter;

    // 儲存選取的Device Address
    static String mDeviceAddress = "";

    private RecyclerView mRecyclerView;
    private MyAdaptor mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    // 記錄目前最新的位置
    private android.location.Location currentLocation;
    private LocationSource.OnLocationChangedListener mLocationChangerListener;
    private LocationManager manager;
    private List<Location> location = null;
    private String county = null;
    private Intent nextView = new Intent();
    private static String LINK = "http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0001-001" +
            "&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8";
    private static String LINK2 = "http://opendata.cwb.gov.tw/opendataapi?dataid=O-A0003-001" +
            "&authorizationkey=CWB-402F8B44-8664-45DF-BBDD-378D529BE8E8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_scan);
        mDevicesScanActivity = this;

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        AsyncTask t = new getLocation(this).execute(LINK);
        AsyncTask t2 = new getLocation(this).execute(LINK2);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected  void onStart(){
        super.onStart();

    }



    @Override
    protected void onResume() {
        super.onResume();
        enableLocationUpdate();
        mBT = new MorSensorBleService(this, mBTCallback);
        // Initializes list view adapter.
//        mLeDeviceListAdapter = new LeDeviceListAdapter();
//        setListAdapter(mLeDeviceListAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdaptor(sDeviceList);
        //在rescycleview上增加按鍵監聽器
        mAdapter.setmOnItemClickListener(new MyAdaptor.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(county!=null){
                    Log.d(TAG,"Click Position: "+position);
                    if (mBT.isScanning()) {
                        ScanDevices(false);
                    }
                    mDeviceAddress = sDeviceList.get(position).getAddress();
                    Log.e(TAG, "mDeviceAddress: '" + mDeviceAddress + "'");

                    nextView.setClass(mainContect, MainActivity.class);
                    nextView.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);

                    startActivity(nextView);
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        ScanDevices(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, " -- ON DESTROY -- ");
        if (mBT.isScanning()) {
            mBT.stopScanDevices();
        }
        disableLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, " -- ON PAUSE -- ");
        disableLocationUpdate();

    }

    public void ScanDevices(boolean ScanStatus) {
        if (ScanStatus) {
//            sDeviceList.clear();
//            mLeDeviceListAdapter.clear();
            mBT.startScanDevices();
        } else {
            mBT.stopScanDevices();
        }
        invalidateOptionsMenu();
    }

    // 宣告一個MorSensorConnectionCallback物件，並override裡面的方法
    final MorSensorConnectionCallback mBTCallback = new MorSensorConnectionCallback() {
        @Override
        public void onBluetoothDeviceFound(BluetoothDevice device, int rssi) {
            // 當找到一個藍芽裝置時
            Log.e(TAG,"device:"+device+" rssi:"+rssi);

        }
        @Override
        public void onBluetoothDeviceListUpdate(List<BluetoothDevice> deviceList) {
            Log.i(TAG, "onBluetoothDeviceListUpdate!");
            sDeviceList = deviceList; // Update the device list here
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mLeDeviceListAdapter.notifyDataSetChanged();
                    mAdapter.swapData(sDeviceList);
                    mAdapter.notifyDataSetChanged();//更新devicelist
                }
            });
        }
    };
    //取得目前縣市位置
    @Override
    public void updateCounty(String result) {
        Log.i(TAG, " -- UPDATE COUNTY : " + result);
        county = result;
        int len = location.size();
        float[] distance = new float[3];
        distance[0] = 0;
        distance[1] = 0;
        distance[2] = 0;
        android.location.Location temp = new android.location.Location("tem");
        float distances;
        float a = 2000000;
        float temparature = -1;
        for (int i = 0; i < len; i++) {
//            System.out.println(location.get(i).getCityName());
            if (true) { //location.get(i).getCityName().equals(county)
                temp.setLatitude((double) location.get(i).getLattitude());
                temp.setLongitude((double) location.get(i).getLontitude());
                distances = currentLocation.distanceTo(temp);
                if (a > distances) {
                    a = distances;
                    temparature = location.get(i).getTemp();
                    Log.i(TAG, " -- NEAREST LOCATION : " + this.location.get(i).getCityName());
                }

                System.out.println(location.get(i).getCityName());
                System.out.println(String.valueOf(location.get(i).getTemp()));
            }
        }
        Bundle send = new Bundle();
        send.putString("County", county);
        send.putFloat("TEMP", temparature);
        nextView.putExtra("send", send);
    }
//取得氣象局天氣資料
    public void updateList(List<Location> result) {
        if (this.location == null && result != null) {
            this.location = result;
            Log.i(TAG, " -- UPDATE LIST : " + this.location.size());
        } else if (result != null) {
            this.location.addAll(result);
            Log.i(TAG, " -- UPDATE LIST : " + this.location.size());
            if (currentLocation != null) {

                AsyncTask<String, Integer, String> q = new getCounty(this).execute(
                        String.valueOf(currentLocation.getLatitude()),
                        String.valueOf(currentLocation.getLongitude()));
            }
        } else {
            Log.i(TAG, " -- UPDATE LIST : NO RESULT");
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location1) {
        // 位置改變
        if (mLocationChangerListener != null) {
            mLocationChangerListener.onLocationChanged(location1);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, " -- 定位功能失敗");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, " -- 暫時無法定位");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        enableLocationUpdate();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void enableLocationUpdate() {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }

//            TextView mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
//            TextView mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null) {
//                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
//                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        } else {
            currentLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                mLatitudeText.setText(String.valueOf(currentLocation.getLatitude()));
//                mLongitudeText.setText(String.valueOf(currentLocation.getLongitude()));
        }
    }

    private void disableLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.removeUpdates(this);}

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
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
