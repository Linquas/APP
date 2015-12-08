package com.example.linquas.myapplication;

// AIzaSyAPUZaXr3dXfVXB-MNmQkjXS-8g2KqStSo

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.LocationSource;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        LocationListener, LocationSource, asyncTaskListener {
    private static final String TAG = "MainActivity";
    // 記錄目前最新的位置
    private android.location.Location currentLocation;
    private OnLocationChangedListener mLocationChangerListener;
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
        setContentView(R.layout.activity_main);
        Log.e(TAG, ".onCreate()");

//        ConnectivityManager cm =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);



        ImageButton btnGo = (ImageButton) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(btnGoOnClick);


        AsyncTask t = new getLocation(this).execute(LINK);
        AsyncTask t2 = new getLocation(this).execute(LINK2);


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
        back.setImageResource(R.drawable.back2);
        //get location data (lat,lon)
        if (currentLocation != null && county == null) {
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

    private View.OnClickListener btnGoOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(MainActivity.this, Main2Activity.class);
            startActivity(nextView);

        }
    };

    @Override
    public void updateConty(String result) {
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
        nextView.putExtras(send);

    }

    public void updateList(List<Location> result) {
        if (this.location == null && result != null) {
            this.location = result;
            Log.i(TAG, " -- UPDATE LIST : " + this.location.size());
        } else if (result != null) {
            this.location.addAll(result);
            Log.i(TAG, " -- UPDATE LIST : " + this.location.size());
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
        String str = provider;
        switch (status) {
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

    private void enableLocationUpdate() {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
//                   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                          int[] grantResults)
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

//        blurTest();

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        Bitmap c = Bitmap.createBitmap(b,0,b.getHeight()-223,b.getWidth(),223);
        Bitmap res = FastBlur.doBlur(c,20,true);


//        Bitmap img = BlurBuilder.blur(this.getWindow().getDecorView().findViewById(R.id.blur));
        ImageView bb = (ImageView) findViewById(R.id.blur);
        bb.setBackground(new BitmapDrawable(getResources(),res));

    }

    private void applyBlur() {
        View view = this.getWindow().getDecorView();

        /**
         * 获取当前窗口快照，相当于截屏
         */
        Bitmap bmp1 = save(view);
        int height = getOtherHeight();
        /**
         * 除去状态栏和标题栏
         */
        Bitmap bmp2 = Bitmap.createBitmap(bmp1, 0, height,bmp1.getWidth(), bmp1.getHeight() - height);
        blur(bmp2, view);
    }


    private Bitmap save(View v)
    {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    @SuppressWarnings("deprecation")
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//图片缩放比例；
        float radius = 20;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (bkg.getWidth() / scaleFactor),
                (int) (bkg.getHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        view.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));
        Log.i("jerome", "blur time:" + (System.currentTimeMillis() - startMs));
    }

    private int getOtherHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
        return statusBarHeight + titleBarHeight;
    }


}

