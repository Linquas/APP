package com.example.linquas.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
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

public class Main22Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "Main22Activity";
    Context mainContect = this;
    String[] data = {"0","1","2","3","4","5","6","7","8","9","10","11","12"};
    int[] a = { R.drawable.img0,R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
            R.drawable.img5,R.drawable.img6,R.drawable.img7,R.drawable.img8,R.drawable.img9,
            R.drawable.img10,R.drawable.img11,R.drawable.img12};

    FloatingActionButton animal_fab_all = (FloatingActionButton) findViewById(R.id.animal_fab_all);
    FloatingActionButton plant_fab_all = (FloatingActionButton) findViewById(R.id.plant_fab_all);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_all);
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
//                nextView.setClass(mainContect, Main3Activity.class);

                Intent nextView2 = new Intent();
                nextView2.setClass(Main22Activity.this,Main3Activity.class);                          //設定傳送參數
                Bundle bundle2 = new Bundle();

                bundle2.putString("info",mAdapter.mDataset[position].toString());

                nextView2.putExtras(bundle2);                                                         //將參數放入
                startActivity(nextView2);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        animal_fab_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                mAdapter.swapData();
                mAdapter.notifyDataSetChanged();
            }
        });

        plant_fab_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                mAdapter.swapData();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
