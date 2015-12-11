package com.example.linquas.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Intent nextView = new Intent();
    private static final String TAG = "Main2Activity";
    Context mainContect = this;
    String[] data = {"0","1","2","3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent it = getIntent();
        Bundle bundle = it.getBundleExtra("send");


        TextView city = (TextView) findViewById(R.id.city);
        TextView temp_value2 = (TextView) findViewById(R.id.temp_value2);
        TextView humid_value2 = (TextView) findViewById(R.id.humid_value2);
        TextView sun_value2 = (TextView) findViewById(R.id.sun_value2);

        String get = bundle.getString("County");
        String UV_SENSOR = bundle.getString("UV_SENSOR");
        String TEMP_SENSOR = bundle.getString("TEMP_SENSOR");
        String HUMID_SENSOR = bundle.getString("HUMID_SENSOR");

        city.setText(get);
        temp_value2.setText(TEMP_SENSOR+"°C");
        humid_value2.setText(HUMID_SENSOR+"%");
        sun_value2.setText(UV_SENSOR);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int[] a = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4};
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(data,a);

        mAdapter.setmOnItemClickListener(new MyAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d(TAG,"Click Position: "+position);
//                nextView.setClass(mainContect, Main3Activity.class);

                Intent nextView2 = new Intent();
                nextView2.setClass(Main2Activity.this,Main3Activity.class);                          //設定傳送參數
                Bundle bundle2 = new Bundle();

                bundle2.putString("info",data[position].toString());

                nextView2.putExtras(bundle2);                                                         //將參數放入
                startActivity(nextView2);
            }
        });


        mRecyclerView.setAdapter(mAdapter);


    }
}


