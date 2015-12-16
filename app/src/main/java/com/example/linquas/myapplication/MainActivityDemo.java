package com.example.linquas.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivityDemo extends AppCompatActivity {

    private Intent nextView = new Intent();
    private static Bundle bundle;
    private ImageButton btnGo, btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_demo);

        btnGo = (ImageButton) findViewById(R.id.btnGo_demo);
        btnGo.setOnClickListener(btnGoOnClick);

        btnAll = (ImageButton) findViewById(R.id.all_btn_demo);
        btnAll.setOnClickListener(btnAllOnClick);
    }

    private View.OnClickListener btnGoOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(MainActivityDemo.this, Main2ActivityDemo.class);
            startActivity(nextView);
        }
    };

    private View.OnClickListener btnAllOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(MainActivityDemo.this, Main22Activity.class);
            startActivity(nextView);
        }
    };
}
