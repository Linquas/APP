package com.example.linquas.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tw.org.cic.morsensor.MorSensor;
import tw.org.cic.morsensor.MorSensorConnection;
import tw.org.cic.morsensor.MorSensorEvent;
import tw.org.cic.morsensor.MorSensorEventListener;
import tw.org.cic.morsensor.MorSensorManager;
import tw.org.cic.morsensor.MorSensorService;
import tw.org.cic.morsensor.connection.MorSensorBleService;
import tw.org.cic.morsensor.connection.MorSensorConnectionCallback;
import tw.org.cic.morsensor.sensor.IMUSensor;


public class MainActivity extends AppCompatActivity  {
    public static Activity mMainActivity;
    private static final String TAG = "MainActivityAPI";
    private static final boolean D = false;

    short[] MorSensorID = new short[10];
    short[] MorSensorVersion = {0, 0, 0};
    short[] FirmwareVersion = {0, 0, 0};
    short[] MorSensorLibraryVersion = {0, 0, 0};

    //Byte (127 ~ -128)

    private static final short IMUID = 208;//0xD0 208
    private static final short THID = 128;//0x80 128
    private static final short UVID = 192;//0xC0 192
    private static final short ColorID = 82;//0x52 82
    private static final short SpO2ID = 160;//0xA0 160
    private static final short AlcoholID = 162;//0xA2 162
    private static final short MicID = 164;//0xA4 164
    private static final short PIRID = 168;//0xA8 168

    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String mDeviceAddress = "123";

    // 宣告MorSensor相關的物件
    MorSensorConnection mBT; //  = new MorSensorBleService(this);
    MorSensorService mService; //  = new MorSensorService(mBT);
    MorSensorEventListener listener; //  = new OnDataReceived();

    // 目前選擇的sensor
    static MorSensor[] sensor_t = {null, null, null};
    static boolean sensor_start[] = {false, false, false};

    StringBuilder report_text;

    private TextView tv_MorSensorVersion, tv_FirmwaveVersion, tv_MorSensorID;
    static TextView UV;
    static TextView TEMP;
    static TextView HUMID;
    static String MorSensor_Version = "", Firmwave_Version = "";

    static TextView tv_Temp, tv_Humi, tv_UV;
    static ImageButton btnGo, btnAll;


