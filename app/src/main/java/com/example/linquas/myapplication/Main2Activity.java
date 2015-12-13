package com.example.linquas.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Intent nextView = new Intent();
    private static final String TAG = "Main2Activity";
    Context mainContect = this;
    String[] data = {"0","1","2","3","4","5","6","7","8","9","10","11","12"};
    int[] a = { R.drawable.img0,R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
                R.drawable.img5,R.drawable.img6,R.drawable.img7,R.drawable.img8,R.drawable.img9,
                R.drawable.img10,R.drawable.img11,R.drawable.img12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int ids = 0;
        int altitude;
        int county = 18;
        float baseTemperature = 26, trueTemperature = 18;
        int tempC=0;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Intent it = getIntent();
        Bundle bundle = it.getBundleExtra("send");


        TextView city = (TextView) findViewById(R.id.city);
        TextView temp_value2 = (TextView) findViewById(R.id.temp_value2);
        TextView humid_value2 = (TextView) findViewById(R.id.humid_value2);
        TextView sun_value2 = (TextView) findViewById(R.id.sun_value2);

        String get = bundle.getString("County");
        baseTemperature = bundle.getFloat("AVG2TEMP");
        String UV_SENSOR = bundle.getString("UV_SENSOR");
        String TEMP_SENSOR = bundle.getString("TEMP_SENSOR");
        String HUMID_SENSOR = bundle.getString("HUMID_SENSOR");

        trueTemperature = Float.parseFloat(bundle.getString("TEMP_SENSOR"));
        trueTemperature-=3;
        altitude = Math.abs((int) Math.round(100 * (baseTemperature - trueTemperature) / 0.6));

        city.setText(get);
        temp_value2.setText(TEMP_SENSOR+"°C");
        humid_value2.setText(HUMID_SENSOR+"%");
        sun_value2.setText(String.valueOf(altitude));

        int[] idList = new int[12];

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try{
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.creaturedata);

            InputStreamReader inputreader = new InputStreamReader(in_s);
            //FileReader fr = new FileReader("creatureData.txt");
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            StringBuilder text = new StringBuilder();

            for(int i=0;i<12;i++){
                idList[i]=0;
            }

            while((line = buffreader.readLine()) != null){
                int id = Integer.parseInt(buffreader.readLine());
                int top = Integer.parseInt(buffreader.readLine());      //save name max altitude into node
                int bot = Integer.parseInt(buffreader.readLine());      //save name min altitude into node
                int countries = Integer.parseInt(buffreader.readLine());  //read how many counties
                int[] counties = new int[20];
                for (int counter = 0; counter < countries; counter++) {
                    int countyData = Integer.parseInt(buffreader.readLine());
                    counties[countyData] = 1;            //turn to 1 means this county has this creature
                }
                buffreader.readLine();                                //read the change line
                if ((altitude >= bot) && (altitude <= top) && (counties[county] == 1)) {
                    ids++;
                    idList[tempC]=id;
                    tempC++;
                }
                text.append(line);
                text.append('\n');
            }

            Log.d(TAG,"test"+text.toString());

        } catch (IOException e){
            e.printStackTrace();
        }


        String[] data2 = new String[ids];
        int[] a2 = new int[ids];

        for(int j=0;j<ids;j++){
            int temp = idList[j];
            data2[j]=data[temp];
            a2[j]=a[temp];
        }

        // specify an adapter (see also next example)

        mAdapter = new MyAdapter(data2,a2);

        mAdapter.setmOnItemClickListener(new MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d(TAG,"Click Position: "+position);
//                nextView.setClass(mainContect, Main3Activity.class);

                Intent nextView2 = new Intent();
                nextView2.setClass(Main2Activity.this,Main3Activity.class);                          //設定傳送參數
                Bundle bundle2 = new Bundle();

                bundle2.putString("info",mAdapter.mDataset[position].toString());

                nextView2.putExtras(bundle2);                                                         //將參數放入
                startActivity(nextView2);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


    }
}


