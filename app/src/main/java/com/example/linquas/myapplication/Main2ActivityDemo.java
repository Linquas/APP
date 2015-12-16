package com.example.linquas.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main2ActivityDemo extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Intent nextView = new Intent();
    private static final String TAG = "Main2ActivityDemo";
    int idListLength = 23;
    String[] data = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
    int[] a = { R.drawable.img0,R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
            R.drawable.img5,R.drawable.img6,R.drawable.img7,R.drawable.img8,R.drawable.img9,
            R.drawable.img10,R.drawable.img11,R.drawable.img12,R.drawable.img13,R.drawable.img14,
            R.drawable.img15,R.drawable.img16,R.drawable.img17,R.drawable.img18,R.drawable.img19,
            R.drawable.img20,R.drawable.img21,R.drawable.img22};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity_demo);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_demo);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(data,a);

        mAdapter.setmOnItemClickListener(new MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d(TAG,"Click Position: "+position);

                Intent nextView2 = new Intent();
                nextView2.setClass(Main2ActivityDemo.this,Main3Activity.class);                          //設定傳送參數
                Bundle bundle2 = new Bundle();

                bundle2.putString("info",mAdapter.mDataset[position].toString());

                nextView2.putExtras(bundle2);                                                         //將參數放入
                startActivity(nextView2);
            }
        });


        mRecyclerView.setAdapter(mAdapter);

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
                if (ap==1) {
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
                if (ap==0) {
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
