package com.example.linquas.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main2Activity_Search extends AppCompatActivity {

    Intent nextView = new Intent();
    static ImageButton btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__search);

        btnGo = (ImageButton) findViewById(R.id.btn_search);
        btnGo.setOnClickListener(btnGoOnClick);
    }

    private View.OnClickListener btnGoOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            nextView.setClass(Main2Activity_Search.this, Main22Activity.class);
            startActivity(nextView);
        }
    };

}
