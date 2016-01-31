package com.example.linquas.myapplication;

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
    private searchResultAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "Main2Activity";

    String[] data = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
    int[] a = { R.drawable.img0,R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
                R.drawable.img5,R.drawable.img6,R.drawable.img7,R.drawable.img8,R.drawable.img9,
                R.drawable.img10,R.drawable.img11,R.drawable.img12,R.drawable.img13,R.drawable.img14,
                R.drawable.img15,R.drawable.img16,R.drawable.img17,R.drawable.img18,R.drawable.img19,
                R.drawable.img20,R.drawable.img21,R.drawable.img22};
    boolean[] valid = {true,true,false,false,false,true,true,true,true,false,false,true,true,false,false,false,false,true,false,false,false,true,false};
    int idListLength = 23;
    int county = 18;
    int altitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int ids = 0;
        float baseTemperature;
        float trueTemperature;
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

        county = countyToNum(bundle.getString("County"));

        city.setText(get);
        temp_value2.setText(TEMP_SENSOR+"°C");
        humid_value2.setText(HUMID_SENSOR+"%");
        sun_value2.setText(UV_SENSOR);

        int[] idList = new int[idListLength];

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

            for(int i=0;i<idListLength;i++){
                idList[i]=0;
            }

            while((line = buffreader.readLine()) != null){
                int id = Integer.parseInt(buffreader.readLine());
                int ap = Integer.parseInt(buffreader.readLine());       //save animal(0) or plant(1)
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

        mAdapter = new searchResultAdapter(data2,a2);

        mAdapter.setmOnItemClickListener(new searchResultAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d(TAG,"Click Position: "+position);
                int temp = Integer.parseInt(mAdapter.mDataset[position]);
                if(valid[temp]){
                    //                nextView.setClass(mainContect, Main3Activity.class);

                    Intent nextView2 = new Intent();
                    nextView2.setClass(Main2Activity.this,Main3Activity.class);                          //設定傳送參數
                    Bundle bundle2 = new Bundle();

                    bundle2.putString("info",mAdapter.mDataset[position]);

                    nextView2.putExtras(bundle2);                                                         //將參數放入
                    startActivity(nextView2);
                }



            }
        });


        mRecyclerView.setAdapter(mAdapter);


    }

    private int countyToNum(String county){
        int result;
        switch (county){
            case "臺北市":
                result=0;
                break;
            case "新北市":
                result=1;
                break;
            case "桃園市":
                result=2;
                break;
            case "台中市":
                result=3;
                break;
            case "台南市":
                result=4;
                break;
            case "高雄市":
                result=5;
                break;
            case "基隆市":
                result=6;
                break;
            case "新竹市":
                result=7;
                break;
            case "嘉義市":
                result=8;
                break;
            case "新竹縣":
                result=9;
                break;
            case "苗栗縣":
                result=10;
                break;
            case "彰化縣":
                result=11;
                break;
            case "南投縣":
                result=12;
                break;
            case "雲林縣":
                result=13;
                break;
            case "嘉義縣":
                result=14;
                break;
            case "屏東縣":
                result=15;
                break;
            case "宜蘭縣":
                result=16;
                break;
            case "花蓮縣":
                result=17;
                break;
            case "臺東縣":
                result=18;
                break;
            case "澎湖縣":
                result=19;
                break;
            default:
                result=18;
                break;
        }
        return result;


    }


    public void plantClick(View view) {
        int ids = 0;
        int tempC=0;
        int[] idList = new int[idListLength];

        for(int i=0;i<idListLength;i++){
            idList[i]=0;
        }

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
                int ap = Integer.parseInt(buffreader.readLine());       //save animal(0) or plant(1)
                int top = Integer.parseInt(buffreader.readLine());      //save name max altitude into node
                int bot = Integer.parseInt(buffreader.readLine());      //save name min altitude into node
                int countries = Integer.parseInt(buffreader.readLine());  //read how many counties
                int[] counties = new int[20];
                for (int counter = 0; counter < countries; counter++) {
                    int countyData = Integer.parseInt(buffreader.readLine());
                    counties[countyData] = 1;            //turn to 1 means this county has this creature
                }
                buffreader.readLine();                                //read the change line
                if (ap==1&&(altitude >= bot) && (altitude <= top) && (counties[county] == 1)) {
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



        mAdapter.swapData(data2,a2);
        mAdapter.notifyDataSetChanged();
    }
    public void animalClick(View view) {
        int ids = 0;
        int tempC=0;
        int[] idList = new int[idListLength];

        for(int i=0;i<idListLength;i++){
            idList[i]=0;
        }

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
                int ap = Integer.parseInt(buffreader.readLine());       //save animal(0) or plant(1)
                int top = Integer.parseInt(buffreader.readLine());      //save name max altitude into node
                int bot = Integer.parseInt(buffreader.readLine());      //save name min altitude into node
                int countries = Integer.parseInt(buffreader.readLine());  //read how many counties
                int[] counties = new int[20];
                for (int counter = 0; counter < countries; counter++) {
                    int countyData = Integer.parseInt(buffreader.readLine());
                    counties[countyData] = 1;            //turn to 1 means this county has this creature
                }
                buffreader.readLine();                                //read the change line
                if (ap==0&&(altitude >= bot) && (altitude <= top) && (counties[county] == 1)) {
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


        mAdapter.swapData(data2,a2);
        mAdapter.notifyDataSetChanged();
    }

}