    private Intent nextView = new Intent();
    private static Bundle bundle;
    private static float AVGTEMP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, ".onCreate()");
        report_text = new StringBuilder("");
        mBT = new MorSensorBleService(this, mBTCallback);
        mBT.connect(DevicesScanActivity.mDeviceAddress);
        mService = new MorSensorService(mBT);
        listener = new OnDataReceived();

        Log.e(TAG, "DevicesScanActivity.mDeviceAddress:" + DevicesScanActivity.mDeviceAddress);
        mMainActivity = this;


        //Receive DeviceScanActivity DeviceAddress.
        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        bundle = intent.getBundleExtra("send");
        AVGTEMP = bundle.getFloat("TEMP");

        Log.e(TAG, "mDeviceAddress:" + mDeviceAddress);

        // 取得資源類別檔中的介面元件

        tv_MorSensorVersion = (TextView) findViewById(R.id.MorSensor_Version);
        tv_FirmwaveVersion = (TextView) findViewById(R.id.Firmwave_Version);
        tv_MorSensorID = (TextView) findViewById(R.id.MorSensor_ID);


        ImageView back = (ImageView) findViewById(R.id.background);
        back.setImageResource(R.drawable.back);

        tv_MorSensorID.setText(SensorName);
        tv_MorSensorVersion.setText(MorSensor_Version);
        tv_FirmwaveVersion.setText(Firmwave_Version);

        btnGo = (ImageButton) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(btnGoOnClick);

        btnAll = (ImageButton) findViewById(R.id.all_btn);
        btnAll.setOnClickListener(btnAllOnClick);


        tv_UV = (TextView) findViewById(R.id.tv_UV);
        tv_Temp = (TextView) findViewById(R.id.tv_Temp);
        tv_Humi = (TextView) findViewById(R.id.tv_Humi);

        UV = (TextView) findViewById(R.id.sun_value);
        TEMP = (TextView) findViewById(R.id.temp_value);
        HUMID = (TextView) findViewById(R.id.humid_value);



    }

    @Override
    protected  void onStart(){
        super.onStart();
        Log.e(TAG, " -- ON START -- ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " -- ON RESUME -- ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, " -- ON DESTROY -- ");

    }

    // 宣告一個MorSensorConnectionCallback物件，並override裡面的方法
    final MorSensorConnectionCallback mBTCallback = new MorSensorConnectionCallback() {
        @Override
        public void onBluetoothDeviceFound(BluetoothDevice device, int rssi) {
            // 當找到一個藍芽裝置時
            Log.e(TAG, "device:" + device + " rssi:" + rssi);
//            Log.e(TAG,"device:"+device);
        }

        @Override
        public void onBluetoothDeviceListUpdate(List<BluetoothDevice> deviceList) {
            Log.i(TAG, "onBluetoothDeviceListUpdate! ");
            // 當藍芽裝置清單被更新時
            //            sDeviceList = deviceList; // Update the device list here
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //do the something
                }
            });
        }

        @Override
        public void onConnectionStateChange(MorSensorConnection connection, int newState, int oldState) {
            // 當連線狀態改變時
            Log.i(TAG, "onConnectionStateChange! connectType:" + connection.getType() + "newState:" + newState + " oldState:" + oldState);
        }

        @Override
        public void onWriteStateChange(MorSensorConnection connection, int newState, int oldState) {
            // 當 “能否寫指令” 狀態改變時
            Log.i(TAG, "onWriteStateChange! connectType:" + connection.getType() + "newState:" + newState + " oldState:" + oldState);
        }

        @Override
        public void onConnectionFail(MorSensorConnection connection) {
            Log.i(TAG, "onConnectionFail!");
            // You need to update the UI views via runOnUiThread() here
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    tv_info.setText("Connecting failed!\nPlease press the connect button again.");
                }
            });
        }

        @Override
        public void onManagerInitialized() {
            // 當MorSensorService初始化完成時，此時才開始可以取得MorSensor版本資訊
            // You need to update the UI views via runOnUiThread() here
            Log.i(TAG, "onManagerInitialized!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        start_connection();
                        get_data(sensor_t[1]);
                        Thread.sleep(200);
                        get_data(sensor_t[2]);
                        sensor_start[1] = true;
                        sensor_start[2] = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onCharacterWrite(MorSensorConnection connection, byte[] rawData) {
            // 當MorSensorBleService執行writeCharacter()時
        }

        @Override
        public void onCharacterRead(MorSensorConnection connection, byte[] rawData) {
            // 當MorSensorBleService執行readCharacter()時，可以在這裡取得原始資料(20bytes的陣列)
            // You can get the received raw data here
            float[] data = new IMUSensor().transformData(rawData);
            String note = "color sensor data transform in main activity: ";
            for (float f : data) {
                note += f + " ";
            }
            Log.i(TAG, note);
        }
    };

    // 開始建立連線，取得各種版本資訊，並顯示在畫面上
    public void start_connection() throws InterruptedException {
        MorSensorManager manager = mService.getMorSensorManager();
        final List<MorSensor> sensorList = manager.getFullSensorList();
        report_text.setLength(0);

        MorSensorVersion = manager.getMorSensorVersion();
        report_text.append("MorSensor Version: ").append(MorSensorVersion[0]).append(".").append(MorSensorVersion[1]).append(".").append(MorSensorVersion[2]).append("\n");
        tv_MorSensorVersion.setText(MorSensorVersion[0] + "." + MorSensorVersion[1] + "." + MorSensorVersion[2]);
        Log.e(TAG, MorSensorVersion[0] + "." + MorSensorVersion[1] + "." + MorSensorVersion[2]);

        FirmwareVersion = manager.getFirmwareVersion();
        report_text.append("Firmware Version: ").append(FirmwareVersion[0]).append(".").append(FirmwareVersion[1]).append(".").append(FirmwareVersion[2]).append("\n");
        tv_FirmwaveVersion.setText(FirmwareVersion[0] + "." + FirmwareVersion[1] + "." + FirmwareVersion[2]);
        Log.e(TAG, FirmwareVersion[0] + "." + FirmwareVersion[1] + "." + FirmwareVersion[2]);

        MorSensorLibraryVersion = manager.getLibraryVersion();
        report_text.append("MorSensor Library Version: ").append(MorSensorLibraryVersion[0]).append(".").append(MorSensorLibraryVersion[1]).append(".").append(MorSensorLibraryVersion[2]).append("\n");
        Log.e(TAG, MorSensorLibraryVersion[0] + "." + MorSensorLibraryVersion[1] + "." + MorSensorLibraryVersion[2]);

        report_text.append("MorSensor List Size: ").append(sensorList.size()).append("\n");
        Log.e(TAG, "sensorList.size():" + sensorList.size());

        MorSensorID = new short[sensorList.size()];
        for (int i = 0; i < MorSensorID.length; i++) {
            MorSensorID[i] = (short) sensorList.get(i).getID();//[discover][number][ID][ID][ID]....
            setButtonEnabled(true);
            tv_MorSensorID.setText(SensorName);
        }

        for (int i = 0; i < 3; i++) {
            if (sensorList.size() > i) {
                MorSensor sensor = sensorList.get(i);
                mService.getMorSensorManager().registerListener(listener, sensor);
                report_text.append("Sensor").append(i + 1).append(" Version: ");
                if (sensor != null) {
                    int[] sensorVersion = sensor.getFirmwareVersion();
                    report_text.append(sensorVersion[0]).append(".").append(sensorVersion[1]).append(".").append(sensorVersion[2]);
                } else {
                    report_text.append("null");
                }
                report_text.append("\n");
            }
        }

        List<String> stringList = new ArrayList<>();
        for (MorSensor sensor : sensorList) {
            stringList.add(String.format("%d", sensor.getID()) + " (" + String.format("0X%02X", sensor.getID()) + ")");
        }
        if (sensorList.size() == 1) {
            sensor_t[0] = mService.getMorSensorManager().getDefaultSensorById(MorSensorID[0]);
        } else {
            sensor_t[0] = mService.getMorSensorManager().getDefaultSensorById(MorSensorID[0]);
            sensor_t[1] = mService.getMorSensorManager().getDefaultSensorById(MorSensorID[1]);
            switch (MorSensorID[1]) {
                case THID:
                    Log.e(TAG, "TH");
                    sensor_t[2] = mService.getMorSensorManager().getDefaultSensorById(MorSensorID[2]);
                    break;
                case UVID:
                    Log.e(TAG, "UV");
                    sensor_t[2] = mService.getMorSensorManager().getDefaultSensorById(MorSensorID[2]);
                    break;
                case ColorID:
                    Log.e(TAG, "Color");
                    break;
                case SpO2ID:
                    Log.e(TAG, "SpO2");
                    break;
                case AlcoholID:
                    Log.e(TAG, "Alcohol");
                    break;
                case MicID:
                    Log.e(TAG, "Mic");
                    break;
                case PIRID:
                    Log.e(TAG, "PIR");
                    break;
            }
        }
    }

    // Implement MorSensorEventListener類別，並override裡面的方法
    public class OnDataReceived implements MorSensorEventListener {
        @Override
        public void onMorSensorChanged(MorSensorEvent e) {
            String note;
            switch (e.getType()) {
                case MorSensorEvent.DATA_RECEIVE: // 收到sensor data時
                    note = "onDataReceived ";
                    float[] data = e.getData();
                    for (float x : data)
                        note += x + " ";
                    Log.e(TAG, note);
                    switch (e.getSensor().getID()) {
                        case IMUID:
//                            IMUViewActivity.DisplayIMUData(data);
//                            IMUViewPlusActivity.DisplayIMUData(data);
                            break;
                        case THID:
                            DisplayTHData(data);
                            break;
                        case UVID:
                            DisplayUVData(data);
                            break;
                        case ColorID:
//                            ColorViewActivity.DisplayColorData(data);
                            break;
                        case SpO2ID:
//                            SpO2ViewActivity.DisplaySpO2Data(data);
                            break;
                        case AlcoholID:
//                            AlcoholViewActivity.DisplayAlcoholData(data);
                            break;
                        case PIRID:
//                            PIRViewActivity.DisplayPIRData(data);
                            break;
                    }
                    break;
                case MorSensorEvent.FILE_DATA_RECEIVE: // 收到file data時，目前只有麥克風使用這一項
                    note = "onFileDataReceived Microphone";
                    byte[] fileData = e.getFileData();
                    Log.e(TAG, note);
                    FileOutputStream fos = null;
                    try {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio.wav");
                        if (file.exists()) {
                            file.delete();
                        }
                        fos = new FileOutputStream(file);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (fos != null) {
                        try {
                            fos.write(fileData);
                            fos.close();
                            Log.e(TAG, "write wav data complete!");
                            Toast.makeText(mMainActivity, "write wav data complete!", Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    MicViewActivitySimple.tv_start.setEnabled(true);
//                                    MicViewActivitySimple.tv_play.setEnabled(true);
                                }
                            });
//                            tv_data.setText("Recoding has been written in /andio.wav");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                case MorSensorEvent.FILE_DATA_PROGRESS: // When the file data progress updated
                    note = "onFileDataProgressReceived ";
                    note += e.getFileDataProgress();
//                    Log.e(TAG, note);
                    mProgress = (short) (e.getFileDataProgress() * 100);
                    switch (e.getSensor().getID()) {
                        case SpO2ID:
//                            SpO2ViewActivity.DisplayProgress(mProgress);
                            break;
                        case MicID:
//                            MicViewActivitySimple.DisplayProgress(mProgress);
                            break;
                    }
//                    tv_data.setText("Progress: " + String.format("%3d", (int) (e.getFileDataProgress() * 100)) + "%");
                    break;
                case MorSensorEvent.REGCONT_RECEIVE: // 收到回傳的register contents時，目前只有SpO2 sensor使用這一項
                    note = "onRegContReceived ";
                    int[] regCont = e.getRegCont();
                    for (int x : regCont)
                        note += x + " ";
                    Log.e(TAG, note);
//                    tv_data.setText(note);
                    break;
                case MorSensorEvent.TRANSMODE_RECEIVE: // 收到回傳的傳輸模式時
                    note = "onTransModeReceived ";
                    int transMode = e.getTransMode();
                    note += transMode;
                    Log.e(TAG, note);
//                    tv_data.setText(note);
                    break;
                default:
                    Log.e(TAG, "Error: unrecognized MorSensor event!");
                    break;
            }
        }
    }

    public static short mProgress = 0;

    public static void get_data_default() {
        if (sensor_t[1] != null) {
            sensor_t[1].getData();
            sensor_start[1] = true;
        }
    }

    private void get_data(MorSensor sensorTemp) {
        if (sensorTemp != null)
            sensorTemp.getData();
    }

    public static void stop_transmission(MorSensor sensorTemp) {
        try {
            sensorTemp.stopData();
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stop_all_sensor() {
        for (int i = 0; i < sensor_start.length; i++) {
            if (sensor_start[i]) {
                stop_transmission(sensor_t[i]);
            }
        }
    }

    static String SensorID = "", SensorName = "";

    private void setButtonEnabled(boolean mEnabled) throws InterruptedException {
        for (int i = 0; i < MorSensorID.length; i++) {
            switch (MorSensorID[i]) {
                case IMUID:
                    SensorName = "IMUSensor ";
//                    btIMU.setEnabled(mEnabled);
//                    sensor_t[0].getData();
                    sensor_start[0] = true;

                    break;
                case THID:
                    SensorName += "THUSensor ";
//                    btTH.setEnabled(mEnabled);
//                    Thread.sleep(500);
                    break;
                case UVID:
//                    btTH.setEnabled(mEnabled);
                    break;
                case ColorID:
                    SensorName += "ColorSensor ";
//                    btColor.setEnabled(mEnabled);
                    break;
                case SpO2ID:
                    SensorName += "SpO2Sensor ";
//                    btSpO2.setEnabled(mEnabled);
                    break;
                case AlcoholID:
                    SensorName += "AlcoholSensor ";
//                    btAlcohol.setEnabled(mEnabled);
                    break;
                case MicID:
                    SensorName += "MicSensor ";
//                    btMic.setEnabled(mEnabled);
                    break;
                case PIRID:
                    SensorName += "PIRSensor ";
//                    btPIR.setEnabled(mEnabled);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //創建訊息方塊
            builder.setMessage("確定要離開？");
            builder.setTitle("離開");
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //dismiss為關閉dialog,Activity還會保留dialog的狀態
                    stop_all_sensor();
                    mBT.disconnect();
                    mMainActivity.finish();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //dismiss為關閉dialog,Activity還會保留dialog的狀態
                }
            });
            builder.create().show();
            return false;
        }

        return super.onKeyDown(keyCode, event);
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

    //顯示數值在頁面上
    public static void DisplayTHData(float[] data) {
        tv_Temp.setText((int) (data[0] * 1000) / 1000.0 + ""); //Temp
        tv_Humi.setText((int) (data[1] * 1000) / 1000.0 + ""); //Humi

        DecimalFormat df = new DecimalFormat("0.0");
        TEMP.setText(df.format((int) (data[0] * 1000) / 1000.0) + "°C");
        HUMID.setText(df.format((int) (data[1] * 1000) / 1000.0) + "%");
        DecimalFormat df2 = new DecimalFormat("0");
        bundle.putString("TEMP_SENSOR",df2.format((int) (data[0] * 1000) / 1000.0));
        bundle.putFloat("AVG2TEMP",AVGTEMP);
        bundle.putString("HUMID_SENSOR",df2.format((int) (data[1] * 1000) / 1000.0));
    }

    public static void DisplayUVData(float[] data) {
        tv_UV.setText((int) (data[0] * 1000) / 1000.0 + ""); //UV
        DecimalFormat df = new DecimalFormat("0.00");
        UV.setText(df.format((int) (data[0] * 1000) / 1000.0));
        bundle.putString("UV_SENSOR",df.format((int) (data[0] * 1000) / 1000.0));

    }

    private View.OnClickListener btnGoOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(MainActivity.this, Main2Activity.class);
            nextView.putExtra("send", bundle);
            startActivity(nextView);
        }
    };

    private View.OnClickListener btnAllOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(MainActivity.this, Main2Activity_Search.class);
            startActivity(nextView);
        }
    };


    @Override
    public void onWindowFocusChanged(boolean focus){
        super.onWindowFocusChanged(focus);
//        必須等到所有View都讀取完畢，return值才會不等於0。
//        applyBlur();
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

    private void blurTest(){
        View view =this.getWindow().getDecorView().findViewById(R.id.background);
        Resources res=getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.back);
        blur(bmp,view);
    }

    private Bitmap save(View v)
    {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }


    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//图片缩放比例；
        float radius = 20;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
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

