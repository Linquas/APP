package com.example.linquas.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";
    int[] image = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ImageView imageView = (ImageView) findViewById(R.id.back3);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();                                                         //取得Bundle

        int temp = Integer.parseInt(bundle.getString("info"));
        imageView.setImageResource(image[temp]);

    }
}
