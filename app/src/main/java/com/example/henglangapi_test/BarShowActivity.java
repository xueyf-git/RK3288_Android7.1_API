package com.example.henglangapi_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.enjoysdk.EnjoySDK;


public class BarShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_show_layout);
        EnjoySDK enjoySDK = new EnjoySDK(this);

        // 显示状态栏按钮
        Button statusBarShow = findViewById(R.id.statusBarShow);
        statusBarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setStatusBarShowStatus(true);
            }
        });

        // 隐藏状态栏按钮
        Button statusBarHide = findViewById(R.id.statusBarHide);
        statusBarHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enjoySDK.setStatusBarShowStatus(false);
            }
        });




    }
}
