package com.example.linquas.myapplication;

// AIzaSyAPUZaXr3dXfVXB-MNmQkjXS-8g2KqStSo
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
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
         LocationListener , LocationSource {
    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler();
    // 記錄目前最新的位置
    private android.location.Location currentLocation;
    private OnLocationChangedListener mLocationChangerListener;
    private LocationManager manager;
    private List<Location> location;
    private String county;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, ".onCreate()");


//        ConnectivityManager cm =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final TextView textView = (TextView) findViewById(R.id.textView2);

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

        getXMLDate x = new getXMLDate();
        x.setmHandler(mHandler);
        x.setTextView(textView);
        x.setAPP(this);
        x.setList(location);
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
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " -- ON START -- ");
        enableLocationUpdate();
        getAddress y = new getAddress();

        ImageView back = (ImageView) findViewById(R.id.background);
//        setPic(R.drawable.back,back);
        back.setImageResource(R.drawable.back);

        if(currentLocation!= null){
            y.setLon(String.valueOf(currentLocation.getLongitude()));
            y.setLat(String.valueOf(currentLocation.getLatitude()));
            y.setmHandler(mHandler);
            y.getCounty(county);
            y.start();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);

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

    private View.OnClickListener btnGoOnClick = new View.OnClickListener(){
        public void onClick(View v){
            Intent it = new Intent();
            it.setClass(MainActivity.this, Main2Activity.class);
            startActivity(it);
        }
    };

    private void setPic(int id, ImageView destination) {
        int targetW = destination.getWidth();
        int targetH = destination.getHeight();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.getResources(), id);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), id);
        destination.setImageBitmap(bitmap);
    }

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


}

