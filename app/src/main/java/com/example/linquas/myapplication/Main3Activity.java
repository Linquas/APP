package com.example.linquas.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";
    int[] image = { R.drawable.page0,R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,
                    R.drawable.page5,R.drawable.page6,R.drawable.page7,R.drawable.page8,R.drawable.img9,
                    R.drawable.img10,R.drawable.img11,R.drawable.page12,R.drawable.img13,R.drawable.img14,
                    R.drawable.img15,R.drawable.img16,R.drawable.img17,R.drawable.img18,R.drawable.img19,
                    R.drawable.img20,R.drawable.img21,R.drawable.img22};

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
